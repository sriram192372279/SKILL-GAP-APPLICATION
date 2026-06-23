// renderer.js

let mediaStream = null;
let autoCaptureIntervalId = null;
let recognition = null;
let isVoiceListening = false;
let isAutoCapturing = false;
let isClickThroughActive = false;

// Configuration
let apiProvider = 'gemini';
let apiKey = '';
let apiModel = '';
let captureSourceId = '';
let captureSourceName = '';
let captureInterval = 0;
let systemPrompt = '';

document.addEventListener('DOMContentLoaded', () => {
  initUI();
  initConfig();
  initSpeechRecognition();
});

// Watch for settings changes from the settings window
window.addEventListener('storage', () => {
  initConfig();
});

function initConfig() {
  apiProvider = localStorage.getItem('apiProvider') || 'gemini';
  apiKey = localStorage.getItem('apiKey') || '';
  apiModel = localStorage.getItem('apiModel') || '';
  captureSourceId = localStorage.getItem('captureSourceId') || '';
  captureSourceName = localStorage.getItem('captureSourceName') || '';
  captureInterval = parseInt(localStorage.getItem('captureInterval') || '0', 10);
  systemPrompt = localStorage.getItem('systemPrompt') || '';

  const setupWarning = document.getElementById('setup-warning');
  const btnManualCapture = document.getElementById('btn-manual-capture');
  const btnToggleAuto = document.getElementById('btn-toggle-auto');
  const captureSourceBar = document.getElementById('capture-source-bar');
  const txtSourceName = document.getElementById('txt-source-name');

  // Check if API key is configured
  if (!apiKey) {
    setupWarning.classList.remove('hidden');
    btnManualCapture.disabled = true;
    btnManualCapture.classList.add('opacity-50', 'pointer-events-none');
    btnToggleAuto.classList.add('opacity-50', 'pointer-events-none');
    stopAutoCapture();
    stopVideoStream();
    return;
  } else {
    setupWarning.classList.add('hidden');
    btnManualCapture.disabled = false;
    btnManualCapture.classList.remove('opacity-50', 'pointer-events-none');
    btnToggleAuto.classList.remove('opacity-50', 'pointer-events-none');
  }

  // Handle capture source
  if (captureSourceId) {
    captureSourceBar.classList.remove('hidden');
    txtSourceName.textContent = captureSourceName || 'Selected Window';
    startVideoStream();
  } else {
    captureSourceBar.classList.add('hidden');
    txtSourceName.textContent = 'No window selected';
    stopVideoStream();
  }

  // Adjust auto-capture interval if it changed
  if (isAutoCapturing) {
    stopAutoCapture();
    if (captureInterval > 0) {
      startAutoCapture();
    }
  }
}

function initUI() {
  const btnClose = document.getElementById('btn-close');
  const btnSettings = document.getElementById('btn-settings');
  const btnSetup = document.getElementById('btn-setup');
  const btnChangeSource = document.getElementById('btn-change-source');
  const btnClickThrough = document.getElementById('btn-click-through');
  const btnVoice = document.getElementById('btn-voice');
  const btnManualCapture = document.getElementById('btn-manual-capture');
  const btnToggleAuto = document.getElementById('btn-toggle-auto');
  const opacitySlider = document.getElementById('opacity-slider');

  // Opacity Slider
  opacitySlider.addEventListener('input', (e) => {
    const opacity = parseFloat(e.target.value);
    window.electronAPI.setOpacity(opacity);
  });

  // Window Controls
  btnClose.addEventListener('click', () => {
    window.electronAPI.hideWindow();
  });

  btnSettings.addEventListener('click', () => {
    window.electronAPI.openSettings();
  });

  btnSetup.addEventListener('click', () => {
    window.electronAPI.openSettings();
  });

  btnChangeSource.addEventListener('click', () => {
    window.electronAPI.openSettings();
  });

  // Click-Through Toggle
  btnClickThrough.addEventListener('click', () => {
    isClickThroughActive = !isClickThroughActive;
    window.electronAPI.toggleClickThrough(isClickThroughActive);

    if (isClickThroughActive) {
      btnClickThrough.classList.add('bg-indigo-600/30', 'text-indigo-400');
      btnClickThrough.classList.remove('hover:bg-white/5', 'text-slate-400');
    } else {
      btnClickThrough.classList.remove('bg-indigo-600/30', 'text-indigo-400');
      btnClickThrough.classList.add('hover:bg-white/5', 'text-slate-400');
    }
  });

  // Voice Helper Toggle
  btnVoice.addEventListener('click', () => {
    toggleVoiceHelper();
  });

  // Manual Capture & Analyze
  btnManualCapture.addEventListener('click', () => {
    captureAndAnalyze();
  });

  // Auto Capture Toggle
  btnToggleAuto.addEventListener('click', () => {
    if (isAutoCapturing) {
      stopAutoCapture();
    } else {
      if (captureInterval <= 0) {
        // If set to manual, guide them to settings
        window.electronAPI.openSettings();
        alert('Please select an Auto Capture Interval in settings first.');
        return;
      }
      startAutoCapture();
    }
  });

  // Listen for Mute Status from global shortcut
  window.electronAPI.onMuteStatus((isMuted) => {
    updateMuteUI(isMuted);
  });
}

function updateMuteUI(isMuted) {
  const btnVoice = document.getElementById('btn-voice');
  if (isMuted) {
    if (isVoiceListening) {
      stopSpeechRecognition();
    }
    btnVoice.classList.add('text-red-400', 'bg-red-500/10');
    btnVoice.querySelector('i').setAttribute('data-lucide', 'mic-off');
  } else {
    btnVoice.classList.remove('text-red-400', 'bg-red-500/10');
    btnVoice.querySelector('i').setAttribute('data-lucide', 'mic');
  }
  lucide.createIcons();
}

// Video Stream Management
async function startVideoStream() {
  stopVideoStream();

  if (!captureSourceId) return;

  try {
    const stream = await navigator.mediaDevices.getUserMedia({
      audio: false,
      video: {
        mandatory: {
          chromeMediaSource: 'desktop',
          chromeMediaSourceId: captureSourceId,
          minWidth: 1280,
          minHeight: 720
        }
      }
    });

    mediaStream = stream;
    const video = document.getElementById('hidden-video');
    video.srcObject = stream;
    video.onloadedmetadata = () => {
      video.play().catch(e => console.error("Error playing video:", e));
      updateStatusIndicator('active');
    };
  } catch (err) {
    console.error('Error starting video stream for source ' + captureSourceId, err);
    updateStatusIndicator('error');
  }
}

function stopVideoStream() {
  if (mediaStream) {
    mediaStream.getTracks().forEach(track => track.stop());
    mediaStream = null;
  }
  const video = document.getElementById('hidden-video');
  video.srcObject = null;
  updateStatusIndicator('idle');
}

function updateStatusIndicator(status) {
  const indicator = document.getElementById('status-indicator');
  indicator.className = 'w-2.5 h-2.5 rounded-full transition-all duration-300 ';
  
  if (status === 'active') {
    indicator.classList.add('bg-emerald-500', 'shadow-[0_0_8px_rgba(16,185,129,0.6)]');
  } else if (status === 'error') {
    indicator.classList.add('bg-red-500', 'shadow-[0_0_8px_rgba(239,68,68,0.6)]');
  } else if (status === 'analyzing') {
    indicator.classList.add('bg-indigo-500', 'animate-ping');
  } else {
    indicator.classList.add('bg-slate-500');
  }
}

// Auto Capture Engine
function startAutoCapture() {
  if (captureInterval <= 0) return;
  isAutoCapturing = true;
  
  const icon = document.getElementById('icon-auto');
  const btn = document.getElementById('btn-toggle-auto');
  
  icon.setAttribute('data-lucide', 'pause');
  btn.classList.add('bg-indigo-600/30', 'text-indigo-400');
  btn.classList.remove('bg-white/5', 'text-slate-300');
  lucide.createIcons();

  autoCaptureIntervalId = setInterval(() => {
    captureAndAnalyze();
  }, captureInterval);
}

function stopAutoCapture() {
  isAutoCapturing = false;
  if (autoCaptureIntervalId) {
    clearInterval(autoCaptureIntervalId);
    autoCaptureIntervalId = null;
  }
  
  const icon = document.getElementById('icon-auto');
  const btn = document.getElementById('btn-toggle-auto');
  if (icon && btn) {
    icon.setAttribute('data-lucide', 'play');
    btn.classList.remove('bg-indigo-600/30', 'text-indigo-400');
    btn.classList.add('bg-white/5', 'text-slate-300');
    lucide.createIcons();
  }
}

// Speech Recognition Engine (Web Speech API)
function initSpeechRecognition() {
  const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
  if (!SpeechRecognition) {
    console.warn('Speech recognition not supported in this browser.');
    return;
  }

  recognition = new SpeechRecognition();
  recognition.continuous = true;
  recognition.interimResults = true;
  recognition.lang = 'en-US';

  recognition.onstart = () => {
    isVoiceListening = true;
    document.getElementById('transcription-indicator').classList.remove('hidden');
  };

  recognition.onresult = (event) => {
    let interimTranscript = '';
    let finalTranscript = '';

    for (let i = event.resultIndex; i < event.results.length; ++i) {
      if (event.results[i].isFinal) {
        finalTranscript += event.results[i][0].transcript + ' ';
      } else {
        interimTranscript += event.results[i][0].transcript;
      }
    }

    const currentText = (finalTranscript || interimTranscript).trim();
    if (currentText) {
      const transcriptBox = document.getElementById('txt-transcript');
      transcriptBox.textContent = currentText;
      transcriptBox.scrollTop = transcriptBox.scrollHeight;
    }
  };

  recognition.onerror = (event) => {
    console.error('Speech recognition error:', event.error);
    if (event.error === 'not-allowed') {
      stopSpeechRecognition();
    }
  };

  recognition.onend = () => {
    isVoiceListening = false;
    document.getElementById('transcription-indicator').classList.add('hidden');
    // Restart if we still want it running
    if (isVoiceListening) {
      recognition.start();
    }
  };
}

function toggleVoiceHelper() {
  if (!recognition) {
    alert('Speech recognition is not supported on this platform.');
    return;
  }

  const btnVoice = document.getElementById('btn-voice');

  if (isVoiceListening) {
    stopSpeechRecognition();
    btnVoice.classList.remove('bg-indigo-600/30', 'text-indigo-400');
    btnVoice.classList.add('hover:bg-white/5', 'text-slate-400');
  } else {
    startSpeechRecognition();
    btnVoice.classList.add('bg-indigo-600/30', 'text-indigo-400');
    btnVoice.classList.remove('hover:bg-white/5', 'text-slate-400');
  }
}

function startSpeechRecognition() {
  if (recognition && !isVoiceListening) {
    try {
      recognition.start();
      isVoiceListening = true;
    } catch (e) {
      console.error("Error starting speech recognition:", e);
    }
  }
}

function stopSpeechRecognition() {
  if (recognition && isVoiceListening) {
    try {
      recognition.stop();
      isVoiceListening = false;
      document.getElementById('transcription-indicator').classList.add('hidden');
    } catch (e) {
      console.error("Error stopping speech recognition:", e);
    }
  }
}

// Core Capture & Analysis Function
async function captureAndAnalyze() {
  if (!apiKey) return;

  const video = document.getElementById('hidden-video');
  const responseContainer = document.getElementById('response-container');
  const responseTime = document.getElementById('response-time');
  const transcriptText = document.getElementById('txt-transcript').textContent.trim();

  updateStatusIndicator('analyzing');

  // 1. Capture screenshot from stream if available
  let base64Image = null;
  if (mediaStream && video.readyState === video.HAVE_ENOUGH_DATA) {
    try {
      const canvas = document.createElement('canvas');
      canvas.width = video.videoWidth;
      canvas.height = video.videoHeight;
      const ctx = canvas.getContext('2d');
      ctx.drawImage(video, 0, 0, canvas.width, canvas.height);
      const dataUrl = canvas.toDataURL('image/jpeg', 0.85);
      base64Image = dataUrl.split(',')[1]; // Remove 'data:image/jpeg;base64,' prefix
    } catch (err) {
      console.error('Error drawing canvas frame:', err);
    }
  }

  // If no visual capture and no speech, return
  if (!base64Image && (transcriptText === '' || transcriptText.startsWith('No question detected'))) {
    updateStatusIndicator('active');
    return;
  }

  // Show loading indicator in response container
  responseContainer.innerHTML = `
    <div class="flex flex-col items-center justify-center h-36 text-indigo-400 space-y-3">
      <i data-lucide="loader" class="w-8 h-8 animate-spin"></i>
      <span class="text-xs font-medium animate-pulse">Analyzing context...</span>
    </div>
  `;
  lucide.createIcons();

  try {
    let resultText = '';
    const start = performance.now();

    if (apiProvider === 'gemini') {
      resultText = await callGeminiAPI(base64Image, transcriptText);
    } else {
      resultText = await callOpenAIAPI(base64Image, transcriptText);
    }

    const duration = ((performance.now() - start) / 1000).toFixed(1);
    responseTime.textContent = `${duration}s`;

    // Render Markdown response safely
    if (window.marked) {
      responseContainer.innerHTML = window.marked.parse(resultText);
    } else {
      responseContainer.textContent = resultText;
    }

    // Add copy functionality to code blocks
    addCopyButtons(responseContainer);

    // Save Log
    window.electronAPI.saveLog({
      provider: apiProvider,
      model: apiModel,
      transcript: transcriptText,
      response: resultText
    });

    updateStatusIndicator('active');
  } catch (err) {
    console.error('AI Analysis failed:', err);
    responseContainer.innerHTML = `
      <div class="flex flex-col items-center justify-center h-36 text-red-400 space-y-2 text-center p-4">
        <i data-lucide="alert-octagon" class="w-8 h-8"></i>
        <span class="text-xs font-semibold">Analysis Failed</span>
        <span class="text-[10px] text-slate-400">${err.message || 'Check API Key / connection'}</span>
      </div>
    `;
    lucide.createIcons();
    updateStatusIndicator('error');
  }
}

// API Call Implementations
async function callGeminiAPI(base64Image, transcriptText) {
  const model = apiModel || 'gemini-1.5-flash';
  const url = `https://generativelanguage.googleapis.com/v1beta/models/${model}:generateContent?key=${apiKey}`;

  let prompt = systemPrompt || 'Analyze this image and transcribe or answer the interview questions.';
  if (transcriptText && !transcriptText.startsWith('No question detected')) {
    prompt += `\n\nVerbal Context (transcribed from audio): "${transcriptText}"`;
  }

  const parts = [];
  
  // Text prompt
  parts.push({ text: prompt });

  // Multimodal image part
  if (base64Image) {
    parts.push({
      inlineData: {
        mimeType: 'image/jpeg',
        data: base64Image
      }
    });
  }

  const response = await fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      contents: [{ parts: parts }]
    })
  });

  if (!response.ok) {
    const errorData = await response.json().catch(() => ({}));
    throw new Error(errorData.error?.message || `HTTP ${response.status}`);
  }

  const data = await response.json();
  const candidateText = data.candidates?.[0]?.content?.parts?.[0]?.text;
  if (!candidateText) {
    throw new Error('Empty response from Gemini API');
  }
  
  return candidateText;
}

async function callOpenAIAPI(base64Image, transcriptText) {
  const model = apiModel || 'gpt-4o';
  const url = 'https://api.openai.com/v1/chat/completions';

  const messages = [
    {
      role: 'system',
      content: systemPrompt
    }
  ];

  const userContent = [];
  let prompt = 'Please assist me with the current interview question.';
  if (transcriptText && !transcriptText.startsWith('No question detected')) {
    prompt += `\n\nVerbal transcript context: "${transcriptText}"`;
  }
  userContent.push({ type: 'text', text: prompt });

  if (base64Image) {
    userContent.push({
      type: 'image_url',
      image_url: {
        url: `data:image/jpeg;base64,${base64Image}`
      }
    });
  }

  messages.push({
    role: 'user',
    content: userContent
  });

  const response = await fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${apiKey}`
    },
    body: JSON.stringify({
      model: model,
      messages: messages
    })
  });

  if (!response.ok) {
    const errorData = await response.json().catch(() => ({}));
    throw new Error(errorData.error?.message || `HTTP ${response.status}`);
  }

  const data = await response.json();
  const choiceText = data.choices?.[0]?.message?.content;
  if (!choiceText) {
    throw new Error('Empty response from OpenAI API');
  }

  return choiceText;
}

// Enhance UI with Copy Buttons for code blocks
function addCopyButtons(container) {
  const codeBlocks = container.querySelectorAll('pre');
  codeBlocks.forEach(pre => {
    pre.classList.add('relative', 'group', 'p-3', 'bg-black/40', 'rounded-lg', 'border', 'border-white/5', 'my-2', 'overflow-x-auto');
    
    // Check if code element exists
    const code = pre.querySelector('code');
    if (!code) return;
    
    const copyBtn = document.createElement('button');
    copyBtn.className = 'absolute right-2 top-2 p-1.5 rounded bg-white/5 hover:bg-indigo-600/30 hover:text-indigo-400 text-slate-400 opacity-0 group-hover:opacity-100 transition-all text-xs font-semibold';
    copyBtn.innerHTML = '<i data-lucide="copy" class="w-3.5 h-3.5"></i>';
    
    copyBtn.addEventListener('click', async () => {
      try {
        await navigator.clipboard.writeText(code.innerText);
        copyBtn.innerHTML = '<i data-lucide="check" class="w-3.5 h-3.5 text-emerald-400"></i>';
        setTimeout(() => {
          copyBtn.innerHTML = '<i data-lucide="copy" class="w-3.5 h-3.5"></i>';
          lucide.createIcons({ attrs: { class: 'w-3.5 h-3.5' } });
        }, 1500);
        lucide.createIcons({ attrs: { class: 'w-3.5 h-3.5 text-emerald-400' } });
      } catch (err) {
        console.error('Failed to copy text:', err);
      }
    });

    pre.appendChild(copyBtn);
  });
  
  lucide.createIcons();
}

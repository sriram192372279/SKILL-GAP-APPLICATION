// settings-renderer.js

const defaultPrompt = `You are an expert AI Interview Copilot.
Your task is to analyze the screenshot of the interview session (or transcription text).
Provide concise, structured guidance for the candidate:
1. Identify the core question being asked.
2. Provide 3-4 bullet points of essential Talking Points / Keywords to cover.
3. Offer a concise, well-structured, professional answer (1-2 paragraphs max).
4. If it's a technical or coding question, write a clean, compact code snippet in the appropriate language.
Keep it brief and readable so the candidate can scan it quickly.`;

document.addEventListener('DOMContentLoaded', async () => {
  const providerSelect = document.getElementById('api-provider');
  const apiKeyInput = document.getElementById('api-key');
  const apiModelInput = document.getElementById('api-model');
  const captureSourceSelect = document.getElementById('capture-source');
  const captureIntervalSelect = document.getElementById('capture-interval');
  const systemPromptTextarea = document.getElementById('system-prompt');
  const btnToggleKey = document.getElementById('btn-toggle-key');
  const btnRefreshSources = document.getElementById('btn-refresh-sources');
  const btnSave = document.getElementById('btn-save');
  const btnCancel = document.getElementById('btn-cancel');

  // Load saved values
  providerSelect.value = localStorage.getItem('apiProvider') || 'gemini';
  apiKeyInput.value = localStorage.getItem('apiKey') || '';
  apiModelInput.value = localStorage.getItem('apiModel') || '';
  captureIntervalSelect.value = localStorage.getItem('captureInterval') || '0';
  systemPromptTextarea.value = localStorage.getItem('systemPrompt') || defaultPrompt;

  // Set default model on provider change if model is empty
  providerSelect.addEventListener('change', () => {
    const currentModel = apiModelInput.value.trim();
    if (!currentModel || currentModel === 'gemini-1.5-flash' || currentModel === 'gpt-4o') {
      apiModelInput.value = providerSelect.value === 'gemini' ? 'gemini-1.5-flash' : 'gpt-4o';
    }
  });

  // Toggle API Key visibility
  btnToggleKey.addEventListener('click', () => {
    const isPassword = apiKeyInput.type === 'password';
    apiKeyInput.type = isPassword ? 'text' : 'password';
    const icon = btnToggleKey.querySelector('i');
    if (icon) {
      icon.setAttribute('data-lucide', isPassword ? 'eye-off' : 'eye');
      lucide.createIcons();
    }
  });

  // Refresh Desktop Sources
  async function refreshSources() {
    try {
      captureSourceSelect.innerHTML = '<option value="">Loading sources...</option>';
      const sources = await window.electronAPI.getDesktopSources();
      
      captureSourceSelect.innerHTML = '<option value="">Select a window or screen...</option>';
      const savedSourceId = localStorage.getItem('captureSourceId') || '';

      sources.forEach(source => {
        const option = document.createElement('option');
        option.value = source.id;
        option.textContent = source.name;
        if (source.id === savedSourceId) {
          option.selected = true;
        }
        captureSourceSelect.appendChild(option);
      });
    } catch (err) {
      console.error('Failed to get desktop sources:', err);
      captureSourceSelect.innerHTML = '<option value="">Error loading sources</option>';
    }
  }

  // Initial source refresh
  await refreshSources();

  btnRefreshSources.addEventListener('click', refreshSources);

  // Save Settings
  btnSave.addEventListener('click', () => {
    localStorage.setItem('apiProvider', providerSelect.value);
    localStorage.setItem('apiKey', apiKeyInput.value.trim());
    localStorage.setItem('apiModel', apiModelInput.value.trim() || (providerSelect.value === 'gemini' ? 'gemini-1.5-flash' : 'gpt-4o'));
    localStorage.setItem('captureSourceId', captureSourceSelect.value);
    localStorage.setItem('captureSourceName', captureSourceSelect.options[captureSourceSelect.selectedIndex]?.text || '');
    localStorage.setItem('captureInterval', captureIntervalSelect.value);
    localStorage.setItem('systemPrompt', systemPromptTextarea.value);

    // Show Toast
    showToast('Settings saved successfully!');

    // Close window after short delay to let user see toast
    setTimeout(() => {
      window.close();
    }, 1000);
  });

  // Cancel / Close
  btnCancel.addEventListener('click', () => {
    window.close();
  });

  function showToast(message) {
    const toast = document.getElementById('toast');
    const toastMsg = document.getElementById('toast-message');
    toastMsg.textContent = message;
    toast.style.opacity = '1';
    toast.style.transform = 'translateY(0)';
    
    setTimeout(() => {
      toast.style.opacity = '0';
      toast.style.transform = 'translateY(20px)';
    }, 2000);
  }
});

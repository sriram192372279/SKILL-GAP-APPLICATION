import { useState, useCallback } from 'react';
import { useApp } from '../context/AppContext';
import { 
  Upload, FileText, CheckCircle, Loader, X, Zap, Target, Eye, Star, 
  Briefcase, ChevronRight, Lock, Map, Brain, Building2, TrendingUp, AlertTriangle, Sparkles 
} from 'lucide-react';
import * as pdfjsLib from 'pdfjs-dist';
// @ts-ignore
import pdfWorkerUrl from 'pdfjs-dist/build/pdf.worker.min.mjs?url';

// Initialize PDF.js worker using local Vite asset URL to bypass CORS
pdfjsLib.GlobalWorkerOptions.workerSrc = pdfWorkerUrl;

function ScoreBar({ label, value, color }: { label: string; value: number; color: string }) {
  return (
    <div className="space-y-1.5">
      <div className="flex justify-between items-center">
        <span className="text-xs text-slate-400">{label}</span>
        <span className="text-xs font-bold" style={{ color }}>{value}/100</span>
      </div>
      <div className="progress-bar">
        <div className="progress-fill" style={{ width: `${value}%`, background: `linear-gradient(90deg, ${color}, ${color}99)` }} />
      </div>
    </div>
  );
}

const SCORE_COLORS = ['#0ea5e9', '#d946ef', '#00ff88', '#ff6b35', '#fbbf24', '#60a5fa'];

const JOB_ROLES = [
  { id: 'Software Developer', label: 'Software Developer', icon: '💻', desc: 'Full-stack, backend & general dev' },
  { id: 'Frontend Developer', label: 'Frontend Developer', icon: '🎨', desc: 'React, Vue, UI/UX engineering' },
  { id: 'Backend Developer', label: 'Backend Developer', icon: '⚙️', desc: 'APIs, databases, server-side' },
  { id: 'Full Stack Developer', label: 'Full Stack Developer', icon: '🔁', desc: 'End-to-end web development' },
  { id: 'Data Scientist', label: 'Data Scientist', icon: '📊', desc: 'ML, analytics, Python & stats' },
  { id: 'Machine Learning Engineer', label: 'ML Engineer', icon: '🤖', desc: 'Deep learning, MLOps, AI systems' },
  { id: 'DevOps Engineer', label: 'DevOps Engineer', icon: '🚀', desc: 'CI/CD, cloud, Kubernetes' },
  { id: 'Android Developer', label: 'Android Developer', icon: '📱', desc: 'Kotlin, Jetpack, mobile apps' },
];

const INDUSTRIES = [
  'Technology & Software',
  'Banking & Finance',
  'Management Consulting',
  'Healthcare & Biotech',
  'E-commerce & Retail',
  'Media & Entertainment',
  'Automotive & Aerospace',
  'Education & EdTech'
];

const LOCKED_CARDS = [
  { title: 'Skill Gap Analysis', desc: 'Identify critical technology gaps in your skill set.', icon: Target, color: '#0ea5e9' },
  { title: 'Learning Roadmap', desc: 'Personalized study schedule with verified courses & labs.', icon: Map, color: '#f87171' },
  { title: 'Job Matcher', desc: 'Evaluate compatibility with top job postings.', icon: Briefcase, color: '#00ff88' },
  { title: 'Interview Preparation', desc: 'Generate customized HR and technical interview drills.', icon: Brain, color: '#d946ef' },
  { title: 'Company Preparation', desc: 'Review specific interview pipelines for target companies.', icon: Building2, color: '#fbbf24' },
  { title: 'Career Mentor', desc: 'Interactive AI coaching agent for career development.', icon: Sparkles, color: '#60a5fa' },
];

// Intelligent text extractor from files (PDF, TXT, DOCX fallback)
async function extractTextFromBuffer(buffer: ArrayBuffer, file: File): Promise<string> {
  const fileName = file.name.toLowerCase();
  
  if (fileName.endsWith('.txt')) {
    const decoder = new TextDecoder('utf-8');
    return decoder.decode(buffer);
  }
  
  if (fileName.endsWith('.pdf')) {
    try {
      const loadingTask = pdfjsLib.getDocument({ data: new Uint8Array(buffer) });
      const pdf = await loadingTask.promise;
      let fullText = '';
      
      for (let i = 1; i <= pdf.numPages; i++) {
        const page = await pdf.getPage(i);
        const textContent = await page.getTextContent();
        const pageText = textContent.items.map((item: any) => item.str).join(' ');
        fullText += pageText + '\n';
      }
      return fullText.trim();
    } catch (error) {
      console.error("PDF Extraction error:", error);
      return "Error extracting PDF text. Please paste the resume text manually.";
    }
  }

  // Fallback raw binary extraction for docx or other formats
  const bytes = new Uint8Array(buffer);
  let text = '';
  for (let i = 0; i < bytes.length; i++) {
    const char = bytes[i];
    if ((char >= 32 && char <= 126) || char === 10 || char === 13 || char === 9) {
      text += String.fromCharCode(char);
    } else if (char > 127) {
      text += ' ';
    }
  }
  let cleanText = text
    .replace(/[\x00-\x08\x0B\x0C\x0E-\x1F\x7F]/g, '')
    .replace(/\s+/g, ' ')
    .trim();
  return cleanText;
}

function StatusTracker({ status }: { status: 'not_uploaded' | 'uploaded' | 'completed' }) {
  return (
    <div className="glass-card p-4 flex flex-col md:flex-row justify-between items-center gap-4 border border-white/10">
      <div className="flex items-center gap-2">
        <span className="text-xs font-semibold text-slate-400 uppercase tracking-wider">Setup Progress:</span>
        <span className={`text-xs font-bold px-2 py-0.5 rounded-full ${
          status === 'completed' ? 'bg-green-500/15 text-green-400' :
          status === 'uploaded' ? 'bg-blue-500/15 text-blue-400 animate-pulse' :
          'bg-slate-500/15 text-slate-400'
        }`}>
          {status === 'completed' ? '✓ Analysis Completed' :
           status === 'uploaded' ? '• Resume Uploaded' :
           '• Not Uploaded'}
        </span>
      </div>
      <div className="flex items-center gap-2 sm:gap-4 flex-wrap">
        {[
          { id: 'not_uploaded', label: '1. Upload Resume', active: status === 'uploaded' || status === 'completed', current: status === 'not_uploaded' },
          { id: 'uploaded', label: '2. Career Setup', active: status === 'completed', current: status === 'uploaded' },
          { id: 'completed', label: '3. Features Unlocked', active: status === 'completed', current: status === 'completed' }
        ].map(step => (
          <div key={step.id} className="flex items-center gap-2">
            <div className={`w-5 h-5 rounded-full flex items-center justify-center text-[10px] font-bold ${
              step.active ? 'bg-green-500 text-white' : step.current ? 'bg-primary-500 text-white animate-pulse' : 'bg-white/5 text-slate-500'
            }`}>
              {step.active ? '✓' : ''}
            </div>
            <span className={`text-xs font-medium ${step.active ? 'text-green-400' : step.current ? 'text-white' : 'text-slate-500'}`}>
              {step.label}
            </span>
          </div>
        ))}
      </div>
    </div>
  );
}

export default function ResumePage() {
  const { 
    resumeText, resumeScore, isAnalyzing, analyzeResume, setActiveModule, 
    setSelectedRole, selectedRole, setSelectedCompany, selectedCompany,
    setSelectedIndustry, selectedIndustry 
  } = useApp();
  const [dragging, setDragging] = useState(false);
  const [fileName, setFileName] = useState('');
  const [localText, setLocalText] = useState(resumeText);
  const [tab, setTab] = useState<'upload' | 'paste'>('upload');
  const [parsing, setParsing] = useState(false);

  // Step: 'input' | 'career' | 'done'
  const [step, setStep] = useState<'input' | 'career' | 'done'>(resumeScore ? 'done' : 'input');
  
  const [chosenRole, setChosenRole] = useState(selectedRole || 'Software Developer');
  const [chosenIndustry, setChosenIndustry] = useState(selectedIndustry || 'Technology & Software');
  const [chosenCompany, setChosenCompany] = useState(selectedCompany || 'Google');

  const processFile = (file: File) => {
    setFileName(file.name);
    setParsing(true);
    const reader = new FileReader();
    reader.onload = async (ev) => {
      const buffer = ev.target?.result as ArrayBuffer;
      const extractedText = await extractTextFromBuffer(buffer, file);
      setLocalText(extractedText);
      setParsing(false);
    };
    reader.onerror = () => { setParsing(false); };
    reader.readAsArrayBuffer(file);
  };

  const handleDrop = useCallback((e: React.DragEvent) => {
    e.preventDefault();
    setDragging(false);
    const file = e.dataTransfer.files[0];
    if (!file) return;
    processFile(file);
  }, []);

  const handleFileInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;
    processFile(file);
  };

  const handleProceedToCareer = () => {
    if (!localText && !fileName) return;
    setStep('career');
  };

  const handleStartAnalysis = () => {
    setSelectedRole(chosenRole);
    setSelectedIndustry(chosenIndustry);
    setSelectedCompany(chosenCompany);
    setStep('done');
    analyzeResume(
      localText || 'Sample resume with JavaScript React Node.js experience and projects',
      chosenRole,
      chosenCompany,
      chosenIndustry
    );
  };

  const scoreEntries = resumeScore ? [
    { label: 'ATS Score', value: resumeScore.ats },
    { label: 'Technical Score', value: resumeScore.technical },
    { label: 'Readability', value: resumeScore.readability },
    { label: 'Project Strength', value: resumeScore.projectStrength },
    { label: 'Keyword Density', value: resumeScore.keywordDensity },
    { label: 'Employability', value: resumeScore.employability },
  ] : [];

  const resumeStatus: 'not_uploaded' | 'uploaded' | 'completed' = 
    !localText ? 'not_uploaded' : !resumeScore ? 'uploaded' : 'completed';

  return (
    <div className="space-y-6 animate-fade-in">
      <div>
        <div className="section-label w-fit">📄 Resume Intelligence Center</div>
        <h2 className="text-2xl font-display font-bold text-white">Multi-Factor Resume Evaluation</h2>
        <p className="text-slate-400 mt-1">Upload your resume and setup target career options to get a full AI intelligence report</p>
      </div>

      {/* Progress Status Tracker */}
      <StatusTracker status={resumeStatus} />

      {/* ── Step 1: Upload / Paste ── */}
      {step === 'input' && (
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 animate-slide-up">
          {/* Upload Panel */}
          <div className="space-y-4">
            <div className="flex gap-2 p-1 rounded-xl" style={{ background: 'rgba(255,255,255,0.05)' }}>
              {(['upload', 'paste'] as const).map(t => (
                <button key={t} onClick={() => setTab(t)}
                  className={`flex-1 py-2 rounded-lg text-sm font-medium capitalize transition-all ${tab === t ? 'bg-primary-600 text-white' : 'text-slate-400 hover:text-white'}`}>
                  {t === 'upload' ? '📂 Upload File' : '📝 Paste Text'}
                </button>
              ))}
            </div>

            {tab === 'upload' ? (
              <div
                onDragOver={(e) => { e.preventDefault(); setDragging(true); }}
                onDragLeave={() => setDragging(false)}
                onDrop={handleDrop}
                className={`relative rounded-2xl p-8 text-center cursor-pointer transition-all duration-300 border-2 border-dashed ${dragging ? 'border-primary-500 scale-[1.02]' : 'border-white/10 hover:border-primary-500/50'}`}
                style={{ background: dragging ? 'rgba(14,165,233,0.08)' : 'rgba(255,255,255,0.02)' }}>
                <input type="file" accept=".txt,.pdf,.docx" onChange={handleFileInput} className="absolute inset-0 opacity-0 cursor-pointer" />
                <div className="w-16 h-16 rounded-2xl flex items-center justify-center mx-auto mb-4" style={{ background: 'rgba(14,165,233,0.1)' }}>
                  {parsing ? <Loader size={28} className="text-primary-400 animate-spin" /> : <Upload size={28} className="text-primary-400" />}
                </div>
                {fileName ? (
                  <div className="flex items-center justify-center gap-2">
                    <FileText size={16} className="text-primary-400" />
                    <span className="text-sm font-medium text-white">{fileName}</span>
                    <button onClick={(e) => { e.stopPropagation(); setFileName(''); setLocalText(''); }}>
                      <X size={14} className="text-slate-500" />
                    </button>
                  </div>
                ) : (
                  <>
                    <p className="text-slate-300 font-medium">Drop your resume here</p>
                    <p className="text-slate-500 text-sm mt-1">Supports PDF, DOCX, TXT</p>
                  </>
                )}
              </div>
            ) : (
              <textarea value={localText} onChange={e => setLocalText(e.target.value)} rows={10}
                placeholder="Paste your resume content here..."
                className="w-full input-field resize-none font-mono text-xs leading-relaxed" />
            )}

            <button
              onClick={handleProceedToCareer}
              disabled={parsing || (!localText && !fileName)}
              className="btn-primary w-full flex items-center justify-center gap-2 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              <ChevronRight size={16} />
              <span>Next: Career Setup</span>
            </button>

            <div className="flex items-center gap-2 text-xs text-slate-500 p-3 rounded-xl" style={{ background: 'rgba(255,255,255,0.03)' }}>
              <CheckCircle size={14} className="text-green-500 flex-shrink-0" />
              <span>🔒 Privacy-first: All analysis runs locally. Your resume never leaves your browser.</span>
            </div>
          </div>

          {/* Info Panel */}
          <div className="glass-card p-8 flex flex-col items-center justify-center text-center gap-4">
            <div className="w-20 h-20 rounded-2xl flex items-center justify-center mx-auto" style={{ background: 'rgba(14,165,233,0.08)' }}>
              <FileText size={36} className="text-primary-400/50" />
            </div>
            <div>
              <p className="text-white font-semibold mb-1">How it works</p>
              <ol className="text-sm text-slate-400 space-y-2 text-left list-none">
                {[
                  '📤 Upload or paste your resume (PDF, DOCX, TXT)',
                  '🎯 Select the job role, target industry & dream company',
                  '🤖 AI analyzes your skill gaps & resume metrics',
                  '📋 Get a full report of what\'s missing',
                  '🗺️ Follow a personalized study roadmap',
                ].map((s, i) => (
                  <li key={i} className="flex items-center gap-2">
                    <span className="text-primary-400 font-bold text-xs">{i + 1}.</span> {s}
                  </li>
                ))}
              </ol>
            </div>
          </div>
        </div>
      )}

      {/* ── Step 2: Career Targets Setup ── */}
      {step === 'career' && (
        <div className="space-y-6 animate-slide-up">
          {/* Header */}
          <div className="glass-card p-5 flex items-center gap-4" style={{ background: 'linear-gradient(135deg,rgba(14,165,233,0.08),rgba(217,70,239,0.08))' }}>
            <div className="w-10 h-10 rounded-xl flex items-center justify-center flex-shrink-0" style={{ background: 'rgba(14,165,233,0.15)' }}>
              <Briefcase size={20} className="text-primary-400" />
            </div>
            <div className="flex-1">
              <p className="text-white font-semibold">Configure Your Career Targets</p>
              <p className="text-slate-400 text-sm mt-0.5">
                Resume loaded. Provide your target parameters to customize study maps, matches and question banks.
              </p>
            </div>
            <button onClick={() => setStep('input')} className="text-slate-500 hover:text-white transition-colors">
              <X size={18} />
            </button>
          </div>

          {/* Dream Role selection grid */}
          <div className="space-y-3">
            <label className="text-xs text-slate-400 font-semibold block uppercase tracking-wider">Dream Role</label>
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-3">
              {JOB_ROLES.map(role => (
                <button
                  type="button"
                  key={role.id}
                  onClick={() => setChosenRole(role.id)}
                  className={`glass-card p-4 text-left transition-all duration-200 hover:scale-[1.03] ${chosenRole === role.id ? 'ring-2 ring-primary-500' : ''}`}
                  style={chosenRole === role.id ? { background: 'rgba(14,165,233,0.1)', borderColor: 'rgba(14,165,233,0.4)' } : {}}
                >
                  <div className="text-2xl mb-2">{role.icon}</div>
                  <p className="text-sm font-semibold text-white">{role.label}</p>
                  <p className="text-xs text-slate-500 mt-0.5">{role.desc}</p>
                  {chosenRole === role.id && (
                    <div className="mt-2">
                      <CheckCircle size={14} className="text-primary-400" />
                    </div>
                  )}
                </button>
              ))}
            </div>
            <div className="glass-card p-4">
              <label className="text-xs text-slate-400 font-medium">Or type a custom role:</label>
              <input
                type="text"
                value={JOB_ROLES.find(r => r.id === chosenRole) ? '' : chosenRole}
                onChange={e => setChosenRole(e.target.value)}
                placeholder="e.g. Cloud Architect, QA Engineer, iOS Developer..."
                className="input-field mt-2 w-full"
              />
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {/* Target Industry Dropdown */}
            <div className="glass-card p-5 space-y-2">
              <label className="text-xs text-slate-400 font-semibold uppercase tracking-wider block">Target Industry</label>
              <select
                value={chosenIndustry}
                onChange={e => setChosenIndustry(e.target.value)}
                className="w-full input-field bg-slate-900 border border-white/10 rounded-xl px-4 py-3 text-sm text-white focus:outline-none focus:border-primary-500"
              >
                {INDUSTRIES.map(ind => (
                  <option key={ind} value={ind} className="bg-slate-900 text-white">
                    {ind}
                  </option>
                ))}
              </select>
            </div>

            {/* Target Company input */}
            <div className="glass-card p-5 space-y-2">
              <label className="text-xs text-slate-400 font-semibold uppercase tracking-wider block">Dream Company (Optional)</label>
              <input
                type="text"
                value={chosenCompany}
                onChange={e => setChosenCompany(e.target.value)}
                placeholder="e.g. Google, Microsoft, Amazon"
                className="input-field w-full"
              />
            </div>
          </div>

          {/* Analyze Button */}
          <button
            onClick={handleStartAnalysis}
            disabled={!chosenRole || !chosenIndustry}
            className="btn-primary w-full flex items-center justify-center gap-2 disabled:opacity-50 disabled:cursor-not-allowed py-4 text-base font-semibold"
          >
            <Zap size={18} />
            <span>Analyze Resume & Lock Settings</span>
          </button>
        </div>
      )}

      {/* ── Step 3: Results (Only visible once analysis complete or for return visits) ── */}
      {step === 'done' && (
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 animate-slide-up">
          {/* Re-analyze panel */}
          <div className="space-y-4">
            <div className="glass-card p-4 flex items-center gap-3" style={{ background: 'rgba(14,165,233,0.05)' }}>
              <FileText size={18} className="text-primary-400 flex-shrink-0" />
              <div className="flex-1 min-w-0">
                <p className="text-sm font-medium text-white truncate">{fileName || 'Pasted Resume'}</p>
                <p className="text-xs text-slate-500">Targeting: <span className="text-primary-400">{selectedRole}</span> in {selectedIndustry}</p>
              </div>
              <button
                onClick={() => { setStep('input'); setFileName(''); setLocalText(''); }}
                className="text-xs text-slate-400 hover:text-white px-3 py-1.5 rounded-lg transition-all"
                style={{ background: 'rgba(255,255,255,0.06)' }}
              >
                Re-upload
              </button>
            </div>

            <button
              onClick={() => setStep('career')}
              className="w-full flex items-center justify-center gap-2 py-3 rounded-xl text-sm font-medium text-slate-300 hover:text-white transition-all"
              style={{ background: 'rgba(255,255,255,0.04)', border: '1px solid rgba(255,255,255,0.08)' }}
            >
              <Briefcase size={14} />
              <span>Change Targets (currently: {selectedRole} | {selectedCompany})</span>
            </button>
          </div>

          {/* Results Panel */}
          <div className="space-y-4">
            {isAnalyzing ? (
              <div className="glass-card p-8 text-center">
                <div className="w-16 h-16 mx-auto mb-4 relative">
                  <div className="absolute inset-0 rounded-full border-4 border-primary-500/20 animate-ping" />
                  <div className="w-full h-full rounded-full border-4 border-t-primary-500 border-primary-500/20 animate-spin" />
                </div>
                <p className="text-white font-semibold">Analyzing your resume...</p>
                <p className="text-slate-400 text-sm mt-1">Running 6-factor AI evaluation for <span className="text-primary-400">{selectedRole}</span></p>
              </div>
            ) : resumeScore ? (
              <>
                {/* Overall Score */}
                <div className="glass-card p-5 text-center" style={{ background: 'linear-gradient(135deg,rgba(14,165,233,0.1),rgba(217,70,239,0.1))' }}>
                  <div className="text-5xl font-display font-black gradient-text mb-1">{resumeScore.overall}</div>
                  <p className="text-slate-300 font-medium">Overall Resume Score</p>
                  <p className="text-xs text-slate-500 mt-1">For: <span className="text-primary-400 font-medium">{selectedRole}</span></p>
                  <div className="flex justify-center gap-2 mt-3">
                    {resumeScore.overall >= 80 ? (
                      <span className="badge-green"><Star size={10} /> Excellent</span>
                    ) : resumeScore.overall >= 60 ? (
                      <span className="badge-blue"><Star size={10} /> Good</span>
                    ) : (
                      <span className="badge-orange"><Star size={10} /> Needs Work</span>
                    )}
                  </div>
                </div>

                {/* Score Bars */}
                <div className="glass-card p-5 space-y-4">
                  <h3 className="text-sm font-semibold text-white flex items-center gap-2"><Target size={14} className="text-primary-400" /> Score Breakdown</h3>
                  {scoreEntries.map(({ label, value }, i) => (
                    <ScoreBar key={label} label={label} value={value} color={SCORE_COLORS[i]} />
                  ))}
                </div>

                {/* Actions */}
                <div className="grid grid-cols-3 gap-2">
                  {[
                    { label: 'Skill Gap', mod: 'skillgap', icon: Target },
                    { label: 'Roadmap', mod: 'roadmap', icon: Eye },
                    { label: 'Risk', mod: 'career-risk', icon: Zap },
                  ].map(({ label, mod, icon: Icon }) => (
                    <button key={mod} onClick={() => setActiveModule(mod)}
                      className="glass-card p-3 flex flex-col items-center gap-1.5 hover:scale-[1.03] transition-all text-slate-400 hover:text-white">
                      <Icon size={16} className="text-primary-400" />
                      <span className="text-xs">{label}</span>
                    </button>
                  ))}
                </div>
              </>
            ) : (
              <div className="glass-card p-8 text-center h-full flex flex-col items-center justify-center">
                <div className="w-20 h-20 rounded-2xl flex items-center justify-center mx-auto mb-4" style={{ background: 'rgba(14,165,233,0.08)' }}>
                  <FileText size={36} className="text-primary-400/50" />
                </div>
                <p className="text-slate-400">Upload or paste your resume to see your AI-powered score analysis</p>
              </div>
            )}
          </div>
        </div>
      )}

      {/* Locked Preview Cards */}
      {!resumeScore && (
        <div className="space-y-4 pt-6 border-t border-white/5 animate-fade-in">
          <div className="flex items-center gap-2">
            <Lock size={16} className="text-slate-500" />
            <h3 className="text-sm font-semibold uppercase tracking-wider text-slate-400">Locked Features Preview</h3>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {LOCKED_CARDS.map(({ title, desc, icon: Icon, color }) => (
              <div key={title} className="glass-card p-5 border border-white/5 opacity-60 hover:opacity-80 transition-opacity relative group overflow-hidden">
                <div className="absolute top-3 right-3 text-slate-500 group-hover:text-white transition-colors">
                  <Lock size={16} />
                </div>
                <div className="flex items-center gap-3 mb-3">
                  <div className="w-10 h-10 rounded-xl flex items-center justify-center" style={{ background: `${color}15` }}>
                    <Icon size={20} style={{ color }} />
                  </div>
                  <h4 className="text-sm font-bold text-white">{title}</h4>
                </div>
                <p className="text-xs text-slate-400">{desc}</p>
                <p className="text-[10px] text-primary-400 mt-3 font-medium flex items-center gap-1">
                  🔒 Upload and analyze your resume to unlock this feature.
                </p>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

import { useState } from 'react';
import { useApp } from '../context/AppContext';
import { Brain, Zap, RefreshCw, AlertTriangle, Play } from 'lucide-react';
import { COMPANIES, ROLES } from '../data/staticData';

export default function InterviewGenPage() {
  const { selectedCompany, selectedRole, skillGap, resumeScore, setActiveModule } = useApp();
  const [company, setCompany] = useState(selectedCompany);
  const [role, setRole] = useState(selectedRole);
  const [generated, setGenerated] = useState(false);
  const [activeType, setActiveType] = useState<'technical' | 'coding' | 'hr' | 'scenario'>('technical');

  // Fallback default base questions database
  const BASE_QUESTIONS: Record<string, string[]> = {
    technical: [
      'Explain the difference between SQL and NoSQL database indexing. How would you choose between them?',
      'What are microservices, and how do you handle data consistency across database boundaries?',
      'Describe the lifecycle of a thread. How do concurrency primitives differ in high-scale backends?',
      'What is a content delivery network (CDN), and how does it optimize media rendering pathways?',
    ],
    coding: [
      'Write an optimized algorithm to detect a cycle in a directed graph using topological sort.',
      'Implement an LRU Cache with constant time complexity O(1) for retrieval and insertion.',
      'Find the longest common subsequence between two input strings using bottom-up dynamic programming.',
      'Design a scalable URL shortener system, including database schemas, routing, and caching.',
    ],
    hr: [
      'Tell me about a time you worked on a highly ambiguous software requirement. How did you resolve the specs?',
      'Describe a scenario where you made a critical technical mistake. What did you learn and how did you communicate it?',
      'How do you manage deadlines when multiple senior stack stakeholders present conflicting priorities?',
      'Why do you want to join our engineering division specifically, and how do our cultural guidelines fit you?',
    ],
    scenario: [
      'Your production server CPU utilization spikes to 99%. Walk me through your step-by-step diagnostic strategy.',
      'A database transaction locks up mid-migration, affecting active checkout pipelines. What is your immediate mitigation action?',
      'A fellow senior developer insists on a custom routing architecture, while you prefer standard templates. How do you resolve this?',
      'Design a secure payment API flow that guarantees exactly-once processing even during high network drop rates.',
    ],
  };

  // Helper to dynamically build highly custom personalized questions based on user's skill gaps
  const getDynamicQuestions = (type: 'technical' | 'coding' | 'hr' | 'scenario') => {
    const list = [...BASE_QUESTIONS[type]];
    const missing = skillGap?.missingSkills || [];
    const roleTitle = role;
    const targetComp = company;

    if (type === 'technical') {
      if (missing.length > 0) {
        list.unshift(`Since your profile shows a gap in [${missing[0]}], can you explain its core architectural principles and how you plan to master it?`);
      }
      if (missing.length > 1) {
        list.unshift(`Your resume lacks demonstrated experience with [${missing[1]}]. In ${targetComp}'s stack, this is used extensively. How would you quickly design a basic prototype using it?`);
      }
    } else if (type === 'coding') {
      if (missing.length > 0) {
        list.unshift(`[Coding Challenge] Write a clean, highly optimized implementation using [${missing[0] || 'your core language'}] to showcase algorithmic proficiency.`);
      }
    } else if (type === 'hr') {
      list.unshift(`At ${targetComp}, we value quick adaptation. Tell me how you solved a complex development issue when you had zero prior experience with the required technology.`);
    } else if (type === 'scenario') {
      if (missing.length > 0) {
        list.unshift(`[System Design Scenario] You are tasked at ${targetComp} to integrate [${missing[0]}] into a legacy monolithic system. How do you ensure high availability and zero downtime during the transition?`);
      }
    }

    return list.slice(0, 4);
  };

  if (!skillGap) {
    return (
      <div className="space-y-6 animate-fade-in">
        <div>
          <div className="section-label w-fit">🧠 Personalized Interview Gen</div>
          <h2 className="text-2xl font-display font-bold text-white">Personalized Interview Generator</h2>
          <p className="text-slate-400 mt-1">Get custom questions targeted at your exact resume weaknesses</p>
        </div>
        <div className="glass-card p-12 text-center flex flex-col items-center gap-4"
          style={{ border: '2px dashed rgba(217,70,239,0.2)' }}>
          <AlertTriangle size={48} className="text-accent-400/30" />
          <p className="text-slate-400 text-lg font-medium">No Resume Analysis Found</p>
          <p className="text-slate-500 text-sm">We need to understand your resume weaknesses and target role to generate custom questions.</p>
          <button onClick={() => setActiveModule('resume')} className="btn-primary flex items-center gap-2 mt-2">
            <Zap size={14} />Analyze My Resume
          </button>
        </div>
      </div>
    );
  }

  const currentQuestions = getDynamicQuestions(activeType);

  return (
    <div className="space-y-6 animate-fade-in">
      <div>
        <div className="section-label w-fit">🧠 PATENT FEATURE</div>
        <h2 className="text-2xl font-display font-bold text-white">Personalized Interview Generator</h2>
        <p className="text-slate-400 mt-1">AI-generated questions tailored to your skill gaps, weaknesses, target company, and role</p>
      </div>

      {/* Config */}
      <div className="glass-card p-5 grid grid-cols-1 sm:grid-cols-2 gap-4">
        <div>
          <label className="text-xs text-slate-400 block mb-1.5 font-semibold uppercase">Target Employer</label>
          <select value={company} onChange={e => setCompany(e.target.value)} className="input-field">
            {COMPANIES.map(c => <option key={c.id} value={c.name} style={{ background: '#0d1422' }}>{c.name}</option>)}
          </select>
        </div>
        <div>
          <label className="text-xs text-slate-400 block mb-1.5 font-semibold uppercase">Target Career Track</label>
          <select value={role} onChange={e => setRole(e.target.value)} className="input-field">
            {ROLES.map(r => <option key={r.id} value={r.title} style={{ background: '#0d1422' }}>{r.title}</option>)}
          </select>
        </div>
        <div className="sm:col-span-2">
          <button onClick={() => setGenerated(true)} className="btn-primary w-full flex items-center justify-center gap-2">
            <Zap size={14} />Generate Dynamic Weakness-Targeted Questions
          </button>
        </div>
      </div>

      {generated && (
        <>
          {/* Question Category Selectors */}
          <div className="flex flex-wrap gap-2">
            {(['technical', 'coding', 'hr', 'scenario'] as const).map(type => (
              <button key={type} onClick={() => setActiveType(type)}
                className={`flex-1 py-2.5 rounded-xl text-xs sm:text-sm font-semibold capitalize transition-all min-w-[100px] ${activeType === type ? 'text-white' : 'text-slate-400 hover:text-white'}`}
                style={activeType === type ? { background: 'linear-gradient(135deg,#0ea5e9,#d946ef)' } : { background: 'rgba(255,255,255,0.04)', border: '1px solid rgba(255,255,255,0.06)' }}>
                {type === 'hr' ? 'Behavioral (HR)' : type === 'scenario' ? 'Scenario-Based' : type}
              </button>
            ))}
          </div>

          {/* Question List */}
          <div className="space-y-3">
            <div className="flex items-center justify-between">
              <h3 className="text-sm font-semibold text-white">
                Personalized Questions for <span className="gradient-text">{company}</span> — <span className="gradient-text">{role}</span>
              </h3>
              <button onClick={() => setGenerated(false)} className="flex items-center gap-1 text-xs text-slate-500 hover:text-white">
                <RefreshCw size={11} />Regenerate Stack
              </button>
            </div>
            {currentQuestions.map((q, i) => (
              <div key={i} className="glass-card p-4 hover:scale-[1.01] transition-all group" style={{ border: '1px solid rgba(255,255,255,0.06)' }}>
                <div className="flex items-start gap-3 justify-between">
                  <div className="flex items-start gap-3">
                    <div className="w-6 h-6 rounded-full flex items-center justify-center text-xs font-bold flex-shrink-0" style={{ background: 'linear-gradient(135deg,#0ea5e9,#d946ef)', color: 'white' }}>{i + 1}</div>
                    <p className="text-sm text-slate-300 leading-relaxed font-medium">{q}</p>
                  </div>
                  <button onClick={() => setActiveModule('mentor')} className="text-[10px] text-primary-400 bg-primary-400/10 border border-primary-400/20 px-2 py-1 rounded-lg hover:bg-primary-400/20 transition-all flex items-center gap-1 flex-shrink-0">
                    <Play size={8} /> Mock Practice
                  </button>
                </div>
              </div>
            ))}
          </div>

          {/* AI Strategy Info */}
          <div className="glass-card p-4 flex items-center gap-3" style={{ background: 'rgba(14,165,233,0.06)', border: '1px solid rgba(14,165,233,0.15)' }}>
            <Brain size={16} className="text-primary-400 flex-shrink-0" />
            <p className="text-xs text-slate-400 leading-relaxed">
              These questions are generated based on the <span className="text-yellow-400 font-bold">{skillGap.missingSkills.length} missing skill gaps</span> from your resume. Practice mock sessions by clicking "Mock Practice" to load them into the AI Career Mentor.
            </p>
          </div>
        </>
      )}
    </div>
  );
}

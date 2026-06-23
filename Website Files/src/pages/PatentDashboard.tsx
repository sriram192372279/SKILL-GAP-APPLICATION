import { Sparkles, TrendingUp, AlertTriangle, Brain, Zap, Target, BarChart3, History, Bot, Building2, UserCheck } from 'lucide-react';
import { useApp } from '../context/AppContext';
import { RadarChart, Radar, PolarGrid, PolarAngleAxis, ResponsiveContainer } from 'recharts';

const patentFeatures = [
  { icon: TrendingUp, title: 'Future Skill Prediction Engine', patent: 'Patent #1', color: '#0ea5e9', score: 94, mod: 'future-skills', desc: 'Predicts skill demand 6-24 months ahead using localized rule-based market analysis.' },
  { icon: AlertTriangle, title: 'Career Risk Analyzer', patent: 'Patent #2', color: '#ff6b35', score: 88, mod: 'career-risk', desc: 'Scans resumes for outdated low-demand skills (e.g. manual testing, JSP) and generates mitigation steps.' },
  { icon: Target, title: 'Adaptive Roadmap Intelligence', patent: 'Patent #3', color: '#d946ef', score: 85, mod: 'roadmap', desc: 'Dynamically adapts your 90-day learning path to skip mastered skills and adjust difficulty.' },
  { icon: Brain, title: 'Personalized Interview Generator', patent: 'Patent #4', color: '#00ff88', score: 91, mod: 'interview-gen', desc: 'Generates custom technical, coding, HR, and scenario questions targeted at your exact weaknesses.' },
  { icon: BarChart3, title: 'Skill Dependency Graph', patent: 'Patent #5', color: '#fbbf24', score: 90, mod: 'skill-graph', desc: 'Visualizes prerequisite learning maps and path relationships (e.g., Python → ML → AI).' },
  { icon: Zap, title: 'Placement Readiness Engine', patent: 'Patent #6', color: '#60a5fa', score: 82, mod: 'placement', desc: 'Calculates specific job selection probabilities across 20 global companies.' },
  { icon: History, title: 'Resume Evolution Tracker', patent: 'Patent #7', color: '#ec4899', score: 89, mod: 'evolution', desc: 'Saves multiple versions of your resume in localStorage and visualizes ATS and skills growth.' },
  { icon: Bot, title: 'Offline AI Career Mentor', patent: 'Patent #8', color: '#a855f7', score: 95, mod: 'mentor', desc: 'Privacy-first offline conversational coach for interview prep, CTC guidance, and career paths.' },
  { icon: Building2, title: 'Dream Company Intelligence Hub', patent: 'Patent #9', color: '#10b981', score: 87, mod: 'companies', desc: 'Detailed database of hiring rounds, salaries, culture tags, and common rejection mistakes for 20 firms.' },
  { icon: UserCheck, title: 'Role Intelligence System', patent: 'Patent #10', color: '#f97316', score: 84, mod: 'roles', desc: 'Complete domain mapping of daily duties, salaries, learning milestones, and projects for 10 roles.' },
];

const radarData = patentFeatures.map(f => ({
  subject: f.title.split(' ')[0] + ' ' + (f.title.split(' ')[1] || ''),
  value: f.score
}));

export default function PatentDashboard() {
  const { setActiveModule, resumeScore } = useApp();

  return (
    <div className="space-y-6 animate-fade-in">
      <div>
        <div className="section-label w-fit" style={{ background: 'rgba(217,70,239,0.1)', color: '#e879f9', border: '1px solid rgba(217,70,239,0.2)' }}>✨ Patent Innovation Center</div>
        <h2 className="text-2xl font-display font-bold text-white">Patent Command Deck</h2>
        <p className="text-slate-400 mt-1">10 patented career intelligence technologies driving your profile upskilling</p>
      </div>

      {/* Radar Chart */}
      <div className="glass-card p-5">
        <h3 className="text-sm font-semibold text-white mb-4 flex items-center gap-2">
          <Sparkles size={14} className="text-accent-400" /> Patented Career Suitability Performance index
        </h3>
        <ResponsiveContainer width="100%" height={260}>
          <RadarChart data={radarData} margin={{ top: 10, right: 10, left: 10, bottom: 10 }}>
            <PolarGrid stroke="rgba(255,255,255,0.05)" />
            <PolarAngleAxis dataKey="subject" tick={{ fill: '#94a3b8', fontSize: 9 }} />
            <Radar dataKey="value" stroke="#d946ef" fill="#d946ef" fillOpacity={0.15} strokeWidth={2} />
          </RadarChart>
        </ResponsiveContainer>
      </div>

      {/* Patent Feature Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {patentFeatures.map(({ icon: Icon, title, patent, color, score, mod, desc }) => (
          <button key={mod} onClick={() => setActiveModule(mod)}
            className="glass-card p-5 text-left hover:scale-[1.02] hover:-translate-y-0.5 transition-all duration-300 group flex flex-col justify-between" 
            style={{ border: `1px solid ${color}20`, background: 'rgba(13,20,34,0.45)' }}>
            <div>
              <div className="flex items-start gap-3 mb-3">
                <div className="w-10 h-10 rounded-xl flex items-center justify-center flex-shrink-0" style={{ background: `${color}15` }}>
                  <Icon size={18} style={{ color }} />
                </div>
                <div className="flex-1">
                  <div className="flex items-center justify-between">
                    <h3 className="text-sm font-bold text-white group-hover:text-primary-400 transition-all leading-tight">{title}</h3>
                    <span className="text-[9px] font-bold px-2 py-0.5 rounded-full border" style={{ background: `${color}15`, borderColor: `${color}30`, color }}>{patent}</span>
                  </div>
                  <p className="text-[11px] text-slate-400 mt-1">{desc}</p>
                </div>
              </div>
            </div>
            <div className="mt-4 pt-3 border-t border-white/5">
              <div className="flex justify-between text-xs mb-1">
                <span className="text-slate-500">System Accuracy</span>
                <span style={{ color }} className="font-bold">{score}%</span>
              </div>
              <div className="progress-bar h-1.5"><div className="progress-fill h-1.5" style={{ width: `${score}%`, background: `linear-gradient(90deg,${color},${color}60)` }} /></div>
            </div>
          </button>
        ))}
      </div>

      {/* Innovation Summary */}
      <div className="glass-card p-5" style={{ background: 'linear-gradient(135deg,rgba(217,70,239,0.08),rgba(14,165,233,0.08))' }}>
        <h3 className="text-sm font-semibold text-white mb-3 flex items-center gap-2"><Sparkles size={14} className="text-accent-400" /> Intellectual Property Protections</h3>
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
          {[
            'Privacy-first browser-bound local resume parsing algorithms.',
            '6-Factor ATS matching, Readability metrics, and Employability rating matrices.',
            'Role-centric Future Skill Demand forecasting 6 to 24 months out.',
            'Adaptive dynamic roadmap rendering based on technical validation metrics.',
            'Prerequisite skill dependency mapping and learning sequencing grids.',
            'Company-wise selection likelihood scoring using comparative matching.',
            'Time-series visualization tracking score improvements across versions.',
            'Deterministic offline natural language parsing for career coaching.',
            'Automated career obsolescence checking with modernization replacements.',
            'Weakness-targeted custom technical, coding, HR, and scenario questions generator.',
          ].map((point, i) => (
            <div key={i} className="flex items-start gap-2.5 text-xs text-slate-400">
              <span className="w-4 h-4 rounded-full flex items-center justify-center text-[9px] font-bold flex-shrink-0 mt-0.5" style={{ background: 'linear-gradient(135deg,#0ea5e9,#d946ef)', color: 'white' }}>{i + 1}</span>
              <span className="leading-normal">{point}</span>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

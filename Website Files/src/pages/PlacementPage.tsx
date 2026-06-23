import { useApp } from '../context/AppContext';
import { Zap, Building2, Upload } from 'lucide-react';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, Cell } from 'recharts';

export default function PlacementPage() {
  const { placementReadiness, setActiveModule } = useApp();

  if (!placementReadiness) {
    return (
      <div className="space-y-6 animate-fade-in">
        <div>
          <div className="section-label w-fit">⚡ Placement Readiness</div>
          <h2 className="text-2xl font-display font-bold text-white">Placement Readiness Engine</h2>
          <p className="text-slate-400 mt-1">Your overall job placement probability score</p>
        </div>
        <div className="glass-card p-12 text-center flex flex-col items-center gap-4"
          style={{ border: '2px dashed rgba(14,165,233,0.2)' }}>
          <Zap size={48} className="text-primary-400/30" />
          <p className="text-slate-400 text-lg font-medium">No Analysis Available</p>
          <p className="text-slate-500 text-sm">Your placement readiness is calculated from your resume. Please analyze your resume first.</p>
          <button onClick={() => setActiveModule('resume')} className="btn-primary flex items-center gap-2 mt-2">
            <Upload size={14} />Analyze My Resume
          </button>
        </div>
      </div>
    );
  }

  const metrics = [
    { label: 'Technical Readiness', value: placementReadiness.technical, color: '#0ea5e9' },
    { label: 'Coding Readiness', value: placementReadiness.coding, color: '#d946ef' },
    { label: 'Interview Readiness', value: placementReadiness.interview, color: '#00ff88' },
  ];

  return (
    <div className="space-y-6 animate-fade-in">
      <div>
        <div className="section-label w-fit">⚡ PATENT FEATURE</div>
        <h2 className="text-2xl font-display font-bold text-white">Placement Readiness Engine</h2>
        <p className="text-slate-400 mt-1">Your overall job placement probability score</p>
      </div>

      {/* Overall Score */}
      <div className="glass-card p-6 text-center" style={{ background: 'linear-gradient(135deg,rgba(14,165,233,0.1),rgba(217,70,239,0.1))' }}>
        <div className="text-6xl font-display font-black gradient-text mb-2">{placementReadiness.overall}%</div>
        <p className="text-slate-300 font-semibold">Overall Placement Readiness</p>
        <p className="text-slate-500 text-sm mt-1">
          {placementReadiness.overall >= 80 ? '🎉 Placement Ready! You can apply to top companies now.' :
           placementReadiness.overall >= 60 ? '📈 Good progress! A few more weeks of prep will get you there.' :
           '📚 Keep learning! Focus on your personalized roadmap to improve this score.'}
        </p>
      </div>

      {/* Metric Bars */}
      <div className="glass-card p-5 space-y-4">
        {metrics.map(({ label, value, color }) => (
          <div key={label}>
            <div className="flex justify-between mb-1.5">
              <span className="text-sm text-slate-300">{label}</span>
              <span className="text-sm font-bold" style={{ color }}>{value}%</span>
            </div>
            <div className="progress-bar h-2.5">
              <div className="h-2.5 rounded-full transition-all duration-1000" style={{ width: `${value}%`, background: `linear-gradient(90deg,${color},${color}80)` }} />
            </div>
          </div>
        ))}
      </div>

      {/* Company Readiness */}
      <div className="glass-card p-5">
        <h3 className="text-sm font-semibold text-white mb-4 flex items-center gap-2"><Building2 size={14} className="text-primary-400" /> Company Readiness Score</h3>
        <ResponsiveContainer width="100%" height={220}>
          <BarChart data={placementReadiness.companyReadiness}>
            <XAxis dataKey="company" tick={{ fill: '#64748b', fontSize: 11 }} axisLine={false} tickLine={false} />
            <YAxis tick={{ fill: '#64748b', fontSize: 10 }} axisLine={false} tickLine={false} domain={[0, 100]} />
            <Tooltip contentStyle={{ background: 'rgba(13,20,34,0.95)', border: '1px solid rgba(14,165,233,0.3)', borderRadius: 8, fontSize: 11 }} />
            <Bar dataKey="score" radius={[4, 4, 0, 0]}>
              {placementReadiness.companyReadiness.map((entry, i) => (
                <Cell key={i} fill={entry.score >= 70 ? '#00ff88' : entry.score >= 50 ? '#fbbf24' : '#ef4444'} opacity={0.85} />
              ))}
            </Bar>
          </BarChart>
        </ResponsiveContainer>
      </div>

      {/* Next Actions */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-3">
        {[
          { title: 'Skill Gap Analysis', desc: 'See which skills are missing', icon: '🎯', mod: 'skillgap' },
          { title: 'Learning Roadmap', desc: 'Get your personalized plan', icon: '🗺️', mod: 'roadmap' },
          { title: 'Company Prep', desc: 'Prepare for target companies', icon: '🏢', mod: 'companies' },
        ].map(({ title, desc, icon, mod }) => (
          <button key={title} onClick={() => setActiveModule(mod)} className="glass-card p-4 text-center hover:scale-[1.02] transition-all">
            <div className="text-2xl mb-2">{icon}</div>
            <p className="text-sm font-semibold text-white">{title}</p>
            <p className="text-xs text-slate-500 mt-1">{desc}</p>
          </button>
        ))}
      </div>
    </div>
  );
}

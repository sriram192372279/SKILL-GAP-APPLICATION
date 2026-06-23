import { useApp } from '../context/AppContext';
import { History, TrendingUp, Award, Upload } from 'lucide-react';
import { LineChart, Line, XAxis, YAxis, Tooltip, ResponsiveContainer, Legend } from 'recharts';

export default function EvolutionPage() {
  const { resumeVersions, setActiveModule } = useApp();

  if (resumeVersions.length === 0) {
    return (
      <div className="space-y-6 animate-fade-in">
        <div>
          <div className="section-label w-fit">📈 Resume Evolution</div>
          <h2 className="text-2xl font-display font-bold text-white">Resume Evolution Tracker</h2>
          <p className="text-slate-400 mt-1">Track your resume improvement across uploads</p>
        </div>
        <div className="glass-card p-12 text-center flex flex-col items-center gap-4"
          style={{ border: '2px dashed rgba(14,165,233,0.2)' }}>
          <History size={48} className="text-primary-400/30" />
          <p className="text-slate-400 text-lg font-medium">No Resume History Yet</p>
          <p className="text-slate-500 text-sm">Each time you analyze a resume, a version is saved here. Upload your first resume to get started.</p>
          <button onClick={() => setActiveModule('resume')} className="btn-primary flex items-center gap-2 mt-2">
            <Upload size={14} />Analyze My First Resume
          </button>
        </div>
      </div>
    );
  }

  const chartData = resumeVersions.map(v => ({
    version: `v${v.version}`,
    ATS: v.atsScore,
    Employability: v.employabilityScore,
    Skills: v.skillCount * 4,
  }));

  return (
    <div className="space-y-6 animate-fade-in">
      <div>
        <div className="section-label w-fit">📈 PATENT FEATURE</div>
        <h2 className="text-2xl font-display font-bold text-white">Resume Evolution Tracker</h2>
        <p className="text-slate-400 mt-1">Track your resume improvement across {resumeVersions.length} version{resumeVersions.length > 1 ? 's' : ''}</p>
      </div>

      {/* Chart */}
      {resumeVersions.length > 1 && (
        <div className="glass-card p-5">
          <h3 className="text-sm font-semibold text-white mb-4 flex items-center gap-2"><TrendingUp size={14} className="text-primary-400" /> Score Growth Trend</h3>
          <ResponsiveContainer width="100%" height={250}>
            <LineChart data={chartData}>
              <XAxis dataKey="version" tick={{ fill: '#64748b', fontSize: 11 }} axisLine={false} tickLine={false} />
              <YAxis tick={{ fill: '#64748b', fontSize: 10 }} axisLine={false} tickLine={false} domain={[0, 100]} />
              <Tooltip contentStyle={{ background: 'rgba(13,20,34,0.95)', border: '1px solid rgba(14,165,233,0.3)', borderRadius: 8, fontSize: 11 }} />
              <Legend wrapperStyle={{ fontSize: 11, color: '#94a3b8' }} />
              <Line type="monotone" dataKey="ATS" stroke="#0ea5e9" strokeWidth={2} dot={{ fill: '#0ea5e9', r: 4 }} />
              <Line type="monotone" dataKey="Employability" stroke="#d946ef" strokeWidth={2} dot={{ fill: '#d946ef', r: 4 }} />
              <Line type="monotone" dataKey="Skills" stroke="#00ff88" strokeWidth={2} dot={{ fill: '#00ff88', r: 4 }} />
            </LineChart>
          </ResponsiveContainer>
        </div>
      )}

      {/* Version Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {resumeVersions.map((v, i) => (
          <div key={v.id} className={`glass-card p-5 ${i === resumeVersions.length - 1 ? 'ring-1 ring-primary-500/40' : ''}`}>
            <div className="flex items-center justify-between mb-3">
              <div className="flex items-center gap-2">
                <History size={14} className="text-primary-400" />
                <span className="text-sm font-bold text-white">Resume v{v.version}</span>
              </div>
              {i === resumeVersions.length - 1 && <span className="badge-green text-[10px]">Latest</span>}
            </div>
            <p className="text-xs text-slate-500 mb-3">{new Date(v.uploadedAt).toLocaleDateString()}</p>
            <div className="space-y-2">
              {[['ATS Score', v.atsScore, '#0ea5e9'], ['Employability', v.employabilityScore, '#d946ef']].map(([label, val, color]) => (
                <div key={label as string}>
                  <div className="flex justify-between text-xs mb-1">
                    <span className="text-slate-500">{label}</span>
                    <span style={{ color: color as string }} className="font-bold">{val}</span>
                  </div>
                  <div className="progress-bar h-1.5">
                    <div className="h-1.5 rounded-full" style={{ width: `${val}%`, background: `linear-gradient(90deg,${color},${color as string}80)` }} />
                  </div>
                </div>
              ))}
              <div className="flex items-center gap-1 mt-2">
                <Award size={11} className="text-yellow-400" />
                <span className="text-xs text-slate-400">{v.skillCount} skills detected</span>
              </div>
            </div>
            {v.improvements.length > 0 && (
              <div className="mt-3 pt-3 border-t border-white/5">
                {v.improvements.map(imp => <p key={imp} className="text-[10px] text-green-400">✓ {imp}</p>)}
              </div>
            )}
          </div>
        ))}
      </div>

      {/* Total Improvement */}
      {resumeVersions.length > 1 && (
        <div className="glass-card p-5" style={{ background: 'linear-gradient(135deg,rgba(14,165,233,0.06),rgba(217,70,239,0.06))' }}>
          <h3 className="text-sm font-semibold text-white mb-3">📊 Total Improvement Since First Upload</h3>
          <div className="grid grid-cols-3 gap-4 text-center">
            {[
              { label: 'ATS Growth', value: `+${resumeVersions[resumeVersions.length - 1].atsScore - resumeVersions[0].atsScore} pts` },
              { label: 'Employability', value: `+${resumeVersions[resumeVersions.length - 1].employabilityScore - resumeVersions[0].employabilityScore} pts` },
              { label: 'Skills Added', value: `+${resumeVersions[resumeVersions.length - 1].skillCount - resumeVersions[0].skillCount}` },
            ].map(({ label, value }) => (
              <div key={label}>
                <p className="text-2xl font-black gradient-text-green">{value}</p>
                <p className="text-xs text-slate-500">{label}</p>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

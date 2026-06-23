import { useApp } from '../context/AppContext';
import { TrendingUp, Target, Zap, AlertTriangle, Award, FileText, BarChart3, Brain, Building2, Map, Upload } from 'lucide-react';
import { RadarChart, Radar, PolarGrid, PolarAngleAxis, ResponsiveContainer, AreaChart, Area, XAxis, YAxis, Tooltip, BarChart, Bar, Cell } from 'recharts';

function ScoreRing({ value, label, color, size = 80 }: { value: number; label: string; color: string; size?: number }) {
  const r = (size - 12) / 2;
  const circ = 2 * Math.PI * r;
  const offset = circ - (value / 100) * circ;
  return (
    <div className="flex flex-col items-center gap-1">
      <svg width={size} height={size}>
        <circle cx={size / 2} cy={size / 2} r={r} fill="none" stroke="rgba(255,255,255,0.05)" strokeWidth={6} />
        <circle cx={size / 2} cy={size / 2} r={r} fill="none" stroke={color} strokeWidth={6}
          strokeDasharray={circ} strokeDashoffset={offset} strokeLinecap="round"
          transform={`rotate(-90 ${size / 2} ${size / 2})`} style={{ transition: 'stroke-dashoffset 1.2s ease' }} />
        <text x="50%" y="50%" textAnchor="middle" dy="0.35em" fill="white" fontSize={size * 0.2} fontWeight="bold">{value}</text>
      </svg>
      <span className="text-[11px] text-slate-400 text-center">{label}</span>
    </div>
  );
}

/** Shown when no resume has been analyzed yet */
function NoResumePrompt({ onAnalyze }: { onAnalyze: () => void }) {
  return (
    <div className="glass-card p-10 text-center flex flex-col items-center gap-4"
      style={{ border: '2px dashed rgba(14,165,233,0.25)', background: 'rgba(14,165,233,0.04)' }}>
      <div className="w-20 h-20 rounded-2xl flex items-center justify-center"
        style={{ background: 'rgba(14,165,233,0.1)' }}>
        <Upload size={36} className="text-primary-400" />
      </div>
      <div>
        <h3 className="text-lg font-bold text-white">No Resume Analyzed Yet</h3>
        <p className="text-sm text-slate-400 mt-1 max-w-md">
          Upload your resume to unlock your personalized dashboard — real ATS score, skill gap analysis, roadmap, and placement readiness.
        </p>
      </div>
      <button onClick={onAnalyze} className="btn-primary flex items-center gap-2">
        <Upload size={16} />
        Upload & Analyze Resume
      </button>
    </div>
  );
}

export default function Dashboard() {
  const { setActiveModule, resumeScore, skillGap, placementReadiness, user, resumeText } = useApp();

  const firstName = user?.name?.split(' ')[0] || 'there';
  const hasData = !!resumeScore;

  // Build dynamic data from real analysis
  const radarData = resumeScore ? [
    { subject: 'ATS', A: resumeScore.ats },
    { subject: 'Technical', A: resumeScore.technical },
    { subject: 'Readability', A: resumeScore.readability },
    { subject: 'Projects', A: resumeScore.projectStrength },
    { subject: 'Keywords', A: resumeScore.keywordDensity },
    { subject: 'Employ.', A: resumeScore.employability },
  ] : [];

  const lowerResume = (resumeText || '').toLowerCase();
  const skillMatchData = skillGap?.matchingSkills.slice(0, 6).map(skill => {
    const term = skill.toLowerCase();
    const matches = (lowerResume.split(term).length - 1);
    const matchScore = Math.min(98, 65 + Math.min(33, matches * 11));
    return {
      skill: skill.length > 10 ? skill.slice(0, 10) + '…' : skill,
      match: matchScore,
    };
  }) || [];

  const statCards = hasData ? [
    { label: 'Employability Score', value: `${resumeScore.employability}%`, raw: resumeScore.employability, icon: Zap, color: '#0ea5e9' },
    { label: 'ATS Score', value: `${resumeScore.ats}`, raw: resumeScore.ats, icon: FileText, color: '#d946ef' },
    { label: 'Skill Match', value: `${skillGap?.matchPercentage ?? 0}%`, raw: skillGap?.matchPercentage ?? 0, icon: Target, color: '#00ff88' },
    { label: 'Placement Ready', value: `${placementReadiness?.overall ?? 0}%`, raw: placementReadiness?.overall ?? 0, icon: Award, color: '#ff6b35' },
  ] : [];

  const scoreLevel = (v: number) => v >= 80 ? { label: 'Excellent', color: '#00ff88' } : v >= 65 ? { label: 'Good', color: '#0ea5e9' } : v >= 50 ? { label: 'Average', color: '#fbbf24' } : { label: 'Needs Work', color: '#ef4444' };

  return (
    <div className="space-y-6 animate-fade-in">
      {/* Hero Banner */}
      <div className="relative overflow-hidden rounded-2xl p-6 sm:p-8"
        style={{ background: 'linear-gradient(135deg, rgba(14,165,233,0.15), rgba(217,70,239,0.15))', border: '1px solid rgba(14,165,233,0.2)' }}>
        <div className="absolute inset-0 opacity-30"
          style={{ backgroundImage: 'radial-gradient(circle at 20% 50%, rgba(14,165,233,0.3) 0%, transparent 50%), radial-gradient(circle at 80% 20%, rgba(217,70,239,0.3) 0%, transparent 50%)' }} />
        <div className="relative z-10">
          <div className="section-label w-fit">🚀 AI Career Intelligence Platform</div>
          <h1 className="text-2xl sm:text-3xl font-display font-bold text-white mb-2">
            {hasData
              ? <>Your Resume Score: <span className="gradient-text">{resumeScore.overall}/100</span></>
              : <>Welcome, <span className="gradient-text">{firstName}!</span></>
            }
          </h1>
          <p className="text-slate-400 mb-6 max-w-xl">
            {hasData
              ? <>Your employability score is <span className="font-semibold" style={{ color: scoreLevel(resumeScore.employability).color }}>{scoreLevel(resumeScore.employability).label}</span>. Skill match: <span className="text-primary-400 font-semibold">{skillGap?.matchPercentage ?? 0}%</span> for your target role.</>
              : <>Upload your resume below to get your AI-powered career intelligence report — ATS score, skill gaps, roadmap & more.</>
            }
          </p>
          <div className="flex flex-wrap gap-3">
            <button onClick={() => setActiveModule('resume')} className="btn-primary text-sm">📄 {hasData ? 'Re-Analyze Resume' : 'Analyze Resume'}</button>
            {hasData && <button onClick={() => setActiveModule('roadmap')} className="btn-secondary text-sm">🗺️ View Roadmap</button>}
            {hasData && <button onClick={() => setActiveModule('companies')} className="btn-secondary text-sm">🏢 Dream Companies</button>}
          </div>
        </div>
      </div>

      {/* If no data, show prompt */}
      {!hasData && (
        <NoResumePrompt onAnalyze={() => setActiveModule('resume')} />
      )}

      {/* Stat Cards — only shown after analysis */}
      {hasData && (
        <>
          <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
            {statCards.map(({ label, value, raw, icon: Icon, color }) => {
              const level = scoreLevel(raw);
              return (
                <div key={label} className="glass-card p-5 hover:scale-[1.02] transition-all duration-200 cursor-pointer group">
                  <div className="flex items-center justify-between mb-3">
                    <div className="w-9 h-9 rounded-xl flex items-center justify-center" style={{ background: `${color}18` }}>
                      <Icon size={18} style={{ color }} />
                    </div>
                    <span className="text-xs font-semibold px-2 py-0.5 rounded-full" style={{ background: `${level.color}15`, color: level.color }}>
                      {level.label}
                    </span>
                  </div>
                  <p className="text-2xl font-bold text-white font-display">{value}</p>
                  <p className="text-xs text-slate-500 mt-1">{label}</p>
                  <div className="progress-bar mt-3">
                    <div className="progress-fill" style={{ width: `${raw}%`, background: `linear-gradient(90deg, ${color}, ${color}80)` }} />
                  </div>
                </div>
              );
            })}
          </div>

          {/* Resume Score Rings */}
          <div className="glass-card p-6">
            <h3 className="text-base font-semibold text-white mb-4">📊 Resume Score Breakdown</h3>
            <div className="flex flex-wrap justify-around gap-4">
              <ScoreRing value={resumeScore.ats} label="ATS" color="#0ea5e9" />
              <ScoreRing value={resumeScore.technical} label="Technical" color="#d946ef" />
              <ScoreRing value={resumeScore.readability} label="Readability" color="#00ff88" />
              <ScoreRing value={resumeScore.projectStrength} label="Projects" color="#ff6b35" />
              <ScoreRing value={resumeScore.keywordDensity} label="Keywords" color="#fbbf24" />
              <ScoreRing value={resumeScore.employability} label="Employability" color="#60a5fa" />
            </div>
          </div>

          {/* Charts Row */}
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-4">
            {/* Score Radar */}
            <div className="glass-card p-5">
              <h3 className="text-sm font-semibold text-white mb-4">🎯 Skill Radar</h3>
              <ResponsiveContainer width="100%" height={160}>
                <RadarChart data={radarData}>
                  <PolarGrid stroke="rgba(255,255,255,0.05)" />
                  <PolarAngleAxis dataKey="subject" tick={{ fill: '#64748b', fontSize: 9 }} />
                  <Radar dataKey="A" stroke="#d946ef" fill="#d946ef" fillOpacity={0.15} strokeWidth={2} />
                </RadarChart>
              </ResponsiveContainer>
            </div>

            {/* Company Readiness */}
            <div className="glass-card p-5">
              <h3 className="text-sm font-semibold text-white mb-4">🏢 Company Readiness</h3>
              <ResponsiveContainer width="100%" height={160}>
                <BarChart data={placementReadiness?.companyReadiness.slice(0, 4) || []}>
                  <XAxis dataKey="company" tick={{ fill: '#64748b', fontSize: 10 }} axisLine={false} tickLine={false} />
                  <YAxis hide domain={[0, 100]} />
                  <Tooltip contentStyle={{ background: 'rgba(13,20,34,0.9)', border: '1px solid rgba(14,165,233,0.3)', borderRadius: 8, fontSize: 11 }} />
                  <Bar dataKey="score" radius={[4, 4, 0, 0]}>
                    {(placementReadiness?.companyReadiness.slice(0, 4) || []).map((entry, i) => (
                      <Cell key={i} fill={entry.score >= 70 ? '#00ff88' : entry.score >= 50 ? '#fbbf24' : '#ef4444'} opacity={0.85} />
                    ))}
                  </Bar>
                </BarChart>
              </ResponsiveContainer>
            </div>

            {/* Skill Match Bar */}
            <div className="glass-card p-5">
              <h3 className="text-sm font-semibold text-white mb-4">🔍 Matched Skills</h3>
              {skillMatchData.length > 0 ? (
                <ResponsiveContainer width="100%" height={160}>
                  <BarChart data={skillMatchData} layout="vertical">
                    <XAxis type="number" hide domain={[0, 100]} />
                    <YAxis dataKey="skill" type="category" tick={{ fill: '#64748b', fontSize: 10 }} axisLine={false} tickLine={false} width={65} />
                    <Tooltip contentStyle={{ background: 'rgba(13,20,34,0.9)', border: '1px solid rgba(14,165,233,0.3)', borderRadius: 8, fontSize: 11 }} />
                    <Bar dataKey="match" fill="#0ea5e9" radius={[0, 4, 4, 0]} opacity={0.85} />
                  </BarChart>
                </ResponsiveContainer>
              ) : (
                <div className="flex items-center justify-center h-40 text-slate-600 text-sm">No data yet</div>
              )}
            </div>
          </div>
        </>
      )}

      {/* Quick Actions — always visible */}
      <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-3">
        {[
          { label: 'Future Skills', icon: TrendingUp, mod: 'future-skills', color: '#0ea5e9' },
          { label: 'Career Risk', icon: AlertTriangle, mod: 'career-risk', color: '#ff6b35' },
          { label: 'Skill Graph', icon: BarChart3, mod: 'skill-graph', color: '#d946ef' },
          { label: 'AI Mentor', icon: Brain, mod: 'mentor', color: '#00ff88' },
          { label: 'Companies', icon: Building2, mod: 'companies', color: '#fbbf24' },
          { label: 'Roadmap', icon: Map, mod: 'roadmap', color: '#f87171' },
        ].map(({ label, icon: Icon, mod, color }) => (
          <button key={mod} onClick={() => setActiveModule(mod)}
            className="glass-card p-4 flex flex-col items-center gap-2 hover:scale-[1.05] transition-all duration-200 cursor-pointer">
            <div className="w-10 h-10 rounded-xl flex items-center justify-center" style={{ background: `${color}18` }}>
              <Icon size={20} style={{ color }} />
            </div>
            <span className="text-xs text-slate-400 text-center">{label}</span>
          </button>
        ))}
      </div>
    </div>
  );
}

import { useApp } from '../context/AppContext';
import { AlertTriangle, CheckCircle, TrendingDown, Upload, Zap } from 'lucide-react';

export default function CareerRiskPage() {
  const { careerRisk, setActiveModule } = useApp();

  if (!careerRisk) {
    return (
      <div className="space-y-6 animate-fade-in">
        <div>
          <div className="section-label w-fit">⚠️ Career Risk Analyzer</div>
          <h2 className="text-2xl font-display font-bold text-white">Career Risk Analyzer</h2>
          <p className="text-slate-400 mt-1">Detect outdated skills before they hurt your career</p>
        </div>
        <div className="glass-card p-12 text-center flex flex-col items-center gap-4"
          style={{ border: '2px dashed rgba(255,107,53,0.2)' }}>
          <AlertTriangle size={48} className="text-orange-400/30" />
          <p className="text-slate-400 text-lg font-medium">No Analysis Available</p>
          <p className="text-slate-500 text-sm">Analyze your resume first to detect outdated or risky skills.</p>
          <button onClick={() => setActiveModule('resume')} className="btn-primary flex items-center gap-2 mt-2">
            <Upload size={14} />Analyze My Resume
          </button>
        </div>
      </div>
    );
  }

  const riskColor = careerRisk.riskLevel === 'safe' ? '#00ff88' : careerRisk.riskLevel === 'moderate' ? '#fbbf24' : '#ef4444';
  const riskLabel = careerRisk.riskLevel === 'safe' ? '✅ Low Risk' : careerRisk.riskLevel === 'moderate' ? '⚠️ Moderate Risk' : '🚨 High Risk';

  return (
    <div className="space-y-6 animate-fade-in">
      <div>
        <div className="section-label w-fit">⚠️ PATENT FEATURE</div>
        <h2 className="text-2xl font-display font-bold text-white">Career Risk Analyzer</h2>
        <p className="text-slate-400 mt-1">AI-detected outdated skills with modern replacements</p>
      </div>

      {/* Risk Score Banner */}
      <div className="glass-card p-6 flex flex-col sm:flex-row items-center gap-6"
        style={{ background: `${riskColor}08`, border: `1px solid ${riskColor}25` }}>
        <div className="text-center flex-shrink-0">
          <div className="text-5xl font-display font-black" style={{ color: riskColor }}>{careerRisk.riskScore}</div>
          <p className="text-slate-400 text-sm mt-1">Risk Score</p>
          <span className="text-xs font-bold mt-1 inline-block" style={{ color: riskColor }}>{riskLabel}</span>
        </div>
        <div className="flex-1">
          <div className="progress-bar h-3">
            <div className="h-3 rounded-full transition-all duration-1000" style={{ width: `${careerRisk.riskScore}%`, background: `linear-gradient(90deg, ${riskColor}, ${riskColor}80)` }} />
          </div>
          <p className="text-sm text-slate-300 mt-3">
            {careerRisk.riskLevel === 'safe'
              ? '🎉 Your skillset looks modern! No critically outdated technologies detected.'
              : careerRisk.riskLevel === 'moderate'
              ? '⚠️ Some skills in your resume are losing market demand. Consider updating them.'
              : '🚨 Several outdated technologies detected. Upskilling is critical for your career progression.'}
          </p>
        </div>
      </div>

      {/* Outdated Skills with Replacements */}
      {careerRisk.replacements.length > 0 && (
        <div className="glass-card p-5">
          <h3 className="text-sm font-semibold text-white mb-4 flex items-center gap-2">
            <TrendingDown size={15} className="text-red-400" />
            Outdated Skills & Modern Replacements
          </h3>
          <div className="space-y-3">
            {careerRisk.replacements.map(({ old, new: newSkill }) => (
              <div key={old} className="flex items-center gap-3 p-3 rounded-xl"
                style={{ background: 'rgba(255,255,255,0.03)', border: '1px solid rgba(255,255,255,0.06)' }}>
                <span className="skill-tag-missing flex-shrink-0">{old}</span>
                <Zap size={14} className="text-yellow-400 flex-shrink-0" />
                <span className="text-xs text-slate-400">Replace with</span>
                <span className="skill-tag-present flex-shrink-0">{newSkill}</span>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Safe Skills */}
      {careerRisk.riskLevel === 'safe' && (
        <div className="glass-card p-5" style={{ background: 'rgba(0,255,136,0.05)', border: '1px solid rgba(0,255,136,0.15)' }}>
          <h3 className="text-sm font-semibold text-green-400 mb-3 flex items-center gap-2">
            <CheckCircle size={15} />Your Skills Are Market-Ready!
          </h3>
          <p className="text-sm text-slate-400">No outdated or low-demand skills were detected in your resume. Continue staying current with emerging technologies like AI/ML, Cloud Native, and modern DevOps practices.</p>
        </div>
      )}

      {/* Recommendations */}
      <div className="glass-card p-5">
        <h3 className="text-sm font-semibold text-white mb-3">💡 Risk Mitigation Recommendations</h3>
        <div className="space-y-2">
          {[
            'Follow industry blogs (dev.to, medium, Hacker News) to track emerging technologies',
            'Allocate 5–10 hours/week for learning new tools and frameworks',
            'Replace deprecated tools immediately with modern, in-demand alternatives',
            'Build at least one project using modern tech stack every quarter',
            'Get certified in cloud platforms (AWS / Azure / GCP) to stay relevant',
          ].map(tip => (
            <div key={tip} className="flex items-start gap-2 text-xs text-slate-400">
              <CheckCircle size={12} className="text-primary-400 flex-shrink-0 mt-0.5" />
              {tip}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

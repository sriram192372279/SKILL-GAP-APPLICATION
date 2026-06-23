import { useState } from 'react';
import { COMPANIES } from '../data/staticData';
import { Building2, ChevronDown, ChevronUp, Clock, Zap } from 'lucide-react';

export default function CompaniesPage() {
  const [selected, setSelected] = useState<string | null>(null);
  const [expandedRound, setExpandedRound] = useState<number | null>(null);

  const company = COMPANIES.find(c => c.id === selected);

  const DIFFICULTY_COLORS: Record<string, string> = {
    Easy: '#00ff88', Medium: '#fbbf24', Hard: '#ff6b35', 'Very Hard': '#ef4444',
  };

  return (
    <div className="space-y-6 animate-fade-in">
      <div>
        <div className="section-label w-fit">🏢 PATENT FEATURE</div>
        <h2 className="text-2xl font-display font-bold text-white">Dream Company Intelligence Hub</h2>
        <p className="text-slate-400 mt-1">Deep company intelligence for your target employers</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Company List */}
        <div className="lg:col-span-1">
          <div className="glass-card p-3 space-y-1">
            <p className="text-xs text-slate-500 px-2 py-1 uppercase tracking-wider font-semibold">Top Companies</p>
            {COMPANIES.map(c => (
              <button key={c.id} onClick={() => { setSelected(c.id); setExpandedRound(null); }}
                className={`w-full flex items-center gap-3 px-3 py-3 rounded-xl transition-all text-left ${selected === c.id ? 'text-white' : 'text-slate-400 hover:text-white hover:bg-white/5'}`}
                style={selected === c.id ? { background: `${c.color}18`, border: `1px solid ${c.color}40` } : {}}>
                <span className="text-lg">{c.logo}</span>
                <div className="flex-1">
                  <p className="text-sm font-medium">{c.name}</p>
                  <p className="text-[10px]" style={{ color: DIFFICULTY_COLORS[c.difficulty] }}>{c.difficulty}</p>
                </div>
                <span className="text-[10px] text-slate-500">
                  {(c.salaryRange.min / 100000).toFixed(0)}L-{(c.salaryRange.max / 100000).toFixed(0)}L
                </span>
              </button>
            ))}
          </div>
        </div>

        {/* Company Detail */}
        <div className="lg:col-span-2 space-y-4">
          {!company ? (
            <div className="glass-card p-12 text-center h-full flex flex-col items-center justify-center">
              <Building2 size={48} className="text-primary-400/30 mx-auto mb-3" />
              <p className="text-slate-400">Select a company to view full intelligence profile</p>
            </div>
          ) : (
            <>
              {/* Header */}
              <div className="glass-card p-5" style={{ background: `linear-gradient(135deg, ${company.color}12, rgba(14,165,233,0.06))`, border: `1px solid ${company.color}25` }}>
                <div className="flex items-start gap-4">
                  <div className="w-14 h-14 rounded-2xl flex items-center justify-center text-3xl" style={{ background: `${company.color}20` }}>{company.logo}</div>
                  <div className="flex-1">
                    <h2 className="text-xl font-bold text-white">{company.name}</h2>
                    <p className="text-slate-400 text-sm mt-1">{company.overview}</p>
                    <div className="flex flex-wrap gap-2 mt-3">
                      <span className="badge" style={{ background: `${DIFFICULTY_COLORS[company.difficulty]}15`, color: DIFFICULTY_COLORS[company.difficulty], border: `1px solid ${DIFFICULTY_COLORS[company.difficulty]}30` }}>
                        {company.difficulty} Difficulty
                      </span>
                      <span className="badge-blue">
                        ₹{(company.salaryRange.min / 100000).toFixed(0)}L – ₹{(company.salaryRange.max / 100000).toFixed(0)}L
                      </span>
                    </div>
                  </div>
                </div>
              </div>

              {/* Grid Info */}
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                {/* Technologies */}
                <div className="glass-card p-4">
                  <h3 className="text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Tech Stack</h3>
                  <div className="flex flex-wrap gap-1.5">
                    {company.technologies.map(t => <span key={t} className="skill-tag-present text-xs">{t}</span>)}
                  </div>
                </div>

                {/* Culture */}
                <div className="glass-card p-4">
                  <h3 className="text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Work Culture</h3>
                  <div className="flex flex-wrap gap-1.5">
                    {company.culture.map(c => <span key={c} className="badge-blue text-xs">{c}</span>)}
                  </div>
                </div>

                {/* Eligibility */}
                <div className="glass-card p-4 sm:col-span-2">
                  <h3 className="text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Eligibility Criteria</h3>
                  <div className="grid grid-cols-2 gap-1">
                    {company.eligibility.map(e => (
                      <div key={e} className="flex items-start gap-1.5">
                        <span className="text-primary-400 text-xs mt-0.5">✓</span>
                        <span className="text-xs text-slate-300">{e}</span>
                      </div>
                    ))}
                  </div>
                </div>
              </div>

              {/* Hiring Rounds */}
              <div className="glass-card p-5">
                <h3 className="text-sm font-semibold text-white mb-3 flex items-center gap-2"><Zap size={14} className="text-primary-400" /> Hiring Process</h3>
                <div className="space-y-2">
                  {company.hiringRounds.map((round) => (
                    <div key={round.round} className="rounded-xl overflow-hidden" style={{ border: '1px solid rgba(255,255,255,0.06)' }}>
                      <button onClick={() => setExpandedRound(expandedRound === round.round ? null : round.round)}
                        className="w-full flex items-center gap-3 p-3 hover:bg-white/3 transition-all text-left">
                        <div className="w-7 h-7 rounded-full flex items-center justify-center text-xs font-bold text-white flex-shrink-0" style={{ background: 'linear-gradient(135deg,#0ea5e9,#d946ef)' }}>R{round.round}</div>
                        <div className="flex-1">
                          <p className="text-sm font-medium text-white">{round.name}</p>
                          <div className="flex gap-3 mt-0.5">
                            <span className="flex items-center gap-1 text-[10px] text-slate-500"><Clock size={8} />{round.duration}</span>
                            <span className="text-[10px]" style={{ color: DIFFICULTY_COLORS[round.difficulty] ?? '#94a3b8' }}>{round.difficulty}</span>
                          </div>
                        </div>
                        {expandedRound === round.round ? <ChevronUp size={14} className="text-slate-500" /> : <ChevronDown size={14} className="text-slate-500" />}
                      </button>
                      {expandedRound === round.round && (
                        <div className="p-3 pt-0 space-y-2" style={{ background: 'rgba(255,255,255,0.02)' }}>
                          <p className="text-xs text-slate-400">{round.description}</p>
                          <div>
                            <p className="text-[10px] font-semibold text-green-400 mb-1">💡 Tips</p>
                            {round.tips.map(t => <p key={t} className="text-[10px] text-slate-500">• {t}</p>)}
                          </div>
                          <div>
                            <p className="text-[10px] font-semibold text-red-400 mb-1">❌ Common Mistakes</p>
                            {round.commonMistakes.map(m => <p key={m} className="text-[10px] text-slate-500">• {m}</p>)}
                          </div>
                        </div>
                      )}
                    </div>
                  ))}
                </div>
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  );
}

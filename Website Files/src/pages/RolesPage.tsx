import { useState } from 'react';
import { ROLES } from '../data/staticData';
import { Briefcase, ChevronRight, CheckCircle } from 'lucide-react';

export default function RolesPage() {
  const [selectedRole, setSelectedRole] = useState(ROLES[0]);

  return (
    <div className="space-y-6 animate-fade-in">
      <div>
        <div className="section-label w-fit">💼 Role Intelligence</div>
        <h2 className="text-2xl font-display font-bold text-white">Role Preparation Intelligence System</h2>
        <p className="text-slate-400 mt-1">Complete intelligence for every tech career path</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Role List */}
        <div className="lg:col-span-1 glass-card p-3 space-y-1 h-fit">
          {ROLES.map(role => (
            <button key={role.id} onClick={() => setSelectedRole(role)}
              className={`w-full flex items-center gap-3 px-3 py-3 rounded-xl transition-all text-left group ${selectedRole.id === role.id ? 'text-white' : 'text-slate-400 hover:text-white hover:bg-white/5'}`}
              style={selectedRole.id === role.id ? { background: 'linear-gradient(135deg,rgba(14,165,233,0.15),rgba(217,70,239,0.15))', border: '1px solid rgba(14,165,233,0.3)' } : {}}>
              <Briefcase size={15} className={selectedRole.id === role.id ? 'text-primary-400' : 'text-slate-600 group-hover:text-slate-400'} />
              <span className="text-sm font-medium">{role.title}</span>
              <ChevronRight size={12} className={`ml-auto ${selectedRole.id === role.id ? 'text-primary-400' : 'opacity-0 group-hover:opacity-100'}`} />
            </button>
          ))}
        </div>

        {/* Role Detail */}
        <div className="lg:col-span-2 space-y-4">
          {/* Header */}
          <div className="glass-card p-5" style={{ background: 'linear-gradient(135deg,rgba(14,165,233,0.08),rgba(217,70,239,0.08))' }}>
            <h2 className="text-xl font-bold text-white mb-1">{selectedRole.title}</h2>
            <p className="text-slate-400 text-sm">{selectedRole.overview}</p>
            <div className="mt-3">
              <span className="badge-green">₹{(selectedRole.salaryRange.min / 100000).toFixed(0)}L – ₹{(selectedRole.salaryRange.max / 100000).toFixed(0)}L per year</span>
            </div>
          </div>

          {/* Info Grid */}
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            {/* Required Skills */}
            <div className="glass-card p-4">
              <h3 className="text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Required Skills</h3>
              <div className="flex flex-wrap gap-1.5">
                {selectedRole.requiredSkills.map(s => <span key={s} className="skill-tag-present text-xs">{s}</span>)}
              </div>
            </div>

            {/* Daily Responsibilities */}
            <div className="glass-card p-4">
              <h3 className="text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Daily Responsibilities</h3>
              <div className="space-y-1">
                {selectedRole.dailyResponsibilities.map(r => (
                  <div key={r} className="flex items-start gap-2">
                    <CheckCircle size={11} className="text-primary-400 flex-shrink-0 mt-0.5" />
                    <span className="text-xs text-slate-400">{r}</span>
                  </div>
                ))}
              </div>
            </div>

            {/* Learning Path */}
            <div className="glass-card p-4 sm:col-span-2">
              <h3 className="text-xs font-semibold text-slate-400 uppercase tracking-wider mb-3">Learning Path</h3>
              <div className="flex flex-wrap items-center gap-2">
                {selectedRole.learningPath[0]?.split(' → ').map((step, i, arr) => (
                  <div key={step} className="flex items-center gap-2">
                    <span className="px-2.5 py-1 rounded-lg text-xs font-medium" style={{ background: `hsl(${200 + i * 15},80%,50%)18`, color: `hsl(${200 + i * 15},80%,70%)`, border: `1px solid hsl(${200 + i * 15},80%,50%)30` }}>{step}</span>
                    {i < arr.length - 1 && <span className="text-slate-600">→</span>}
                  </div>
                ))}
              </div>
            </div>

            {/* Certifications */}
            <div className="glass-card p-4">
              <h3 className="text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Certifications</h3>
              <div className="space-y-1">
                {selectedRole.certifications.map(c => <div key={c} className="flex items-center gap-2"><span className="text-yellow-400 text-xs">🏆</span><span className="text-xs text-slate-400">{c}</span></div>)}
              </div>
            </div>

            {/* Projects */}
            <div className="glass-card p-4">
              <h3 className="text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Recommended Projects</h3>
              <div className="space-y-1">
                {selectedRole.projects.map(p => <div key={p} className="flex items-center gap-2"><span className="text-primary-400 text-xs">⚡</span><span className="text-xs text-slate-400">{p}</span></div>)}
              </div>
            </div>
          </div>

          {/* Future Scope */}
          <div className="glass-card p-4" style={{ background: 'linear-gradient(135deg,rgba(0,255,136,0.05),rgba(14,165,233,0.05))' }}>
            <h3 className="text-xs font-semibold text-green-400 uppercase tracking-wider mb-2">🚀 Future Scope</h3>
            <p className="text-sm text-slate-300">{selectedRole.futureScope}</p>
          </div>
        </div>
      </div>
    </div>
  );
}

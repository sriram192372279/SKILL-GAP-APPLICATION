import { Shield, Users, Database, BookOpen, BarChart3 } from 'lucide-react';

const STATS = [
  { label: 'Total Users', value: '1,248', delta: '+12%', color: '#0ea5e9' },
  { label: 'Resumes Analyzed', value: '3,891', delta: '+8%', color: '#d946ef' },
  { label: 'Skills in Database', value: '2,400+', delta: 'Updated', color: '#00ff88' },
  { label: 'Companies', value: '20', delta: 'Curated', color: '#ff6b35' },
];

export default function AdminPage() {
  return (
    <div className="space-y-6 animate-fade-in">
      <div>
        <div className="section-label w-fit" style={{ background: 'rgba(239,68,68,0.1)', color: '#f87171', border: '1px solid rgba(239,68,68,0.2)' }}>🔐 Admin Access</div>
        <h2 className="text-2xl font-display font-bold text-white">Admin Dashboard</h2>
        <p className="text-slate-400 mt-1">Manage platform users, skills, companies, and analytics</p>
      </div>

      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
        {STATS.map(({ label, value, delta, color }) => (
          <div key={label} className="glass-card p-4">
            <p className="text-2xl font-black text-white">{value}</p>
            <p className="text-xs text-slate-500 mt-0.5">{label}</p>
            <p className="text-xs font-semibold mt-2" style={{ color }}>{delta}</p>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {[
          { icon: Users, title: 'User Management', desc: 'View, manage, and export user data', color: '#0ea5e9' },
          { icon: Database, title: 'Skills Database', desc: 'Add, edit, and categorize skills', color: '#d946ef' },
          { icon: Shield, title: 'Companies', desc: 'Manage company profiles and data', color: '#00ff88' },
          { icon: BookOpen, title: 'Learning Resources', desc: 'Curate and update learning content', color: '#ff6b35' },
          { icon: BarChart3, title: 'Platform Analytics', desc: 'View usage metrics and engagement', color: '#fbbf24' },
          { icon: Shield, title: 'System Settings', desc: 'Configure platform settings', color: '#60a5fa' },
        ].map(({ icon: Icon, title, desc, color }) => (
          <button key={title} className="glass-card p-5 text-left hover:scale-[1.02] transition-all">
            <div className="w-10 h-10 rounded-xl flex items-center justify-center mb-3" style={{ background: `${color}15` }}>
              <Icon size={20} style={{ color }} />
            </div>
            <h3 className="text-sm font-semibold text-white">{title}</h3>
            <p className="text-xs text-slate-400 mt-1">{desc}</p>
          </button>
        ))}
      </div>
    </div>
  );
}

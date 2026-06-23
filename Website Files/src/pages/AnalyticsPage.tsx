import { BarChart3, TrendingUp, Users, Zap } from 'lucide-react';
import { PieChart, Pie, Cell, ResponsiveContainer, Tooltip, AreaChart, Area, XAxis, YAxis, BarChart, Bar } from 'recharts';

const pieData = [
  { name: 'Matching', value: 64, color: '#00ff88' },
  { name: 'Missing', value: 24, color: '#ef4444' },
  { name: 'Partial', value: 12, color: '#fbbf24' },
];

const progressData = [
  { week: 'W1', progress: 10 }, { week: 'W2', progress: 18 }, { week: 'W3', progress: 25 },
  { week: 'W4', progress: 35 }, { week: 'W5', progress: 48 }, { week: 'W6', progress: 58 },
];

const companyData = [
  { company: 'TCS', score: 82 }, { company: 'Infosys', score: 78 }, { company: 'Wipro', score: 75 },
  { company: 'Google', score: 45 }, { company: 'Microsoft', score: 52 }, { company: 'Amazon', score: 48 },
];

export default function AnalyticsPage() {
  return (
    <div className="space-y-6 animate-fade-in">
      <div>
        <div className="section-label w-fit">📊 Analytics</div>
        <h2 className="text-2xl font-display font-bold text-white">Career Analytics Dashboard</h2>
        <p className="text-slate-400 mt-1">Comprehensive view of your career intelligence metrics</p>
      </div>

      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
        {[
          { label: 'Total Skills', value: '18', icon: Zap, color: '#0ea5e9' },
          { label: 'Roadmap Complete', value: '20%', icon: TrendingUp, color: '#00ff88' },
          { label: 'Companies Researched', value: '10', icon: Users, color: '#d946ef' },
          { label: 'Questions Practiced', value: '42', icon: BarChart3, color: '#ff6b35' },
        ].map(({ label, value, icon: Icon, color }) => (
          <div key={label} className="glass-card p-4">
            <div className="w-9 h-9 rounded-xl flex items-center justify-center mb-3" style={{ background: `${color}18` }}>
              <Icon size={18} style={{ color }} />
            </div>
            <p className="text-2xl font-black text-white">{value}</p>
            <p className="text-xs text-slate-500 mt-1">{label}</p>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
        {/* Skill Match Pie */}
        <div className="glass-card p-5">
          <h3 className="text-sm font-semibold text-white mb-4">Skill Distribution</h3>
          <div className="flex items-center gap-4">
            <ResponsiveContainer width={160} height={160}>
              <PieChart>
                <Pie data={pieData} cx="50%" cy="50%" innerRadius={45} outerRadius={70} dataKey="value" paddingAngle={3}>
                  {pieData.map((entry, i) => <Cell key={i} fill={entry.color} opacity={0.85} />)}
                </Pie>
                <Tooltip contentStyle={{ background: 'rgba(13,20,34,0.95)', border: '1px solid rgba(14,165,233,0.3)', borderRadius: 8, fontSize: 11 }} />
              </PieChart>
            </ResponsiveContainer>
            <div className="space-y-2">
              {pieData.map(d => (
                <div key={d.name} className="flex items-center gap-2">
                  <div className="w-3 h-3 rounded-full" style={{ background: d.color }} />
                  <span className="text-xs text-slate-400">{d.name}</span>
                  <span className="text-xs font-bold ml-auto" style={{ color: d.color }}>{d.value}%</span>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Learning Progress */}
        <div className="glass-card p-5">
          <h3 className="text-sm font-semibold text-white mb-4">Learning Progress</h3>
          <ResponsiveContainer width="100%" height={150}>
            <AreaChart data={progressData}>
              <defs>
                <linearGradient id="pg" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="5%" stopColor="#d946ef" stopOpacity={0.3} />
                  <stop offset="95%" stopColor="#d946ef" stopOpacity={0} />
                </linearGradient>
              </defs>
              <XAxis dataKey="week" tick={{ fill: '#64748b', fontSize: 10 }} axisLine={false} tickLine={false} />
              <YAxis hide />
              <Tooltip contentStyle={{ background: 'rgba(13,20,34,0.95)', border: '1px solid rgba(14,165,233,0.3)', borderRadius: 8, fontSize: 11 }} />
              <Area type="monotone" dataKey="progress" stroke="#d946ef" fill="url(#pg)" strokeWidth={2} />
            </AreaChart>
          </ResponsiveContainer>
        </div>

        {/* Company Readiness */}
        <div className="glass-card p-5 lg:col-span-2">
          <h3 className="text-sm font-semibold text-white mb-4">Company Readiness Overview</h3>
          <ResponsiveContainer width="100%" height={200}>
            <BarChart data={companyData}>
              <XAxis dataKey="company" tick={{ fill: '#64748b', fontSize: 11 }} axisLine={false} tickLine={false} />
              <YAxis tick={{ fill: '#64748b', fontSize: 10 }} axisLine={false} tickLine={false} domain={[0, 100]} />
              <Tooltip contentStyle={{ background: 'rgba(13,20,34,0.95)', border: '1px solid rgba(14,165,233,0.3)', borderRadius: 8, fontSize: 11 }} />
              <Bar dataKey="score" radius={[4, 4, 0, 0]} opacity={0.9}>
                {companyData.map((entry, i) => <Cell key={i} fill={entry.score >= 70 ? '#00ff88' : entry.score >= 50 ? '#fbbf24' : '#ef4444'} />)}
              </Bar>
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
}

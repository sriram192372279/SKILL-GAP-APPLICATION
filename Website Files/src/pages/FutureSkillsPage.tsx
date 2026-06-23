import { useState } from 'react';
import { useApp } from '../context/AppContext';
import { ROLES, FUTURE_SKILLS } from '../data/staticData';
import { TrendingUp, Zap, AlertTriangle, Briefcase, Sparkles, BookOpen } from 'lucide-react';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, Legend, CartesianGrid } from 'recharts';

export default function FutureSkillsPage() {
  const { selectedRole, setSelectedRole } = useApp();
  const [activeRole, setActiveRole] = useState(selectedRole);

  // Role-specific emerging technology prediction mappings
  const EMERGING_TECH_BY_ROLE: Record<string, { name: string; category: string; demand6m: number; demand12m: number; demand24m: number; priority: 'Critical' | 'High' | 'Medium'; desc: string }[]> = {
    'Software Developer': [
      { name: 'Copilot-driven Dev / AI Coding', category: 'Software Engineering', demand6m: 85, demand12m: 92, demand24m: 97, priority: 'Critical', desc: 'AI-assisted code generation & unit test automation workflows.' },
      { name: 'WebAssembly (WASM)', category: 'Runtime', demand6m: 55, demand12m: 72, demand24m: 88, priority: 'High', desc: 'High-performance binary execution in standard web browsers.' },
      { name: 'Bun & Next-gen Runtimes', category: 'JavaScript Ecosystem', demand6m: 60, demand12m: 70, demand24m: 82, priority: 'Medium', desc: 'Fast all-in-one JavaScript/TypeScript runtime engines.' },
    ],
    'Java Full Stack Developer': [
      { name: 'Spring Boot 3.x Virtual Threads', category: 'Backend Systems', demand6m: 88, demand12m: 94, demand24m: 98, priority: 'Critical', desc: 'Project Loom-based concurrency for scalable database transactions.' },
      { name: 'GraalVM Native Images', category: 'JVM Optimization', demand6m: 65, demand12m: 78, demand24m: 89, priority: 'High', desc: 'Compile Java bytecode directly to OS native executable binaries.' },
      { name: 'GraphQL & Federated Schemas', category: 'API Architectures', demand6m: 70, demand12m: 81, demand24m: 88, priority: 'High', desc: 'Decoupled multi-team API gateways and composite queries.' },
    ],
    'Data Analyst': [
      { name: 'AutoML Insights Tools', category: 'Analytics Automation', demand6m: 78, demand12m: 86, demand24m: 93, priority: 'Critical', desc: 'No-code automated predictive analytics integrated in dashboard systems.' },
      { name: 'dbt (Data Build Tool)', category: 'Analytics Engineering', demand6m: 72, demand12m: 83, demand24m: 90, priority: 'High', desc: 'Version-controlled, tested SQL queries in warehouse pipelines.' },
      { name: 'Real-time Stream Analytics', category: 'Data Pipelines', demand6m: 60, demand12m: 74, demand24m: 85, priority: 'Medium', desc: 'Sub-second event dashboards using Kafka or Spark Streaming.' },
    ],
    'Data Scientist': [
      { name: 'RAG (Retrieval-Augmented Gen)', category: 'Generative AI', demand6m: 92, demand12m: 96, demand24m: 99, priority: 'Critical', desc: 'Connecting massive enterprise knowledge bases to LLM models.' },
      { name: 'Vector Database Vector Indexes', category: 'Storage Systems', demand6m: 85, demand12m: 91, demand24m: 95, priority: 'Critical', desc: 'High-speed semantic search using Pinecone, Milvus, or pgvector.' },
      { name: 'Agentic Workflows (LangGraph)', category: 'Autonomous AI', demand6m: 65, demand12m: 82, demand24m: 93, priority: 'High', desc: 'Iterative, stateful AI agents solving multi-turn analytical problems.' },
    ],
    'AI Engineer': [
      { name: 'LLM Fine-tuning (QLoRA)', category: 'AI Customization', demand6m: 94, demand12m: 97, demand24m: 99, priority: 'Critical', desc: 'Low-rank adaptation optimization on custom domain datasets.' },
      { name: 'Guardrails & AI Safety Systems', category: 'AI Security', demand6m: 82, demand12m: 89, demand24m: 96, priority: 'Critical', desc: 'Real-time output validation, threat checking, and bias control.' },
      { name: 'Small Language Models (SLMs)', category: 'Edge AI Inference', demand6m: 70, demand12m: 84, demand24m: 92, priority: 'High', desc: 'Deploying optimized parameters on mobile chips and IoT hubs.' },
    ],
    'Cloud Engineer': [
      { name: 'Serverless Edge Functions', category: 'Cloud Computing', demand6m: 80, demand12m: 88, demand24m: 93, priority: 'High', desc: 'Executing lightweight application logic close to clients at the edge.' },
      { name: 'FinOps Cost Management Tools', category: 'Infrastructure Optimization', demand6m: 75, demand12m: 84, demand24m: 91, priority: 'High', desc: 'Automated cost analysis and automated scale-down policies.' },
      { name: 'Multi-Cloud Mesh Controllers', category: 'Networking Mesh', demand6m: 60, demand12m: 73, demand24m: 84, priority: 'Medium', desc: 'Cross-cloud unified virtual private networks and secure pipelines.' },
    ],
    'Cybersecurity Analyst': [
      { name: 'AI Threat Prediction Sensors', category: 'Intelligent Defense', demand6m: 90, demand12m: 95, demand24m: 98, priority: 'Critical', desc: 'ML-based anomaly discovery at packet levels in network traffic.' },
      { name: 'Zero Trust Cloud Architecture', category: 'Security Topology', demand6m: 86, demand12m: 92, demand24m: 96, priority: 'Critical', desc: 'Strict continuous authentication parameters across virtual assets.' },
      { name: 'Quantum-Safe Cryptography', category: 'Security Encryption', demand6m: 50, demand12m: 68, demand24m: 85, priority: 'High', desc: 'Lattice-based encryption defending against future supercomputers.' },
    ],
    'DevOps Engineer': [
      { name: 'GitOps (ArgoCD & Flux)', category: 'Continuous Delivery', demand6m: 87, demand12m: 93, demand24m: 96, priority: 'Critical', desc: 'Declarative Kubernetes setups versioned entirely in Git.' },
      { name: 'Platform Engineering & IDPs', category: 'Internal Developer Portals', demand6m: 82, demand12m: 89, demand24m: 94, priority: 'Critical', desc: 'Self-service workspace provisioning via Backstage or custom portals.' },
      { name: 'eBPF-based Cloud Observability', category: 'System Monitoring', demand6m: 68, demand12m: 80, demand24m: 91, priority: 'High', desc: 'Kernel-level traffic checking without instrumenting app code.' },
    ],
    'Android Developer': [
      { name: 'Kotlin Multiplatform (KMP)', category: 'Cross-platform Mobile', demand6m: 78, demand12m: 87, demand24m: 93, priority: 'Critical', desc: 'Sharing core logic libraries across iOS, Android, and Web apps.' },
      { name: 'Jetpack Compose Web/Desktop', category: 'UI Paradigms', demand6m: 65, demand12m: 78, demand24m: 88, priority: 'High', desc: 'Single Kotlin declarative layout system scaling beyond phones.' },
      { name: 'On-device Gemini Nano APIs', category: 'Mobile AI', demand6m: 70, demand12m: 84, demand24m: 91, priority: 'High', desc: 'Running highly optimized small language models offline on mobile hardware.' },
    ],
    'Product Manager': [
      { name: 'AI Product Strategy & Metrics', category: 'Product Leadership', demand6m: 90, demand12m: 95, demand24m: 98, priority: 'Critical', desc: 'Integrating LLM components, RAG, and measuring AI conversion metrics.' },
      { name: 'Data Lakehouse Product Analytics', category: 'Product Analytics', demand6m: 74, demand12m: 82, demand24m: 89, priority: 'High', desc: 'Direct SQL query pipelines over petabytes of user engagement logs.' },
      { name: 'Zero-Code Rapid Mockups', category: 'UI/UX Prototyping', demand6m: 68, demand12m: 76, demand24m: 84, priority: 'Medium', desc: 'AI UI generators transforming screenshots directly into web mockups.' },
    ],
  };

  const currentEmergingTech = EMERGING_TECH_BY_ROLE[activeRole] || EMERGING_TECH_BY_ROLE['Software Developer'];

  const chartData = currentEmergingTech.map(t => ({
    name: t.name.split('/')[0].trim(),
    '6 Months': t.demand6m,
    '12 Months': t.demand12m,
    '24 Months': t.demand24m,
  }));

  const handleRoleChange = (roleTitle: string) => {
    setActiveRole(roleTitle);
    const matchedRole = ROLES.find(r => r.title === roleTitle);
    if (matchedRole) {
      setSelectedRole(roleTitle);
    }
  };

  return (
    <div className="space-y-6 animate-fade-in">
      <div>
        <div className="section-label w-fit">🔮 PATENT FEATURE</div>
        <h2 className="text-2xl font-display font-bold text-white">Future Skill Prediction Engine</h2>
        <p className="text-slate-400 mt-1">AI-predicted skill demand for the next 6, 12, and 24 months based on role dynamics</p>
      </div>

      {/* Role Selection Dropdown */}
      <div className="glass-card p-4 flex flex-col sm:flex-row items-center justify-between gap-4">
        <div className="flex items-center gap-2">
          <Briefcase size={18} className="text-primary-400" />
          <div>
            <p className="text-xs text-slate-500 font-semibold uppercase tracking-wider">Select Career Track</p>
            <p className="text-sm text-white font-medium">Predicting future trends for: <span className="gradient-text font-bold">{activeRole}</span></p>
          </div>
        </div>
        <select
          value={activeRole}
          onChange={(e) => handleRoleChange(e.target.value)}
          className="input-field max-w-xs"
        >
          {ROLES.map(r => (
            <option key={r.id} value={r.title} style={{ background: '#0d1422' }}>{r.title}</option>
          ))}
        </select>
      </div>

      {/* Chart */}
      <div className="glass-card p-5">
        <h3 className="text-sm font-semibold text-white mb-4 flex items-center gap-2"><TrendingUp size={14} className="text-primary-400" /> Dynamic Skill Demand Growth Forecast</h3>
        <ResponsiveContainer width="100%" height={300}>
          <BarChart data={chartData} margin={{ top: 5, right: 10, left: -20, bottom: 20 }}>
            <CartesianGrid strokeDasharray="3 3" stroke="rgba(255,255,255,0.03)" vertical={false} />
            <XAxis dataKey="name" tick={{ fill: '#94a3b8', fontSize: 10 }} axisLine={false} tickLine={false} />
            <YAxis tick={{ fill: '#64748b', fontSize: 10 }} axisLine={false} tickLine={false} domain={[0, 100]} />
            <Tooltip contentStyle={{ background: 'rgba(13,20,34,0.95)', border: '1px solid rgba(14,165,233,0.3)', borderRadius: 8, fontSize: 11 }} />
            <Legend wrapperStyle={{ fontSize: 11, color: '#94a3b8' }} />
            <Bar dataKey="6 Months" fill="#0ea5e9" radius={[4, 4, 0, 0]} opacity={0.9} />
            <Bar dataKey="12 Months" fill="#d946ef" radius={[4, 4, 0, 0]} opacity={0.9} />
            <Bar dataKey="24 Months" fill="#00ff88" radius={[4, 4, 0, 0]} opacity={0.7} />
          </BarChart>
        </ResponsiveContainer>
      </div>

      {/* Emerging Technology Prediction Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {currentEmergingTech.map(tech => (
          <div key={tech.name} className="glass-card p-5 hover:scale-[1.02] transition-all duration-200 flex flex-col justify-between">
            <div>
              <div className="flex items-start justify-between mb-2">
                <div>
                  <h3 className="text-sm font-bold text-white leading-tight">{tech.name}</h3>
                  <span className="badge-blue mt-1 inline-block text-[10px]">{tech.category}</span>
                </div>
                <span className={`flex items-center gap-1 text-[9px] font-bold rounded-full px-2 py-0.5 border ${
                  tech.priority === 'Critical' ? 'text-red-400 bg-red-400/10 border-red-400/20' :
                  tech.priority === 'High' ? 'text-orange-400 bg-orange-400/10 border-orange-400/20' :
                  'text-yellow-400 bg-yellow-400/10 border-yellow-400/20'
                }`}>
                  <Sparkles size={8} /> {tech.priority}
                </span>
              </div>
              <p className="text-[11px] text-slate-400 mb-4">{tech.desc}</p>
            </div>
            <div className="space-y-2 mt-auto">
              {[
                { label: '6 Months', val: tech.demand6m, color: '#0ea5e9' },
                { label: '12 Months', val: tech.demand12m, color: '#d946ef' },
                { label: '24 Months', val: tech.demand24m, color: '#00ff88' }
              ].map(({ label, val, color }) => (
                <div key={label}>
                  <div className="flex justify-between text-[10px] mb-0.5"><span className="text-slate-500">{label}</span><span style={{ color }} className="font-bold">{val}%</span></div>
                  <div className="progress-bar h-1"><div className="progress-fill h-1" style={{ width: `${val}%`, background: `linear-gradient(90deg, ${color}, ${color}80)` }} /></div>
                </div>
              ))}
            </div>
          </div>
        ))}
      </div>

      {/* Recommendations */}
      <div className="glass-card p-5" style={{ background: 'linear-gradient(135deg,rgba(0,255,136,0.05),rgba(14,165,233,0.05))' }}>
        <h3 className="text-sm font-semibold text-white mb-3 flex items-center gap-2"><Zap size={14} className="text-green-400" /> AI Strategic Learning Recommendations</h3>
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-3">
          {[
            { priority: '🔥 Critical Priority', action: 'Up-skill Immediately', desc: `Enroll in courses teaching ${currentEmergingTech.find(t => t.priority === 'Critical')?.name || 'latest core AI components'} to stay competitive.` },
            { priority: '⚡ High Priority', action: 'Build Mini-Projects', desc: 'Design one open-source project within the next 3 months leveraging next-generation frameworks.' },
            { priority: '📈 Medium Priority', action: 'Continuous Observation', desc: 'Allocate 2 hours per week reading technical documentation and research briefs on edge computing.' },
          ].map(({ priority, action, desc }) => (
            <div key={priority} className="p-3 rounded-xl flex flex-col justify-between" style={{ background: 'rgba(255,255,255,0.02)', border: '1px solid rgba(255,255,255,0.06)' }}>
              <div>
                <p className="text-xs font-bold text-white mb-1 flex items-center gap-1"><BookOpen size={10} className="text-primary-400" />{priority}</p>
                <p className="text-[10px] text-slate-500 font-semibold uppercase mb-2">{action}</p>
              </div>
              <p className="text-[11px] text-slate-400 leading-relaxed">{desc}</p>
            </div>
          ))}
        </div>
      </div>

      <div className="flex items-center gap-2 text-xs text-slate-500 p-3 rounded-xl" style={{ background: 'rgba(255,255,255,0.03)' }}>
        <AlertTriangle size={12} className="text-yellow-500" />
        <span>Predictions based on job market trend analysis. Updated monthly. For educational purposes.</span>
      </div>
    </div>
  );
}

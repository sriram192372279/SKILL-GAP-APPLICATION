import { GitBranch, ArrowRight } from 'lucide-react';

const SKILL_TREES = [
  {
    name: 'Java Backend Path',
    color: '#ff6b35',
    nodes: ['Java Basics', 'OOP Principles', 'Collections', 'Exception Handling', 'Multithreading', 'Spring Boot', 'Hibernate/JPA', 'REST APIs', 'Microservices', 'Kubernetes'],
  },
  {
    name: 'Python Data Science Path',
    color: '#0ea5e9',
    nodes: ['Python Basics', 'NumPy', 'Pandas', 'Data Visualization', 'Statistics', 'Machine Learning', 'Deep Learning', 'NLP', 'MLOps', 'AI Systems'],
  },
  {
    name: 'Cloud / DevOps Path',
    color: '#00ff88',
    nodes: ['Linux', 'Networking', 'Docker', 'AWS Basics', 'Terraform', 'Kubernetes', 'CI/CD', 'Monitoring', 'Security', 'Platform Engineering'],
  },
  {
    name: 'Web Full Stack Path',
    color: '#d946ef',
    nodes: ['HTML/CSS', 'JavaScript', 'TypeScript', 'React', 'Node.js', 'Databases', 'REST APIs', 'Authentication', 'Testing', 'Deployment'],
  },
];

export default function SkillGraphPage() {
  return (
    <div className="space-y-6 animate-fade-in">
      <div>
        <div className="section-label w-fit">🔗 PATENT FEATURE</div>
        <h2 className="text-2xl font-display font-bold text-white">Skill Dependency Graph</h2>
        <p className="text-slate-400 mt-1">Visual learning maps showing prerequisite relationships</p>
      </div>

      {SKILL_TREES.map(tree => (
        <div key={tree.name} className="glass-card p-5">
          <h3 className="text-sm font-semibold text-white mb-4 flex items-center gap-2">
            <GitBranch size={14} style={{ color: tree.color }} />{tree.name}
          </h3>
          <div className="overflow-x-auto pb-2">
            <div className="flex items-center gap-2 min-w-max">
              {tree.nodes.map((node, i) => (
                <div key={node} className="flex items-center gap-2">
                  <div className="flex flex-col items-center gap-1">
                    <div className="w-24 text-center px-2 py-2 rounded-xl text-xs font-medium cursor-pointer hover:scale-105 transition-all"
                      style={{ background: `${tree.color}15`, border: `1px solid ${tree.color}40`, color: tree.color }}>
                      {node}
                    </div>
                    <div className="text-[10px] text-slate-600">Step {i + 1}</div>
                  </div>
                  {i < tree.nodes.length - 1 && <ArrowRight size={12} className="flex-shrink-0 text-slate-600" />}
                </div>
              ))}
            </div>
          </div>
          <div className="mt-3 flex items-center gap-2 text-xs text-slate-500">
            <span style={{ color: tree.color }}>→</span>
            <span>Complete each step before progressing. Arrows show prerequisites.</span>
          </div>
        </div>
      ))}

      <div className="glass-card p-5" style={{ background: 'linear-gradient(135deg,rgba(14,165,233,0.06),rgba(217,70,239,0.06))' }}>
        <h3 className="text-sm font-semibold text-white mb-3">💡 How to Use the Skill Graph</h3>
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-3">
          {[
            { icon: '1️⃣', text: 'Choose your target career path from the graphs above' },
            { icon: '2️⃣', text: 'Start from Step 1 and work progressively through each skill node' },
            { icon: '3️⃣', text: 'Each skill unlocks the next — skip nothing, each is a prerequisite' },
          ].map(({ icon, text }) => (
            <div key={text} className="p-3 rounded-xl text-center" style={{ background: 'rgba(255,255,255,0.03)' }}>
              <div className="text-2xl mb-2">{icon}</div>
              <p className="text-xs text-slate-400">{text}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

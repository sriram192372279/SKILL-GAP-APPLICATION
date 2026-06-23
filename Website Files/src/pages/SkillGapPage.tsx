import { useState } from 'react';
import { useApp } from '../context/AppContext';
import { Target, CheckCircle, XCircle, AlertCircle, Upload, BookOpen, Clock, ExternalLink, ChevronDown, ChevronUp, Flame, Zap } from 'lucide-react';

// Learning resources & metadata per skill
const SKILL_LEARNING: Record<string, { time: string; priority: 'critical' | 'high' | 'medium'; why: string; resources: { label: string; url: string; type: 'video' | 'docs' | 'practice' }[] }> = {
  'JavaScript': {
    time: '3–4 weeks', priority: 'critical',
    why: 'Core language for web development — required for almost every frontend & backend role.',
    resources: [
      { label: 'JavaScript.info (Best free guide)', url: 'https://javascript.info', type: 'docs' },
      { label: 'Traversy Media – JS Crash Course', url: 'https://youtube.com/watch?v=hdI2bqOjy3c', type: 'video' },
      { label: 'LeetCode – JS Problems', url: 'https://leetcode.com/problemset/?topicSlugs=javascript', type: 'practice' },
    ],
  },
  'TypeScript': {
    time: '1–2 weeks', priority: 'high',
    why: 'Type-safe JS used in most modern codebases; expected in senior roles.',
    resources: [
      { label: 'TypeScript Official Docs', url: 'https://www.typescriptlang.org/docs/', type: 'docs' },
      { label: 'Net Ninja – TypeScript Tutorial', url: 'https://youtube.com/watch?v=2pZmKW9-I_k', type: 'video' },
      { label: 'TypeScript Exercises', url: 'https://typescript-exercises.github.io', type: 'practice' },
    ],
  },
  'React': {
    time: '3–5 weeks', priority: 'critical',
    why: 'Most in-demand frontend library; used by 40%+ of job listings.',
    resources: [
      { label: 'React Official Docs (react.dev)', url: 'https://react.dev', type: 'docs' },
      { label: 'Scrimba – Learn React for Free', url: 'https://scrimba.com/learn/learnreact', type: 'video' },
      { label: 'Frontend Mentor – React Challenges', url: 'https://www.frontendmentor.io', type: 'practice' },
    ],
  },
  'Node.js': {
    time: '2–3 weeks', priority: 'high',
    why: 'Essential for backend JS development; powers REST APIs and microservices.',
    resources: [
      { label: 'Node.js Official Docs', url: 'https://nodejs.org/en/docs', type: 'docs' },
      { label: 'Traversy Media – Node.js Crash Course', url: 'https://youtube.com/watch?v=fBNz5xF-Kx4', type: 'video' },
      { label: 'Exercism – JavaScript Track', url: 'https://exercism.org/tracks/javascript', type: 'practice' },
    ],
  },
  'SQL': {
    time: '2–3 weeks', priority: 'critical',
    why: 'Required for any role involving data. Almost every company uses relational databases.',
    resources: [
      { label: 'SQLZoo – Interactive SQL', url: 'https://sqlzoo.net', type: 'practice' },
      { label: 'Mode Analytics SQL Tutorial', url: 'https://mode.com/sql-tutorial/', type: 'docs' },
      { label: 'CS50 – SQL Lecture', url: 'https://cs50.harvard.edu/x/', type: 'video' },
    ],
  },
  'Docker': {
    time: '1–2 weeks', priority: 'high',
    why: 'Containerization is standard in modern deployments; expected in DevOps & backend roles.',
    resources: [
      { label: 'Docker Official Get Started', url: 'https://docs.docker.com/get-started/', type: 'docs' },
      { label: 'TechWorld with Nana – Docker Tutorial', url: 'https://youtube.com/watch?v=3c-iBn73dDE', type: 'video' },
      { label: 'Play with Docker', url: 'https://labs.play-with-docker.com', type: 'practice' },
    ],
  },
  'AWS': {
    time: '3–4 weeks', priority: 'high',
    why: 'Top cloud platform; AWS certifications significantly boost employability.',
    resources: [
      { label: 'AWS Free Tier + Documentation', url: 'https://aws.amazon.com/free/', type: 'docs' },
      { label: 'FreeCodeCamp – AWS Full Course', url: 'https://youtube.com/watch?v=ulprqHHWlng', type: 'video' },
      { label: 'AWS Skill Builder', url: 'https://skillbuilder.aws', type: 'practice' },
    ],
  },
  'Git': {
    time: '3–5 days', priority: 'critical',
    why: 'Version control is a day-one skill. No team works without Git.',
    resources: [
      { label: 'Pro Git Book (Free)', url: 'https://git-scm.com/book/en/v2', type: 'docs' },
      { label: 'Git & GitHub Crash Course', url: 'https://youtube.com/watch?v=RGOj5yH7evk', type: 'video' },
      { label: 'Learn Git Branching (Interactive)', url: 'https://learngitbranching.js.org', type: 'practice' },
    ],
  },
  'REST APIs': {
    time: '1–2 weeks', priority: 'high',
    why: 'APIs are how modern apps communicate. REST is the industry standard.',
    resources: [
      { label: 'REST API Design Guide', url: 'https://restfulapi.net', type: 'docs' },
      { label: 'Traversy Media – REST API Tutorial', url: 'https://youtube.com/watch?v=Q-BpqyOT3a8', type: 'video' },
      { label: 'Postman Learning Center', url: 'https://learning.postman.com', type: 'practice' },
    ],
  },
  'System Design': {
    time: '4–6 weeks', priority: 'high',
    why: 'Required for senior roles and FAANG interviews. Tests your engineering thinking.',
    resources: [
      { label: 'System Design Primer (GitHub)', url: 'https://github.com/donnemartin/system-design-primer', type: 'docs' },
      { label: 'Gaurav Sen – System Design Playlist', url: 'https://youtube.com/playlist?list=PLMCXHnjXnTnvo6alSjVkgxV-VH6EPyvoX', type: 'video' },
      { label: 'Pramp – Mock Interviews', url: 'https://www.pramp.com', type: 'practice' },
    ],
  },
  'Python': {
    time: '3–5 weeks', priority: 'critical',
    why: '#1 language for Data Science and ML; also widely used in backend automation.',
    resources: [
      { label: 'Python Official Tutorial', url: 'https://docs.python.org/3/tutorial/', type: 'docs' },
      { label: 'Corey Schafer – Python Tutorials', url: 'https://youtube.com/playlist?list=PL-osiE80TeTt2d9bfVyTiXJA-UTHn6WwU', type: 'video' },
      { label: 'HackerRank – Python Practice', url: 'https://www.hackerrank.com/domains/python', type: 'practice' },
    ],
  },
  'Machine Learning': {
    time: '6–10 weeks', priority: 'critical',
    why: 'Core domain skill for Data Science / ML Engineer roles.',
    resources: [
      { label: 'Andrew Ng – ML Specialization (Coursera)', url: 'https://coursera.org/specializations/machine-learning-introduction', type: 'video' },
      { label: 'Scikit-learn Documentation', url: 'https://scikit-learn.org/stable/user_guide.html', type: 'docs' },
      { label: 'Kaggle – ML Competitions', url: 'https://www.kaggle.com/competitions', type: 'practice' },
    ],
  },
  'Kubernetes': {
    time: '2–3 weeks', priority: 'high',
    why: 'Industry standard for container orchestration in cloud-native applications.',
    resources: [
      { label: 'Kubernetes Official Docs', url: 'https://kubernetes.io/docs/home/', type: 'docs' },
      { label: 'TechWorld with Nana – K8s Tutorial', url: 'https://youtube.com/watch?v=X48VuDVv0do', type: 'video' },
      { label: 'Killer.sh – K8s Practice', url: 'https://killer.sh', type: 'practice' },
    ],
  },
  'Kotlin': {
    time: '3–4 weeks', priority: 'critical',
    why: 'Official Android development language replacing Java.',
    resources: [
      { label: 'Kotlin Official Docs', url: 'https://kotlinlang.org/docs/', type: 'docs' },
      { label: 'Philipp Lackner – Android + Kotlin', url: 'https://youtube.com/c/PhilippLackner', type: 'video' },
      { label: 'Kotlin Koans', url: 'https://play.kotlinlang.org/koans', type: 'practice' },
    ],
  },
  'GraphQL': {
    time: '1–2 weeks', priority: 'medium',
    why: 'Modern API query language adopted by many tech companies as REST alternative.',
    resources: [
      { label: 'GraphQL Official Docs', url: 'https://graphql.org/learn/', type: 'docs' },
      { label: 'Traversy Media – GraphQL Crash Course', url: 'https://youtube.com/watch?v=BcLNfwF04Kw', type: 'video' },
      { label: 'Apollo Odyssey (Hands-on)', url: 'https://www.apollographql.com/tutorials/', type: 'practice' },
    ],
  },
  'CI/CD': {
    time: '1–2 weeks', priority: 'high',
    why: 'Automating builds & deployments is expected in modern engineering teams.',
    resources: [
      { label: 'GitHub Actions Docs', url: 'https://docs.github.com/en/actions', type: 'docs' },
      { label: 'TechWorld Nana – CI/CD Pipeline', url: 'https://youtube.com/watch?v=R8_veQiYBjI', type: 'video' },
      { label: 'GitHub Actions – Starter Workflows', url: 'https://github.com/actions/starter-workflows', type: 'practice' },
    ],
  },
};

const PRIORITY_CONFIG = {
  critical: { label: 'Critical', color: '#ef4444', bg: 'rgba(239,68,68,0.1)', icon: Flame },
  high: { label: 'High', color: '#f97316', bg: 'rgba(249,115,22,0.1)', icon: Zap },
  medium: { label: 'Medium', color: '#fbbf24', bg: 'rgba(251,191,36,0.1)', icon: AlertCircle },
};

const RESOURCE_TYPE_CONFIG = {
  video: { label: '▶ Video', color: '#ef4444' },
  docs: { label: '📄 Docs', color: '#60a5fa' },
  practice: { label: '🏋 Practice', color: '#00ff88' },
};

function SkillLearnCard({ skill, index }: { skill: string; index: number }) {
  const [open, setOpen] = useState(index === 0);
  const info = SKILL_LEARNING[skill];

  if (!info) {
    return (
      <div className="glass-card p-4 flex items-center gap-3">
        <div className="w-7 h-7 rounded-lg flex items-center justify-center text-xs font-bold text-white flex-shrink-0"
          style={{ background: 'linear-gradient(135deg,#0ea5e9,#d946ef)' }}>{index + 1}</div>
        <div className="flex-1">
          <p className="text-sm font-semibold text-white">{skill}</p>
          <p className="text-xs text-slate-500">Search on YouTube, official docs, and practice on LeetCode / HackerRank.</p>
        </div>
      </div>
    );
  }

  const pConf = PRIORITY_CONFIG[info.priority];
  const PIcon = pConf.icon;

  return (
    <div className="glass-card overflow-hidden transition-all duration-200">
      <button
        onClick={() => setOpen(o => !o)}
        className="w-full p-4 flex items-center gap-3 text-left hover:bg-white/[0.02] transition-colors"
      >
        <div className="w-7 h-7 rounded-lg flex items-center justify-center text-xs font-bold text-white flex-shrink-0"
          style={{ background: 'linear-gradient(135deg,#0ea5e9,#d946ef)' }}>{index + 1}</div>
        <div className="flex-1 min-w-0">
          <div className="flex items-center gap-2">
            <p className="text-sm font-semibold text-white">{skill}</p>
            <span className="flex items-center gap-1 text-[10px] px-2 py-0.5 rounded-full font-medium"
              style={{ background: pConf.bg, color: pConf.color }}>
              <PIcon size={9} />{pConf.label}
            </span>
          </div>
          <p className="text-xs text-slate-500 mt-0.5 flex items-center gap-1">
            <Clock size={10} /> {info.time} to learn
          </p>
        </div>
        {open ? <ChevronUp size={16} className="text-slate-500 flex-shrink-0" /> : <ChevronDown size={16} className="text-slate-500 flex-shrink-0" />}
      </button>

      {open && (
        <div className="px-4 pb-4 space-y-3 border-t" style={{ borderColor: 'rgba(255,255,255,0.06)' }}>
          <p className="text-xs text-slate-400 pt-3">💡 <span className="text-slate-300">{info.why}</span></p>
          <div className="space-y-2">
            <p className="text-[11px] font-semibold text-slate-500 uppercase tracking-wide flex items-center gap-1"><BookOpen size={10} /> Learning Resources</p>
            {info.resources.map(r => {
              const rtConf = RESOURCE_TYPE_CONFIG[r.type];
              return (
                <a key={r.label} href={r.url} target="_blank" rel="noreferrer"
                  className="flex items-center gap-2 p-2 rounded-xl hover:scale-[1.01] transition-all group"
                  style={{ background: 'rgba(255,255,255,0.03)', border: '1px solid rgba(255,255,255,0.06)' }}>
                  <span className="text-[10px] px-2 py-0.5 rounded-full font-medium flex-shrink-0"
                    style={{ background: 'rgba(255,255,255,0.06)', color: rtConf.color }}>{rtConf.label}</span>
                  <span className="text-xs text-slate-300 group-hover:text-white transition-colors flex-1">{r.label}</span>
                  <ExternalLink size={11} className="text-slate-600 group-hover:text-slate-400 flex-shrink-0" />
                </a>
              );
            })}
          </div>
        </div>
      )}
    </div>
  );
}

export default function SkillGapPage() {
  const { skillGap, setActiveModule, selectedRole } = useApp();

  if (!skillGap) {
    return (
      <div className="space-y-6 animate-fade-in">
        <div>
          <div className="section-label w-fit">🎯 Skill Gap Analysis</div>
          <h2 className="text-2xl font-display font-bold text-white">Skill Gap Analysis</h2>
          <p className="text-slate-400 mt-1">See exactly which skills you're missing for your target role</p>
        </div>
        <div className="glass-card p-12 text-center flex flex-col items-center gap-4"
          style={{ border: '2px dashed rgba(14,165,233,0.2)' }}>
          <Target size={48} className="text-primary-400/30" />
          <p className="text-slate-400 text-lg font-medium">No Analysis Available</p>
          <p className="text-slate-500 text-sm">Upload your resume and select a target job role first.</p>
          <button onClick={() => setActiveModule('resume')} className="btn-primary flex items-center gap-2 mt-2">
            <Upload size={14} />Analyze My Resume
          </button>
        </div>
      </div>
    );
  }

  const { matchingSkills, missingSkills, matchPercentage, role } = skillGap;
  const matchColor = matchPercentage >= 70 ? '#00ff88' : matchPercentage >= 45 ? '#fbbf24' : '#ef4444';

  return (
    <div className="space-y-6 animate-fade-in">
      <div>
        <div className="section-label w-fit">🎯 Skill Gap Analysis</div>
        <h2 className="text-2xl font-display font-bold text-white">Skill Gap Analysis</h2>
        <p className="text-slate-400 mt-1">Results for <span className="text-primary-400 font-semibold">{role}</span></p>
      </div>

      {/* Match Score Banner */}
      <div className="glass-card p-6 flex flex-col sm:flex-row items-center gap-6"
        style={{ background: `${matchColor}08`, border: `1px solid ${matchColor}25` }}>
        <div className="text-center">
          <div className="text-6xl font-display font-black" style={{ color: matchColor }}>{matchPercentage}%</div>
          <p className="text-slate-400 text-sm mt-1">Skills Matched</p>
        </div>
        <div className="flex-1">
          <div className="progress-bar h-4">
            <div className="progress-fill h-4" style={{ width: `${matchPercentage}%`, background: `linear-gradient(90deg, ${matchColor}, ${matchColor}80)` }} />
          </div>
          <div className="flex justify-between text-xs text-slate-500 mt-1">
            <span>0% – No match</span><span>100% – Perfect match</span>
          </div>
          <p className="text-sm text-slate-300 mt-3">
            {matchPercentage >= 70
              ? '🎉 Great profile match! Focus on the missing skills below to strengthen your candidacy.'
              : matchPercentage >= 45
              ? '📈 Moderate match. Upskilling in the missing areas can significantly improve your chances.'
              : '⚠️ Low match for this role. Focus on building the missing foundational skills first.'}
          </p>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {/* Matching Skills */}
        <div className="glass-card p-5">
          <h3 className="text-sm font-semibold text-white mb-3 flex items-center gap-2">
            <CheckCircle size={15} className="text-green-400" />
            Skills You Have ({matchingSkills.length})
          </h3>
          <div className="flex flex-wrap gap-2">
            {matchingSkills.map(skill => (
              <span key={skill} className="skill-tag-present">{skill}</span>
            ))}
            {matchingSkills.length === 0 && <p className="text-xs text-slate-500">No matching skills detected for this role.</p>}
          </div>
        </div>

        {/* Missing Skills */}
        <div className="glass-card p-5">
          <h3 className="text-sm font-semibold text-white mb-3 flex items-center gap-2">
            <XCircle size={15} className="text-red-400" />
            Skills You're Missing ({missingSkills.length})
          </h3>
          <div className="flex flex-wrap gap-2">
            {missingSkills.map(skill => (
              <span key={skill} className="skill-tag-missing">{skill}</span>
            ))}
            {missingSkills.length === 0 && <p className="text-xs text-green-400">🎉 You have all required skills!</p>}
          </div>
        </div>
      </div>

      {/* ── What to Learn ── */}
      {missingSkills.length > 0 && (
        <div className="space-y-3">
          <div className="flex items-center gap-3">
            <div>
              <h3 className="text-lg font-semibold text-white flex items-center gap-2">
                <BookOpen size={18} className="text-primary-400" />
                What You Need to Learn for <span className="text-primary-400 ml-1">{role}</span>
              </h3>
              <p className="text-slate-500 text-sm mt-0.5">
                {missingSkills.length} skill{missingSkills.length > 1 ? 's' : ''} missing — click each to see resources & time estimates
              </p>
            </div>
          </div>

          <div className="space-y-2">
            {missingSkills.map((skill, i) => (
              <SkillLearnCard key={skill} skill={skill} index={i} />
            ))}
          </div>

          <button onClick={() => setActiveModule('roadmap')} className="btn-primary w-full mt-2 text-sm">
            🗺️ Generate Full 90-Day Learning Roadmap for {role}
          </button>
        </div>
      )}
    </div>
  );
}

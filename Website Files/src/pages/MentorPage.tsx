import { useState, useRef, useEffect } from 'react';
import { useApp } from '../context/AppContext';
import { Send, Bot, User, Zap } from 'lucide-react';
import type { ChatMessage } from '../types';

const RESPONSES: Record<string, string> = {
  default: "I'm your AI Career Mentor! Ask me about career paths, interview prep, skill gaps, company research, or learning roadmaps. I'm here to help you land your dream job! 🚀",
  google: "**Google Interview Prep:**\n• Master LeetCode Medium/Hard problems daily\n• Study System Design (Grokking the System Design Interview)\n• Practice Googleyness behavioral questions using STAR method\n• Focus on: Arrays, Trees, Graphs, Dynamic Programming\n• Typical timeline: 3-6 months of dedicated preparation",
  resume: "**Resume Tips:**\n• Use ATS-friendly format with clean sections\n• Quantify achievements (e.g., 'Improved performance by 40%')\n• Include relevant keywords from job descriptions\n• Keep to 1 page for <5 years experience\n• Strong action verbs: Led, Built, Optimized, Designed",
  java: "**Java Full Stack Roadmap:**\n1. Core Java → OOP → Collections → Multithreading\n2. Spring Boot → Hibernate → REST APIs\n3. React.js → Axios → State Management\n4. MySQL → JPA → Transactions\n5. Docker → AWS EC2 → CI/CD with Jenkins",
  python: "**Python for Data Science:**\n• Start: Python basics, NumPy, Pandas\n• Visualization: Matplotlib, Seaborn, Plotly\n• ML: Scikit-learn, XGBoost\n• Deep Learning: TensorFlow/PyTorch\n• Projects: Recommendation system, NLP classifier",
  salary: "**IT Salary Ranges in India (2024):**\n• Fresher (0-1yr): ₹3L – ₹8L\n• Junior (1-3yr): ₹6L – ₹15L\n• Mid-level (3-6yr): ₹12L – ₹30L\n• Senior (6-10yr): ₹25L – ₹60L\n• Tech Lead / Architect: ₹50L+\n\nGoogle/Microsoft/Amazon pay 2-3x market rate.",
  interview: "**Interview Preparation Strategy:**\n1. DSA: Practice 150+ problems on LeetCode\n2. System Design: Study 10+ common systems\n3. OOPS: Solid principles, design patterns\n4. Behavioral: 10 STAR stories\n5. Company research: Products, culture, recent news\n\nTimeline: 2-3 months for top companies",
};

function getResponse(input: string, role: string, company: string, missingSkills: string[], atsScore: number | null): string {
  const lower = input.toLowerCase();
  
  if (lower.includes('google') || lower.includes('amazon') || lower.includes('microsoft') || lower.includes(company.toLowerCase())) {
    const target = lower.includes('google') ? 'Google' : lower.includes('amazon') ? 'Amazon' : lower.includes('microsoft') ? 'Microsoft' : company;
    return `**${target} Interview Prep Plan for ${role}:**\n• Study core technologies: ${target === 'Google' ? 'Go, C++, Python, Java' : target === 'Microsoft' ? 'C#, .NET, Azure' : 'Java, Python, AWS'}\n• Focus on solving medium to hard coding challenges regularly.\n• Practice behavioral questions mapping to ${target}'s cultural principles (e.g. Leadership Principles at Amazon, Googleyness at Google).\n• Since your target track is ${role}, build deep hands-on portfolio projects showing system design competence.`;
  }
  
  if (lower.includes('resume') || lower.includes('cv') || lower.includes('ats') || lower.includes('score')) {
    const scoreText = atsScore ? `Your current resume has an ATS score of **${atsScore}/100**.` : `You haven't uploaded a resume yet.`;
    return `**Resume & ATS Optimization Guidance:**\n• ${scoreText}\n• Focus on structural clarity (e.g. headers like Experience, Projects, Skills).\n• Include measurable metrics (e.g. 'boosted query speed by 35%').\n• Ensure action verbs (Built, Led, Engineered) start each bullet point.\n• Avoid using graphical icons or multi-column layouts which can sometimes confuse parser scanning algorithms.`;
  }
  
  if (lower.includes('skill') || lower.includes('gap') || lower.includes('missing') || lower.includes('learn')) {
    if (missingSkills.length > 0) {
      return `**Custom Skills Upskilling Plan:**\n• Your analysis indicates a gap in: **${missingSkills.slice(0, 3).join(', ')}**.\n• Focus on mastering **${missingSkills[0]}** first, allocating about 10-15 hours per week.\n• Build small sandbox tools using **${missingSkills[0]}** to showcase capability on your GitHub profile.\n• Leverage documentation and structured sandboxes for interactive practice.`;
    }
    return `**Skills & Learning Strategy:**\n• Your current tech stack is highly aligned with your target track of ${role}!\n• To stand out further, start exploring advanced architectures or cloud certifications (AWS/Azure).\n• Build full-scale deployment pipelines with CI/CD tools to demonstrate production capability.`;
  }
  
  if (lower.includes('java') || lower.includes('spring') || lower.includes('fullstack')) {
    return "**Java Full Stack Roadmap:**\n1. Core Java → OOP → Collections → Multithreading\n2. Spring Boot 3.x → JPA/Hibernate → REST APIs\n3. Front-end Framework: React.js / TypeScript\n4. Database: PostgreSQL / MySQL\n5. Cloud & Containers: Docker → AWS EC2 → GitHub Actions";
  }
  
  if (lower.includes('python') || lower.includes('data') || lower.includes('ml') || lower.includes('science')) {
    return "**Data Science & Machine Learning Pathway:**\n• Foundations: Python, NumPy, Pandas, Data Wrangling\n• Visualizations: Matplotlib, Seaborn, Tableau\n• Machine Learning: Scikit-learn, Feature Engineering, Regression & Classification\n• Deep Learning: PyTorch or TensorFlow frameworks\n• Key Project Ideas: Recommendation systems, NLP text classifier";
  }
  
  if (lower.includes('salary') || lower.includes('pay') || lower.includes('ctc')) {
    return `**IT Compensation Trends for ${role} (India):**\n• Entry-level (0-1 yr): ₹4L – ₹8L\n• Intermediate (1-3 yrs): ₹7L – ₹15L\n• Mid-to-Senior (3-6 yrs): ₹14L – ₹30L\n• Senior / Tech Lead (6+ yrs): ₹28L – ₹60L\n• Top-tier product companies (Google/Atlassian/Microsoft) pay significantly above market averages, often ranging from ₹18L to ₹45L+ base for entry/mid-level roles.`;
  }
  
  if (lower.includes('interview') || lower.includes('prepare') || lower.includes('prep') || lower.includes('strategy')) {
    return `**Core Preparation Roadmap for ${role} at ${company}:**\n1. Brush up on core concepts of ${role}.\n2. Solve 100+ coding challenges focusing on key algorithms.\n3. Research ${company}'s company culture, products, and tech stack.\n4. Conduct simulated mock prep drills with sample interview questions.`;
  }
  
  return `👋 I'm your AI Career Coach. Ask me how to improve your ATS score, how to learn missing skills like **${missingSkills[0] || 'TypeScript/Docker'}**, how to prep for **${company}** as a **${role}**, or details about compensation packages!`;
}

const QUICK_PROMPTS = ['How to crack target interview?', 'Resume improvement tips', 'Java Full Stack roadmap', 'Data Science career path', 'Salary ranges in IT', 'Interview preparation strategy'];

export default function MentorPage() {
  const { user, selectedRole, selectedCompany, skillGap, resumeScore } = useApp();
  const [messages, setMessages] = useState<ChatMessage[]>([
    { id: '0', role: 'assistant', content: `👋 Hi! I'm your **AI Career Mentor**. I'm here to help with career guidance, interview prep, skill recommendations, and company research. All responses are generated locally — your conversations stay private!\n\nWhat would you like to discuss today?`, timestamp: new Date().toISOString() },
  ]);
  const [input, setInput] = useState('');
  const [typing, setTyping] = useState(false);
  const bottomRef = useRef<HTMLDivElement>(null);

  useEffect(() => { bottomRef.current?.scrollIntoView({ behavior: 'smooth' }); }, [messages]);

  const sendMessage = (text: string) => {
    if (!text.trim()) return;
    const userMsg: ChatMessage = { id: Date.now().toString(), role: 'user', content: text, timestamp: new Date().toISOString() };
    setMessages(prev => [...prev, userMsg]);
    setInput('');
    setTyping(true);
    
    const role = selectedRole || 'Software Developer';
    const company = selectedCompany || 'Google';
    const missing = skillGap?.missingSkills || [];
    const ats = resumeScore?.ats || null;

    setTimeout(() => {
      const reply: ChatMessage = { id: (Date.now() + 1).toString(), role: 'assistant', content: getResponse(text, role, company, missing, ats), timestamp: new Date().toISOString() };
      setMessages(prev => [...prev, reply]);
      setTyping(false);
    }, 1000 + Math.random() * 800);
  };

  const formatMessage = (text: string) => {
    return text.split('\n').map((line, i) => {
      if (line.startsWith('**') && line.endsWith('**')) return <p key={i} className="font-bold text-white mb-1">{line.replace(/\*\*/g, '')}</p>;
      if (line.startsWith('•')) return <p key={i} className="text-slate-300 ml-2 text-sm">• {line.slice(1)}</p>;
      if (/^\d+\./.test(line)) return <p key={i} className="text-slate-300 ml-2 text-sm">{line}</p>;
      if (line === '') return <div key={i} className="h-1" />;
      return <p key={i} className="text-slate-300 text-sm">{line.replace(/\*\*/g, '')}</p>;
    });
  };

  return (
    <div className="flex flex-col h-[calc(100vh-8rem)] animate-fade-in">
      <div className="mb-4">
        <div className="section-label w-fit">🤖 PATENT FEATURE</div>
        <h2 className="text-2xl font-display font-bold text-white">Offline AI Career Mentor</h2>
        <p className="text-slate-400 mt-1 text-sm">Privacy-first local AI — conversations never leave your device</p>
      </div>

      {/* Chat Area */}
      <div className="flex-1 overflow-y-auto space-y-4 pb-4 no-scrollbar">
        {messages.map(msg => (
          <div key={msg.id} className={`flex gap-3 ${msg.role === 'user' ? 'flex-row-reverse' : ''}`}>
            <div className={`w-8 h-8 rounded-full flex-shrink-0 flex items-center justify-center ${msg.role === 'assistant' ? 'bg-gradient-to-br from-primary-500 to-accent-500' : 'bg-gradient-to-br from-slate-600 to-slate-700'}`}>
              {msg.role === 'assistant' ? <Bot size={14} className="text-white" /> : <User size={14} className="text-white" />}
            </div>
            <div className={`max-w-[80%] p-4 rounded-2xl ${msg.role === 'assistant' ? 'rounded-tl-sm' : 'rounded-tr-sm'}`}
              style={msg.role === 'assistant'
                ? { background: 'rgba(14,165,233,0.08)', border: '1px solid rgba(14,165,233,0.15)' }
                : { background: 'linear-gradient(135deg,#0ea5e9,#d946ef)', borderRadius: '16px 4px 16px 16px' }}>
              <div className="space-y-0.5">{formatMessage(msg.content)}</div>
              <p className="text-[10px] text-slate-600 mt-2">{new Date(msg.timestamp).toLocaleTimeString()}</p>
            </div>
          </div>
        ))}
        {typing && (
          <div className="flex gap-3">
            <div className="w-8 h-8 rounded-full flex items-center justify-center" style={{ background: 'linear-gradient(135deg,#0ea5e9,#d946ef)' }}>
              <Bot size={14} className="text-white" />
            </div>
            <div className="p-4 rounded-2xl rounded-tl-sm flex items-center gap-1.5" style={{ background: 'rgba(14,165,233,0.08)', border: '1px solid rgba(14,165,233,0.15)' }}>
              {[0, 1, 2].map(i => <div key={i} className="w-2 h-2 rounded-full bg-primary-400 animate-bounce" style={{ animationDelay: `${i * 0.15}s` }} />)}
            </div>
          </div>
        )}
        <div ref={bottomRef} />
      </div>

      {/* Quick Prompts */}
      <div className="flex gap-2 overflow-x-auto pb-2 no-scrollbar">
        {QUICK_PROMPTS.map(p => (
          <button key={p} onClick={() => sendMessage(p)}
            className="flex-shrink-0 text-xs px-3 py-1.5 rounded-full text-primary-400 border border-primary-400/30 hover:bg-primary-400/10 transition-all whitespace-nowrap">
            {p}
          </button>
        ))}
      </div>

      {/* Input */}
      <div className="flex gap-3 mt-2">
        <input value={input} onChange={e => setInput(e.target.value)} onKeyDown={e => e.key === 'Enter' && sendMessage(input)}
          placeholder="Ask me anything about your career..." className="input-field flex-1" />
        <button onClick={() => sendMessage(input)} disabled={!input.trim() || typing}
          className="w-11 h-11 rounded-xl flex items-center justify-center transition-all disabled:opacity-40"
          style={{ background: 'linear-gradient(135deg,#0ea5e9,#d946ef)' }}>
          <Send size={16} className="text-white" />
        </button>
      </div>
    </div>
  );
}

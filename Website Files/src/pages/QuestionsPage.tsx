import { useState } from 'react';
import { INTERVIEW_QUESTIONS } from '../data/staticData';
import { HelpCircle, ChevronDown, ChevronUp, Search } from 'lucide-react';

type Category = 'all' | 'aptitude' | 'coding' | 'technical' | 'hr' | 'oops' | 'sql' | 'dsa';

const CATEGORIES: Category[] = ['all', 'aptitude', 'coding', 'technical', 'hr', 'oops', 'sql', 'dsa'];
const DIFF_COLORS = { Easy: '#00ff88', Medium: '#fbbf24', Hard: '#ef4444' };

export default function QuestionsPage() {
  const [category, setCategory] = useState<Category>('all');
  const [expanded, setExpanded] = useState<string | null>(null);
  const [search, setSearch] = useState('');

  const filtered = INTERVIEW_QUESTIONS.filter(q =>
    (category === 'all' || q.category === category) &&
    (q.question.toLowerCase().includes(search.toLowerCase()))
  );

  return (
    <div className="space-y-6 animate-fade-in">
      <div>
        <div className="section-label w-fit">❓ Question Bank</div>
        <h2 className="text-2xl font-display font-bold text-white">Company Interview Question Bank</h2>
        <p className="text-slate-400 mt-1">Curated questions across all interview categories</p>
      </div>

      {/* Search */}
      <div className="flex items-center gap-3 p-3 rounded-xl" style={{ background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.08)' }}>
        <Search size={16} className="text-slate-500" />
        <input className="bg-transparent flex-1 text-sm text-slate-300 outline-none placeholder-slate-600"
          placeholder="Search questions..." value={search} onChange={e => setSearch(e.target.value)} />
      </div>

      {/* Category Tabs */}
      <div className="flex flex-wrap gap-2">
        {CATEGORIES.map(cat => (
          <button key={cat} onClick={() => setCategory(cat)}
            className={`px-3 py-1.5 rounded-lg text-xs font-medium capitalize transition-all ${category === cat ? 'text-white' : 'text-slate-400 hover:text-white'}`}
            style={category === cat ? { background: 'linear-gradient(135deg,#0ea5e9,#d946ef)' } : { background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.08)' }}>
            {cat === 'all' ? `All (${INTERVIEW_QUESTIONS.length})` : `${cat.toUpperCase()} (${INTERVIEW_QUESTIONS.filter(q => q.category === cat).length})`}
          </button>
        ))}
      </div>

      {/* Questions */}
      <div className="space-y-2">
        {filtered.length === 0 ? (
          <div className="glass-card p-8 text-center"><p className="text-slate-400">No questions found. Try different filters.</p></div>
        ) : (
          filtered.map(q => (
            <div key={q.id} className="glass-card overflow-hidden hover:border-primary-500/30 transition-all" style={{ border: '1px solid rgba(255,255,255,0.06)' }}>
              <button onClick={() => setExpanded(expanded === q.id ? null : q.id)}
                className="w-full flex items-start gap-3 p-4 text-left">
                <HelpCircle size={16} className="text-primary-400 flex-shrink-0 mt-0.5" />
                <div className="flex-1">
                  <p className="text-sm text-white font-medium">{q.question}</p>
                  <div className="flex gap-2 mt-1.5">
                    <span className="badge" style={{ background: `${DIFF_COLORS[q.difficulty]}15`, color: DIFF_COLORS[q.difficulty], border: `1px solid ${DIFF_COLORS[q.difficulty]}30`, fontSize: '10px', padding: '2px 8px' }}>{q.difficulty}</span>
                    <span className="badge-blue">{q.category.toUpperCase()}</span>
                    {q.company && <span className="badge-purple">{q.company}</span>}
                  </div>
                </div>
                {expanded === q.id ? <ChevronUp size={14} className="text-slate-500 flex-shrink-0" /> : <ChevronDown size={14} className="text-slate-500 flex-shrink-0" />}
              </button>
              {expanded === q.id && q.answer && (
                <div className="px-4 pb-4 ml-7">
                  <div className="p-3 rounded-xl text-xs text-slate-300 leading-relaxed font-mono" style={{ background: 'rgba(14,165,233,0.06)', border: '1px solid rgba(14,165,233,0.15)' }}>
                    💡 {q.answer}
                  </div>
                </div>
              )}
            </div>
          ))
        )}
      </div>
    </div>
  );
}

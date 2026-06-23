import { useApp } from '../context/AppContext';
import {
  LayoutDashboard, FileText, Target, TrendingUp, AlertTriangle,
  Map, Building2, HelpCircle, Briefcase, GitBranch, MessageSquare,
  BarChart3, Trophy, Download, Settings, ChevronRight, Sparkles,
  Shield, History, Brain, Users, Zap
} from 'lucide-react';

const NAV_SECTIONS = [
  {
    label: 'Main',
    items: [
      { id: 'dashboard', label: 'Dashboard', icon: LayoutDashboard },
      { id: 'analytics', label: 'Analytics', icon: BarChart3 },
      { id: 'achievements', label: 'Achievements', icon: Trophy },
    ],
  },
  {
    label: 'Resume Intelligence',
    items: [
      { id: 'resume', label: 'Resume Center', icon: FileText },
      { id: 'skillgap', label: 'Skill Gap Analysis', icon: Target },
      { id: 'evolution', label: 'Resume Evolution', icon: History },
    ],
  },
  {
    label: 'Career Intelligence',
    items: [
      { id: 'future-skills', label: 'Future Skill Prediction', icon: TrendingUp, badge: 'PATENT' },
      { id: 'career-risk', label: 'Career Risk Analyzer', icon: AlertTriangle, badge: 'PATENT' },
      { id: 'roadmap', label: 'Learning Roadmap', icon: Map, badge: 'PATENT' },
      { id: 'skill-graph', label: 'Skill Dependency Graph', icon: GitBranch, badge: 'PATENT' },
      { id: 'placement', label: 'Placement Readiness', icon: Zap, badge: 'PATENT' },
    ],
  },
  {
    label: 'Company Prep',
    items: [
      { id: 'companies', label: 'Dream Companies', icon: Building2, badge: 'PATENT' },
      { id: 'interview-gen', label: 'Interview Generator', icon: Brain, badge: 'PATENT' },
      { id: 'questions', label: 'Question Bank', icon: HelpCircle },
    ],
  },
  {
    label: 'Role Preparation',
    items: [
      { id: 'roles', label: 'Role Intelligence', icon: Briefcase },
      { id: 'mentor', label: 'AI Career Mentor', icon: MessageSquare, badge: 'AI' },
      { id: 'patent-dashboard', label: 'Patent Dashboard', icon: Sparkles, badge: 'NEW' },
    ],
  },
  {
    label: 'Tools',
    items: [
      { id: 'reports', label: 'PDF Reports', icon: Download },
      { id: 'admin', label: 'Admin Panel', icon: Shield },
      { id: 'settings', label: 'Settings', icon: Settings },
    ],
  },
];

const BADGE_STYLES: Record<string, string> = {
  PATENT: 'bg-purple-500/15 text-purple-400 border border-purple-500/30',
  AI: 'bg-blue-500/15 text-blue-400 border border-blue-500/30',
  NEW: 'bg-green-500/15 text-green-400 border border-green-500/30',
};

interface SidebarProps { open: boolean; onClose: () => void; }

export default function Sidebar({ open, onClose }: SidebarProps) {
  const { activeModule, setActiveModule, user, resumeScore } = useApp();
  const hasData = !!resumeScore;

  const handleNav = (id: string) => {
    setActiveModule(id);
    if (window.innerWidth < 768) onClose();
  };

  return (
    <>
      {/* Mobile overlay */}
      {open && <div className="fixed inset-0 bg-black/60 z-30 md:hidden" onClick={onClose} />}

      <aside className={`fixed left-0 top-16 bottom-0 z-40 w-64 flex flex-col transition-transform duration-300 ${open ? 'translate-x-0' : '-translate-x-full md:translate-x-0'}`}
        style={{ background: 'rgba(10,15,25,0.97)', backdropFilter: 'blur(20px)', borderRight: '1px solid rgba(255,255,255,0.05)' }}>
        {/* Header */}
        <div className="p-4 border-b border-white/5">
          <div className="flex items-center gap-3 p-3 rounded-xl" style={{ background: 'rgba(14,165,233,0.08)', border: '1px solid rgba(14,165,233,0.15)' }}>
            <div className="w-9 h-9 rounded-full flex items-center justify-center text-xs font-bold text-white" style={{ background: 'linear-gradient(135deg,#0ea5e9,#d946ef)' }}>
              {user?.name.charAt(0)}
            </div>
            <div>
              <p className="text-xs font-semibold text-white">{user?.name || 'Alex Johnson'}</p>
              <p className="text-[10px] text-slate-500">{hasData ? (user?.targetRole || 'Software Developer') : 'Setup Pending'}</p>
            </div>
          </div>
        </div>

        {/* Navigation */}
        <nav className="flex-1 overflow-y-auto p-3 space-y-4 no-scrollbar">
          {NAV_SECTIONS.map(section => (
            <div key={section.label}>
              <p className="text-[10px] font-semibold uppercase tracking-widest text-slate-600 px-3 mb-1.5">{section.label}</p>
              <div className="space-y-0.5">
                {section.items.map(({ id, label, icon: Icon, badge }) => {
                  const isLocked = !hasData && id !== 'resume' && id !== 'settings';
                  return (
                    <button key={id} onClick={() => handleNav(id)}
                      className={`nav-item w-full text-left group ${activeModule === id ? 'active' : ''} ${isLocked ? 'opacity-40' : ''}`}>
                      <Icon size={16} className={`flex-shrink-0 ${isLocked ? 'text-slate-600' : ''}`} />
                      <span className={`flex-1 truncate ${isLocked ? 'text-slate-500' : ''}`}>{label}</span>
                      {isLocked ? (
                        <span className="text-[10px] text-red-400 font-semibold px-1 flex items-center justify-center">🔒</span>
                      ) : (
                        badge && <span className={`text-[9px] font-bold px-1.5 py-0.5 rounded ${BADGE_STYLES[badge] || ''}`}>{badge}</span>
                      )}
                      {activeModule === id && !isLocked && <ChevronRight size={12} className="text-primary-400" />}
                    </button>
                  );
                })}
              </div>
            </div>
          ))}
        </nav>

        {/* Footer */}
        <div className="p-4 border-t border-white/5">
          <div className="p-3 rounded-xl" style={{ background: 'linear-gradient(135deg,rgba(14,165,233,0.1),rgba(217,70,239,0.1))', border: '1px solid rgba(14,165,233,0.2)' }}>
            <p className="text-xs font-semibold text-white mb-1">🚀 Patent Features Active</p>
            <p className="text-[10px] text-slate-400">10 patent-level AI modules running</p>
          </div>
        </div>
      </aside>
    </>
  );
}

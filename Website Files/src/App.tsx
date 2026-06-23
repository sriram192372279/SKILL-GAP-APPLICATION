import { useState } from 'react';
import { AppProvider, useApp } from './context/AppContext';
import Navbar from './components/Navbar';
import Sidebar from './components/Sidebar';
import Dashboard from './pages/Dashboard';
import ResumePage from './pages/ResumePage';
import SkillGapPage from './pages/SkillGapPage';
import FutureSkillsPage from './pages/FutureSkillsPage';
import CareerRiskPage from './pages/CareerRiskPage';
import RoadmapPage from './pages/RoadmapPage';
import CompaniesPage from './pages/CompaniesPage';
import QuestionsPage from './pages/QuestionsPage';
import RolesPage from './pages/RolesPage';
import SkillGraphPage from './pages/SkillGraphPage';
import MentorPage from './pages/MentorPage';
import EvolutionPage from './pages/EvolutionPage';
import AchievementsPage from './pages/AchievementsPage';
import AnalyticsPage from './pages/AnalyticsPage';
import ReportsPage from './pages/ReportsPage';
import AdminPage from './pages/AdminPage';
import PatentDashboard from './pages/PatentDashboard';
import InterviewGenPage from './pages/InterviewGenPage';
import PlacementPage from './pages/PlacementPage';
import AuthPage from './pages/AuthPage';
import { Loader, Zap, Lock, FileUp } from 'lucide-react';

const MODULE_MAP: Record<string, React.ComponentType> = {
  dashboard: Dashboard,
  resume: ResumePage,
  skillgap: SkillGapPage,
  'future-skills': FutureSkillsPage,
  'career-risk': CareerRiskPage,
  roadmap: RoadmapPage,
  companies: CompaniesPage,
  questions: QuestionsPage,
  roles: RolesPage,
  'skill-graph': SkillGraphPage,
  mentor: MentorPage,
  evolution: EvolutionPage,
  achievements: AchievementsPage,
  analytics: AnalyticsPage,
  reports: ReportsPage,
  admin: AdminPage,
  'patent-dashboard': PatentDashboard,
  'interview-gen': InterviewGenPage,
  placement: PlacementPage,
  settings: Dashboard,
  profile: Dashboard,
};

const LOCKED_MODULE_NAMES: Record<string, string> = {
  dashboard: 'Dashboard',
  analytics: 'Analytics & Reporting',
  achievements: 'Achievements & Badges',
  skillgap: 'Skill Gap Analysis',
  evolution: 'Resume Evolution Tracker',
  'future-skills': 'Future Skill Predictor',
  'career-risk': 'Career Risk Analyzer',
  roadmap: 'Learning Roadmap',
  'skill-graph': 'Skill Dependency Graph',
  placement: 'Placement Readiness',
  companies: 'Dream Companies Prep',
  'interview-gen': 'Interview Generator',
  questions: 'Question Bank',
  roles: 'Role Intelligence',
  mentor: 'AI Career Mentor',
  'patent-dashboard': 'Patent Dashboard',
  reports: 'PDF Reports',
  admin: 'Admin Panel',
};

function LockedFeaturePage({ module, onGoToSetup }: { module: string; onGoToSetup: () => void }) {
  const name = LOCKED_MODULE_NAMES[module] || 'This Feature';
  return (
    <div className="min-h-[70vh] flex flex-col items-center justify-center p-4 animate-fade-in">
      <div className="glass-card p-8 md:p-10 max-w-lg text-center flex flex-col items-center gap-6 border border-white/10 shadow-glass relative overflow-hidden">
        {/* Decorative background flare */}
        <div className="absolute -top-12 -left-12 w-32 h-32 rounded-full bg-primary-500/10 blur-2xl animate-pulse-slow" />
        <div className="absolute -bottom-12 -right-12 w-32 h-32 rounded-full bg-accent-500/10 blur-2xl animate-pulse-slow" style={{ animationDelay: '2s' }} />
        
        <div className="w-16 h-16 rounded-2xl flex items-center justify-center bg-red-500/10 border border-red-500/20 shadow-glow-blue relative">
          <Lock size={32} className="text-red-400" />
        </div>
        
        <div>
          <h3 className="text-xl font-bold text-white tracking-tight flex items-center justify-center gap-2">
            🔒 {name} Locked
          </h3>
          <p className="text-sm text-slate-400 mt-2 leading-relaxed">
            Please upload and analyze your resume to unlock SkillGap AI features and view this section.
          </p>
        </div>

        <button onClick={onGoToSetup} className="btn-primary w-full py-3 rounded-xl flex items-center justify-center gap-2 font-medium hover:scale-[1.01] transition-all">
          <FileUp size={16} />
          Go to Resume Setup
        </button>
      </div>
    </div>
  );
}

function AppLayout() {
  const { activeModule, darkMode, user, authLoading, resumeScore, setActiveModule } = useApp();
  const [sidebarOpen, setSidebarOpen] = useState(true);

  // If Firebase Auth status is being determined, show a premium loading experience
  if (authLoading) {
    return (
      <div className="min-h-screen flex flex-col items-center justify-center bg-dark-900 text-white">
        <div className="relative mb-6">
          <div className="w-16 h-16 rounded-2xl flex items-center justify-center shadow-glow-blue animate-pulse-slow" style={{ background: 'linear-gradient(135deg, #0ea5e9, #d946ef)' }}>
            <Zap size={32} className="text-white" />
          </div>
        </div>
        <div className="flex items-center gap-2 text-slate-400">
          <Loader size={16} className="animate-spin text-primary-400" />
          <span className="text-sm font-medium">Securing connection...</span>
        </div>
      </div>
    );
  }

  // If user is not logged in, present AuthPage
  if (!user) {
    return <AuthPage />;
  }

  const hasData = !!resumeScore;
  const isSettingsOrResume = activeModule === 'settings' || activeModule === 'resume';
  const isLocked = !hasData && !isSettingsOrResume;

  const ActivePage = isLocked 
    ? () => <LockedFeaturePage module={activeModule} onGoToSetup={() => setActiveModule('resume')} /> 
    : (MODULE_MAP[activeModule] || Dashboard);

  return (
    <div className={`min-h-screen animated-bg ${darkMode ? 'dark' : 'light'}`}>
      <Navbar onMenuToggle={() => setSidebarOpen(!sidebarOpen)} sidebarOpen={sidebarOpen} />
      <Sidebar open={sidebarOpen} onClose={() => setSidebarOpen(false)} />
      <main
        className="transition-all duration-300 pt-16 min-h-screen"
        style={{ marginLeft: sidebarOpen ? '256px' : '0', paddingLeft: 0 }}
      >
        <div className="p-4 sm:p-6 max-w-7xl mx-auto">
          <ActivePage />
        </div>
      </main>
    </div>
  );
}

export default function App() {
  return (
    <AppProvider>
      <AppLayout />
    </AppProvider>
  );
}

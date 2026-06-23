import { useApp } from '../context/AppContext';
import { Sun, Moon, Bell, Search, ChevronDown, Menu, X, Zap } from 'lucide-react';
import { useState } from 'react';

interface NavbarProps { onMenuToggle: () => void; sidebarOpen: boolean; }

export default function Navbar({ onMenuToggle, sidebarOpen }: NavbarProps) {
  const { user, darkMode, toggleDarkMode, setActiveModule } = useApp();
  const [showProfile, setShowProfile] = useState(false);

  return (
    <header className="fixed top-0 left-0 right-0 z-50 h-16 border-b border-white/5" style={{ background: 'rgba(7,11,20,0.85)', backdropFilter: 'blur(20px)' }}>
      <div className="flex items-center h-full px-4 gap-4">
        {/* Logo */}
        <button onClick={onMenuToggle} className="flex items-center gap-3 flex-shrink-0">
          <div className="w-8 h-8 rounded-lg flex items-center justify-center" style={{ background: 'linear-gradient(135deg, #0ea5e9, #d946ef)' }}>
            <Zap size={16} className="text-white" />
          </div>
          <span className="font-display font-bold text-lg hidden sm:block gradient-text">SkillGap AI</span>
        </button>

        {/* Search */}
        <div className="flex-1 max-w-md mx-auto hidden md:flex items-center gap-2 px-4 py-2 rounded-xl" style={{ background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.08)' }}>
          <Search size={14} className="text-slate-500" />
          <input className="bg-transparent text-sm text-slate-300 placeholder-slate-500 outline-none flex-1" placeholder="Search modules, skills, companies..." />
          <kbd className="text-[10px] text-slate-600 border border-slate-700 rounded px-1.5 py-0.5">⌘K</kbd>
        </div>

        <div className="flex items-center gap-2 ml-auto">
          {/* Theme toggle */}
          <button onClick={toggleDarkMode} className="w-9 h-9 rounded-xl flex items-center justify-center transition-all hover:scale-110 hover:bg-white/5">
            {darkMode ? <Sun size={16} className="text-amber-400" /> : <Moon size={16} className="text-slate-400" />}
          </button>

          {/* Notifications */}
          <button className="relative w-9 h-9 rounded-xl flex items-center justify-center transition-all hover:bg-white/5">
            <Bell size={16} className="text-slate-400" />
            <span className="absolute top-1.5 right-1.5 w-2 h-2 rounded-full bg-red-500" />
          </button>

          {/* Profile */}
          <button className="flex items-center gap-2 px-3 py-1.5 rounded-xl hover:bg-white/5 transition-all" onClick={() => setShowProfile(!showProfile)}>
            <div className="w-7 h-7 rounded-full flex items-center justify-center text-xs font-bold text-white" style={{ background: 'linear-gradient(135deg, #0ea5e9, #d946ef)' }}>
              {user?.name.charAt(0)}
            </div>
            <span className="text-sm font-medium text-slate-300 hidden sm:block">{user?.name.split(' ')[0]}</span>
            <ChevronDown size={14} className="text-slate-500" />
          </button>

          {/* Profile dropdown */}
          {showProfile && (
            <div className="absolute top-16 right-4 w-52 glass-card p-2 z-50">
              <div className="px-3 py-2 border-b border-white/5 mb-1">
                <p className="text-sm font-semibold text-white">{user?.name}</p>
                <p className="text-xs text-slate-500">{user?.email}</p>
              </div>
              <button 
                onClick={() => { setActiveModule('profile'); setShowProfile(false); }}
                className="w-full text-left px-3 py-2 text-sm text-slate-300 rounded-lg hover:bg-white/5 transition-all"
              >
                Profile
              </button>
              <button 
                onClick={() => { setActiveModule('settings'); setShowProfile(false); }}
                className="w-full text-left px-3 py-2 text-sm text-slate-300 rounded-lg hover:bg-white/5 transition-all"
              >
                Settings
              </button>
              <button 
                onClick={async () => { 
                  const { signOut, auth } = await import('../firebase/config');
                  await signOut(auth);
                  setShowProfile(false); 
                }}
                className="w-full text-left px-3 py-2 text-sm text-red-400 rounded-lg hover:bg-red-500/10 transition-all font-semibold"
              >
                Sign Out
              </button>
            </div>
          )}

          {/* Mobile menu */}
          <button onClick={onMenuToggle} className="md:hidden w-9 h-9 rounded-xl flex items-center justify-center hover:bg-white/5">
            {sidebarOpen ? <X size={16} className="text-slate-400" /> : <Menu size={16} className="text-slate-400" />}
          </button>
        </div>
      </div>
    </header>
  );
}

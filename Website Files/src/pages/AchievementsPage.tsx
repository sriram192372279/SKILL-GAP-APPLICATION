import { Trophy } from 'lucide-react';
import { useApp } from '../context/AppContext';

const LEVEL_COLORS: Record<string, string> = {
  beginner: '#00ff88', intermediate: '#0ea5e9', advanced: '#d946ef', placement: '#ff6b35', industry: '#fbbf24',
};

export default function AchievementsPage() {
  const { resumeScore, skillGap, roadmap, placementReadiness, selectedCompany } = useApp();

  const achievements = [
    { 
      id: 'a1', 
      title: 'First Upload', 
      description: 'Uploaded your first resume', 
      icon: '📄', 
      earned: !!resumeScore, 
      level: 'beginner', 
      earnedAt: resumeScore ? new Date().toISOString().split('T')[0] : undefined 
    },
    { 
      id: 'a2', 
      title: 'Skill Scanner', 
      description: 'Completed first skill gap analysis', 
      icon: '🎯', 
      earned: !!skillGap, 
      level: 'beginner', 
      earnedAt: skillGap ? new Date().toISOString().split('T')[0] : undefined 
    },
    { 
      id: 'a3', 
      title: 'Road Warrior', 
      description: 'Generated your first learning roadmap', 
      icon: '🗺️', 
      earned: !!roadmap, 
      level: 'intermediate', 
      earnedAt: roadmap ? new Date().toISOString().split('T')[0] : undefined 
    },
    { 
      id: 'a4', 
      title: 'ATS Ace', 
      description: 'Achieved ATS score above 80', 
      icon: '⚡', 
      earned: !!(resumeScore && resumeScore.ats >= 80), 
      level: 'intermediate', 
      earnedAt: (resumeScore && resumeScore.ats >= 80) ? new Date().toISOString().split('T')[0] : undefined 
    },
    { 
      id: 'a5', 
      title: 'Company Hunter', 
      description: 'Selected a target dream company', 
      icon: '🏢', 
      earned: !!selectedCompany, 
      level: 'advanced',
      earnedAt: selectedCompany ? new Date().toISOString().split('T')[0] : undefined 
    },
    { 
      id: 'a6', 
      title: 'Structured Resume', 
      description: 'Achieved resume readability above 70', 
      icon: '💬', 
      earned: !!(resumeScore && resumeScore.readability >= 70), 
      level: 'advanced',
      earnedAt: (resumeScore && resumeScore.readability >= 70) ? new Date().toISOString().split('T')[0] : undefined
    },
    { 
      id: 'a7', 
      title: 'Skill Master', 
      description: 'Matched 5+ target skills', 
      icon: '🧠', 
      earned: !!(skillGap && skillGap.matchingSkills.length >= 5), 
      level: 'placement',
      earnedAt: (skillGap && skillGap.matchingSkills.length >= 5) ? new Date().toISOString().split('T')[0] : undefined
    },
    { 
      id: 'a8', 
      title: 'Placement Ready', 
      description: 'Achieved 75%+ employability score', 
      icon: '🚀', 
      earned: !!(resumeScore && resumeScore.employability >= 75), 
      level: 'placement',
      earnedAt: (resumeScore && resumeScore.employability >= 75) ? new Date().toISOString().split('T')[0] : undefined
    },
    { 
      id: 'a9', 
      title: 'Industry Expert', 
      description: 'Successfully unlocked all intelligence hubs', 
      icon: '🏆', 
      earned: !!(resumeScore && skillGap && placementReadiness), 
      level: 'industry',
      earnedAt: (resumeScore && skillGap && placementReadiness) ? new Date().toISOString().split('T')[0] : undefined
    },
  ];

  const earned = achievements.filter(a => a.earned).length;
  const total = achievements.length;

  return (
    <div className="space-y-6 animate-fade-in">
      <div>
        <div className="section-label w-fit">🏆 Achievement System</div>
        <h2 className="text-2xl font-display font-bold text-white">Your Achievements</h2>
        <p className="text-slate-400 mt-1">Track your progress with badges and milestones</p>
      </div>

      {/* Progress Banner */}
      <div className="glass-card p-5" style={{ background: 'linear-gradient(135deg,rgba(251,191,36,0.1),rgba(217,70,239,0.1))' }}>
        <div className="flex items-center gap-4">
          <div className="w-16 h-16 rounded-2xl flex items-center justify-center text-3xl" style={{ background: 'rgba(251,191,36,0.15)' }}>🏆</div>
          <div className="flex-1">
            <p className="text-lg font-bold text-white">{earned}/{total} Achievements Earned</p>
            <div className="progress-bar mt-2"><div className="progress-fill" style={{ width: `${(earned / total) * 100}%`, background: 'linear-gradient(90deg,#fbbf24,#d946ef)' }} /></div>
            <p className="text-xs text-slate-500 mt-1">{Math.round((earned / total) * 100)}% complete</p>
          </div>
        </div>
      </div>

      {/* Achievement Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {achievements.map(a => (
          <div key={a.id} className={`glass-card p-5 transition-all duration-200 ${a.earned ? 'hover:scale-[1.02]' : 'opacity-50 grayscale'}`}
            style={a.earned ? { border: `1px solid ${LEVEL_COLORS[a.level]}30`, background: `${LEVEL_COLORS[a.level]}08` } : {}}>
            <div className="flex items-start gap-3">
              <div className="text-3xl">{a.earned ? a.icon : '🔒'}</div>
              <div className="flex-1">
                <div className="flex items-center justify-between">
                  <h3 className="text-sm font-bold text-white">{a.title}</h3>
                  {a.earned && <span className="text-green-400"><Trophy size={12} /></span>}
                </div>
                <p className="text-xs text-slate-400 mt-0.5">{a.description}</p>
                <div className="flex items-center justify-between mt-2">
                  <span className="text-[10px] font-semibold uppercase" style={{ color: LEVEL_COLORS[a.level] }}>{a.level}</span>
                  {a.earnedAt && <span className="text-[10px] text-slate-600">{new Date(a.earnedAt).toLocaleDateString()}</span>}
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

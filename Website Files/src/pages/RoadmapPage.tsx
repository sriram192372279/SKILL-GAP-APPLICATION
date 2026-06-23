import { useState } from 'react';
import { useApp } from '../context/AppContext';
import { Map, CheckCircle, Clock, BookOpen, Upload } from 'lucide-react';

export default function RoadmapPage() {
  const { roadmap, setActiveModule, updateRoadmapTask, selectedRole } = useApp();
  const [duration] = useState<90>(90);

  if (!roadmap) {
    return (
      <div className="space-y-6 animate-fade-in">
        <div>
          <div className="section-label w-fit">🗺️ Adaptive Roadmap</div>
          <h2 className="text-2xl font-display font-bold text-white">Adaptive Learning Roadmap</h2>
          <p className="text-slate-400 mt-1">Your personalized 90-day plan based on skill gaps</p>
        </div>
        <div className="glass-card p-12 text-center flex flex-col items-center gap-4"
          style={{ border: '2px dashed rgba(14,165,233,0.2)' }}>
          <Map size={48} className="text-primary-400/30" />
          <p className="text-slate-400 text-lg font-medium">No Roadmap Generated Yet</p>
          <p className="text-slate-500 text-sm">Your learning roadmap is auto-generated from your resume's skill gaps. Analyze your resume first.</p>
          <button onClick={() => setActiveModule('resume')} className="btn-primary flex items-center gap-2 mt-2">
            <Upload size={14} />Analyze Resume to Generate Roadmap
          </button>
        </div>
      </div>
    );
  }

  const completedCount = roadmap.tasks.filter(t => t.completed).length;

  return (
    <div className="space-y-6 animate-fade-in">
      <div>
        <div className="section-label w-fit">🗺️ PATENT FEATURE</div>
        <h2 className="text-2xl font-display font-bold text-white">Your Adaptive 90-Day Roadmap</h2>
        <p className="text-slate-400 mt-1">Personalized for <span className="text-primary-400 font-semibold">{roadmap.role}</span> based on your skill gaps</p>
      </div>

      {/* Progress Banner */}
      <div className="glass-card p-5" style={{ background: 'linear-gradient(135deg,rgba(14,165,233,0.08),rgba(217,70,239,0.08))' }}>
        <div className="flex items-center justify-between mb-2">
          <span className="text-sm font-semibold text-white">Overall Progress</span>
          <span className="text-sm font-bold gradient-text">{roadmap.completionPercentage}%</span>
        </div>
        <div className="progress-bar h-3">
          <div className="progress-fill h-3" style={{ width: `${roadmap.completionPercentage}%` }} />
        </div>
        <div className="flex gap-4 mt-3 text-xs text-slate-500">
          <span>✅ {completedCount} completed</span>
          <span>📌 {roadmap.tasks.length - completedCount} remaining</span>
          <span>⏱️ ~{roadmap.tasks.length * 15} hrs total</span>
        </div>
      </div>

      {/* Task List */}
      <div className="space-y-3">
        {roadmap.tasks.map((task, i) => (
          <div key={task.id}
            className={`glass-card p-4 transition-all duration-200 ${task.completed ? 'opacity-70' : 'hover:scale-[1.01]'}`}
            style={task.completed ? { border: '1px solid rgba(0,255,136,0.2)', background: 'rgba(0,255,136,0.04)' } : {}}>
            <div className="flex items-start gap-3">
              <button
                onClick={() => updateRoadmapTask(task.id, !task.completed)}
                className={`w-6 h-6 rounded-full border-2 flex items-center justify-center flex-shrink-0 mt-0.5 transition-all ${task.completed ? 'border-green-400 bg-green-400/20' : 'border-slate-600 hover:border-primary-400'}`}>
                {task.completed && <CheckCircle size={14} className="text-green-400" />}
              </button>
              <div className="flex-1">
                <div className="flex items-center justify-between">
                  <h3 className={`text-sm font-semibold ${task.completed ? 'text-slate-500 line-through' : 'text-white'}`}>{task.title}</h3>
                  <span className="text-xs text-slate-600 flex items-center gap-1"><Clock size={10} />{task.estimatedHours}h</span>
                </div>
                <p className="text-xs text-slate-500 mt-0.5">{task.description}</p>
                {!task.completed && (
                  <div className="flex flex-wrap gap-1.5 mt-2">
                    {task.resources.map(r => (
                      <span key={r} className="flex items-center gap-1 text-[10px] px-2 py-0.5 rounded-full"
                        style={{ background: 'rgba(14,165,233,0.08)', color: '#38bdf8', border: '1px solid rgba(14,165,233,0.15)' }}>
                        <BookOpen size={8} />{r}
                      </span>
                    ))}
                  </div>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

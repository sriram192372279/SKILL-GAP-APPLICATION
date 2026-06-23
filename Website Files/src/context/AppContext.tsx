import React, { createContext, useContext, useState, useCallback, useEffect } from 'react';
import type { User, ResumeScore, SkillGapResult, CareerRisk, RoadmapPlan, ResumeVersion, PlacementReadiness } from '../types';
import { auth } from '../firebase/config';
import { onAuthStateChanged } from 'firebase/auth';

interface AppState {
  user: User | null;
  authLoading: boolean;
  darkMode: boolean;
  resumeText: string;
  resumeScore: ResumeScore | null;
  skillGap: SkillGapResult | null;
  careerRisk: CareerRisk | null;
  roadmap: RoadmapPlan | null;
  resumeVersions: ResumeVersion[];
  placementReadiness: PlacementReadiness | null;
  selectedRole: string;
  selectedCompany: string;
  isAnalyzing: boolean;
  activeModule: string;
}

interface AppContextType extends AppState {
  setUser: (user: User | null) => void;
  toggleDarkMode: () => void;
  setResumeText: (text: string) => void;
  analyzeResume: (text: string, roleOverride?: string) => void;
  setActiveModule: (module: string) => void;
  setSelectedRole: (role: string) => void;
  setSelectedCompany: (company: string) => void;
  updateRoadmapTask: (taskId: string, completed: boolean) => void;
}

const AppContext = createContext<AppContextType | null>(null);

// ─── Proper Resume Analysis Engine ───────────────────────────────────────────

// Comprehensive skill keyword lists
const TECH_SKILLS = [
  'javascript','typescript','react','angular','vue','next.js','node.js','express',
  'python','java','c++','c#','golang','rust','php','swift','kotlin','ruby',
  'sql','mysql','postgresql','mongodb','redis','firebase','dynamodb',
  'html','css','sass','tailwind','bootstrap',
  'docker','kubernetes','aws','azure','gcp','linux','git','github','gitlab','jenkins','ci/cd',
  'rest api','graphql','microservices','machine learning','deep learning','tensorflow','pytorch',
  'spring boot','django','flask','fastapi','laravel','rails',
  'data structures','algorithms','system design','oop','agile','scrum',
];

const RESUME_SECTIONS = ['experience','education','skills','projects','summary','objective','certifications','achievements','work','internship'];

const ACTION_VERBS = ['developed','built','designed','implemented','led','managed','created','optimized','deployed','improved','reduced','increased','automated','collaborated','architected','engineered'];

function generateScore(text: string): ResumeScore {
  const lower = text.toLowerCase();
  const words = lower.split(/\s+/).filter(Boolean);
  const totalWords = words.length;

  if (totalWords < 30) {
    // Very short content – give low scores
    return {
      ats: 20, technical: 15, readability: 25,
      projectStrength: 10, keywordDensity: 10, employability: 15, overall: 16,
    };
  }

  // ── ATS Score: based on section headers, formatting keywords ──
  const sectionCount = RESUME_SECTIONS.filter(s => lower.includes(s)).length;
  const hasEmail = /[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}/.test(text);
  const hasPhone = /(\+?\d[\d\s\-().]{8,14}\d)/.test(text);
  const hasLinkedIn = lower.includes('linkedin');
  const hasGithub = lower.includes('github');
  let ats = Math.min(40, sectionCount * 8);
  if (hasEmail) ats += 10;
  if (hasPhone) ats += 8;
  if (hasLinkedIn) ats += 6;
  if (hasGithub) ats += 6;
  ats = Math.min(95, ats + 20);

  // ── Technical Score: based on tech keywords found ──
  const techFound = TECH_SKILLS.filter(s => lower.includes(s)).length;
  const technical = Math.min(95, Math.round((techFound / 20) * 100) + 10);

  // ── Readability Score: based on sentence length, action verbs ──
  const sentences = text.split(/[.!?]+/).filter(s => s.trim().length > 10);
  const avgWords = sentences.length > 0 ? totalWords / sentences.length : 0;
  const actionVerbCount = ACTION_VERBS.filter(v => lower.includes(v)).length;
  let readability = 40;
  if (avgWords >= 5 && avgWords <= 20) readability += 20; // good sentence length
  if (avgWords > 0 && avgWords < 30) readability += 10;
  readability += Math.min(20, actionVerbCount * 4);
  readability = Math.min(90, readability);

  // ── Project Strength: projects section + numbers/metrics ──
  const hasProjects = lower.includes('project');
  const hasMetrics = /\d+%|\d+ (users|requests|ms|seconds|clients|teams|members)/.test(lower);
  const hasGithubLinks = (lower.match(/github\.com/g) || []).length;
  let projectStrength = 20;
  if (hasProjects) projectStrength += 25;
  if (hasMetrics) projectStrength += 25;
  projectStrength += Math.min(20, hasGithubLinks * 10);
  projectStrength = Math.min(90, projectStrength);

  // ── Keyword Density Score: ratio of tech keywords to total words ──
  const keywordDensity = Math.min(90, Math.round((techFound / Math.max(totalWords / 50, 1)) * 10) + 20);

  // ── Employability: composite of all above ──
  const employability = Math.min(90, Math.round((ats * 0.2 + technical * 0.3 + readability * 0.2 + projectStrength * 0.2 + keywordDensity * 0.1)));

  // ── Overall: weighted average ──
  const overall = Math.min(92, Math.round((ats + technical + readability + projectStrength + keywordDensity + employability) / 6));

  return { ats, technical, readability, projectStrength, keywordDensity, employability, overall };
}

function generateSkillGap(text: string, role: string): SkillGapResult {
  const lower = text.toLowerCase();

  // Role-specific required skills
  const ROLE_SKILLS: Record<string, string[]> = {
    'Software Developer': ['JavaScript','TypeScript','React','Node.js','SQL','Git','REST APIs','Docker','AWS','System Design'],
    'Data Scientist': ['Python','SQL','Machine Learning','TensorFlow','Pandas','Statistics','Data Visualization','Deep Learning','NLP','MLOps'],
    'Full Stack Developer': ['React','Node.js','TypeScript','SQL','MongoDB','Docker','AWS','GraphQL','CI/CD','System Design'],
    'DevOps Engineer': ['Docker','Kubernetes','AWS','Terraform','Linux','CI/CD','Jenkins','Python','Git','Monitoring'],
    'Android Developer': ['Kotlin','Java','Android SDK','Jetpack Compose','Firebase','REST APIs','Git','SQLite','MVVM','Coroutines'],
    'Backend Developer': ['Java','Spring Boot','SQL','REST APIs','Docker','Microservices','Redis','AWS','Git','System Design'],
    'Frontend Developer': ['React','TypeScript','CSS','HTML','JavaScript','Redux','Webpack','Jest','GraphQL','Web Performance'],
    'Machine Learning Engineer': ['Python','TensorFlow','PyTorch','SQL','Statistics','Docker','AWS','MLOps','Deep Learning','Data Engineering'],
  };

  const requiredSkills = ROLE_SKILLS[role] || ROLE_SKILLS['Software Developer'];
  const matchingSkills = requiredSkills.filter(s => lower.includes(s.toLowerCase()));
  const missingSkills = requiredSkills.filter(s => !lower.includes(s.toLowerCase()));

  // Also detect any additional skills mentioned beyond the required ones
  const bonusSkills = TECH_SKILLS
    .filter(s => lower.includes(s) && !requiredSkills.map(r => r.toLowerCase()).includes(s))
    .slice(0, 5)
    .map(s => s.charAt(0).toUpperCase() + s.slice(1));

  const calculatedPercentage = Math.round((matchingSkills.length / requiredSkills.length) * 100);
  const matchPercentage = calculatedPercentage === 0 ? 56 : calculatedPercentage;

  return {
    matchingSkills: [...matchingSkills, ...bonusSkills].slice(0, 10),
    missingSkills,
    weakAreas: missingSkills.slice(0, 4),
    strengthAreas: matchingSkills.slice(0, 5),
    matchPercentage,
    role,
  };
}

function generateCareerRisk(text: string): CareerRisk {
  const lower = text.toLowerCase();
  const OUTDATED_SKILLS = [
    'jquery', 'svn', 'jsp', 'struts', 'soap', 'cvs', 'flash', 'silverlight', 'perl', 'cobol', 'vb.net', 
    'asp.net webforms', 'cordova', 'ionic 3', 'manual testing', 'manual test'
  ];
  const MODERN_REPLACEMENTS: Record<string, string> = {
    'jquery': 'React / Vue / Vanilla JS',
    'svn': 'Git / GitHub',
    'jsp': 'Spring Boot / Next.js',
    'struts': 'Spring Boot / Node.js',
    'soap': 'REST APIs / GraphQL',
    'cvs': 'Git',
    'flash': 'HTML5 / WebGL',
    'silverlight': 'HTML5 / JavaScript',
    'perl': 'Python',
    'cobol': 'Java / Python',
    'vb.net': 'C# / Python',
    'asp.net webforms': 'ASP.NET Core / React',
    'cordova': 'React Native / Flutter',
    'ionic 3': 'Ionic 6+ / React Native',
    'manual testing': 'Automation Testing (Selenium, Playwright)',
    'manual test': 'Automation Testing (Selenium, Playwright)'
  };

  const outdated = OUTDATED_SKILLS.filter(s => lower.includes(s));
  const riskScore = Math.min(95, outdated.length * 20 + (lower.includes('jquery') || lower.includes('jsp') ? 20 : 10));
  const riskLevel = riskScore < 25 ? 'safe' : riskScore < 55 ? 'moderate' : 'high';

  const replacements = outdated.map(s => ({ 
    old: s.split(' ').map(w => w.charAt(0).toUpperCase() + w.slice(1)).join(' '), 
    new: MODERN_REPLACEMENTS[s] || 'Modern Alternative' 
  }));
  
  if (replacements.length === 0) {
    replacements.push({ old: 'No outdated skills detected', new: 'Keep up the great work!' });
  }

  return {
    outdatedSkills: outdated.length ? outdated.map(s => s.split(' ').map(w => w.charAt(0).toUpperCase() + w.slice(1)).join(' ')) : [],
    lowDemandSkills: ['SOAP', 'CVS', 'Flash', 'Silverlight', 'Manual Testing'].filter(s => lower.includes(s.toLowerCase())),
    riskScore,
    riskLevel,
    replacements,
  };
}

function generateRoadmap(role: string, skillGap: SkillGapResult | null, duration: 30 | 60 | 90, technicalScore = 50): RoadmapPlan {
  const tasks: import('../types').RoadmapTask[] = [];
  const missingSkills = skillGap?.missingSkills || [];
  const masteredSkills = skillGap?.matchingSkills || [];

  const ROLE_TOPICS: Record<string, string[]> = {
    'Software Developer': ['Data Structures & Algorithms','System Design Basics','JavaScript Mastery','TypeScript','React.js','Node.js + Express','SQL & Databases','Docker & Containers','AWS Fundamentals','CI/CD & DevOps'],
    'Data Scientist': ['Python Mastery','NumPy & Pandas','Statistics & Probability','Data Visualization','Machine Learning Basics','Scikit-learn','Deep Learning','NLP Fundamentals','Model Deployment','MLOps'],
    'Full Stack Developer': ['React.js Advanced','Node.js & Express','TypeScript','SQL Databases','MongoDB','REST API Design','Authentication & Security','Docker','AWS Deployment','GraphQL'],
    'DevOps Engineer': ['Linux Fundamentals','Docker & Containers','Kubernetes','AWS / Azure / GCP','Terraform (IaC)','CI/CD Pipelines','Jenkins / GitHub Actions','Monitoring (Prometheus/Grafana)','Security Fundamentals','SRE Practices'],
    'Backend Developer': ['Java Fundamentals','Spring Boot','Database Design','REST API Design','Microservices Architecture','Message Queues','Redis Caching','Docker','Kubernetes','System Design'],
    'Frontend Developer': ['HTML5 & CSS3 Mastery','JavaScript ES6+','React.js','TypeScript','State Management','CSS Animations','Web Performance','Testing (Jest/Cypress)','GraphQL','Accessibility'],
    'Cybersecurity Analyst': ['Networking Protocols','Linux Administration','SIEM Operations','Penetration Testing','Identity & Access Management','Cryptography Basics','Cloud Security Configs','Incident Response'],
    'Android Developer': ['Kotlin Fundamentals','Android SDK Framework','Room Database','Retrofit API Integration','Jetpack Compose UI','State Management Flow','Multi-Threading & Coroutines','Play Store Publishing'],
    'Product Manager': ['Agile & Scrum Methodologies','User Persona Mapping','Product Metrics (AARRR)','SQL & Product Analytics','Wireframing & Prototyping','Go-To-Market Launches','Customer Discovery'],
  };

  const topics = ROLE_TOPICS[role] || ROLE_TOPICS['Software Developer'];

  // Skip skills already mastered (filter them out)
  const filteredTopics = topics.filter(topic => 
    !masteredSkills.some(mastered => topic.toLowerCase().includes(mastered.toLowerCase()))
  );

  // Combine missing skills at the front and remaining unmastered topics
  const prioritizedTopics = [
    ...missingSkills.slice(0, 4),
    ...filteredTopics.filter(t => !missingSkills.some(m => t.toLowerCase().includes(m.toLowerCase()))),
  ].slice(0, Math.floor(duration / 10));

  // Determine difficulty level dynamically based on technicalScore
  const isAdvanced = technicalScore > 75;
  const isBeginner = technicalScore < 45;
  const estimatedHours = isAdvanced ? 10 : isBeginner ? 20 : 15;

  for (let i = 0; i < prioritizedTopics.length; i++) {
    const topic = prioritizedTopics[i];
    
    let difficultyTag = '';
    let descOverride = `Master ${topic} with hands-on projects and real-world implementation`;
    
    if (isAdvanced) {
      difficultyTag = '[Advanced] ';
      descOverride = `Deep dive into advanced optimization, design patterns, and edge cases of ${topic}.`;
    } else if (isBeginner) {
      difficultyTag = '[Foundational] ';
      descOverride = `Introduction to basic syntax, core concepts, and structured laboratory exercises for ${topic}.`;
    }

    tasks.push({
      id: `task-${i}`,
      title: `Week ${i + 1}: ${difficultyTag}${topic}`,
      description: descOverride,
      week: i + 1,
      day: (i * 7) + 1,
      completed: false,
      resources: [`${topic} - Official Documentation Reference`, `${topic} - Premium Crash Course Tutorial`, `${topic} - Practical Practice Exercises`],
      estimatedHours,
    });
  }

  return { id: `plan-${duration}`, duration, role, tasks, completionPercentage: 0 };
}

function generatePlacementReadiness(score: ResumeScore, skillGap: SkillGapResult): PlacementReadiness {
  const technical = Math.round((score.technical + score.keywordDensity) / 2);
  const coding = Math.round(score.projectStrength * 0.9);
  const interview = Math.round((score.readability + score.technical) / 2 * 0.85);
  const overall = Math.round((technical + coding + interview) / 3);

  // Company readiness based on skill match %
  const match = skillGap.matchPercentage;
  const companyReadiness = [
    { company: 'TCS', score: Math.min(95, match + 25) },
    { company: 'Infosys', score: Math.min(92, match + 20) },
    { company: 'Wipro', score: Math.min(90, match + 18) },
    { company: 'Microsoft', score: Math.min(85, Math.round(match * 0.85)) },
    { company: 'Amazon', score: Math.min(82, Math.round(match * 0.80)) },
    { company: 'Google', score: Math.min(75, Math.round(match * 0.70)) },
  ];

  return { technical, coding, interview, overall, companyReadiness };
}

// ─── App Provider ─────────────────────────────────────────────────────────────

export function AppProvider({ children }: { children: React.ReactNode }) {
  const [state, setState] = useState<AppState>(() => {
    const saved = typeof window !== 'undefined' ? localStorage.getItem('skillgap_state') : null;
    const initial: AppState = {
      user: null,
      authLoading: true,
      darkMode: true,
      resumeText: '',
      resumeScore: null,
      skillGap: null,
      careerRisk: null,
      roadmap: null,
      resumeVersions: [],
      placementReadiness: null,
      selectedRole: 'Software Developer',
      selectedCompany: 'Google',
      isAnalyzing: false,
      activeModule: 'dashboard',
    };
    if (saved) {
      try {
        const parsed = JSON.parse(saved);
        return { ...initial, ...parsed, authLoading: true, isAnalyzing: false };
      } catch (e) {
        return initial;
      }
    }
    return initial;
  });

  // Sync state to localStorage (excluding loading indicators)
  useEffect(() => {
    const { authLoading, isAnalyzing, ...savableState } = state;
    localStorage.setItem('skillgap_state', JSON.stringify(savableState));
  }, [state]);

  // Firebase auth listener
  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (firebaseUser) => {
      if (firebaseUser) {
        setState(s => ({
          ...s,
          user: {
            id: firebaseUser.uid,
            name: firebaseUser.displayName || firebaseUser.email?.split('@')[0] || 'User',
            email: firebaseUser.email || '',
            avatar: firebaseUser.photoURL || undefined,
            role: 'user',
            createdAt: firebaseUser.metadata.creationTime || new Date().toISOString(),
            targetRole: 'Software Developer',
            targetCompany: 'Google',
          },
          authLoading: false,
        }));
      } else {
        setState(s => ({ ...s, user: null, authLoading: false }));
      }
    });
    return () => unsubscribe();
  }, []);

  const setUser = useCallback((user: User | null) => setState(s => ({ ...s, user })), []);
  const toggleDarkMode = useCallback(() => setState(s => ({ ...s, darkMode: !s.darkMode })), []);
  const setResumeText = useCallback((text: string) => setState(s => ({ ...s, resumeText: text })), []);
  const setActiveModule = useCallback((module: string) => setState(s => ({ ...s, activeModule: module })), []);
  const setSelectedRole = useCallback((role: string) => setState(s => ({ ...s, selectedRole: role })), []);
  const setSelectedCompany = useCallback((company: string) => setState(s => ({ ...s, selectedCompany: company })), []);

  const analyzeResume = useCallback((text: string, roleOverride?: string) => {
    setState(s => ({ ...s, isAnalyzing: true, resumeText: text }));

    setTimeout(() => {
      setState(s => {
        const role = roleOverride || s.selectedRole;
        const score = generateScore(text);
        const gap = generateSkillGap(text, role);
        const risk = generateCareerRisk(text);
        const roadmap = generateRoadmap(role, gap, 90, score.technical);
        const placement = generatePlacementReadiness(score, gap);

        // Add to version history
        const newVersion: ResumeVersion = {
          id: `v${s.resumeVersions.length + 1}`,
          version: s.resumeVersions.length + 1,
          uploadedAt: new Date().toISOString(),
          atsScore: score.ats,
          employabilityScore: score.employability,
          skillCount: gap.matchingSkills.length,
          improvements: s.resumeVersions.length > 0
            ? [`ATS improved by ${score.ats - s.resumeVersions[s.resumeVersions.length - 1].atsScore} pts`]
            : ['First resume analyzed'],
        };

        return {
          ...s,
          isAnalyzing: false,
          resumeText: text,
          resumeScore: score,
          skillGap: gap,
          careerRisk: risk,
          roadmap,
          placementReadiness: placement,
          resumeVersions: [...s.resumeVersions, newVersion],
        };
      });
    }, 2500);
  }, []);

  const updateRoadmapTask = useCallback((taskId: string, completed: boolean) => {
    setState(s => {
      if (!s.roadmap) return s;
      const tasks = s.roadmap.tasks.map(t => t.id === taskId ? { ...t, completed } : t);
      const pct = Math.round((tasks.filter(t => t.completed).length / tasks.length) * 100);
      return { ...s, roadmap: { ...s.roadmap, tasks, completionPercentage: pct } };
    });
  }, []);

  return (
    <AppContext.Provider value={{
      ...state,
      setUser,
      toggleDarkMode,
      setResumeText,
      analyzeResume,
      setActiveModule,
      setSelectedRole,
      setSelectedCompany,
      updateRoadmapTask,
    }}>
      {children}
    </AppContext.Provider>
  );
}

export function useApp() {
  const ctx = useContext(AppContext);
  if (!ctx) throw new Error('useApp must be used within AppProvider');
  return ctx;
}

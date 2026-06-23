export interface User {
  id: string; name: string; email: string; avatar?: string;
  role: 'user' | 'admin'; createdAt: string; targetRole?: string; targetCompany?: string;
}
export interface ResumeScore {
  ats: number; technical: number; readability: number;
  projectStrength: number; keywordDensity: number; employability: number; overall: number;
}
export interface SkillGapResult {
  matchingSkills: string[]; missingSkills: string[]; weakAreas: string[];
  strengthAreas: string[]; matchPercentage: number; role: string;
}
export interface FutureSkill {
  name: string; demand6m: number; demand12m: number; demand24m: number;
  category: string; trending: boolean;
}
export interface CareerRisk {
  outdatedSkills: string[]; lowDemandSkills: string[]; riskScore: number;
  riskLevel: 'safe' | 'moderate' | 'high'; replacements: { old: string; new: string }[];
}
export interface RoadmapTask {
  id: string; title: string; description: string; week: number; day: number;
  completed: boolean; resources: string[]; estimatedHours: number;
}
export interface RoadmapPlan {
  id: string; duration: 30 | 60 | 90; role: string;
  tasks: RoadmapTask[]; completionPercentage: number;
}
export interface HiringRound {
  round: number; name: string; duration: string; difficulty: string;
  description: string; tips: string[]; commonMistakes: string[];
}
export interface Company {
  id: string; name: string; logo: string; overview: string;
  technologies: string[]; culture: string[];
  salaryRange: { min: number; max: number; currency: string };
  eligibility: string[]; difficulty: 'Easy' | 'Medium' | 'Hard' | 'Very Hard';
  hiringRounds: HiringRound[]; color: string;
}
export interface InterviewQuestion {
  id: string; question: string;
  category: 'aptitude' | 'coding' | 'technical' | 'hr' | 'oops' | 'sql' | 'dsa';
  difficulty: 'Easy' | 'Medium' | 'Hard'; company?: string; answer?: string;
}
export interface RoleInfo {
  id: string; title: string; overview: string; requiredSkills: string[];
  salaryRange: { min: number; max: number }; dailyResponsibilities: string[];
  futureScope: string; learningPath: string[]; certifications: string[]; projects: string[];
}
export interface ResumeVersion {
  id: string; version: number; uploadedAt: string; atsScore: number;
  employabilityScore: number; skillCount: number; improvements: string[];
}
export interface Achievement {
  id: string; title: string; description: string; icon: string; earned: boolean;
  earnedAt?: string; level: 'beginner' | 'intermediate' | 'advanced' | 'placement' | 'industry';
}
export interface ChatMessage {
  id: string; role: 'user' | 'assistant'; content: string; timestamp: string;
}
export interface PlacementReadiness {
  technical: number; coding: number; interview: number;
  companyReadiness: { company: string; score: number }[]; overall: number;
}

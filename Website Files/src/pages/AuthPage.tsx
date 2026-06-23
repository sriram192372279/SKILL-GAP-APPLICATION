import React, { useState } from 'react';
import { 
  signInWithEmailAndPassword, 
  createUserWithEmailAndPassword, 
  signInWithPopup, 
  googleProvider,
  auth,
  sendPasswordResetEmail,
  updateProfile
} from '../firebase/config';
import { Zap, Mail, Lock, User, Phone, Eye, EyeOff, Loader, Sparkles } from 'lucide-react';

export default function AuthPage() {
  const [mode, setMode] = useState<'login' | 'signup' | 'forgot'>('login');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [fullName, setFullName] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');

  const handleEmailAuth = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setMessage('');
    setLoading(true);

    try {
      if (mode === 'signup') {
        if (!fullName.trim()) throw new Error('Please enter your full name.');
        const userCredential = await createUserWithEmailAndPassword(auth, email, password);
        await updateProfile(userCredential.user, { displayName: fullName });
      } else if (mode === 'login') {
        await signInWithEmailAndPassword(auth, email, password);
      } else if (mode === 'forgot') {
        await sendPasswordResetEmail(auth, email);
        setMessage('Password reset email sent! Check your inbox.');
      }
    } catch (err: any) {
      console.error(err);
      setError(err.message || 'Authentication failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleGoogleAuth = async () => {
    setError('');
    setMessage('');
    setLoading(true);
    try {
      await signInWithPopup(auth, googleProvider);
    } catch (err: any) {
      console.error(err);
      setError(err.message || 'Google Sign-In failed.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center relative overflow-hidden px-4 py-12 bg-dark-900">
      {/* Background Orbs */}
      <div className="absolute top-1/4 left-1/4 w-[350px] h-[350px] rounded-full bg-primary-600/10 blur-[100px] animate-pulse-slow" />
      <div className="absolute bottom-1/4 right-1/4 w-[350px] h-[350px] rounded-full bg-accent-600/10 blur-[100px] animate-pulse-slow" style={{ animationDelay: '2s' }} />

      <div className="w-full max-w-md relative z-10">
        {/* Brand Logo */}
        <div className="flex flex-col items-center mb-8 animate-slide-up">
          <div className="w-12 h-12 rounded-2xl flex items-center justify-center mb-3 shadow-glow-blue" style={{ background: 'linear-gradient(135deg, #0ea5e9, #d946ef)' }}>
            <Zap size={24} className="text-white" />
          </div>
          <h1 className="text-3xl font-display font-black text-white tracking-tight">
            SkillGap <span className="gradient-text">AI</span>
          </h1>
          <p className="text-sm text-slate-400 mt-1">Learn What You're Missing</p>
        </div>

        {/* Glassmorphism Card */}
        <div className="glass-card p-8 border border-white/10 shadow-glass animate-slide-up relative">
          {/* Accent corner line */}
          <div className="absolute top-0 left-0 right-0 h-[2px] rounded-t-2xl" style={{ background: 'linear-gradient(90deg, #0ea5e9, #d946ef)' }} />

          {/* Heading */}
          <div className="mb-6">
            <h2 className="text-xl font-bold text-white flex items-center gap-2">
              {mode === 'login' && 'Welcome Back'}
              {mode === 'signup' && 'Create Your Account'}
              {mode === 'forgot' && 'Reset Password'}
              <Sparkles size={16} className="text-primary-400" />
            </h2>
            <p className="text-xs text-slate-400 mt-1">
              {mode === 'login' && 'Log in to continue your adaptive learning journey'}
              {mode === 'signup' && 'Join the patent-level career intelligence ecosystem'}
              {mode === 'forgot' && 'Enter your email to receive password recovery details'}
            </p>
          </div>

          {/* Notifications */}
          {error && (
            <div className="mb-4 p-3 rounded-xl border border-red-500/20 text-xs text-red-400 font-medium leading-relaxed" style={{ background: 'rgba(239, 68, 68, 0.08)' }}>
              ⚠️ {error}
            </div>
          )}
          {message && (
            <div className="mb-4 p-3 rounded-xl border border-green-500/20 text-xs text-green-400 font-medium leading-relaxed" style={{ background: 'rgba(0, 255, 136, 0.08)' }}>
              ✓ {message}
            </div>
          )}

          {/* Form */}
          <form onSubmit={handleEmailAuth} className="space-y-4">
            {mode === 'signup' && (
              <>
                <div className="space-y-1.5">
                  <label className="text-xs text-slate-400 font-semibold block">Full Name</label>
                  <div className="relative">
                    <User className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-500" size={16} />
                    <input 
                      type="text" 
                      required 
                      value={fullName}
                      onChange={e => setFullName(e.target.value)}
                      placeholder="John Doe" 
                      className="input-field pl-11"
                    />
                  </div>
                </div>

                <div className="space-y-1.5">
                  <label className="text-xs text-slate-400 font-semibold block">Phone Number</label>
                  <div className="relative">
                    <Phone className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-500" size={16} />
                    <input 
                      type="tel" 
                      required 
                      value={phoneNumber}
                      onChange={e => setPhoneNumber(e.target.value)}
                      placeholder="+1 (555) 000-0000" 
                      className="input-field pl-11"
                    />
                  </div>
                </div>
              </>
            )}

            <div className="space-y-1.5">
              <label className="text-xs text-slate-400 font-semibold block">Email Address</label>
              <div className="relative">
                <Mail className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-500" size={16} />
                <input 
                  type="email" 
                  required 
                  value={email}
                  onChange={e => setEmail(e.target.value)}
                  placeholder="name@domain.com" 
                  className="input-field pl-11"
                />
              </div>
            </div>

            {mode !== 'forgot' && (
              <div className="space-y-1.5">
                <div className="flex justify-between items-center">
                  <label className="text-xs text-slate-400 font-semibold block">Password</label>
                  {mode === 'login' && (
                    <button 
                      type="button" 
                      onClick={() => setMode('forgot')} 
                      className="text-xs text-primary-400 hover:text-primary-300 transition-colors"
                    >
                      Forgot?
                    </button>
                  )}
                </div>
                <div className="relative">
                  <Lock className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-500" size={16} />
                  <input 
                    type={showPassword ? 'text' : 'password'} 
                    required 
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                    placeholder="••••••••" 
                    className="input-field pl-11 pr-11"
                  />
                  <button 
                    type="button"
                    onClick={() => setShowPassword(!showPassword)}
                    className="absolute right-4 top-1/2 -translate-y-1/2 text-slate-500 hover:text-slate-300 transition-colors"
                  >
                    {showPassword ? <EyeOff size={16} /> : <Eye size={16} />}
                  </button>
                </div>
              </div>
            )}

            <button 
              type="submit" 
              disabled={loading}
              className="btn-primary w-full py-3 rounded-xl font-semibold text-white flex items-center justify-center gap-2 mt-6 disabled:opacity-50 transition-all hover:scale-[1.01]"
            >
              {loading ? (
                <>
                  <Loader size={16} className="animate-spin text-white" />
                  <span>Processing...</span>
                </>
              ) : (
                <span>
                  {mode === 'login' && 'Log In'}
                  {mode === 'signup' && 'Sign Up'}
                  {mode === 'forgot' && 'Send Reset Link'}
                </span>
              )}
            </button>
          </form>

          {/* Social Sign-In (Skip for Forgot page) */}
          {mode !== 'forgot' && (
            <>
              <div className="relative my-6 text-center">
                <div className="absolute inset-0 flex items-center">
                  <div className="w-full border-t border-white/5" />
                </div>
                <span className="relative px-3 bg-dark-800 text-[10px] uppercase font-bold text-slate-500 tracking-wider">
                  Or Connect With
                </span>
              </div>

              <button 
                type="button"
                onClick={handleGoogleAuth}
                disabled={loading}
                className="w-full py-3 rounded-xl border border-white/10 hover:border-white/20 text-sm font-semibold text-white bg-white/5 transition-all flex items-center justify-center gap-2 hover:scale-[1.01]"
              >
                {/* Google SVG Icon */}
                <svg className="w-4 h-4 mr-1" viewBox="0 0 24 24">
                  <path
                    fill="#EA4335"
                    d="M12.24 10.285V14.4h6.887c-.648 2.41-2.519 4.114-5.136 4.114-3.41 0-6.19-2.78-6.19-6.19s2.78-6.19 6.19-6.19c1.7 0 3.23.69 4.34 1.81l3.055-3.055C19.347 2.925 15.99 1.5 12.24 1.5 6.442 1.5 1.74 6.202 1.74 12s4.702 10.5 10.5 10.5c6.26 0 10.15-4.4 10.15-10.35 0-.6-.05-1.2-.16-1.865H12.24z"
                  />
                </svg>
                Sign In with Google
              </button>
            </>
          )}

          {/* Footer toggle */}
          <div className="mt-6 text-center">
            {mode === 'login' && (
              <p className="text-xs text-slate-400">
                Don't have an account?{' '}
                <button onClick={() => { setMode('signup'); setError(''); }} className="text-primary-400 hover:underline font-semibold">
                  Sign up
                </button>
              </p>
            )}
            {mode === 'signup' && (
              <p className="text-xs text-slate-400">
                Already have an account?{' '}
                <button onClick={() => { setMode('login'); setError(''); }} className="text-primary-400 hover:underline font-semibold">
                  Log in
                </button>
              </p>
            )}
            {mode === 'forgot' && (
              <button onClick={() => { setMode('login'); setError(''); }} className="text-xs text-primary-400 hover:underline font-semibold">
                Back to Login
              </button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

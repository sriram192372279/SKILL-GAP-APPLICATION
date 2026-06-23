import { initializeApp } from "firebase/app";
import { 
  getAuth, 
  GoogleAuthProvider, 
  signInWithPopup, 
  signInWithEmailAndPassword, 
  createUserWithEmailAndPassword,
  signOut,
  updateProfile,
  sendPasswordResetEmail
} from "firebase/auth";

import { getFirestore } from "firebase/firestore";

const firebaseConfig = {
  apiKey: "AIzaSyA1G0m4qcc92M2181PM-MFkxL7M1y6Afpo",
  authDomain: "skill-gap-35a66.firebaseapp.com",
  projectId: "skill-gap-35a66",
  storageBucket: "skill-gap-35a66.firebasestorage.app",
  messagingSenderId: "570682841937",
  appId: "1:570682841937:web:065a4ce71a18732ef71170",
  measurementId: "G-8X1S6E8LS4"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);
export const db = getFirestore(app);
export const googleProvider = new GoogleAuthProvider();

export { 
  signInWithPopup, 
  signInWithEmailAndPassword, 
  createUserWithEmailAndPassword, 
  signOut,
  updateProfile,
  sendPasswordResetEmail
};

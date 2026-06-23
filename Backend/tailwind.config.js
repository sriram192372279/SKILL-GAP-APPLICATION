/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,js}",
  ],
  theme: {
    extend: {
      fontFamily: {
        outfit: ['Outfit', 'sans-serif'],
      },
      colors: {
        glass: "rgba(255, 255, 255, 0.05)",
        "glass-border": "rgba(255, 255, 255, 0.15)",
        "glass-dark": "rgba(8, 10, 20, 0.85)",
      },
      backdropBlur: {
        xs: "2px",
        '4xl': "40px",
      }
    },
  },
  plugins: [],
}

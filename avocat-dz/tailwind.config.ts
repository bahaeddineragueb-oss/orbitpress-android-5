import type { Config } from "tailwindcss";
const config: Config = {
  content: ["./app/**/*.{js,ts,jsx,tsx,mdx}", "./components/**/*.{js,ts,jsx,tsx,mdx}"],
  darkMode: "class",
  theme: {
    extend: {
      fontFamily: {
        sans: ["Tajawal", "Cairo", "system-ui", "sans-serif"],
        display: ["Cairo", "Tajawal", "sans-serif"],
      },
      colors: {
        primary: {
          50: "#f0f9ff",
          100: "#e0f2fe",
          500: "#0e7490",
          600: "#0c6580",
          700: "#0a556c",
          800: "#084558",
          900: "#063544",
        },
        gold: {
          400: "#facc15",
          500: "#c5a000",
          600: "#a88700",
        },
      },
      boxShadow: {
        soft: "0 8px 32px rgba(0,0,0,0.06)",
        card: "0 4px 20px rgba(0,0,0,0.05)",
      },
      borderRadius: {
        xl: "1rem",
        "2xl": "1.25rem",
      },
    },
  },
  plugins: [],
};
export default config;

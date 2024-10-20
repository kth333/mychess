import React, { useState, useEffect } from 'react';

const themes = [
  "light",
  "dark",
  "cupcake",
  "bumblebee",
  "emerald",
  "corporate",
  "synthwave",
  "retro",
  "cyberpunk",
  "valentine",
  "halloween",
  "garden",
  "forest",
  "aqua",
  "lofi",
  "pastel",
  "fantasy",
  "wireframe",
  "black",
  "luxury",
  "dracula",
  "cmyk",
  "autumn",
  "business",
  "acid",
  "lemonade",
  "night",
  "coffee",
  "winter",
  "dim",
  "nord",
  "sunset",
];

const ThemeSwitcher = () => {
  const [currentTheme, setCurrentTheme] = useState('dark');

  // Load the theme from sessionStorage on mount
  useEffect(() => {
    const savedTheme = sessionStorage.getItem('theme');
    if (savedTheme) {
      setCurrentTheme(savedTheme);
      document.documentElement.setAttribute('data-theme', savedTheme);
    }
  }, []);

  // Handle theme change and save to sessionStorage
  const handleThemeChange = (event) => {
    const theme = event.target.value;
    setCurrentTheme(theme);
    document.documentElement.setAttribute('data-theme', theme);
    sessionStorage.setItem('theme', theme); // Save the theme to sessionStorage
  };

  return (
    <div className="theme-switcher">
      <select
        value={currentTheme}
        onChange={handleThemeChange}
        className="select select-bordered"
      >
        {themes.map((theme) => (
          <option key={theme} value={theme}>
            {theme.charAt(0).toUpperCase() + theme.slice(1)}
          </option>
        ))}
      </select>
    </div>
  );
};

export default ThemeSwitcher;


import React, { useState } from 'react';

const themes = ["light", "dark", "pastel", "coffee", "dim", "forest"]; 

const ThemeSwitcher = () => {
  const [currentTheme, setCurrentTheme] = useState('dark');

  const handleThemeChange = (event) => {
    const theme = event.target.value;
    setCurrentTheme(theme);
    document.documentElement.setAttribute('data-theme', theme);
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


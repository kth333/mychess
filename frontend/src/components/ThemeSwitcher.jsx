import React, { useState, useEffect } from 'react';

const ThemeSwitcher = () => {
  const [isDim, setIsDim] = useState(false);

  // Load the theme from sessionStorage on mount
  useEffect(() => {
    const savedTheme = sessionStorage.getItem('theme');
    if (savedTheme) {
      setIsDim(savedTheme === 'dim');
      document.documentElement.setAttribute('data-theme', savedTheme);
    }
  }, []);

  // Handle theme change and save to sessionStorage
  const handleThemeChange = () => {
    const newTheme = isDim ? 'light' : 'dim';
    setIsDim(!isDim);
    document.documentElement.setAttribute('data-theme', newTheme);
    sessionStorage.setItem('theme', newTheme); // Save the theme to sessionStorage
  };

  return (
    <div className="flex items-center">
      <label className="relative inline-flex items-center cursor-pointer">
        <input
          type="checkbox"
          className="sr-only peer"
          checked={isDim}
          onChange={handleThemeChange}
        />
        <div className="w-11 h-6 bg-gray-300 rounded-full peer peer-checked:bg-primary transition-colors duration-300"></div>
        <span className={`absolute left-1 top-0.5 w-4 h-4 bg-white rounded-full transition-transform duration-300 ${isDim ? 'translate-x-5' : 'translate-x-0'}`}></span>
      </label>
      {/* <span className="ml-2">{isDim ? 'Dim' : 'Light'}</span> */}
    </div>
  );
};

export default ThemeSwitcher;

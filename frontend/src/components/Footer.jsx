import React from "react";

const Footer = () => {
  return (
    <footer className="bg-background border-t py-6 px-6 text-center">
      <p>&copy; 2024 MyChess. All rights reserved.</p>
      <div className="mt-2">
        <a href="/about" className="text-primary hover:underline">About</a>
        <span className="mx-2">|</span>
        <a href="/contact" className="text-primary hover:underline">Contact</a>
        
      </div>
    </footer>
  );
};

export default Footer;

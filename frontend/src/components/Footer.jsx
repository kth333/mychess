import React from "react";
import { Link } from "react-router-dom"; // Import Link from react-router-dom

const Footer = () => {
  return (
    <footer className="bg-background border-t py-6 px-6 text-center">
      <p>&copy; 2024 MyChess. All rights reserved.</p>
      <div className="mt-2">
        <Link to="/about" className="text-primary hover:underline">About</Link>
        <span className="mx-2">|</span>
        <Link to="/contact" className="text-primary hover:underline">Contact</Link>
      </div>
    </footer>
  );
};

export default Footer;

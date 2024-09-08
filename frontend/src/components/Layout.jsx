import React from "react";
import { Outlet, useLocation } from "react-router-dom";
import NavBar from './NavBar';
import ThemeSwitcher from "./ThemeSwitcher";


const Layout = ({ children }) => {
  const location = useLocation();

  return (
      <>
          <NavBar key={location.pathname}/>
          
          <div className="navbar-space"></div>
          <div className="main-body">

              {children}
              <Outlet/>

          </div>
          
      </>
  );
};

export default Layout;
import React from "react";
import { Outlet, useLocation } from "react-router-dom";
import NavBar from './NavBar';
import Footer from './Footer';


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
          <Footer />
          
      </>
  );
};

export default Layout;
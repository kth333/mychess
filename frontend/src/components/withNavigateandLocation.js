import React from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';

function withNavigateandLocation(Component) {
  return function WrapperComponent(props) {
    const navigate = useNavigate();
    const location = useLocation();
    const params = useParams(); // Get the route parameters

    return <Component {...props} navigate={navigate} location={location} params={params} />;
  };
}

export default withNavigateandLocation;

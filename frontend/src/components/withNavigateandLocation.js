import React from 'react';
import {useLocation, useNavigate} from 'react-router-dom';

const withNavigateandLocation = (Component) => {
    return (props) => {
        const navigate = useNavigate();
        const location = useLocation();
        return <Component {...props} location={location} navigate={navigate} />;
    };
};

export default withNavigateandLocation;
import React from 'react';
import { BrowserRouter as Router, Routes, Route, BrowserRouter } from 'react-router-dom';
import './App.css';
import Login from "./components/Login";
import Home from './components/Home';
import Layout from './components/Layout';
import Tournaments from './components/tournaments/Tournaments';
import CreateTournament from './components/tournaments/CreateTournament';
import Profile from './components/Profile';
import TournamentDetails from './components/tournaments/TournamentDetails';
import ProtectedRoute from './components/ProtectedRoute';
import UpdateTournament from './components/tournaments/UpdateTournament';
import UpdateProfile from './components/UpdateProfile';
import Blacklist from './components/Blacklist';
import Whitelist from './components/Whitelist';
import PasswordResetRequest from "./components/PasswordResetRequest";
import PasswordReset from './components/PasswordReset';
import ContactUs from './components/ContactUs';
import ScheduleMatches from './components/tournaments/ScheduleMatches';

function App() {
  return (
      <Router>
        <div className="App">
          <Routes>
            <Route element={<Layout />}>
              <Route exact index element={<Home />} />
              <Route exact path="/login" element={<Login />} />
              <Route exact path="/tournaments/page/:pageNumber" element={<Tournaments />} />
              <Route exact path="/tournaments/:name" element={<TournamentDetails />} />
              <Route exact path="/password-reset-request" element={<PasswordResetRequest/>} />
              <Route exact path="/password-reset/:token" element={<PasswordReset/>} />
              <Route exact path="/contact-us" element={<ContactUs />} />

              <Route element={<ProtectedRoute />}>
                <Route exact path="/schedule-matches/:id" element={<ScheduleMatches />} />
                <Route exact path="/create-tournament" element={<CreateTournament />} />
                <Route exact path="/update-tournament/:name" element={<UpdateTournament />} />
                <Route exact path="/profile/:playerId" element={<Profile/>} />
                <Route exact path="/profile/update/:id" element={<UpdateProfile/>} />
                <Route exact path="/blacklist" element={<Blacklist/>} />
                <Route exact path="/whitelist" element={<Whitelist/>} />
              </Route>


            </Route>
          </Routes>
        </div>
      </Router>
  );
}

export default App;

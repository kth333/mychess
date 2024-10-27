import React from 'react';
import { BrowserRouter as Router, Routes, Route, BrowserRouter } from 'react-router-dom';
import './App.css';
import Login from "./components/Login";
import Register from './components/Register';
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

function App() {
  return (
      <Router>
        <div className="App">
          <Routes>
            <Route element={<Layout />}>
              <Route exact index element={<Home />} />
              <Route exact path="/login" element={<Login />} />
              <Route exact path="/register" element={<Register />} />
              <Route exact path="/tournaments/page/:pageNumber" element={<Tournaments />} />
              <Route exact path="/tournaments/:name" element={<TournamentDetails />} />
              
              <Route element={<ProtectedRoute />}>
                <Route exact path="/create-tournament" element={<CreateTournament />} />
                <Route exact path="/update-tournament/:name" element={<UpdateTournament />} />
                <Route exact path="/profile" element={<Profile/>} />
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

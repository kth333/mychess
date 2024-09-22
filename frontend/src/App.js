import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
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

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route element={<Layout />}>
            <Route exact index element={<Home />} />
            <Route exact path="/login" element={<Login />} />
            <Route exact path="/register" element={<Register />} />
            <Route exact path="/tournaments" element={<Tournaments />} />
            <Route exact path="/tournaments/:name" element={<TournamentDetails />} />
            
            <Route element={<ProtectedRoute />}>
              <Route exact path="/create-tournament" element={<CreateTournament />} />
              <Route exact path="/profile" element={<Profile/>} />
            </Route>


          </Route>
        </Routes>
      </div>
    </Router>
  );
}

export default App;

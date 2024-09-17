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
            <Route exact path="/create-tournament" element={<CreateTournament />} />
            <Route exact path="/profile" element={<Profile/>} />


          </Route>
        </Routes>
      </div>
    </Router>
  );
}

export default App;

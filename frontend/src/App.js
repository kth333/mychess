import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';
import Login from "./components/Login";
import Register from './components/Register';

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route exact index element={<Login />} />
          <Route exact path="/register" element={<Register />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;

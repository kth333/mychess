import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';
import Login from "./components/Login";
import Register from './components/Register';
import Home from './components/Home';
import Layout from './components/Layout';

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route element={<Layout />}>
            <Route exact index element={<Home />} />
            <Route exact path="/login" element={<Login />} />
            <Route exact path="/register" element={<Register />} />
          </Route>
        </Routes>
      </div>
    </Router>
  );
}

export default App;

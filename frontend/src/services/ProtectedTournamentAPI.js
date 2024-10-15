import axios from 'axios';

// const API_BASE_URL = "http://52.77.229.96:8083/api/v1";
const API_BASE_URL = "http://localhost:8083/api/v1";



let ProtectedTournamentAPI = axios.create({
  baseURL: API_BASE_URL,
  timeout: 100000,
});

ProtectedTournamentAPI.interceptors.request.use(
  (config) => {
    const token = sessionStorage.getItem("token");
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    console.log('Request Headers:', config.headers);
    return config;
  },
  (error) => {
    console.error('Request Error:', error);
    return Promise.reject(error);
  }
);

export { ProtectedTournamentAPI };
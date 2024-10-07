import axios from 'axios';

const API_BASE_URL = "http://localhost:8083/api/v1";



let ProtectedTournamentAPI = axios.create({
  baseURL: API_BASE_URL,
  timeout: 100000,
});

ProtectedTournamentAPI.defaults.headers.common['Authorization'] = `Bearer ${sessionStorage.getItem("token")}`;

// Interceptor to log request details
ProtectedTournamentAPI.interceptors.request.use(
  (config) => {
    console.log('Request Headers:', config.headers);
    return config;
  },
  (error) => {
    console.error('Request Error:', error);
    return Promise.reject(error);
  }
);

export { ProtectedTournamentAPI };
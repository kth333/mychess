import axios from 'axios';

const API_BASE_URL = "http://localhost:8081/api/v1";



let ProtectedPlayerAPI = axios.create({
  baseURL: API_BASE_URL,
  timeout: 100000,
});

ProtectedPlayerAPI.defaults.headers.common['Authorization'] = `Bearer ${localStorage.getItem("token")}`;

export { ProtectedPlayerAPI };
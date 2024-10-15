import axios from 'axios';

// const API_BASE_URL = "http://13.229.216.67:8082/api/v1";
const API_BASE_URL = "http://localhost:8082/api/v1";



let ProtectedMatchAPI = axios.create({
  baseURL: API_BASE_URL,
  timeout: 100000,
});

ProtectedMatchAPI.defaults.headers.common['Authorization'] = `Bearer ${sessionStorage.getItem("token")}`;

export { ProtectedMatchAPI };
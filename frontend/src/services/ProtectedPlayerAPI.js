import axios from 'axios';

// const API_BASE_URL = "http://47.129.181.253:8081/api/v1";
const API_BASE_URL = "http://localhost:8081/api/v1";



let ProtectedPlayerAPI = axios.create({
  baseURL: API_BASE_URL,
  timeout: 100000,
});

// Add a request interceptor to dynamically set the Authorization header before each request
ProtectedPlayerAPI.interceptors.request.use(
  (config) => {
    // Get the token from sessionStorage before each request
    const token = sessionStorage.getItem("token");

    // If token exists, set it in the Authorization header
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config; // Return the updated config
  },
  (error) => {
    // Handle any errors before the request is sent
    return Promise.reject(error);
  }
);

export { ProtectedPlayerAPI };
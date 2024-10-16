import axios from 'axios';

// const API_BASE_URL = "http://54.255.248.183:8080/api/v1";
const API_BASE_URL = "http://localhost:8080/api/v1";


const AuthAPI = axios.create({
  baseURL: API_BASE_URL,
  timeout: 100000
  
});

export { AuthAPI };

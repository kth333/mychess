import axios from 'axios';

const API_BASE_URL = "http://13.212.236.106:8080/api/v1";


const AuthAPI = axios.create({
  baseURL: API_BASE_URL,
  timeout: 100000
  
});

export { AuthAPI };

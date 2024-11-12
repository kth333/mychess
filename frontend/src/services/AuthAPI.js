import axios from 'axios';

const API_BASE_URL = "https://13.229.54.190:8080/api/v1";
// const API_BASE_URL = "http://localhost:8080/api/v1";


const AuthAPI = axios.create({
  baseURL: API_BASE_URL,
  timeout: 100000
  
});

export { AuthAPI };

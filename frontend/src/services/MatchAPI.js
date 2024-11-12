import axios from 'axios';

const API_BASE_URL = "https://13.229.46.119:8082/api/v1";
// const API_BASE_URL = "http://localhost:8082/api/v1";


const MatchAPI = axios.create({
  baseURL: API_BASE_URL,
  timeout: 100000
  
});

export { MatchAPI };

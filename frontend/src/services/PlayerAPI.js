import axios from 'axios';

// const API_BASE_URL = "http://13.229.46.119:8082/api/v1";
const API_BASE_URL = "http://localhost:8081/api/v1";


const PlayerAPI = axios.create({
  baseURL: API_BASE_URL,
  timeout: 100000
  
});

export { PlayerAPI };

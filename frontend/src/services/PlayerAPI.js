import axios from 'axios';

const API_BASE_URL = "https://mychesss.com/api/v1";
// const API_BASE_URL = "http://localhost:8081/api/v1";


const PlayerAPI = axios.create({
  baseURL: API_BASE_URL,
  timeout: 100000
  
});

export { PlayerAPI };

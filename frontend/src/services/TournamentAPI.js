import axios from 'axios';

const API_BASE_URL = "http://52.77.229.96:8083/api/v1";
// const API_BASE_URL = "http://localhost:8083/api/v1";


const TournamentAPI = axios.create({
  baseURL: API_BASE_URL,
  timeout: 100000
  
});

export { TournamentAPI };
import axios from 'axios';

const API_BASE_URL = "https://mychesss.com/api/v1";
// const API_BASE_URL = "http://localhost:8085/api/v1";


const EmailAPI = axios.create({
  baseURL: API_BASE_URL,
  timeout: 100000
  
});

export { EmailAPI };

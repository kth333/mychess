import axios from 'axios';

// const API_BASE_URL = "http://47.129.181.253:8081/api/v1";
const API_BASE_URL = "http://localhost:8081/api/v1";



let ProtectedPlayerAPI = axios.create({
  baseURL: API_BASE_URL,
  timeout: 100000,
});

ProtectedPlayerAPI.defaults.headers.common['Authorization'] = `Bearer ${sessionStorage.getItem("token")}`;

export { ProtectedPlayerAPI };
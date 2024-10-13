import axios from 'axios';

const API_BASE_URL = "http://54.179.149.124:8081/api/v1";



let ProtectedPlayerAPI = axios.create({
  baseURL: API_BASE_URL,
  timeout: 100000,
});

ProtectedPlayerAPI.defaults.headers.common['Authorization'] = `Bearer ${sessionStorage.getItem("token")}`;

export { ProtectedPlayerAPI };
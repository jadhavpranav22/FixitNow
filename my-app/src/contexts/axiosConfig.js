import axios from 'axios';

const API_BASE = 'http://localhost:6969';

// Create Axios instance
const api = axios.create({
  baseURL: API_BASE,
});

// Attach token automatically from localStorage
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token'); // get token stored by AuthContext
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export default api;

import axios from 'axios';
import { jwtDecode } from 'jwt-decode'; // Am schimbat din 'jwt-decode' în 'jwtDecode' conform standardului

// Setează URL-ul de bază al API-ului (gateway-ul Traefik)
const apiClient = axios.create({
    baseURL: 'http://localhost:81/api',
});

// Funcția de login
const login = async (username, password) => {
    try {
        // Face cererea de login
        const response = await apiClient.post('/auth/login', {
            username,
            password,
        });

        if (response.data.token) {
            // Preia token-ul din răspuns
            const { token } = response.data;

            // Stochează token-ul în localStorage
            localStorage.setItem('token', token);

            // Decodează token-ul pentru a obține rolul și ID-ul
            // Token-ul conține "role" și "id"
            const decodedUser = jwtDecode(token);

            // Stochează informațiile utilizatorului (opțional, dar util)
            localStorage.setItem('user', JSON.stringify({
                id: decodedUser.id,
                username: decodedUser.username,
                role: decodedUser.role
            }));

            // Configurează interceptorul axios
            setupAxiosInterceptor(token);

            return decodedUser;
        }
    } catch (error) {
        console.error("Eroare la login:", error);
        throw error;
    }
};

// Funcția de logout
const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    delete apiClient.defaults.headers.common['app-auth'];
};

// Funcție pentru a seta interceptorul
const setupAxiosInterceptor = (token) => {
    // Atașează header-ul 'app-auth' la toate cererile viitoare
    // Numele header-ului este "app-auth"
    apiClient.defaults.headers.common['app-auth'] = token;
};

// Verifică dacă există deja un token la încărcarea aplicației
const token = localStorage.getItem('token');
if (token) {
    setupAxiosInterceptor(token);
}
// Funcție helper pentru a prelua user-ul din localStorage
const getCurrentUser = () => {
    const userStr = localStorage.getItem('user');
    if (userStr) {
        return JSON.parse(userStr);
    }
    return null;
};

// Funcție helper pentru a verifica dacă token-ul este valid (nu e expirat)
const isLoggedIn = () => {
    const token = localStorage.getItem('token');
    if (!token) {
        return false;
    }
    try {
        const decoded = jwtDecode(token); // jwtDecode vine din 'jwt-decode'
        // Verifică dacă token-ul a expirat
        if (Date.now() >= decoded.exp * 1000) {
            logout(); // Șterge token-ul expirat
            return false;
        }
        return true;
    } catch (err) {
        return false; // Token invalid
    }
};

export {
    apiClient,
    login,
    logout,
    getCurrentUser, // exportă funcția nouă
    isLoggedIn,     // exportă funcția nouă
};
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';
const apiClient = axios.create({
    baseURL: 'http://localhost:81/api',
});

const login = async (username, password) => {
    try {
        const response = await apiClient.post('/auth/login', {
            username,
            password,
        });

        if (response.data.token) {
            const { token } = response.data;

            localStorage.setItem('token', token);

            const decodedUser = jwtDecode(token);

            localStorage.setItem('user', JSON.stringify({
                id: decodedUser.id,
                username: decodedUser.username,
                role: decodedUser.role
            }));

            setupAxiosInterceptor(token);

            return decodedUser;
        }
    } catch (error) {
        console.error("Eroare la login:", error);
        throw error;
    }
};

const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    delete apiClient.defaults.headers.common['app-auth'];
};

const setupAxiosInterceptor = (token) => {
    apiClient.defaults.headers.common['app-auth'] = token;
};

const token = localStorage.getItem('token');
if (token) {
    setupAxiosInterceptor(token);
}

const getCurrentUser = () => {
    const userStr = localStorage.getItem('user');
    if (userStr) {
        return JSON.parse(userStr);
    }
    return null;
};

const isLoggedIn = () => {
    const token = localStorage.getItem('token');
    if (!token) {
        return false;
    }
    try {
        const decoded = jwtDecode(token);
        if (Date.now() >= decoded.exp * 1000) {
            logout();
            return false;
        }
        return true;
    } catch (err) {
        return false;
    }
};

const getDevicesForUser = async (userId) => {
    try {
        const response = await apiClient.get(`/device/${userId}`);
        return response.data;
    } catch (error) {
        console.error(`Eroare la preluarea device-urilor pentru user ${userId}:`, error);
        throw error;
    }
};

const getAllUsers = async () => {
    try {
        const response = await apiClient.get('/user');
        return response.data;
    } catch (error) {
        console.error(`Eroare la preluarea utilizatorilor:`, error);
        throw error;
    }
};

const createUser = async (userData) => {
    let newUserId;

    try {
        const authResponse = await apiClient.post('/auth/register-admin', {
            username: userData.username,
            password: userData.password,
            role: userData.role
        });

        newUserId = authResponse.data;

        if (!newUserId) {
            throw new Error("Nu am primit ID de la AuthenticationService");
        }

        await apiClient.post('/user', {
            id: newUserId, // Folosim ID-ul de la Pasul 1
            firstName: userData.firstName,
            lastName: userData.lastName,
            email: userData.email,
            telephone: userData.telephone,
            address: userData.address
        });

        await apiClient.post('/device/user', {
            id: newUserId // Folosim același ID
        });

        return { success: true, user: { ...userData, id: newUserId } };

    } catch (error) {
        console.error("Eroare la crearea utilizatorului în 3 pași:", error);

        throw error;
    }
};

export {
    apiClient,
    login,
    logout,
    getCurrentUser,
    isLoggedIn,
    getDevicesForUser,
    getAllUsers,
    createUser,
};
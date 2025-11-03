import React, { useState } from 'react';
import { login } from '../apiService';
import { useNavigate } from 'react-router-dom'; // 1. Importă useNavigate

function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);
    const navigate = useNavigate(); // 2. Inițializează hook-ul

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null); // Resetează eroarea

        try {
            // 3. Funcția de login este apelată
            const userData = await login(username, password);

            console.log('Login reușit:', userData);

            // 4. Redirecționează pe bază de rol
            if (userData.role === 'ADMIN') {
                navigate('/admin');
            } else if (userData.role === 'USER') { // Am actualizat la 'USER' conform discuției
                navigate('/client');
            } else {
                // Fallback, deși nu ar trebui să ajungă aici
                navigate('/');
            }

        } catch (err) {
            setError('Nume de utilizator sau parolă incorectă.');
            console.error(err);
        }
    };

    return (
        <div>
            <h2>Login</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Username:</label>
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Password:</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Login</button>
                {error && <p style={{ color: 'red' }}>{error}</p>}
            </form>
        </div>
    );
}

export default Login;
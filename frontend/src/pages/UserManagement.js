import React, { useState, useEffect, useCallback } from 'react';
import { getAllUsers } from '../apiService';
import CreateUserForm from '../components/CreateUserForm';

function UserManagement() {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchUsers = useCallback(async () => {
        setLoading(true);
        try {
            const userData = await getAllUsers();
            setUsers(userData);
            setError(null);
        } catch (err) {
            setError('Nu am putut prelua lista de utilizatori.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    }, []);


    useEffect(() => {
        fetchUsers();
    }, [fetchUsers]);

    // La încărcarea inițială
    if (loading && users.length === 0) return <h1>Se încarcă utilizatorii...</h1>;
    // Dacă a eșuat încărcarea inițială
    if (error && users.length === 0) return <h1 style={{ color: 'red' }}>{error}</h1>;

    return (
        <div>
            <CreateUserForm onUserCreated={fetchUsers} />

            <hr style={{ margin: '20px 0' }} />

            <h2>Listă Utilizatori</h2>
            {loading && users.length > 0 && <p>Se reîncarcă lista...</p>}
            {error && users.length > 0 && <p style={{ color: 'red' }}>{error}</p>}

            <table border="1" style={{ width: '100%', borderCollapse: 'collapse' }}>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Nume</th>
                    <th>Prenume</th>
                    <th>Email</th>
                    <th>Telefon</th>
                    <th>Adresă</th>
                </tr>
                </thead>
                <tbody>
                {users.map(user => (
                    <tr key={user.id}>
                        <td>{user.id}</td>
                        <td>{user.lastName}</td>
                        <td>{user.firstName}</td>
                        <td>{user.email}</td>
                        <td>{user.telephone}</td>
                        <td>{user.address}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default UserManagement;
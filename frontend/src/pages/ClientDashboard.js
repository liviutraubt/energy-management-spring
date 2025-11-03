import React, { useState, useEffect } from 'react';
import { getCurrentUser, getDevicesForUser } from '../apiService';

function ClientDashboard() {
    const [devices, setDevices] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const user = getCurrentUser();

        if (user && user.id) {
            const fetchDevices = async () => {
                try {
                    const userDevices = await getDevicesForUser(user.id);
                    setDevices(userDevices);
                    setError(null);
                } catch (err) {
                    setError('Nu am putut prelua lista de device-uri.');
                    console.error(err);
                } finally {
                    setLoading(false);
                }
            };

            fetchDevices();
        } else {
            setError('Nu am putut identifica utilizatorul.');
            setLoading(false);
        }
    }, []);

    if (loading) {
        return <h1>Se încarcă device-urile...</h1>;
    }

    if (error) {
        return <h1 style={{ color: 'red' }}>{error}</h1>;
    }

    return (
        <div>
            <h1>Device-urile Mele (Rol CLIENT)</h1>
            {devices.length === 0 ? (
                <p>Nu aveți niciun device alocat.</p>
            ) : (
                <table border="1" style={{ width: '100%', borderCollapse: 'collapse' }}>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Tip Device</th>
                        <th>Consum (kW)</th>
                        <th>Activ</th>
                    </tr>
                    </thead>
                    <tbody>
                    {devices.map(device => (
                        <tr key={device.id}>
                            <td>{device.id}</td>
                            <td>{device.device_type}</td>
                            <td>{device.consumption}</td>
                            <td>{device.active ? 'Da' : 'Nu'}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}

export default ClientDashboard;
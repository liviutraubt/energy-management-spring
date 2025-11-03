import React, { useState } from 'react';
import { createUser } from '../apiService';

function CreateUserForm({ onUserCreated }) {
    const [formData, setFormData] = useState({
        username: '',
        password: '',
        role: 'USER', // Rolul default este USER
        firstName: '',
        lastName: '',
        email: '',
        telephone: '',
        address: ''
    });
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);
        setSuccess(null);

        if (!formData.username || !formData.password || !formData.firstName || !formData.lastName) {
            setError("Câmpurile Username, Password, Nume și Prenume sunt obligatorii.");
            return;
        }

        try {
            await createUser(formData);
            setSuccess('Utilizator creat cu succes!');

            setFormData({
                username: '', password: '', role: 'USER',
                firstName: '', lastName: '', email: '',
                telephone: '', address: ''
            });

            if (onUserCreated) {
                onUserCreated();
            }
        } catch (err) {
            setError(err.response?.data?.error || 'A apărut o eroare la crearea utilizatorului.');
        }
    };

    const formStyle = {
        display: 'flex',
        flexWrap: 'wrap',
        gap: '10px 15px',
        padding: '20px',
        border: '1px solid #ccc',
        borderRadius: '8px'
    };
    const inputGroupStyle = { display: 'flex', flexDirection: 'column', minWidth: '200px' };
    const labelStyle = { marginBottom: '5px', fontWeight: 'bold' };
    const inputStyle = { padding: '8px', border: '1px solid #ddd', borderRadius: '4px' };
    const buttonStyle = { padding: '10px 15px', background: '#007bff', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer', alignSelf: 'flex-end' };

    return (
        <div>
            <h3>Creare Utilizator Nou</h3>
            <form onSubmit={handleSubmit} style={formStyle}>

                <div style={inputGroupStyle}>
                    <label style={labelStyle} htmlFor="username">Username:</label>
                    <input style={inputStyle} type="text" id="username" name="username" value={formData.username} onChange={handleChange} required />
                </div>
                <div style={inputGroupStyle}>
                    <label style={labelStyle} htmlFor="password">Parolă:</label>
                    <input style={inputStyle} type="password" id="password" name="password" value={formData.password} onChange={handleChange} required />
                </div>
                <div style={inputGroupStyle}>
                    <label style={labelStyle} htmlFor="role">Rol:</label>
                    <select style={inputStyle} id="role" name="role" value={formData.role} onChange={handleChange}>
                        <option value="USER">USER (Client)</option>
                        <option value="ADMIN">ADMIN</option>
                    </select>
                </div>

                <div style={inputGroupStyle}>
                    <label style={labelStyle} htmlFor="firstName">Prenume:</label>
                    <input style={inputStyle} type="text" id="firstName" name="firstName" value={formData.firstName} onChange={handleChange} required />
                </div>
                <div style={inputGroupStyle}>
                    <label style={labelStyle} htmlFor="lastName">Nume:</label>
                    <input style={inputStyle} type="text" id="lastName" name="lastName" value={formData.lastName} onChange={handleChange} required />
                </div>
                <div style={inputGroupStyle}>
                    <label style={labelStyle} htmlFor="email">Email:</label>
                    <input style={inputStyle} type="email" id="email" name="email" value={formData.email} onChange={handleChange} />
                </div>
                <div style={inputGroupStyle}>
                    <label style={labelStyle} htmlFor="telephone">Telefon:</label>
                    <input style={inputStyle} type="tel" id="telephone" name="telephone" value={formData.telephone} onChange={handleChange} />
                </div>
                <div style={inputGroupStyle}>
                    <label style={labelStyle} htmlFor="address">Adresă:</label>
                    <input style={inputStyle} type="text" id="address" name="address" value={formData.address} onChange={handleChange} />
                </div>

                <button type="submit" style={buttonStyle}>Creare Utilizator</button>
            </form>
            {success && <p style={{ color: 'green', marginTop: '10px' }}>{success}</p>}
            {error && <p style={{ color: 'red', marginTop: '10px' }}>{error}</p>}
        </div>
    );
}

export default CreateUserForm;
import React from 'react';
import UserManagement from './UserManagement';
import DeviceManagement from './DeviceManagement';

function AdminDashboard() {
    return (
        <div>
            <h1>Admin Dashboard</h1>
            <hr />
            <UserManagement />

            <DeviceManagement />
        </div>
    );
}

export default AdminDashboard;
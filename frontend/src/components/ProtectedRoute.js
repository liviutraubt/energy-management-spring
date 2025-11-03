import React from 'react';
import { Navigate } from 'react-router-dom';
import { isLoggedIn, getCurrentUser } from '../apiService';

// `allowedRoles` este un array, ex: ['ADMIN'] sau ['USER']
function ProtectedRoute({ children, allowedRoles }) {
    if (!isLoggedIn()) {
        // 1. Verifică dacă e logat
        return <Navigate to="/login" replace />;
    }

    const user = getCurrentUser();

    if (!user || !allowedRoles.includes(user.role)) {
        // 2. Verifică dacă are rolul corect
        // Poți redirecționa către /login sau o pagină 403 (Neautorizat)
        return <Navigate to="/login" replace />;
    }

    // 3. Dacă totul e ok, randează pagina
    return children;
}

export default ProtectedRoute;
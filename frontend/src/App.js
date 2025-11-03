import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Login from './components/Login';
import Layout from './components/Layout';
import ProtectedRoute from './components/ProtectedRoute';
import AdminDashboard from './pages/AdminDashboard';
import ClientDashboard from './pages/ClientDashboard';

function App() {
    return (
        <BrowserRouter>
            <Routes>
                {/* Grupăm rutele protejate.
          Acestea vor folosi componenta Layout ca "wrapper"
          datorită <Route element={<Layout />}>.
          Layout-ul (cu bara de navigație și butonul de Logout)
          va fi afișat doar pentru aceste rute.
        */}
                <Route element={<Layout />}>
                    <Route
                        path="/admin" // Ruta completă
                        element={
                            <ProtectedRoute allowedRoles={['ADMIN']}>
                                <AdminDashboard />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/client" // Ruta completă
                        element={
                            <ProtectedRoute allowedRoles={['USER']}>
                                <ClientDashboard />
                            </ProtectedRoute>
                        }
                    />
                </Route>

                {/* Rutele publice.
          Acum, atât "/" (root-ul) cât și "/login"
          vor randa componenta Login.
        */}
                <Route path="/login" element={<Login />} />
                <Route path="/" element={<Login />} /> {/* <-- Aceasta este modificarea cerută */}

                {/* Redirecționează orice altă rută necunoscută (catch-all)
          către pagina principală (care este acum Login).
        */}
                <Route path="*" element={<Navigate to="/" replace />} />

            </Routes>
        </BrowserRouter>
    );
}

export default App;
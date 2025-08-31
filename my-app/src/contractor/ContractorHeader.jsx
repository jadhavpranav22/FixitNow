

import React from 'react';
import { useAuth } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';
import { FaUserCircle } from 'react-icons/fa';
import ServiceSearch from '../pages/ServiceSearch';
import Logo from '../assets/Logo.png';

function ContractorHeader() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header className="bg-dark py-2">
      <div className="container d-flex align-items-center justify-content-between">
        <img src={Logo} alt="FixItNow Logo" style={{ height: '50px' }} />

        <div className="d-flex align-items-center gap-4">
          <ServiceSearch variant="provider" />
          <div className="text-white d-flex align-items-center gap-2">
            <FaUserCircle size={22} />
            <span>{user?.email?.split('@')[0]}</span>
            <button onClick={handleLogout} className="btn btn-sm btn-outline-danger ms-2">
              Logout
            </button>
          </div>
        </div>
      </div>
    </header>
  );
}

export default ContractorHeader;

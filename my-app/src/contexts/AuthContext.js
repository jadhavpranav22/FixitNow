import React, { createContext, useContext, useState, useEffect, useCallback } from "react";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [auth, setAuth] = useState({
    token: null,
    role: null,
    email: null,
    userId: null,
  });

  const [loading, setLoading] = useState(true);

  const isValidToken = useCallback((token) => {
    if (!token) return false;
    try {
      const parts = token.split('.');
      if (parts.length !== 3) return false;
      const payload = JSON.parse(atob(parts[1]));
      const currentTime = Date.now() / 1000;
      return !payload.exp || payload.exp > currentTime;
    } catch {
      return false;
    }
  }, []);

  useEffect(() => {
    const token = localStorage.getItem("token");
    const role = localStorage.getItem("role");
    const email = localStorage.getItem("email");
    const userId = localStorage.getItem("userId");

    if (token && role && email && userId && isValidToken(token)) {
      setAuth({ token, role: role.toUpperCase(), email, userId });
    } else {
      localStorage.clear();
    }
    setLoading(false);
  }, [isValidToken]);

  const login = useCallback(({ token, role, email, userId }) => {
    if (!isValidToken(token)) throw new Error('Invalid token');
    setAuth({ token, role: role.toUpperCase(), email, userId });
    localStorage.setItem("token", token);
    localStorage.setItem("role", role.toUpperCase());
    localStorage.setItem("email", email);
    localStorage.setItem("userId", userId);
  }, [isValidToken]);

  const logout = useCallback(() => {
    setAuth({ token: null, role: null, email: null, userId: null });
    localStorage.clear();
  }, []);

  const hasRole = useCallback((requiredRole) => auth.role === requiredRole?.toUpperCase(), [auth.role]);
  const hasAnyRole = useCallback((roles) => roles.some(r => auth.role === r?.toUpperCase()), [auth.role]);

  return (
    <AuthContext.Provider value={{ ...auth, login, logout, hasRole, hasAnyRole, isLoggedIn: Boolean(auth.token), loading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);

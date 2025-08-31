import React from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  useLocation,
} from "react-router-dom";
import { Provider } from "react-redux";

import store from "./redux/store";

import Header from "./components/Header";
import ContractorHeader from "./contractor/ContractorHeader";
import Footer from "./components/Footer";   // ⬅️ Import Footer

import LoginPage from "./LoginRegister/LoginPage";
import Home from "./pages/Home";
import RegisterPage from "./LoginRegister/RegisterPage";
import CartPage from "./pages/CartPage";
import ResetPassword from "./LoginRegister/ResetPassword";
import ServicesPage from "./pages/Servicepage";
import Unauthorized from "./pages/Unauthorized";

import ContractorDashboard from "./contractor/ContractorDashboard";
import CustomerDashboard from "./pages/CustomerDashboard";
import AdminDashboard from "./admin/AdminDashboard";

import { AuthProvider } from "./contexts/AuthContext";
import { NotificationProvider } from "./contexts/NotificationContext";
import Contact from "./pages/Contact";
import AboutUs from "./pages/About";

import ProtectedRoute from "./components/ProtectedRoute";

// Layout component with dynamic header + footer
function AppLayout({ children }) {
  const location = useLocation();

  // Show contractor header only on contractor dashboard routes
  const isContractorRoute = location.pathname.startsWith("/provider");

  // Hide footer on login & register
  const hideFooter = ["/login", "/register"].includes(location.pathname);

  return (
    <div
      className="app-container"
      style={{ display: "flex", flexDirection: "column", minHeight: "100vh" }}
    >
      {isContractorRoute ? <ContractorHeader /> : <Header />}
      <main style={{ flex: 1 }}>{children}</main>
      {!hideFooter && <Footer />}   {/* ⬅️ Footer on all pages except login/register */}
    </div>
  );
}

function App() {
  return (
    <Provider store={store}>
      <AuthProvider>
        <NotificationProvider>
          <Router>
            <AppLayout>
              <Routes>
                {/* Public Routes */}
                <Route path="/" element={<Home />} />
                <Route path="/services" element={<ServicesPage />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route path="/reset-password" element={<ResetPassword />} />
                <Route path="/contact" element={<Contact />} />
                <Route path="/about" element={<AboutUs />} />
                <Route path="/unauthorized" element={<Unauthorized />} />

                {/* USER Routes */}
                <Route
                  path="/user/home"
                  element={
                    <ProtectedRoute allowedRoles={["CUSTOMER", "ADMIN"]}>
                      <Home />
                    </ProtectedRoute>
                  }
                />
                <Route
                  path="/user/dashboard"
                  element={
                    <ProtectedRoute allowedRoles={["CUSTOMER"]}>
                      <CustomerDashboard />
                    </ProtectedRoute>
                  }
                />
                <Route
                  path="/cart"
                  element={
                    <ProtectedRoute allowedRoles={["CUSTOMER"]}>
                      <CartPage />
                    </ProtectedRoute>
                  }
                />

                {/* PROVIDER Routes */}
                <Route
                  path="/provider/dashboard"
                  element={
                    <ProtectedRoute allowedRoles={["CONTRACTOR"]}>
                      <ContractorDashboard />
                    </ProtectedRoute>
                  }
                />

                {/* ADMIN Routes */}
                <Route
                  path="/admin/dashboard"
                  element={
                    <ProtectedRoute allowedRoles={["ADMIN"]}>
                      <AdminDashboard />
                    </ProtectedRoute>
                  }
                />
              </Routes>
            </AppLayout>
          </Router>
        </NotificationProvider>
      </AuthProvider>
    </Provider>
  );
}

export default App;

import React, { useState } from "react";
import {
  TextField,
  Button,
  Typography,
  Link,
  Box,
  MenuItem,
  Card,
  CardContent,
  Alert,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const RegisterPage = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    confirmPassword: "",
    role: "",
  });
  const [error, setError] = useState("");

  const handleChange = (e) => {
    setFormData((prev) => ({ ...prev, [e.target.name]: e.target.value }));
    setError("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (formData.password !== formData.confirmPassword) {
      setError("Passwords do not match");
      return;
    }

    try {
      await axios.post("http://localhost:6969/auth/register", {
        name: formData.name,
        email: formData.email.toLowerCase(), // Force lowercase
        password: formData.password,
        role: formData.role,
      });
      navigate("/login");
    } catch (err) {
      if (err.response && err.response.data) {
        const data = err.response.data;
        if (typeof data === "string") {
          setError(data);
        } else if (data.message) {
          setError(data.message);
        } else if (data.error) {
          setError(data.error);
        } else {
          setError("Registration failed. Please try again.");
        }
      } else {
        setError("Server error. Please try again later.");
      }
    }
  };

  return (
    <Box
      sx={{
        minHeight: "100vh",
        background: "linear-gradient(135deg, #cceeff, #e6f7ff)",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        p: 2,
      }}
    >
      <Card
        sx={{
          maxWidth: 400,
          width: "100%",
          borderRadius: 4,
          boxShadow: "0px 8px 24px rgba(0,0,0,0.15)",
          backgroundColor: "#ffffff",
        }}
      >
        <CardContent>
          <Typography
            variant="h5"
            align="center"
            fontWeight="bold"
            color="primary"
            gutterBottom
          >
            Create Account
          </Typography>

          <Typography
            variant="subtitle2"
            align="center"
            sx={{ mb: 2 }}
            color="text.secondary"
          >
            Register as a User or Provider
          </Typography>

          {error && (
            <Alert severity="error" sx={{ mb: 2 }}>
              {error}
            </Alert>
          )}

          <form onSubmit={handleSubmit}>
            <TextField
              label="Name"
              name="name"
              fullWidth
              margin="normal"
              value={formData.name}
              onChange={handleChange}
              required
            />
            <TextField
              label="Email"
              name="email"
              type="email"
              fullWidth
              margin="normal"
              value={formData.email}
              onChange={handleChange}
              required
            />
            <TextField
              label="Password"
              name="password"
              type="password"
              fullWidth
              margin="normal"
              value={formData.password}
              onChange={handleChange}
              required
            />
            <TextField
              label="Confirm Password"
              name="confirmPassword"
              type="password"
              fullWidth
              margin="normal"
              value={formData.confirmPassword}
              onChange={handleChange}
              required
            />
            <TextField
              select
              label="Role"
              name="role"
              fullWidth
              margin="normal"
              value={formData.role}
              onChange={handleChange}
              required
            >
              <MenuItem value="CUSTOMER">Customer</MenuItem>
              <MenuItem value="CONTRACTOR">Contractor</MenuItem>
            </TextField>

            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{
                mt: 3,
                py: 1.3,
                fontWeight: "bold",
                borderRadius: 2,
                backgroundColor: "#2196f3",
                ":hover": {
                  backgroundColor: "#1976d2",
                },
              }}
            >
              Register
            </Button>
          </form>

          <Typography
            variant="body2"
            align="center"
            sx={{ mt: 3 }}
            color="text.secondary"
          >
            Already have an account?{" "}
            <Link
              component="button"
              variant="body2"
              onClick={() => navigate("/login")}
              sx={{ fontWeight: "bold", color: "#1976d2" }}
            >
              Login here
            </Link>
          </Typography>
        </CardContent>
      </Card>
    </Box>
  );
};

export default RegisterPage;

import React, { useState } from 'react';
import {
  Typography,
  TextField,
  Button,
  Link,
  Box,
  Card,
  CardContent,
  Alert,
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import api from '../api/axiosConfig';

function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);

    try {
      const response = await api.post('/auth/login', {
        email: email.toLowerCase(),
        password
      });

      const { token, role, userId } = response.data;

      if (!token || !role || !userId) {
        throw new Error('❌ Invalid response from server');
      }

      login({ token, role, email, userId });

      switch (role.toUpperCase()) {
        case 'CUSTOMER':
          navigate('/user/home');
          break;
        case 'CONTRACTOR':
          navigate('/provider/dashboard');
          break;
        case 'ADMIN':
          navigate('/admin/dashboard');
          break;
        default:
          setError('❌ Unknown role');
      }
    } catch (err) {
      console.error('Login error:', err);
      const errorMessage = err.response?.data || err.message || 'Login failed: Invalid credentials or server error';
      setError(errorMessage);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Box
      sx={{
        width: '100vw',
        height: '100vh',
        backgroundColor: '#000',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        p: 2,
      }}
    >
      <Card
        sx={{
          width: '100%',
          maxWidth: 420,
          borderRadius: 5,
          p: 3,
          bgcolor: '#121212',
          color: '#fff',
          boxShadow: '0 8px 24px rgba(0,0,0,0.6)',
        }}
      >
        <CardContent>
          <Typography
            variant="h4"
            align="center"
            gutterBottom
            fontWeight="bold"
            sx={{ color: '#f50057' }}
          >
            🔐 Login
          </Typography>

          <Typography variant="subtitle1" align="center" sx={{ mb: 3 }} color="#ccc">
            Enter your credentials to access the dashboard
          </Typography>

          {error && (
            <Alert severity="error" sx={{ mb: 2 }}>
              {error}
            </Alert>
          )}

          <form onSubmit={handleLogin}>
            <TextField
              label="Email"
              type="email"
              fullWidth
              required
              margin="normal"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              disabled={isLoading}
              InputLabelProps={{ style: { color: '#aaa' } }}
              InputProps={{ style: { color: '#fff' } }}
              sx={{
                '& .MuiOutlinedInput-root': {
                  '& fieldset': { borderColor: '#555' },
                  '&:hover fieldset': { borderColor: '#888' },
                  '&.Mui-focused fieldset': { borderColor: '#f50057' },
                },
              }}
            />

            <TextField
              label="Password"
              type="password"
              fullWidth
              required
              margin="normal"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              disabled={isLoading}
              InputLabelProps={{ style: { color: '#aaa' } }}
              InputProps={{ style: { color: '#fff' } }}
              sx={{
                '& .MuiOutlinedInput-root': {
                  '& fieldset': { borderColor: '#555' },
                  '&:hover fieldset': { borderColor: '#888' },
                  '&.Mui-focused fieldset': { borderColor: '#f50057' },
                },
              }}
            />

            <Box sx={{ textAlign: 'right', mt: 1 }}>
              <Link href="/reset-password" underline="hover" sx={{ color: '#90caf9' }}>
                Forgot password?
              </Link>
            </Box>

            <Button
              type="submit"
              fullWidth
              variant="contained"
              disabled={isLoading}
              sx={{
                mt: 3,
                py: 1.5,
                borderRadius: '30px',
                fontWeight: 'bold',
                fontSize: '1rem',
                background: 'linear-gradient(to right, #ff416c, #ff4b2b)',
                color: '#fff',
                '&:hover': {
                  background: 'linear-gradient(to right, #ff3a5a, #e03d20)',
                },
              }}
            >
              {isLoading ? '🔄 Logging in...' : '🚀 Login'}
            </Button>

            <Box sx={{ textAlign: 'center', mt: 3 }}>
              <Typography variant="body2" color="#aaa">
                Don&apos;t have an account?{' '}
                <Link href="/register" underline="hover" sx={{ color: '#90caf9' }}>
                  Register
                </Link>
              </Typography>
            </Box>
          </form>
        </CardContent>
      </Card>
    </Box>
  );
}

export default LoginPage;

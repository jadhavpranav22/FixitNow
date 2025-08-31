
import React, { useState } from 'react';
import {
  Typography,
  TextField,
  Button,
  Box,
  Card,
  CardContent,
  Alert,
} from '@mui/material';

function ResetPassword() {
  const [email, setEmail] = useState('');
  const [success, setSuccess] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    try {
      const response = await fetch('http://localhost:6969/auth/forgot-password', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: email.toLowerCase() }),
      });

      if (!response.ok) {
        const data = await response.json();
        throw new Error(data.message || 'Failed to send reset email');
      }

      setSuccess('✅ Password reset email sent! Please check your inbox.');
      setEmail('');
    } catch (err) {
      setError(err.message);
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
            variant="h5"
            align="center"
            gutterBottom
            fontWeight="bold"
            sx={{ color: '#f50057' }}
          >
            🔗 Reset Password
          </Typography>

          <Typography variant="body1" align="center" sx={{ mb: 3 }} color="#ccc">
            Enter your email to receive a password reset link
          </Typography>

          {error && (
            <Alert severity="error" sx={{ mb: 2 }}>
              {error}
            </Alert>
          )}
          {success && (
            <Alert severity="success" sx={{ mb: 2 }}>
              {success}
            </Alert>
          )}

          <form onSubmit={handleSubmit}>
            <TextField
              label="Email"
              type="email"
              fullWidth
              required
              margin="normal"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
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

            <Button
              type="submit"
              fullWidth
              variant="contained"
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
              Send Reset Link
            </Button>
          </form>
        </CardContent>
      </Card>
    </Box>
  );
}

export default ResetPassword;

// src/pages/Unauthorized.jsx
import React from 'react';
import { Typography, Box, Button } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const Unauthorized = () => {
  const navigate = useNavigate();

  return (
    <Box
      sx={{
        height: '100vh',
        bgcolor: '#1a1a1a',
        color: '#fff',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        flexDirection: 'column',
        textAlign: 'center',
        p: 4,
      }}
    >
      <Typography variant="h3" sx={{ mb: 2, color: '#f50057' }}>
        🚫 Unauthorized
      </Typography>
      <Typography variant="body1" sx={{ mb: 4 }}>
        You do not have access to this page.
      </Typography>
      <Button
        variant="contained"
        onClick={() => navigate('/')}
        sx={{
          background: 'linear-gradient(to right, #ff416c, #ff4b2b)',
          color: '#fff',
          borderRadius: 3,
          px: 4,
        }}
      >
        Go to Home
      </Button>
    </Box>
  );
};

export default Unauthorized;

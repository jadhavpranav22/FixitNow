import React from 'react';
import {
  Container,
  Typography,
  Box,
  useTheme,
  useMediaQuery,
} from '@mui/material';
import AboutImg from '../assets/aboutus.png';
import AboutTeam from '../assets/Usericon.jpg';
import TeamIcon from '../assets/Teamicon.jpg';

const AboutUs = () => {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('sm'));

  return (
    <>
      {/* Hero Section */}
      <Box
        sx={{
          height: { xs: 400, sm: 350 },
          backgroundImage: `url(${AboutImg})`,
          backgroundSize: 'cover',
          backgroundPosition: 'center',
          backgroundRepeat: 'no-repeat',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'flex-start',
          pl: 4,
          position: 'relative',
        }}
      >
        <Box
          sx={{
            position: 'absolute',
            top: 0,
            left: 0,
            width: '100%',
            height: '100%',
            bgcolor: 'rgba(0, 0, 0, 0.5)',
            zIndex: 1,
          }}
        />
        <Typography
          variant="h3"
          component="h1"
          sx={{
            fontSize: '75px',
            fontWeight: 800,
            color: '#fff',
            zIndex: 2,
            pb: 1,
            pt: 25,
          }}
        >
          About Us
        </Typography>
      </Box>

      {/* Main Section */}
      <Container maxWidth="lg" sx={{ py: 8 }}>
        {/* First Section */}
        <Box
          sx={{
            mt: 10,
            display: 'flex',
            flexDirection: { xs: 'column', md: 'row' },
            alignItems: 'flex-start',
            justifyContent: 'space-between',
            gap: 5,
          }}
        >
          {/* First Image */}
          <Box
            sx={{
              display: 'flex',
              flexDirection: 'column',
              gap: 3,
              flex: { md: 0.4 },
              width: '100%',
              maxWidth: 400,
            }}
          >
            <Box
              component="img"
              src={AboutTeam}
              alt="Team collaboration"
              sx={{
                width: '100%',
                height: 'auto',
                borderRadius: 4,
                boxShadow: '0 8px 30px rgba(139, 0, 139, 0.4)',
                objectFit: 'cover',
                transition: 'transform 0.4s ease-in-out, box-shadow 0.4s ease-in-out',
                '&:hover': {
                  transform: 'scale(0.95)',
                },
              }}
            />
          </Box>

          {/* First Text */}
          <Box sx={{ flex: { md: 0.6 }, maxWidth: 600 }}>
            <Typography
              variant="h5"
              sx={{ fontSize: '35px', fontWeight: 600 }}
              gutterBottom
            >
              Connecting Skilled Workers with Happy Users
            </Typography>
            <Typography
              variant="body1"
              color="text.secondary"
              sx={{ fontSize: '20px' }}
            >
              Our platform bridges the gap between experienced professionals and customers seeking reliable services. Whether you need repairs, installations, or maintenance,
              we ensure a smooth experience by pairing skilled workers with the right users.
              Our commitment is to quality, trust, and seamless communication every step of the way.
            </Typography>
          </Box>
        </Box>

        {/* Second Section */}
        <Box
          sx={{
            mt: 12,
            display: 'flex',
            flexDirection: { xs: 'column', md: 'row' },
            alignItems: 'flex-start',
            justifyContent: 'space-between',
            gap: 5,
          }}
        >
          {/* Second Text */}
          <Box sx={{ flex: { md: 0.6 }, maxWidth: 600 }}>
            <Typography
              variant="h5"
              sx={{ fontSize: '35px', fontWeight: 600 }}
              gutterBottom
            >
              Meet Our Professional Team
            </Typography>
            <Typography
              variant="body1"
              color="text.secondary"
              sx={{ fontSize: '20px' }}
            >
              Behind every service is a skilled, vetted professional committed to excellence.
              Our team is made up of individuals with real experience, real tools, and real dedication.
              From electricians and plumbers to handymen, we ensure every job is handled with care and confidence.
            </Typography>
          </Box>

          {/* Second Image */}
          <Box
            sx={{
              display: 'flex',
              flexDirection: 'column',
              gap: 3,
              flex: { md: 0.4 },
              width: '100%',
              maxWidth: 400,
            }}
          >
            <Box
              component="img"
              src={TeamIcon}
              alt="Our expert team"
              sx={{
                width: '100%',
                height: 'auto',
                borderRadius: 4,
                boxShadow: '0 8px 30px rgba(139, 0, 139, 0.4)',
                objectFit: 'cover',
                transition: 'transform 0.4s ease-in-out, box-shadow 0.4s ease-in-out',
                '&:hover': {
                  transform: 'scale(0.95)',
                },
              }}
            />
          </Box>
        </Box>

        {/* Mission and Vision Section */}
        <Box sx={{ mt: 12 }}>
          <Typography variant="h4" sx={{ fontWeight: 700, mb: 4 }}>
            Our Mission & Vision
          </Typography>

          <Box
            sx={{
              display: 'flex',
              flexDirection: { xs: 'column', md: 'row' },
              gap: 4,
            }}
          >
            {/* Mission */}
            <Box
              sx={{
                flex: 1,
                p: 3,
                borderRadius: 2,
                boxShadow: 3,
                transition: 'background-color 0.3s ease, transform 0.3s ease',
                '&:hover': {
                  bgcolor: '#8B008B',
                  transform: 'scale(1.02)',
                  '& h5, & p': {
                    color: '#fff',
                  },
                },
              }}
            >
              <Typography variant="h5" sx={{ fontWeight: 600, mb: 2 }}>
                Our Mission
              </Typography>
              <Typography color="text.secondary" sx={{ fontWeight: 500 }}>
                To connect skilled professionals with customers seeking reliable, quality home services. We ensure every interaction is rooted in trust, satisfaction, and excellence.
              </Typography>
            </Box>

            {/* Vision */}
            <Box
              sx={{
                flex: 1,
                p: 3,
                borderRadius: 2,
                boxShadow: 3,
                transition: 'background-color 0.3s ease, transform 0.3s ease',
                '&:hover': {
                  bgcolor: '#8B008B',
                  transform: 'scale(1.02)',
                  '& h5, & p': {
                    color: '#fff',
                  },
                },
              }}
            >
              <Typography variant="h5" sx={{ fontWeight: 600, mb: 2 }}>
                Our Vision
              </Typography>
              <Typography color="text.secondary" sx={{ fontWeight: 500 }}>
                To be the most trusted service marketplace that empowers local talent and transforms the way people experience home services.
              </Typography>
            </Box>
          </Box>
        </Box>
      </Container>
    </>
  );
};

export default AboutUs;
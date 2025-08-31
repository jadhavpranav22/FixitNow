const axios = require('axios');

const BASE_URL = 'http://localhost:6969';

async function testContractorDashboard() {
  try {
    console.log('🔍 Testing Contractor Dashboard API...');
    
    // First, let's check if there are any pending bookings
    console.log('\n1. Checking pending bookings...');
    const pendingResponse = await axios.get(`${BASE_URL}/bookings/pending`);
    console.log('Pending bookings:', pendingResponse.data.length);
    console.log('Sample pending booking:', pendingResponse.data[0]);
    
    // Test with a specific contractor ID (using contractor ID 3 from your database)
    const contractorId = 3; // cp@example.com contractor
    console.log(`\n2. Testing contractor endpoint for contractor ID: ${contractorId}`);
    
    const contractorResponse = await axios.get(`${BASE_URL}/bookings/contractor/${contractorId}`);
    console.log('Contractor bookings response:', contractorResponse.data.length);
    console.log('Sample contractor booking:', contractorResponse.data[0]);
    
    // Check all bookings
    console.log('\n3. Checking all bookings...');
    const allResponse = await axios.get(`${BASE_URL}/bookings`);
    console.log('Total bookings:', allResponse.data.length);
    
    // Show booking statuses
    const statusCounts = {};
    allResponse.data.forEach(booking => {
      statusCounts[booking.status] = (statusCounts[booking.status] || 0) + 1;
    });
    console.log('Booking status counts:', statusCounts);
    
  } catch (error) {
    console.error('❌ Error:', error.response?.data || error.message);
  }
}

testContractorDashboard();



import React, { useState } from "react";
import {
  TextField,
  Button,
  Typography,
  Alert,
  Paper,
} from "@mui/material";
import { useDispatch } from "react-redux";
import { clearCart } from "../redux/createSlice";
import api from "../api/axiosConfig";
import { useAuth } from "../contexts/AuthContext";

const BookingForm = ({ cartItems }) => {
  const { token, userId: customerId, role } = useAuth();
  const dispatch = useDispatch();
  const [address, setAddress] = useState("");
  const [scheduledDate, setScheduledDate] = useState("");
  const [notes, setNotes] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const [error, setError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccessMessage("");
    setIsSubmitting(true);

    if (!token) {
      setError("Authentication token is missing. Please log in.");
      setIsSubmitting(false);
      return;
    }

    if (!customerId) {
      setError("Missing customer ID.");
      setIsSubmitting(false);
      return;
    }

    if (role !== "CUSTOMER") {
      setError("Only customers can create bookings.");
      setIsSubmitting(false);
      return;
    }

    if (!address || !scheduledDate) {
      setError("Please fill in all required fields.");
      setIsSubmitting(false);
      return;
    }

    if (!cartItems || cartItems.length === 0) {
      setError("Your cart is empty.");
      setIsSubmitting(false);
      return;
    }

    try {
      const bookingPromises = cartItems.map((item) => {
        const params = new URLSearchParams({
          customerId: customerId.toString(),
          serviceId: item.serviceId.toString(),
        });

        if (item.subServiceId) {
          params.append("subServiceId", item.subServiceId.toString());
        }

        // Only send the booking details in the payload, not service IDs
        const payload = { 
          address, 
          scheduledDate, 
          notes
        };

        return api.post(`/bookings?${params.toString()}`, payload);
      });

      await Promise.all(bookingPromises);

      setSuccessMessage("✅ All booking requests sent successfully!");
      
      // Clear form and cart
      setAddress("");
      setScheduledDate("");
      setNotes("");
      dispatch(clearCart());
      
    } catch (err) {
      console.error("Booking error:", err);
      const errorMessage = err.response?.data || err.message || "❌ Failed to send booking requests.";
      setError(errorMessage);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Paper elevation={4} sx={{ p: 4, mt: 4, borderRadius: 3 }}>
      <Typography variant="h6" gutterBottom>
        📋 Request Service Booking
      </Typography>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}
      {successMessage && (
        <Alert severity="success" sx={{ mb: 2 }}>
          {successMessage}
        </Alert>
      )}

      <form onSubmit={handleSubmit}>
        <TextField
          label="Your Address"
          fullWidth
          required
          margin="normal"
          value={address}
          onChange={(e) => setAddress(e.target.value)}
          disabled={isSubmitting}
        />

        <TextField
          label="Scheduled Date"
          type="date"
          fullWidth
          required
          margin="normal"
          InputLabelProps={{ shrink: true }}
          value={scheduledDate}
          onChange={(e) => setScheduledDate(e.target.value)}
          disabled={isSubmitting}
        />

        <TextField
          label="Additional Notes"
          fullWidth
          multiline
          rows={3}
          margin="normal"
          value={notes}
          onChange={(e) => setNotes(e.target.value)}
          disabled={isSubmitting}
        />

        <Button
          type="submit"
          variant="contained"
          color="primary"
          sx={{ mt: 3, borderRadius: 2 }}
          disabled={isSubmitting}
        >
          {isSubmitting ? "Submitting..." : "Submit Booking"}
        </Button>
      </form>
    </Paper>
  );
};

export default BookingForm;

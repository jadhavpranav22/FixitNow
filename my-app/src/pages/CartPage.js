import React from "react";
import { useSelector, useDispatch } from "react-redux";
import { removeFromCart, clearCart } from "../redux/createSlice";
import { 
  Container, 
  Typography, 
  List, 
  ListItem, 
  ListItemText, 
  ListItemSecondaryAction, 
  IconButton, 
  Paper, 
  Alert,
  Box,
  Button
} from "@mui/material";
import DeleteIcon from '@mui/icons-material/Delete';
import { useAuth } from "../contexts/AuthContext";
import BookingForm from "./BookingForm";

const CartPage = () => {
  const cartItems = useSelector((state) => state.cart.items);
  const dispatch = useDispatch();
  const { isLoggedIn, role } = useAuth();

  const handleRemoveFromCart = (index) => {
    dispatch(removeFromCart(index));
  };

  const handleClearCart = () => {
    dispatch(clearCart());
  };

  if (!isLoggedIn) {
    return (
      <Container maxWidth="md" sx={{ mt: 10, mb: 4 }}>
        <Alert severity="warning">
          Please log in to view your cart and make bookings.
        </Alert>
      </Container>
    );
  }

  if (role !== "CUSTOMER") {
    return (
      <Container maxWidth="md" sx={{ mt: 10, mb: 4 }}>
        <Alert severity="error">
          Only customers can make bookings. Please log in with a customer account.
        </Alert>
      </Container>
    );
  }

  return (
    <Container maxWidth="md" sx={{ mt: 10, mb: 4 }}>
      <Typography variant="h4" gutterBottom>
        🛒 Your Cart
      </Typography>

      {cartItems.length === 0 ? (
        <Paper elevation={2} sx={{ p: 3, textAlign: 'center' }}>
          <Typography variant="h6" color="text.secondary">
            Your cart is empty.
          </Typography>
          <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
            Add some services to get started!
          </Typography>
        </Paper>
      ) : (
        <>
          <Paper elevation={2} sx={{ p: 3, mb: 3 }}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
              <Typography variant="h6">
                Cart Items ({cartItems.length})
              </Typography>
              <Button 
                variant="outlined" 
                color="error" 
                onClick={handleClearCart}
                size="small"
              >
                Clear Cart
              </Button>
            </Box>
            
            <List>
              {cartItems.map((item, index) => (
                <ListItem key={index} divider>
                  <ListItemText
                    primary={item.serviceName}
                    secondary={
                      <Box>
                        <Typography variant="body2" color="text.secondary">
                          Service ID: {item.serviceId}
                          {item.subServiceId && ` | Sub-Service ID: ${item.subServiceId}`}
                        </Typography>
                        <Typography variant="body2" color="primary" fontWeight="bold">
                          Price: ${item.price || 'Contact for pricing'}
                        </Typography>
                      </Box>
                    }
                  />
                  <ListItemSecondaryAction>
                    <IconButton 
                      edge="end" 
                      aria-label="delete"
                      onClick={() => handleRemoveFromCart(index)}
                      color="error"
                    >
                      <DeleteIcon />
                    </IconButton>
                  </ListItemSecondaryAction>
                </ListItem>
              ))}
            </List>
          </Paper>

          <BookingForm cartItems={cartItems} />
        </>
      )}
    </Container>
  );
};

export default CartPage;

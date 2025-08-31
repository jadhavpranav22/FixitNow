// components/Modal.js
import React from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { addToCart } from "../redux/createSlice";
import "../style/Modal.css";
import { useAuth } from "../contexts/AuthContext";

const Modal = ({ service, onClose }) => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { isLoggedIn } = useAuth(); // ✅ use context to check login status

  if (!service) return null;

  const handleAddToCart = (subService) => {
    if (!isLoggedIn) {
      const confirmLogin = window.confirm(
        "⚠️ You must be logged in to book a service. Do you want to login now?"
      );
      if (confirmLogin) {
        navigate("/login");
      }
      return;
    }

    // Add service ID and sub-service ID to the cart item
    const cartItem = {
      serviceId: service.id,
      serviceName: service.name,
      subServiceId: subService.id,
      name: subService.name,
      price: subService.price
    };

    dispatch(addToCart(cartItem));
    
    // Show success message
    alert(`✅ Added "${subService.name}" to cart!`);
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <button className="modal-close" onClick={onClose}>&times;</button>
        <h2>{service.name} - Sub Services</h2>
        <ul>
          {service.subServices.map((sub, index) => (
            <li
              key={index}
              className="modal-sub-service d-flex justify-content-between align-items-center"
            >
              <span>{sub.name} - {sub.price}</span>
              <button onClick={() => handleAddToCart(sub)}>
                Add To Cart
              </button>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default Modal;


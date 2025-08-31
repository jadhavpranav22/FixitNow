import React from 'react';
import '../style/ServicesCard.css';

export default function ServiceCard({ service, onBookNow }) {
  return (
    <div className="service-card">
      <img src={service.icon} alt={service.name} />
      <h3>{service.name}</h3>
      <p>{service.description}</p>
      <button onClick={onBookNow}>Book Now</button>
    </div>
  );
}

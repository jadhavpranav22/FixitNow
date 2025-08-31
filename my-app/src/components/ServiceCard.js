import React from 'react';
import '../style/ServiceCard.css';

function ServiceCard({ service }) {
  return (
    <div className="service-card">
      <div className="service-icon">
        <img src={service.icon} alt={service.name} />
      </div>
      <div className="service-content">
        <h3>{service.name}</h3>
        <p>{service.description || 'Professional service at your doorstep'}</p>
        <button className="service-btn">Book Now</button>
      </div>
    </div>
  );
}

export default ServiceCard;

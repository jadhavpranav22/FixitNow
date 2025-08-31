import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import ServiceCard from './ServicesCard';
import Modal from './Modal';

import "../style/Home.css";
import servicesData from '../data/Services'; 


import PlumbingIcon from '../assets/plumbing.png';
import ElectricalIcon from '../assets/electrical.png';
import CarpentryIcon from '../assets/Carpentary.png';
import PaintingIcon from '../assets/Painting.png';
import MasonryIcon from '../assets/masonry.png';

import Services1 from "../assets/services1.jpg";
import Services2 from "../assets/services2.jpg";
import Services3 from "../assets/services3.jpg";
import Services4 from "../assets/services4.jpg";

// ✅ Map service name to icon
const serviceIcons = {
  Plumbing: PlumbingIcon,
  Electrical: ElectricalIcon,
  Carpentry: CarpentryIcon,
  Painting: PaintingIcon,
  Masonry: MasonryIcon,
};

const carouselImages = [Services1, Services2, Services3, Services4];

export default function Home() {
  const [selectedService, setSelectedService] = useState(null);

  const handleBookNowClick = (serviceName) => {
    const service = servicesData[serviceName];
    if (service) {
      setSelectedService(service);
    } else {
      console.error(`Service "${serviceName}" not found`);
    }
  };

  const handleCloseModal = () => setSelectedService(null);

  const handleSubServiceBooking = (subServiceName) => {
    alert(`Booked: ${subServiceName}`);
  };

  return (
    <div className="home">
      {/* ✅ Hero Carousel Section */}
      <section className="hero">
        <div className="carousel-container">
          {carouselImages.map((img, idx) => (
            <img key={idx} src={img} alt={`carousel-${idx}`} className="carousel-image" />
          ))}
        </div>

        <div className="hero-overlay">
          <div className="hero-text-box">
            <h1>Professional Services at Your Doorstep</h1>
            <p>Get reliable, professional services for all your home and office needs. Book instantly and get service within hours.</p>
            <div className="hero-buttons">
              <Link to="/services" className="btn btn-primary">Explore Services</Link>
              <Link to="/contact" className="btn btn-secondary">Contact Us</Link>
            </div>
          </div>
        </div>
      </section>

      {/* ✅ Features */}
      <section className="features">
        <div className="container">
          <h2>Why FixItNow?</h2>
          <div className="features-grid">
            {[
              { icon: '⚡', title: 'Quick Service', desc: '2-4 hour service window' },
              { icon: '👨‍🔧', title: 'Expert Technicians', desc: 'Trained & Verified' },
              { icon: '💰', title: 'Fair Pricing', desc: 'Transparent pricing guaranteed' },
              { icon: '🛡️', title: 'Work Guarantee', desc: '100% Satisfaction assured' }
            ].map((item, idx) => (
              <div className="feature" key={idx}>
                <div className="feature-icon">{item.icon}</div>
                <h3>{item.title}</h3>
                <p>{item.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ✅ Services */}
      <section className="services">
        <div className="container">
          <h2>Popular Services</h2>
          <div className="service-grid">
            {Object.entries(servicesData).map(([key, service]) => (
              <ServiceCard
                key={key}
                service={{ ...service, icon: serviceIcons[key] }}
                onBookNow={() => handleBookNowClick(key)}
              />
            ))}
          </div>
          <div className="services-cta">
            <Link to="/services" className="btn btn-primary">View All Services</Link>
          </div>
        </div>
      </section>

      {/* ✅ CTA */}
      <section className="cta">
        <div className="container">
          <h2>Need Help Now?</h2>
          <p>Book a service and get it done the same day!</p>
          <Link to="/contact" className="btn btn-primary">Get in touch</Link>
        </div>
      </section>

      {/* ✅ Modal */}
      <Modal
        service={selectedService}
        onClose={handleCloseModal}
        onBook={handleSubServiceBooking}
      />
    </div>
  );
}

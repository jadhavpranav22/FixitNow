import React, { useState } from "react";
import ServiceSearch from "./ServiceSearch";
import Modal from "./Modal";
import servicesData from "../data/Services";
import { FaWrench, FaBolt, FaHammer, FaBuilding, FaPaintBrush } from 'react-icons/fa';

const serviceIcons = {
  Plumbing: FaWrench,
  Electrical: FaBolt,
  Carpentry: FaHammer,
  Masonry: FaBuilding,
  Painting: FaPaintBrush,
};

const ServicesPage = () => {
  const [selectedService, setSelectedService] = useState(null);
  const [searchTerm, setSearchTerm] = useState("");

  const handleServiceSelect = (name) => {
    const matchedService = servicesData[name]; // Access by key directly
    if (matchedService) {
      setSelectedService(matchedService);
    }
  };

  const handleClose = () => setSelectedService(null);

  const handleBook = (subServiceName) => {
    alert(`Added "${subServiceName}" to cart!`);
  };

  const filteredServices = Object.entries(servicesData).filter(([name, service]) =>
    name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    service.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="container py-5" style={{ marginTop: "80px" }}>
      <div className="text-center mb-5">
        <h1 className="display-4 fw-bold text-primary mb-3">Our Services</h1>
        <p className="lead text-muted">Professional household repair services at your doorstep</p>
      </div>

      {/* Search Bar */}
      <div className="row justify-content-center mb-5">
        <div className="col-md-6">
          <div className="input-group">
            <input
              type="text"
              className="form-control form-control-lg"
              placeholder="Search services..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
            <button className="btn btn-primary" type="button">
              <i className="fas fa-search"></i>
            </button>
          </div>
        </div>
      </div>

      {/* Services Grid */}
      <div className="row g-4">
        {filteredServices.map(([key, service]) => {
          const IconComponent = serviceIcons[key] || FaWrench;
          return (
            <div key={service.id} className="col-12 col-md-6 col-lg-4">
              <div 
                className="card h-100 shadow-sm border-0 service-card"
                style={{ 
                  cursor: 'pointer',
                  transition: 'all 0.3s ease',
                  borderRadius: '15px'
                }}
                onClick={() => handleServiceSelect(key)}
                onMouseEnter={(e) => {
                  e.currentTarget.style.transform = 'translateY(-5px)';
                  e.currentTarget.style.boxShadow = '0 10px 25px rgba(0,0,0,0.15)';
                }}
                onMouseLeave={(e) => {
                  e.currentTarget.style.transform = 'translateY(0)';
                  e.currentTarget.style.boxShadow = '0 4px 6px rgba(0,0,0,0.1)';
                }}
              >
                <div className="card-body text-center p-4">
                  <div className="mb-3">
                    <IconComponent 
                      size={50} 
                      className="text-primary"
                    />
                  </div>
                  <h5 className="card-title fw-bold mb-3">{service.name}</h5>
                  <p className="card-text text-muted mb-3">
                    Professional {service.name.toLowerCase()} services with expert technicians
                  </p>
                  <div className="mb-3">
                    <small className="text-muted">
                      {service.subServices.length} sub-services available
                    </small>
                  </div>
                  <button 
                    className="btn btn-primary btn-sm"
                    onClick={(e) => {
                      e.stopPropagation();
                      handleServiceSelect(key);
                    }}
                  >
                    View Services
                  </button>
                </div>
              </div>
            </div>
          );
        })}
      </div>

    
      <Modal
        service={selectedService}
        onClose={handleClose}
        onBook={handleBook}
      />
    </div>
  );
};

export default ServicesPage;

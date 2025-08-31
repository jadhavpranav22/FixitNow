// components/ServiceSearch.js
import React, { useState } from "react";
import servicesData from "../data/Services";// same data source

const ServiceSearch = ({ onServiceSelect }) => {
  const [query, setQuery] = useState("");
  const [filtered, setFiltered] = useState([]);

  const handleChange = (e) => {
    const val = e.target.value;
    setQuery(val);
    if (val.trim() === "") {
      setFiltered([]);
      return;
    }
    const matches = Object.keys(servicesData).filter((key) =>
      key.toLowerCase().includes(val.toLowerCase())
    );
    setFiltered(matches);
  };

  const handleSelect = (name) => {
    setQuery(name);
    setFiltered([]);
    onServiceSelect(name);
  };

  return (
    <div className="position-relative">
      <input
        type="text"
        className="form-control"
        placeholder="Search service..."
        value={query}
        onChange={handleChange}
      />
      {filtered.length > 0 && (
        <ul className="list-group position-absolute w-100" style={{ zIndex: 1000 }}>
          {filtered.map((name, index) => (
            <li
              key={index}
              className="list-group-item list-group-item-action"
              onClick={() => handleSelect(name)}
              style={{ cursor: "pointer" }}
            >
              {name}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default ServiceSearch;

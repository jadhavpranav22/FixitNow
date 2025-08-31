const servicesData = {
  Plumbing: {
    id: 1,
    name: "Plumbing",
    subServices: [
      { id: 101, name: "Pipe Fix", price: "₹350" },
      { id: 102, name: "Leak Repair", price: "₹425" },
      { id: 103, name: "Tap Installation", price: "₹500" },
      { id: 104, name: "Drain Cleaning", price: "₹575" },
      { id: 105, name: "Bathroom Fitting Repair", price: "₹650" },
    ],
  },
  Electrical: {
    id: 2,
    name: "Electrical",
    subServices: [
      { id: 106, name: "Wiring", price: "₹1000" },
      { id: 107, name: "Light Fix", price: "₹275" },
      { id: 108, name: "Switch Replacement", price: "₹210" },
      { id: 109, name: "Fan Installation", price: "₹525" },
      { id: 110, name: "Inverter Setup", price: "₹1750" },
    ],
  },
  Carpentry: {
    id: 3,
    name: "Carpentry",
    subServices: [
      { id: 111, name: "Furniture Repair", price: "₹900" },
      { id: 112, name: "Door Fix", price: "₹475" },
      { id: 113, name: "Wooden Shelf Installation", price: "₹1100" },
      { id: 114, name: "Bed Assembly", price: "₹1300" },
      { id: 115, name: "Window Repair", price: "₹600" },
    ],
  },
  Masonry: {
    id: 4,
    name: "Masonry",
    subServices: [
      { id: 116, name: "Wall Repair", price: "₹1750" },
      { id: 117, name: "Floor Tiling", price: "₹2250" },
      { id: 118, name: "Cement Plaster", price: "₹2000" },
      { id: 119, name: "Brick Work", price: "₹3000" },
      { id: 120, name: "Granite Laying", price: "₹3500" },
    ],
  },
  Painting: {
    id: 5,
    name: "Painting",
    subServices: [
      { id: 121, name: "Interior Painting", price: "₹4500" },
      { id: 122, name: "Exterior Painting", price: "₹7500" },
      { id: 123, name: "Wall Texturing", price: "₹5250" },
      { id: 124, name: "Primer Coating", price: "₹1750" },
      { id: 125, name: "Wood Polish", price: "₹3000" },
    ],
  },
};

export default servicesData;

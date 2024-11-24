import React from 'react';
import Navbar from './components/Navbar';
import Home from './components/Home';
import Logistics from './components/Logistics';
import Destinations from './components/Destinations';
import Dining from './components/Dining';
import Accommodations from './components/Accommodations';
import Transportation from './components/Transportation';
import Travel from './components/Travel';
import Sharing from './components/Sharing';
import Diagrams from './components/Diagram';


function App() {
  return (
    <div>
      <Navbar />
      <Home />
      <Logistics />
      <Destinations />
      <Dining />
      <Accommodations />
      <Transportation />
      <Travel />
      <Sharing />
      <Diagrams />
    </div>
  );
}

export default App;

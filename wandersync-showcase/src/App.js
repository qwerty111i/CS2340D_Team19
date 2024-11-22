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
    </div>
  );
}

export default App;

import React from 'react';
import './Diagram.css';

import Diagram1 from './images/SignInDiagram.png';
import Diagram2 from './images/SignUpDiagram.png';
import Diagram3 from './images/DCD.png';

const Diagram = () => {
  return (
    <div className="diagram" id='diagrams'>
      <div className="info-box">
        <div className="content-box">
          <h1 className="title">Diagrams</h1>
          <p className="description">Below are several diagrams that were instrumental throughout the application development process, providing valuable insights into how the app would be structured. The first two diagrams are Sequence Diagrams, which illustrate the processes of signing in and signing up. The final diagram is a Design Class Diagram (DCD), which offers a high-level view of the application.</p>
          <br />
          <p className="description">Earlier in the development process, diagrams such as the use-case models proved particularly useful.  They allowed for assessing the key features that would need to be implemented and better understand how the various parts of the app would interact.  These diagrams continued to be edited as the development cycle continued.</p>
          <br />
          <p className="description">The Design Class Diagram (DCD) outlines the relationships between components, their respective responsibilities, and the flow of data within the system.  By employing UML (Unified Modeling Language), we were able to visualize the architecture early on, facilitating critical design decisions and ensuring a cohesive development process.</p>
        </div>
        <div className="image-box">
          <div className="image">
            <img className="image-1" src={Diagram1} alt="Sign In Diagram" />
          </div>
          <div className="image">
            <img className="image-2" src={Diagram2} alt="Sign Up Diagram" />
          </div>
          <div className="image">
            <img className="image-3" src={Diagram3} alt="DCD" />
          </div>
        </div>
      </div>
    </div>
  )
}

export default Diagram
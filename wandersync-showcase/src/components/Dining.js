import React from 'react';
import './Dining.css';

import DiningVid from './videos/Dining.mp4';
import DiningScrollVid from './videos/DiningScroll.mp4';

const VideoRender = ({ videoSrc, altText }) => {
  return (
    <video
      src={videoSrc}
      alt={altText}
      autoPlay
      loop
      muted
      playsInline
      className="vid"
    />
  );
};

export default function Dining() {
  return (
    <div className="dining">
      <div className="display-box">
        <div className="dining-video scroll">
          <VideoRender
            videoSrc={DiningScrollVid}
            altText="Dining Scroll Video"
          />
        </div>
        <div className="dining-video screen">
          <VideoRender
            videoSrc={DiningVid}
            altText="Dining Screen Video"
          />
        </div>
      </div>
      <div className="info-box">
        <div className="title-box">
          <h1 className="title">Dining</h1>
        </div>
        <div className="content-box">
          <p className="content">The Dining page allows users to add restaurant reservations for their upcoming trips.  Users can specify the exact time of each reservation, with the details displayed on visually appealing cards.</p>
          <br />
          <p className="content">Reservations can be sorted by name or date, offering users the flexibility to show upcoming reservations first.  This sorting functionality was implemented using the Strategy design pattern, resulting in a more efficient and maintainable codebase.</p>
          <br />
          <p className="content">All reservation details are stored in the database, with any changes updated in real-time.</p>
        </div>
      </div>
    </div>
  )
}

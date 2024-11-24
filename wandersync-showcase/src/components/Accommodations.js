import React from 'react';
import './Accommodations.css';

import AccommodationsVid from './videos/Accommodation.mp4';
import AccommodationsScrollVid from './videos/AccommodationScroll.mp4';

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

export default function Accommodations() {
  return (
    <div className="accommodations">
      <div className="info-box">
        <div className="title-box">
          <h1 className="title">Accommodations</h1>
        </div>
        <div className="content-box">
          <p className="content">The Accommodations page allows users to add accommodation details for their trips, including check-in and check-out dates, the number of rooms, and the type of room.  When the check-in / check-out dates pass, the text color changes to a bright red as an indication.</p>
          <br />
          <p className="content">This screen also utilizes the Strategy design pattern for sorting, allowing users to quickly identify their latest reserved accommodations at a glance.</p>
          <br />
          <p className="content">As with the other details, all accommodation details are stored in the database under the Trips node, making it easy for users to share their accommodation information when sharing a trip.</p>
        </div>
      </div>
      <div className="display-box">
        <div className="accommodations-video scroll">
          <VideoRender
            videoSrc={AccommodationsScrollVid}
            altText="Accommodation Scroll Video"
          />
        </div>
        <div className="accommodations-video screen">
          <VideoRender
            videoSrc={AccommodationsVid}
            altText="Accommodation Screen Video"
          />
        </div>
      </div>
    </div>
  )
}

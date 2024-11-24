import React from 'react';
import './Destinations.css';

import DestinationVid from './videos/Destination.mp4';
import DestinationScrollVid from './videos/DestinationScroll.mp4';

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

export default function Destinations() {
  return (
    <div className="destinations">
      <div className="info-box">
        <div className="title-box">
          <h1 className="title">Destinations</h1>
        </div>
        <div className="content-box">
          <p className="content">The Destinations page lets users add places they plan to visit, specifying start and end dates and selecting the trip under which the destination will be logged.</p>
          <br />
          <p className="content">Destinations are displayed using Android's RecycleView component, enabling scrollable display cards.  Each destination card adapts its view based on specific properties (such as whether it is shared, expired, etc.).  Changes in the database are reflected live.</p>
          <br />
          <p className="content">Since Destinations are associated with specific trips, sharing a trip allows other users to view all destinations within that trip.  This approach simplifies the management of travel locations in each trip.</p>
        </div>
      </div>
      <div className="display-box">
        <div className="destinations-video screen">
          <VideoRender
            videoSrc={DestinationVid}
            altText="Destination Screen Video"
          />
        </div>
        <div className="destinations-video scroll">
          <VideoRender
            videoSrc={DestinationScrollVid}
            altText="Destination Scroll Video"
          />
        </div>
      </div>
    </div>
  )
}

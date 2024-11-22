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
          <p className="content">WanderSync is a travel management application designed to allow users to plan and share trips.  Users can collaborate on creating in itinerary with realtime updates.</p>
          <br />
          <p className="content">The splash screen was designed and animated in Lottie.  The Sign-Up and Sign-In pages are integrated with Firebase to securely encrypt and store credentials.</p>
          <br />
          <p className="content">This application was developed using Java and Android Studio.  The database used to store user details is Firebase.</p>
        </div>
      </div>
      <div className="display-box">
        <div className="destinations-video">
          <VideoRender
            videoSrc={DestinationVid}
            altText="Destination Screen Video"
          />
        </div>
        <div className="destinations-video">
          <VideoRender
            videoSrc={DestinationScrollVid}
            altText="Destination Scroll Video"
          />
        </div>
      </div>
    </div>
  )
}

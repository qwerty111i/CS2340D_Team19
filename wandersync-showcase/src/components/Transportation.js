import React from 'react';
import './Transportation.css';

import TransportationVid from './videos/Transportation.mp4';
import TransportationScrollVid from './videos/TransportationScroll.mp4';

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

export default function Transportation() {
  return (
    <div className="transportation">
      <div className="display-box">
        <div className="transportation-video">
          <VideoRender
            videoSrc={TransportationVid}
            altText="Transportation Screen Video"
          />
        </div>
        <div className="transportation-video">
          <VideoRender
            videoSrc={TransportationScrollVid}
            altText="Transportation Scroll Video"
          />
        </div>
      </div>
      <div className="info-box">
        <div className="title-box">
          <h1 className="title">Transportation</h1>
        </div>
        <div className="content-box">
          <p className="content">WanderSync is a travel management application designed to allow users to plan and share trips.  Users can collaborate on creating in itinerary with realtime updates.</p>
          <br />
          <p className="content">The splash screen was designed and animated in Lottie.  The Sign-Up and Sign-In pages are integrated with Firebase to securely encrypt and store credentials.</p>
          <br />
          <p className="content">This application was developed using Java and Android Studio.  The database used to store user details is Firebase.</p>
        </div>
      </div>
    </div>
  )
}

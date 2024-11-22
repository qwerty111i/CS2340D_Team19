import React from 'react';
import './Logistics.css';

import LogisticsVid from './videos/Logistics.mp4';
import NotesVid from './videos/Notes.mp4';

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

export default function Logistics() {
  return (
    <div className="logistics" id='screens'>
      <div className="display-box">
        <div className="logistics-video">
          <VideoRender
            videoSrc={LogisticsVid}
            altText="Logistics Screen Video"
          />
        </div>
        <div className="logistics-video">
          <VideoRender
            videoSrc={NotesVid}
            altText="Adding Notes Video"
          />
        </div>
      </div>
      <div className="info-box">
        <div className="title-box">
          <h1 className="title">Logistics</h1>
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

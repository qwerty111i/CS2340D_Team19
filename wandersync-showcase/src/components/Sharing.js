import React from 'react';
import './Sharing.css';

import SharingVid from './videos/Sharing.mp4';
import SharingViewVid from './videos/SharingView.mp4';

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

const Sharing = () => {
  return (
    <div className="sharing" id='features'>
      <div className="display-box">
        <div className="sharing-video">
          <VideoRender
            videoSrc={SharingVid}
            altText="Sharing Video"
          />
        </div>
        <div className="sharing-video">
          <VideoRender
            videoSrc={SharingViewVid}
            altText="Sharing View Video"
          />
        </div>
      </div>
      <div className="info-box">
        <div className="title-box">
          <h1 className="title">Sharing</h1>
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

export default Sharing
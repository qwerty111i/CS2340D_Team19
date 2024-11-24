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
        <div className="sharing-video screen">
          <VideoRender
            videoSrc={SharingVid}
            altText="Sharing Video"
          />
        </div>
        <div className="sharing-video view">
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
          <p className="content">The sharing feature allows users to easily share their trip details with other users, facilitating collaboration.  When a user shares a trip, the recipient can view all associated information, including destinations, reservations, accommodations, and transportation arrangements.</p>
          <br />
          <p className="content">Any changes made by the invited users (such as adding new destinations, reservations, etc.) are reflected in real-time on the original trip creator's screen, ensuring a seamless collaborative experience.</p>
          <br />
          <p className="content">Invited users have the flexibility to choose which trip they want to contribute to.  If the invited user has their own trip, they can add accommodations or other details to it, keeping their changes separate from the shared trip.  However, if they choose to update the shared trip, those changes will be visible to all invited users, ensuring that everyone stays on the same page.</p>
        </div>
      </div>
    </div>
  )
}

export default Sharing
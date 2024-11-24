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
        <div className="transportation-video screen">
          <VideoRender
            videoSrc={TransportationVid}
            altText="Transportation Screen Video"
          />
        </div>
        <div className="transportation-video scroll">
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
          <p className="content">The Transportation page allows users to specify their travel arrangements between destinations.  Users can input the mode of transportation, as well as the departure time.</p>
          <br />
          <p className="content">This page is extremely convenient as it helps users plan their routes in advance, reducing stress and anxiety during a trip.  By thinking about the logistics beforehand, users can ensure smoother travel experiences.</p>
          <br />
          <p className="content">The application's modular database setup and adherence to design patterns makes it easy to integrate new screens like this one.  These design choices streamline the development process, allowing for faster implementation with minimal code duplication, which makes it easier to maintain and expand the app in the future.</p>
        </div>
      </div>
    </div>
  )
}

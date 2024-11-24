import React from 'react';
import './Travel.css';

import TravelVid from './videos/Travel.mp4';
import TravelScrollVid from './videos/TravelScroll.mp4';

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

export default function Travel() {
  return (
    <div className="travel">
      <div className="info-box">
        <div className="title-box">
          <h1 className="title">Travel</h1>
        </div>
        <div className="content-box">
          <p className="content">The Travel page serves as a community space where users can share their travel experiences.  Users are able to create new posts detailing their destinations, restaurants, and ratings. These posts are visible to all other users.</p>
          <br />
          <p className="content">This page is a valuable addition to the app, as it helps users make more informed decisions about their own travel plans.  By browsing and interacting with posts from other members of the community, users can gain inspiration, discover hidden gems, and learn about exciting places to visit.</p>
          <br />
          <p className="content">To support this screen, a new Community Post section was added to the database, allowing all users to access and view shared posts.  The Observer design pattern was employed to update all users when a new post is created.  These changes ensured that the database remained organized, and opens the door for future features.</p>
        </div>
      </div>
      <div className="display-box">
        <div className="travel-video scroll">
          <VideoRender
            videoSrc={TravelScrollVid}
            altText="Travel Scroll Video"
          />
        </div>
        <div className="travel-video screen">
          <VideoRender
            videoSrc={TravelVid}
            altText="Travel Screen Video"
          />
        </div>
      </div>
    </div>
  )
}

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
        <div className="logistics-video screen">
          <VideoRender
            videoSrc={LogisticsVid}
            altText="Logistics Screen Video"
          />
        </div>
        <div className="logistics-video notes">
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
          <p className="content">The Logistics page allows users to invite others, add trip notes, and create new trips.  Trip contributors are displayed in a list for easy reference.  Users can also track the allotted versus planned time for each trip.</p>
          <br />
          <p className="content">The notes feature was implemented by configuring the database to associate individual notes with each trip.  When a trip is shared, the notes are also shared and viewable by other users.</p>
          <br />
          <p className="content">To track the Allotted vs. Planned time, the app integrates MPAndroidChart, a powerful Android library designed to display visually appealing charts.  This integration provides users with a clear, visual representation of their trip's time allocation.</p>
        </div>
      </div>
    </div>
  )
}

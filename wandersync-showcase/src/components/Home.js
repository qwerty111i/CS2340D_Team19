import React from 'react';
import './Home.css';

import SplashVid from './videos/Splash.mp4';
import SignUpVid from './videos/SignUp.mp4';

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

export default function Home() {
  return (
    <div className="home" id='home'>
      <div className="info-box">
        <div className="title-box">
          <h1 className="title">Welcome to WanderSync</h1>
        </div>
        <div className="content-box">
          <p className="content">WanderSync is a travel management application designed to help users plan and share trips.  It allows users to collaborate on creating and organizing itineraries for their journeys.</p>
          <br />
          <p className="content">This application was developed using Android Studio, and user details are stored on Firebase.  The app follows the MVVM (Model - View - ViewModel) architecture and employs the Singleton design pattern for the database.  The development process followed the Agile methodology, with the project being divided into 4 sprints.</p>
          <br />
          <p className="content">The splash screen, designed using LottieFiles, smoothly transitions users to the Login / Sign-Up pages.  When users sign up, their credentials are securely encrypted and stored in Firebase.</p>
        </div>
        <div className="project-box">
          <button className='project-link'
                  onClick={() => window.location.href = 'https://github.com/qwerty111i/CS2340D_Team19'}>
                    View Project
          </button>
        </div>
      </div>
      <div className="display-box">
        <div className="splash-video">
          <VideoRender
            videoSrc={SplashVid}
            altText="Splash Screen Animation"
          />
        </div>
        <div className="sign-up-video">
          <VideoRender
            videoSrc={SignUpVid}
            altText="Sign Up / Sign In Video"
          />
        </div>
      </div>
    </div>
  )
}

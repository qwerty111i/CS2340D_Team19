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
          <p className="content">WanderSync is a travel management application designed to allow users to plan and share trips.  Users can collaborate on creating in itinerary with realtime updates.</p>
          <br />
          <p className="content">The splash screen was designed and animated in Lottie.  The Sign-Up and Sign-In pages are integrated with Firebase to securely encrypt and store credentials.</p>
          <br />
          <p className="content">This application was developed using Java and Android Studio.  The database used to store user details is Firebase.</p>
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

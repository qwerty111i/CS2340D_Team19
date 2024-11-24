import React, {useState} from 'react'
import { IoClose } from 'react-icons/io5'
import { RxHamburgerMenu } from "react-icons/rx";

import './Navbar.css'

const Navbar = () => {
const [isOpen, changeState] = useState(false);
const clickMenu = () => changeState(!isOpen);
const closeMenu = () => changeState(false);

  return (
    <div className='header'>
        <nav className='navbar'>
            <h1 className='title'>WanderSync</h1>
            <div className='hamburger' onClick={clickMenu}>
                {isOpen ? (<IoClose size={30} style={{color: '#000000'}}/>) 
                : (<RxHamburgerMenu size={30} style={{color: '#000000'}}/>)}
            </div>
            <ul className={isOpen ? "nav-menu active" : "nav-menu"}>
                <li className='nav-item'>
                    <a href='#home' onClick={closeMenu}>Home</a>
                </li>
                <li className='nav-item'>
                    <a href='#screens' onClick={closeMenu}>Screens</a>
                </li>
                <li className='nav-item'>
                    <a href='#features' onClick={closeMenu}>Sharing</a>
                </li>
                <li className='nav-item'>
                    <a href='#diagrams' onClick={closeMenu}>Diagrams</a>
                </li>
            </ul>
        </nav>
    </div>
  )
}

export default Navbar
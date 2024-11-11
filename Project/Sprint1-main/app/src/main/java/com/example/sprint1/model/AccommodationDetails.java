package com.example.sprint1.model;

public class AccommodationDetails {
    private String checkIn;
    private String checkOut;
    private String location;
    private int numRooms;
    private String roomType;
    private String tripName;

    public AccommodationDetails() { }

    public AccommodationDetails(String checkIn, String checkOut, String location, int numRooms,
                                String roomType, String tripName) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.location = location;
        this.numRooms = numRooms;
        this.roomType = roomType;
        this.tripName = tripName;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNumRooms() {
        return numRooms;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getTripName() {
        return tripName;
    }

    public String getHotel() {
        return location + " Hotel";
    }

    public String getWebsite() {
        return "www." + location + ".com";
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

}

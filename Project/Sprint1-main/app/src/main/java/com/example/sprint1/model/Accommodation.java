package com.example.sprint1.model;

public class Accommodation {
    private String checkIn;
    private String checkOut;
    private String location;
    private int numRooms;
    private String roomType;
    private String trip;

    public Accommodation(String checkIn, String checkOut, String location, int numRooms,
                         String roomType, String trip) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.location = location;
        this.numRooms = numRooms;
        this.roomType = roomType;

        this.trip = trip;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
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

    public void setNumRooms(int numRooms) {
        this.numRooms = numRooms;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }
}

package com.example.sprint1.model;

public class Accommodation {
    private String checkIn;
    private String checkOut;
    private int numRooms;
    private String roomType;

    public Accommodation(String checkIn, String checkOut, int numRooms, String roomType) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.numRooms = numRooms;
        this.roomType = roomType;
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
}

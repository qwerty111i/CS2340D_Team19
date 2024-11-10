package com.example.sprint1.model;

public class ReservationDetails {
    private String name;
    private String location;
    private String website;
    private String date;
    private String time;
    private String tripName;

    public ReservationDetails() { }

    public ReservationDetails(String name, String location, String website,
                              String date, String time, String tripName) {
        this.name = name;
        this.location = location;
        this.website = website;
        this.date = date;
        this.time = time;
        this.tripName = tripName;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getWebsite() {
        return website;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String TripName) {
        this.tripName = TripName;
    }
}

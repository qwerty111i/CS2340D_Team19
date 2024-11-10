package com.example.sprint1.model;

public class TravelDetails {
    private String location;
    private String startDate;
    private String endDate;
    private String tripName;

    public TravelDetails() { }

    public TravelDetails(String location, String startDate, String endDate, String tripName) {
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tripName = tripName;
    }

    public String getLocation() {
        return location;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }
}

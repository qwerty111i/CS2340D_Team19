package com.example.sprint1.model;

public class TravelDetails {
    private String location;
    private String startDate;
    private String endDate;

    public TravelDetails() { }

    public TravelDetails(String location, String startDate, String endDate) {
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
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
    public void setLocation(String Location) {
        this.location = Location;
    }
}

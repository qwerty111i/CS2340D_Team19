package com.example.sprint1.model;

public class TransportationDetails {
    private String type;
    private String startLocation;
    private String endLocation;
    private String startDate;
    private String startTime;
    private String tripName;

    public TransportationDetails() { }

    public TransportationDetails(String type, String startLocation, String endLocation,
                                String startDate, String startTime, String tripName) {
        this.type = type;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.startDate = startDate;
        this.startTime = startTime;
        this.tripName = tripName;
    }

    public String getType() {
        return type;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

}

package com.example.sprint1.model;

import java.util.ArrayList;
import java.util.List;

public class ReservationDetails {
    private String location;
    private String website;
    private String startDate;
    private String endDate;
    private String startTime;
    private String tripName;

    public ReservationDetails(String location, String website,
                              String startDate, String endDate,
                              String startTime, String tripName) {
        this.location = location;
        this.website = website;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.tripName = tripName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }
}

package com.example.sprint1.model;

public class TravelFormEntry {
    private String startDate;
    private String endDate;
    private String destination;
    private String accommodation;
    private String dining;
    private String rating;

    // Default constructor
    public TravelFormEntry() {
    }

    // Parameterized constructor
    public TravelFormEntry(String startDate, String endDate, String destination,
                           String accommodation, String dining, String rating) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.destination = destination;
        this.accommodation = accommodation;
        this.dining = dining;
        this.rating = rating;
    }

    // Getters and setters for the variables
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

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(String accommodation) {
        this.accommodation = accommodation;
    }

    public String getDining() {
        return dining;
    }

    public void setDining(String dining) {
        this.dining = dining;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}

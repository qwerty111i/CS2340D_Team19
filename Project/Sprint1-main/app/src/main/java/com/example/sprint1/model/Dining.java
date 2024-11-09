package com.example.sprint1.model;

import java.util.ArrayList;
import java.util.List;

public class Dining {
    private String location;
    private String website;
    private List<String> reviews;
    private String reservationTime;
    private String trip;

    public Dining(String location, String website, ArrayList<String> reviews,
                  String reservationTime, String trip) {
        this.location = location;
        this.website = website;
        if (reviews == null) {
            this.reviews = new ArrayList<>();
        } else {
            this.reviews = new ArrayList<>(reviews);
        }
        this.reservationTime = reservationTime;
        this.trip = trip;
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

    public ArrayList<String> getReviews() {
        return new ArrayList<>(reviews);
    }

    public void setReviews(ArrayList<String> reviews) {
        this.reviews = new ArrayList<>(reviews);
    }

    public void addReview(String review) {
        reviews.add(review);
    }

    public boolean hasReview(String review) {
        return reviews.contains(review);
    }

    public boolean removeReview(String review) {
        return reviews.remove(review);
    }

    public String getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(String reservationTime) {
        this.reservationTime = reservationTime;
    }

    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }
}

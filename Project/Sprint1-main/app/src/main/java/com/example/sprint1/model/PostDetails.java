package com.example.sprint1.model;

import java.util.List;

public class PostDetails {
    private String userId;
    private Trip trip;
    private List<AccommodationDetails> accommodations;
    private List<ReservationDetails> reservations;
    private List<String> transportation;
    private String notes;

    public PostDetails() {

    }

    public PostDetails(Trip trip, List<String> transportation, String notes, List<AccommodationDetails> accommodations, List<ReservationDetails> reservations) {
        this.trip = trip;
        this.transportation = transportation;
        this.notes = notes;
        this.accommodations = accommodations;
        this.reservations = reservations;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public List<String> getTransportation() {
        return transportation;
    }

    public void setTransportation(List<String> transportation) {
        this.transportation = transportation;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<AccommodationDetails> getAccommodations() {
        return accommodations;
    }

    public void setAccommodations(List<AccommodationDetails> accommodations) {
        this.accommodations = accommodations;
    }

    public List<ReservationDetails> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationDetails> reservations) {
        this.reservations = reservations;
    }
}

package com.example.sprint1.model;

public class VacationTime {
    private String startDate;
    private String endDate;
    private int duration;

    public VacationTime() { }

    public VacationTime(String startDate, String endDate, int duration) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getDuration() {
        return duration;
    }
}

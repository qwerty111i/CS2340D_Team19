package com.example.sprint1.model;

public class VacationTime {
    private String startDate;
    private String endDate;

    public VacationTime() { }

    public VacationTime(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}

package com.example.sprint1.model;

public class VacationTime {
    private Integer duration;
    private String startDate;
    private String endDate;

    public VacationTime(Integer duration, String startDate, String endDate) {
        this.duration = duration;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Integer getDuration() {
        return duration;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}

package com.example.sprint1.model;

public class TFEUser {
    private TravelFormEntry tfe;
    private String userId;

    public TFEUser(TravelFormEntry tfe, String userId) {
        this.tfe = tfe;
        this.userId = userId;
    }

    public TravelFormEntry getTFE() {
        return tfe;
    }

    public void setTFE(TravelFormEntry tfe) {
        this.tfe = tfe;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

package com.example.sprint1.model;

import java.util.ArrayList;
import java.util.List;

public class TravelCommunitySingleton {
    private static TravelCommunitySingleton instance;
    private List<TravelFormEntry> travelFormEntries;
    private List<String> users;

    private TravelCommunitySingleton(){
        travelFormEntries = new ArrayList<>();
        users = new ArrayList<>();
    }

    public static synchronized TravelCommunitySingleton getInstance(){
       if(instance == null){
           instance = new TravelCommunitySingleton();
       }
       return instance;
    }

    public List<TravelFormEntry> getTravelFormEntries(){
        return travelFormEntries;
    }

    public void addTravelFormEntry(TravelFormEntry entry){
        travelFormEntries.add(entry);
    }


    public void addUser(String user){
        users.add(user);
    }

    public List<String> getUsers(){
        return users;
    }

    public void clearEntries(){
        travelFormEntries.clear();
        users.clear();
    }

}

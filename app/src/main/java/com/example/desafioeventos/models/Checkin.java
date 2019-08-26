package com.example.desafioeventos.models;

public class Checkin {
    private String eventId;
    private String name;
    private String email;

    public Checkin() {
    }

    public Checkin(String eventId, String name, String email) {
        this.eventId = eventId;
        this.name = name;
        this.email = email;
    }

    public String getEventID() {
        return eventId;
    }

    public void setEventID(String eventID) {
        this.eventId = eventID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }




}

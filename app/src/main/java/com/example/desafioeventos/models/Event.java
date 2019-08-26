package com.example.desafioeventos.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Event implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("date")
    @Expose
    private Long date;
    @SerializedName("people")
    @Expose
    private List<Person> people = null;
    @SerializedName("cupons")
    @Expose
    private List<Cupom> cupons = null;

    public Event() {
        this.people =new ArrayList<>();
        this.cupons = new ArrayList<>();
    }

    /**
     *
     * @param id
     * @param cupons
     * @param title
     * @param price
     * @param description
     * @param image
     * @param longitude
     * @param latitude
     * @param date
     * @param people
     */
    public Event(String id, String title, Double price, Double latitude, Double longitude, String image, String description, Long date, List<Person> people, List<Cupom> cupons) {
        super();
        this.id = id;
        this.title = title;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.description = description;
        this.date = date;
        this.people = people;
        this.cupons = cupons;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }

    public List<Cupom> getCupons() {
        return cupons;
    }

    public void setCupons(List<Cupom> cupons) {
        this.cupons = cupons;
    }

}




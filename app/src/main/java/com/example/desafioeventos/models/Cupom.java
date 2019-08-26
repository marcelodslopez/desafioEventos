package com.example.desafioeventos.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Cupom implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("eventId")
    @Expose
    private String eventId;
    @SerializedName("discount")
    @Expose
    private Long discount;

    public Cupom() {
    }

    /**
     *
     * @param id
     * @param eventId
     * @param discount
     */
    public Cupom(String id, String eventId, Long discount) {
        super();
        this.id = id;
        this.eventId = eventId;
        this.discount = discount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Long getDiscount() {
        return discount;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

}

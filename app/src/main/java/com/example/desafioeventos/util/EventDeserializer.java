package com.example.desafioeventos.util;

import com.example.desafioeventos.models.Cupom;
import com.example.desafioeventos.models.Event;
import com.example.desafioeventos.models.Person;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EventDeserializer implements JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        Event event = new Event();

        JsonObject eventJsonObject  = json.getAsJsonObject();

        event.setId(eventJsonObject.get("id").getAsString());
        event.setTitle(eventJsonObject.get("title").getAsString());
        event.setPrice(eventJsonObject.get("price").getAsDouble());
        event.setLatitude(eventJsonObject.get("latitude").getAsDouble());
        event.setLongitude(eventJsonObject.get("longitude").getAsDouble());
        event.setImage(eventJsonObject.get("image").getAsString());
        event.setDescription(eventJsonObject.get("description").getAsString());
        event.setDate(eventJsonObject.get("date").getAsLong());

        JsonArray people =  new JsonArray();
        people = eventJsonObject.getAsJsonArray("people");
        List<Person> listPerson = new ArrayList<>();
        for (int i = 0; i < people.size(); i++){
           listPerson.add(new Gson().fromJson(people.get(i), Person.class));
        }
        event.setPeople(listPerson);


        JsonArray cupons =  new JsonArray();
        cupons = eventJsonObject.getAsJsonArray("cupons");
        List<Cupom> listCupons = new ArrayList<>();
        for (int i = 0; i < cupons.size(); i++){
            listCupons.add(new Gson().fromJson(cupons.get(i), Cupom.class));
        }
        event.setCupons(listCupons);



        return event;
        //return (new Gson().fromJson(eventJsonObject, Event.class));
}





}

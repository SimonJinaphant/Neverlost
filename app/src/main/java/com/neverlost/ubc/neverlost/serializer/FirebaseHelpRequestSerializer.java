package com.neverlost.ubc.neverlost.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.neverlost.ubc.neverlost.models.FirebaseHelpRequest;

import java.lang.reflect.Type;

public class FirebaseHelpRequestSerializer implements JsonSerializer<FirebaseHelpRequest> {

    @Override
    public JsonElement serialize(FirebaseHelpRequest src, Type typeOfSrc, JsonSerializationContext context) {
        // Construct the parent JSON object
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("to", src.getTo());

        // Construct the data field for an FCM
        JsonObject jsonData = new JsonObject();
        jsonData.addProperty("lat", src.getLat());
        jsonData.addProperty("lng", src.getLng());
        jsonData.addProperty("dependant", src.getDependant());

        // Construct the notification field for an FCM
        JsonObject jsonNotification = new JsonObject();
        jsonNotification.addProperty("title", "Neverlost");
        jsonNotification.addProperty("body", src.getDependant() + " is in need of help!");

        jsonRequest.add("data", jsonData);
        jsonRequest.add("notification", jsonNotification);

        return jsonRequest;
    }
}
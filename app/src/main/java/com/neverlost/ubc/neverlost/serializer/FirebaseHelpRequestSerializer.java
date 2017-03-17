package com.neverlost.ubc.neverlost.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.neverlost.ubc.neverlost.firebase.MessagingService;
import com.neverlost.ubc.neverlost.models.FirebaseHelpRequest;

import java.lang.reflect.Type;

public class FirebaseHelpRequestSerializer implements JsonSerializer<FirebaseHelpRequest> {

    @Override
    public JsonElement serialize(FirebaseHelpRequest src, Type typeOfSrc, JsonSerializationContext context) {
        // Construct the parent JSON object
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty(MessagingService.FCM_TO, src.getTo());

        // Construct the data field for an FCM
        JsonObject jsonData = new JsonObject();
        jsonData.addProperty(MessagingService.FCM_DATA_LAT, src.getLat());
        jsonData.addProperty(MessagingService.FCM_DATA_LNG, src.getLng());
        jsonData.addProperty(MessagingService.FCM_DATA_DEPENDANT, src.getDependant());

        // Construct the notification field for an FCM
        JsonObject jsonNotification = new JsonObject();
        jsonNotification.addProperty(MessagingService.FCM_NOTIFICATION_TITLE, "Neverlost");
        jsonNotification.addProperty(MessagingService.FCM_NOTIFICATION_BODY, src.getDependant() + " is in need of help!");
        jsonNotification.addProperty(MessagingService.FCM_NOTIFICATION_SOUND, MessagingService.FCM_NOTIFICATION_SOUND_VALUE);

        jsonRequest.add(MessagingService.FCM_DATA, jsonData);
        jsonRequest.add(MessagingService.FCM_NOTIFICATION, jsonNotification);

        return jsonRequest;
    }
}
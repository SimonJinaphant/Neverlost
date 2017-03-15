package com.neverlost.ubc.neverlost;

import android.location.Location;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neverlost.ubc.neverlost.firebase.MessagingService;
import com.neverlost.ubc.neverlost.models.FirebaseHelpRequest;
import com.neverlost.ubc.neverlost.serializer.FirebaseHelpRequestSerializer;

public class MessageUtils {

    private MessageUtils() {
        // Private constructor to prevent instantiation.
    }

    /**
     * Helper function to generate a correctly formatted JSON string to send to FCM.
     *
     * @param dependantName - The name of the person in need of help.
     * @param location  - The location of the dependantName.
     * @return - Correctly formatted JSON string according to FCM guidelines.
     */
    public static String generateHelpMessageJSON(String dependantName, Location location) {
        FirebaseHelpRequest helpRequest = new FirebaseHelpRequest(
                MessagingService.FCM_TOPIC + MessagingService.FCM_TOPIC_NEVERLOST,
                dependantName,
                location.getLatitude(),
                location.getLongitude()
        );

        Gson jsonMessage = new GsonBuilder()
                .registerTypeAdapter(FirebaseHelpRequest.class, new FirebaseHelpRequestSerializer())
                .setPrettyPrinting()
                .create();

        return jsonMessage.toJson(helpRequest);
    }
}

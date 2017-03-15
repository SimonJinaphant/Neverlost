package com.neverlost.ubc.neverlost;

import android.location.Location;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neverlost.ubc.neverlost.firebase.MessagingService;
import com.neverlost.ubc.neverlost.models.FirebaseHelpRequest;
import com.neverlost.ubc.neverlost.serializer.FirebaseHelpRequestSerializer;

public class MessageUtils {

    private static final String FCM_TOPIC_TO = "/topics/";

    private MessageUtils() {
        // Private constructor to prevent instantiation.
    }

    /**
     * Helper function to generate a correctly formatted JSON string to send to FCM.
     *
     * @param dependant - The name of the person in need of help.
     * @param location  - The location of the dependant.
     * @return - Correctly formatted JSON string according to FCM guidelines.
     */
    public static String generateHelpMessageJSON(String dependant, Location location) {
        FirebaseHelpRequest help = new FirebaseHelpRequest(FCM_TOPIC_TO + MessagingService.FCM_TOPIC,
                dependant, location.getLatitude(), location.getLongitude());

        Gson json = new GsonBuilder()
                .registerTypeAdapter(FirebaseHelpRequest.class,
                        new FirebaseHelpRequestSerializer())
                .setPrettyPrinting()
                .create();

        return json.toJson(help);
    }
}

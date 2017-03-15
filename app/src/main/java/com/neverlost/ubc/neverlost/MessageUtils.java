package com.neverlost.ubc.neverlost;

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
     * @param lat       - The dependant's latitude.
     * @param lng       - The dependant's longitude.
     * @return - Correctly formatted JSON string according to FCM guidelines.
     */
    public static String generateHelpMessageJSON(String dependant, double lat, double lng) {
        FirebaseHelpRequest help = new FirebaseHelpRequest(FCM_TOPIC_TO + MessagingService.FCM_TOPIC,
                dependant, lat, lng);

        Gson json = new GsonBuilder()
                .registerTypeAdapter(FirebaseHelpRequest.class,
                        new FirebaseHelpRequestSerializer())
                .setPrettyPrinting()
                .create();

        return json.toJson(help);
    }
}

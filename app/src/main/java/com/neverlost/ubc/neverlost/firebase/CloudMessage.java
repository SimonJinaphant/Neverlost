package com.neverlost.ubc.neverlost.firebase;

import android.support.annotation.NonNull;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class CloudMessage {

    private static final String FCM_TO = "to";
    private static final String FCM_DATA = "data";
    private static final String FCM_NOTIFICATION = "notification";
    private static final String FCM_NOTIFICATION_TITLE = "title";
    private static final String FCM_NOTIFICATION_BODY = "body";

    private String to;
    private HashMap<String, String> data;
    private HashMap<String, String> notification;

    private CloudMessage() {
    }

    public String serializeToJson(){
        // Construct the parent JSON object
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty(FCM_TO, to);

        // Construct the data field for an FCM
        JsonObject jsonMessageData = new JsonObject();
        for(Map.Entry<String, String> entry : data.entrySet()){
            jsonMessageData.addProperty(entry.getKey(), entry.getValue());
        }

        // Construct the notification field for an FCM
        JsonObject jsonMessageNotification = new JsonObject();
        for(Map.Entry<String, String> entry : notification.entrySet()){
            jsonMessageNotification.addProperty(entry.getKey(), entry.getValue());
        }

        jsonMessage.add(FCM_DATA, jsonMessageData);
        jsonMessage.add(FCM_NOTIFICATION, jsonMessageNotification);

        return jsonMessage.toString();
    }

    public static ToStep builder() {
        return new Builder();
    }

    public interface ToStep {
        BuildStep to(String to);
    }

    public interface BuildStep {
        BuildStep withNotification(@NonNull String title, @NonNull String body);

        BuildStep withData(@NonNull String key, @NonNull String value);

        CloudMessage build();
    }

    public static class Builder implements ToStep, BuildStep {


        private String to;
        private HashMap<String, String> data = new HashMap<>();
        private HashMap<String, String> notification = new HashMap<>();

        @Override
        public CloudMessage build() {
            CloudMessage message = new CloudMessage();
            message.to = to;
            message.data = data;
            message.notification = notification;

            return message;
        }

        @Override
        public BuildStep to(String to) {
            this.to = to;
            return this;
        }

        @Override
        public BuildStep withNotification(@NonNull String title, @NonNull String body) {
            this.notification.put(FCM_NOTIFICATION_TITLE, title);
            this.notification.put(FCM_NOTIFICATION_BODY, body);
            return this;
        }

        @Override
        public BuildStep withData(@NonNull String key, @NonNull String value) {
            this.data.put(key, value);
            return this;
        }


    }
}

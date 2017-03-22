package com.neverlost.ubc.neverlost.firebase;

import android.support.annotation.NonNull;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class CloudMessage {

    // Constants for a typical JSON attributes of a Firebase Cloud Message.
    private static final String FCM_TO = "to";
    private static final String FCM_DATA = "data";
    private static final String FCM_NOTIFICATION = "notification";
    private static final String FCM_NOTIFICATION_TITLE = "title";
    private static final String FCM_NOTIFICATION_BODY = "body";

    private String to;
    private HashMap<String, String> data;
    private HashMap<String, String> notification;

    private CloudMessage() {
        // Prevent direct instantiation; must use the Builder
    }

    /**
     * Generate a Cloud Message using a step builder
     *
     * @return - ToStep; the initial step of the build, here you will specify the receiver(s).
     */
    public static ToStep builder() {
        return new Builder();
    }

    /**
     * Serialize the Cloud Message into its correct JSON format as specified by the Firebase API.
     *
     * @return - A JSON formatted string.
     */
    public String serializeToJson() {
        // Construct the parent JSON object
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty(FCM_TO, to);

        // Construct the data field for an FCM
        JsonObject jsonMessageData = new JsonObject();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            jsonMessageData.addProperty(entry.getKey(), entry.getValue());
        }

        // Construct the notification field for an FCM
        JsonObject jsonMessageNotification = new JsonObject();
        for (Map.Entry<String, String> entry : notification.entrySet()) {
            jsonMessageNotification.addProperty(entry.getKey(), entry.getValue());
        }

        jsonMessage.add(FCM_DATA, jsonMessageData);
        jsonMessage.add(FCM_NOTIFICATION, jsonMessageNotification);

        return jsonMessage.toString();
    }

    public interface ToStep {
        BuildStep to(String to);
    }

    public interface BuildStep {
        BuildStep withNotification(@NonNull String title, @NonNull String body);

        BuildStep withData(@NonNull String key, @NonNull String value);

        CloudMessage build();
    }

    /**
     * A Step Builder used to guide API users through a strict step-by-step build of a
     * Firebase Cloud Message with an optional data or notification field.
     */
    public static class Builder implements ToStep, BuildStep {

        private String to;
        private HashMap<String, String> data = new HashMap<>();
        private HashMap<String, String> notification = new HashMap<>();

        /**
         * Complete the step builder by building the final object now
         *
         * @return - CloudMessage with the attributes as specified in the build steps.
         */
        @Override
        public CloudMessage build() {
            CloudMessage message = new CloudMessage();
            message.to = to;
            message.data = data;
            message.notification = notification;

            return message;
        }

        /**
         * Specify the receiver(s) of this cloud message.
         * <p>
         * Put a Client Registration Token to target a specific client device.
         * Put a Topic name to target multiple client devices subscribed to that topic.
         *
         * @param to - The receiver of the message.
         * @return - BuildStep; you are allowed to complete the builder in this step.
         */
        @Override
        public BuildStep to(String to) {
            this.to = to;
            return this;
        }

        /**
         * Include a notification field with a title and body for the cloud message.
         *
         * @param title - The title of the notification.
         * @param body  - The body of the notification.
         * @return - BuildStep; you are allowed to complete the builder in this step.
         */
        @Override
        public BuildStep withNotification(@NonNull String title, @NonNull String body) {
            this.notification.put(FCM_NOTIFICATION_TITLE, title);
            this.notification.put(FCM_NOTIFICATION_BODY, body);
            return this;
        }

        /**
         * Add a data field attribute to the cloud message.
         *
         * @param key   - The data field's key.
         * @param value - The data field's value for the corresponding key.
         * @return - BuildStep; you are allowed to complete the builder in this step.
         */
        @Override
        public BuildStep withData(@NonNull String key, @NonNull String value) {
            this.data.put(key, value);
            return this;
        }

    }
}

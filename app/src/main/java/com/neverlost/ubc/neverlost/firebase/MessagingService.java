package com.neverlost.ubc.neverlost.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.RingtoneManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.facebook.Profile;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.neverlost.ubc.neverlost.R;
import com.neverlost.ubc.neverlost.activities.MapActivity;
import com.neverlost.ubc.neverlost.objects.Coordinate;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MessagingService extends FirebaseMessagingService {

    // FCM topics and Data Key-Value we listen for.
    public static final String FCM_TOPIC_NEVERLOST = "neverlost";
    public static final String FCM_DATA_LAT = "lat";
    public static final String FCM_DATA_LNG = "lng";
    public static final String FCM_DATA_REASON = "reason";
    public static final String FCM_DATA_DEPENDANT_NAME = "dependant_name";
    public static final String FCM_DATA_CARETAKER_ID = "caretaker_id";
    public static final String FCM_DATA_CARETAKER_NAME = "caretaker_name";

    public enum Reason {
        PANIC_BUTTON,
        ABNORMAL_HEARTRATE,
        FAILED_RESPONSE
    }

    // For communicating between this Service and other local Activities
    public static final String NCM_PANIC_MESSAGE = "com.neverlost.ubc.neverlost.MapActivity.NCM_PANIC_MESSAGE";
    public static final String NCM_PROMPT_REQUEST = "com.neverlost.ubc.neverlost.MapActivity.NCM_PROMPT_REQUEST";
    public static final String NCM_PROMPT_RESPONSE = "com.neverlost.ubc.neverlost.MapActivity.NCM_PROMPT_RESPONSE";

    // FCM Essential JSON keys.
    private static final String FCM_TOPIC = "/topics/";
    private static final String FCM_AUTH = "Authorization";
    private static final String FCM_AUTH_VALUE = "key=" + Authorization.FCM_SERVER_KEY;

    // For making HTTP REST calls to the FCM server to send upstream messages.
    private static final OkHttpClient client = new OkHttpClient();
    private static final String FCM_SEND_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String JSON_MEDIA_TYPE = "application/json";

    // For local debug messages.
    private static final String TAG = "NeverlostMsgService";

    // For broadcasting locally between activities.
    private LocalBroadcastManager broadcastManager;

    // Caretakers and Dependents to send to
    private static ArrayList<CloudMessageUser> dependents = new ArrayList<>();
    private static ArrayList<CloudMessageUser> caretakers = new ArrayList<>();


    public static void broadcastForHelp(Reason reason, double latitude, double longitude, Callback callback) {
        String name = Profile.getCurrentProfile().getFirstName();
        for(CloudMessageUser caretaker : caretakers) {
            CloudMessage helpMessage = CloudMessage.builder()
                    .to(caretaker.getFirebaseClientToken())
                    .withNotification("Neverlost", name + " is in need of help!")
                    .withData(FCM_DATA_DEPENDANT_NAME, name)
                    .withData(FCM_DATA_REASON, reason.toString())
                    .withData(FCM_DATA_LAT, String.valueOf(latitude))
                    .withData(FCM_DATA_LNG, String.valueOf(longitude))
                    .build();

            sendUpstreamCloudMessage(helpMessage, callback);
        }
    }

    /**
     * Send a safety verification prompt to a specific dependent.
     *
     * @param toFirebaseId   - The firebase client ID for the dependent we're sending to
     * @param fromFirebaseId - Our firebase client ID so they can reply back to us
     * @param callback       - Callback to handle success/failed transmission cases.
     */
    public static void sendSafetyPrompt(String toFirebaseId, String fromFirebaseId, Callback callback) {
        String name = Profile.getCurrentProfile().getFirstName();

        CloudMessage helpMessage = CloudMessage.builder()
                .to(toFirebaseId)
                .withNotification("Safety Prompt from " + name, name + " wants to know if you're safe")
                .withData(FCM_DATA_CARETAKER_ID, fromFirebaseId)
                .withData(FCM_DATA_CARETAKER_NAME, name)
                .build();

        sendUpstreamCloudMessage(helpMessage, callback);
    }

    /**
     * Respond/ACK to a sent safety verification prompt
     *
     * @param toFirebaseId - The firebase client ID for the dependent we're sending to
     * @param callback     - Callback to handle success/failed transmission cases.
     */
    public static void respondSafetyPrompt(String toFirebaseId, Callback callback) {
        String name = Profile.getCurrentProfile().getFirstName();

        CloudMessage helpMessage = CloudMessage.builder()
                .to(toFirebaseId)
                .withNotification("Safety Confirmed", name + " confirms they're safe")
                .withData(FCM_DATA_DEPENDANT_NAME, name)
                .build();

        sendUpstreamCloudMessage(helpMessage, callback);
    }

    /**
     * Broadcast to care-takers by sending a Firebase Cloud Message for help.
     *
     * @param location - Your current location.
     * @param callback - Callback to handle success/failed transmission cases.
     */
    public static void broadcastForHelpHP(Coordinate location, String name, Callback callback) {

        CloudMessage helpMessage = CloudMessage.builder()
                .to(FCM_TOPIC + FCM_TOPIC_NEVERLOST)
                .withNotification(name + " is in need of help!", name + " has pressed the panic button!")
                .withData(FCM_DATA_DEPENDANT_NAME, name)
                .withData(FCM_DATA_LAT, String.valueOf(location.lat))
                .withData(FCM_DATA_LNG, String.valueOf(location.lng))
                .build();

        sendUpstreamCloudMessage(helpMessage, callback);
    }


    /**
     * Submit an HTTP POST request to the Firebase Connection Server with a JSON formatted payload.
     *
     * @param message  - The message to send in JSON format as specified by the Firebase API.
     * @param callback - Callback to handle success/failed transmission cases.
     */
    private static void sendUpstreamCloudMessage(CloudMessage message, Callback callback) {
        MediaType jsonMediaType = MediaType.parse(JSON_MEDIA_TYPE);
        RequestBody requestBody = RequestBody.create(jsonMediaType, message.serializeToJson());

        Request request = new Request.Builder()
                .header(FCM_AUTH, FCM_AUTH_VALUE)
                .url(FCM_SEND_MESSAGE_URL)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    @Override
    public void onCreate() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
    }

    /**
     * Called when either a notification or data message is received.
     * <p>
     * -------------------------------------------------------------
     * This method gets invoked in the app via the Android Manifest.
     * -------------------------------------------------------------
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        /**
         * Notification messages are only received in this function if the app is in the foreground.
         * Otherwise an Android notification will be auto-generated and displayed; when the user
         * taps on the notification they will relaunch the app.
         */

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            double lat = Double.MAX_VALUE;
            double lng = Double.MAX_VALUE;
            String dependantName = null;
            String caretakerId = null;
            String caretakerName = null;
            Reason reason = null;

            for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                switch (key) {
                    case MessagingService.FCM_DATA_LAT:
                        lat = Double.parseDouble(value);
                        break;

                    case MessagingService.FCM_DATA_LNG:
                        lng = Double.parseDouble(value);
                        break;

                    case MessagingService.FCM_DATA_DEPENDANT_NAME:
                        dependantName = value;
                        break;

                    case MessagingService.FCM_DATA_CARETAKER_ID:
                        caretakerId = value;
                        break;

                    case MessagingService.FCM_DATA_CARETAKER_NAME:
                        caretakerName = value;
                        break;

                    case MessagingService.FCM_DATA_REASON:
                        reason = Reason.valueOf(value);
                        break;
                }

                Log.d(TAG, "Key: " + key + " \t Value: " + value);
            }

            if (reason != null) {
                Intent intent = new Intent(NCM_PANIC_MESSAGE);
                intent.putExtra(MessagingService.FCM_DATA_DEPENDANT_NAME, dependantName);
                intent.putExtra(MessagingService.FCM_DATA_REASON, reason.toString());
                intent.putExtra(MessagingService.FCM_DATA_LAT, lat);
                intent.putExtra(MessagingService.FCM_DATA_LNG, lng);

                broadcastManager.sendBroadcast(intent);
                sendNotification(dependantName+"is in need of help", getTranslation(reason));
            }

            if (caretakerId != null) {
                Intent intent = new Intent(NCM_PROMPT_REQUEST);
                intent.putExtra(MessagingService.FCM_DATA_CARETAKER_NAME, caretakerName);
                intent.putExtra(MessagingService.FCM_DATA_CARETAKER_ID, caretakerId);

                broadcastManager.sendBroadcast(intent);
                sendNotification("Safety prompt!", caretakerName + " wants to confirm you're safe");
            }

            if (reason == null && dependantName != null) {
                Intent intent = new Intent(NCM_PROMPT_RESPONSE);
                intent.putExtra(FCM_DATA_DEPENDANT_NAME, dependantName);

                broadcastManager.sendBroadcast(intent);
                sendNotification(dependantName+" is safe", dependantName+" has responded successfully");
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            //sendNotification(remoteMessage.getNotification().getBody());
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageTitle, String messageBody) {
        Intent intent = new Intent(this, MapActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_person_outline_black_24dp)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(PendingIntent.getActivity(this, 0, intent,
                        PendingIntent.FLAG_ONE_SHOT));

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    public static void addCaretaker(@NonNull CloudMessageUser caretaker) {
        caretakers.add(caretaker);
    }

    public static void addDependent(@NonNull CloudMessageUser dependent) {
        dependents.add(dependent);
    }

    public static ArrayList<CloudMessageUser> getCaretaker() {
        return caretakers;
    }

    public static ArrayList<CloudMessageUser> getDependents() {
        return dependents;
    }

    public String getTranslation(Reason reason){
        switch (reason){
            case FAILED_RESPONSE:
                return "failed to respond in time";
            case ABNORMAL_HEARTRATE:
                return "has abnormal heartrate";
            case PANIC_BUTTON:
                return "pressed the panic button";
            default:
                return "unknown";
        }

    }

}

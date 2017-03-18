package com.neverlost.ubc.neverlost.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.neverlost.ubc.neverlost.R;
import com.neverlost.ubc.neverlost.activities.MainActivity;

import java.util.Map;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MessagingService extends FirebaseMessagingService {

    // FCM topics we listen for.
    public static final String FCM_TOPIC = "/topics/";
    public static final String FCM_TOPIC_NEVERLOST = "neverlost";
    // FCM Essential JSON keys.
    public static final String FCM_TO = "to";
    public static final String FCM_AUTH = "Authorization";
    public static final String FCM_AUTH_VALUE = "key=" + Authorization.FCM_SERVER_KEY;
    // FCM JSON Notification keys.
    public static final String FCM_NOTIFICATION = "notification";
    public static final String FCM_NOTIFICATION_TITLE = "title";
    public static final String FCM_NOTIFICATION_BODY = "body";
    public static final String FCM_NOTIFICATION_SOUND = "sound";
    public static final String FCM_NOTIFICATION_SOUND_VALUE = "default";
    // FCM JSON Data and keys needed for sending dependant help messages.
    public static final String FCM_DATA = "data";
    public static final String FCM_DATA_LAT = "lat";
    public static final String FCM_DATA_LNG = "lng";
    public static final String FCM_DATA_DEPENDANT = "dependant";
    public static final String NEVERLOST_FCM_RESULT = "com.neverlost.ubc.neverlost.MainActivity.FCM_RESULT";
    // For making HTTP REST calls to the FCM server to send upstream messages
    private static final OkHttpClient client = new OkHttpClient();
    private static final String FCM_SEND_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String JSON_MEDIA_TYPE = "application/json";
    private static final String TAG = "NeverlostMsgService";
    // For communicating between this Service and MainActivity (or any other activity).
    private LocalBroadcastManager broadcastManager;

    public static void sendUpstreamMessage(String jsonMessage, Callback callback) {
        MediaType jsonMediaType = MediaType.parse(JSON_MEDIA_TYPE);
        RequestBody requestBody = RequestBody.create(jsonMediaType, jsonMessage);

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
            String dependant = null;

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
                    case MessagingService.FCM_DATA_DEPENDANT:
                        dependant = value;
                        break;
                }

                Log.d(TAG, "Key: " + key + " \t Value: " + value);
            }

            if (dependant != null) {
                Intent intent = new Intent(NEVERLOST_FCM_RESULT);
                intent.putExtra(MessagingService.FCM_DATA_LAT, lat);
                intent.putExtra(MessagingService.FCM_DATA_LNG, lng);
                intent.putExtra(MessagingService.FCM_DATA_DEPENDANT, dependant);

                broadcastManager.sendBroadcast(intent);
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
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_menu_send)
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(PendingIntent.getActivity(this, 0, intent,
                        PendingIntent.FLAG_ONE_SHOT));

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}

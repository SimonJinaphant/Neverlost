package com.neverlost.ubc.neverlost.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.neverlost.ubc.neverlost.MainActivity;
import com.neverlost.ubc.neverlost.R;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MessagingService extends FirebaseMessagingService {

    public static final String FCM_TOPIC = "neverlost";
    private static final String TAG = "FirebaseMsgService";
    private static final String FCM_SEND_URL = "https://fcm.googleapis.com/fcm/send";

    private static final OkHttpClient client = new OkHttpClient();

    public static void sendUpstreamMessage(String jsonMessage, Callback callback) {
        MediaType jsonMediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(jsonMediaType, jsonMessage);

        Request request = new Request.Builder()
                .header("Authorization", "key=" + Authorization.FCM_SERVER_KEY)
                .url(FCM_SEND_URL)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
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
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());
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

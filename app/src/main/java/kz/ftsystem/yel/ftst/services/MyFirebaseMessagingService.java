package kz.ftsystem.yel.ftst.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import kz.ftsystem.yel.ftst.App;
import kz.ftsystem.yel.ftst.Interfaces.MyCallback;
import kz.ftsystem.yel.ftst.R;
import kz.ftsystem.yel.ftst.backend.Backend;
import kz.ftsystem.yel.ftst.backend.MessageEvent;
import kz.ftsystem.yel.ftst.backend.MyConstants;
import kz.ftsystem.yel.ftst.backend.Order;
import kz.ftsystem.yel.ftst.ui.SplashScreenActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService implements MyCallback {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(MyConstants.TAG, "From: " + remoteMessage.getFrom());


        if (remoteMessage.getData().size() > 0) {
            Log.d(MyConstants.TAG, "Message data payload: " + remoteMessage.getData());

        }

        if (remoteMessage.getNotification() != null) {
            if (((App) getApplicationContext()).isAppForeground()) {

                if (Objects.requireNonNull(remoteMessage.getNotification().getTitle()).equals(getString(R.string.title_1)) ||
                        Objects.requireNonNull(remoteMessage.getNotification().getTitle()).equals(getString(R.string.title_2))) {
                    EventBus.getDefault().post(new MessageEvent("1"));
                }

            } else {
                String msgTitle = remoteMessage.getNotification().getTitle();
                String msgBody = remoteMessage.getNotification().getBody();
                sendNotification(msgTitle, msgBody);
            }
        }
    }


    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(MyConstants.TAG, "new token is " + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String fcmToken) {
        SharedPreferences preferences = getSharedPreferences(MyConstants.APP_PREFERENCES,
                Context.MODE_PRIVATE);
        String myId = preferences.getString(MyConstants.PREFERENCE_MY_ID, "");
        String myToken = preferences.getString(MyConstants.PREFERENCE_MY_TOKEN, "");
        Backend backend = new Backend(this, this);
        backend.sendNewFcmToken(myId, myToken, fcmToken);
        Log.d(MyConstants.TAG, "sendRegistrationToServer: " + fcmToken);
    }


    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
        Log.d(MyConstants.TAG, "nonMessageSent");
    }


    private void sendNotification(String msgTitle, String msgBody) {
        Intent intent = new Intent(this, SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0 /* Request code */,
                intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_new_order_24dp)
                        .setContentTitle(msgTitle)
                        .setContentText(msgBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    getString(R.string.default_notification_channel_id),
                    NotificationManager.IMPORTANCE_DEFAULT);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        assert notificationManager != null;
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


    @Override
    public void fromBackend(HashMap<String, String> data, List<Order> orders) {

    }
}

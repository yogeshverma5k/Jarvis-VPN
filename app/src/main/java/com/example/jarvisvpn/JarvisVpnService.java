package com.example.jarvisvpn;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.VpnService;
import android.os.Build;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;

public class JarvisVpnService extends VpnService {

    private static final String CHANNEL_ID = "jarvis_vpn";
    private ParcelFileDescriptor vpnInterface;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        createNotificationChannel();

        Notification notification =
                new Notification.Builder(this, CHANNEL_ID)
                        .setContentTitle("JARVIS VPN")
                        .setContentText("VPN connection is active")
                        .setSmallIcon(android.R.drawable.stat_sys_warning)
                        .setOngoing(true)
                        .build();

        startForeground(1, notification);

        try {
            Builder builder = new Builder();

            builder.setSession("JARVIS VPN")
                    .setMtu(1500)
                    .addAddress("10.7.0.2", 32)
                    .addRoute("10.7.0.0", 24);

            vpnInterface = builder.establish();

        } catch (Exception e) {
            stopSelf();
        }

        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            "JARVIS VPN",
                            NotificationManager.IMPORTANCE_LOW
                    );

            NotificationManager manager =
                    getSystemService(NotificationManager.class);

            manager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onDestroy() {

        if (vpnInterface != null) {
            try {
                vpnInterface.close();
            } catch (Exception ignored) {
            }

            vpnInterface = null;
        }

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }
}
package com.example.jarvisvpn;

import android.app.Activity;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final int VPN_REQUEST = 100;

    private TextView status;
    private Button connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        status = findViewById(R.id.status);
        connectButton = findViewById(R.id.connectButton);

        connectButton.setOnClickListener(v -> {

            Intent intent = VpnService.prepare(this);

            if (intent != null) {
                startActivityForResult(intent, VPN_REQUEST);
            } else {
                startVpn();
            }
        });
    }

    private void startVpn() {

        Intent intent =
                new Intent(this, JarvisVpnService.class);

        startService(intent);

        status.setText("CONNECTED");
        connectButton.setText("DISCONNECT");
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (requestCode == VPN_REQUEST &&
                resultCode == RESULT_OK) {

            startVpn();
        }
    }
}
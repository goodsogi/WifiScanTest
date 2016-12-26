package com.commax.wifiscantest;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
    //private static final String TAG = "maluchi";
    WifiManager wifi;
    BroadcastReceiver receiver;

    TextView textStatus;
    Button buttonScan;
    EditText edit;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup UI
        textStatus = (TextView) findViewById(R.id.textStatus);
        buttonScan = (Button) findViewById(R.id.buttonScan);
        //edit = (EditText)findViewById(R.id.edt);
        buttonScan.setOnClickListener(this);

        // Setup WiFi
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        // Get WiFi status
        WifiInfo info = wifi.getConnectionInfo();
        textStatus.append("\n\nWiFi Status: " + info.toString());

//        // List available networks
//        List<WifiConfiguration> configs = wifi.getConfiguredNetworks();
//        for (WifiConfiguration config : configs) {
//            textStatus.append("\n\n" + config.toString());
//        }

        // Register Broadcast Receiver
        if (receiver == null){
            receiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    // TODO Auto-generated method stub
                    List<ScanResult> results = wifi.getScanResults();
                    ScanResult bestSignal = null;
                    String message = String.format("%s networks found.\n", results.size());

                    Log.d("maluchi", "onReceive() message: " + message);

                    for (ScanResult result : results) {
                        if (bestSignal == null || WifiManager.compareSignalLevel(bestSignal.level, result.level) < 0)
                        {
                            bestSignal = result;

                        }

                        String strStrong = String.format("%s is detected.\n", result.SSID);
                        message +=strStrong;
                        Log.d("maluchi", "AP: " + strStrong);
                    }

                    String msg = String.format("%s is the strongest.", bestSignal.SSID);
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();

                    message +=msg;

                    print(message);
                }
            };
        }

        registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        Log.d("maluchi", "onCreate()");
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        unregisterReceiver(receiver);
        super.onStop();
    }
    public void onClick(View view) {
        Toast.makeText(this, "On Click Clicked. Toast to that!!!",
                Toast.LENGTH_LONG).show();

        if (view.getId() == R.id.buttonScan) {
            Log.d("maluchi", "onClick() wifi.startScan()");
            wifi.startScan();
        }
    }

    public void print(String string)
    {
        String str = textStatus.getText().toString();
        if(str.length() >0)
            str +="\n\n";

        str +=string;

        textStatus.setText(str);

    }
}
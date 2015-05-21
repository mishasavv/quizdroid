package edu.washington.mikhail3.quizdroid;

/**
 * Created by Misha Savvateev on 5/19/2015.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class Receiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Reciever", "rec running");
        String url = intent.getStringExtra("url");
        Toast.makeText(context, "Questions downloaded from: " + url, Toast.LENGTH_LONG).show();
    }
}

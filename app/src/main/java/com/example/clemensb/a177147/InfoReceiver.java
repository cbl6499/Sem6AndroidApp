package com.example.clemensb.a177147;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by ClemensB on 16.04.18.
 */

public class InfoReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Battery is low! Please charge your phone!" , Toast.LENGTH_LONG).show();
    }
}

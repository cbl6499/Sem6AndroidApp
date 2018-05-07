package com.example.clemensb.a177147;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * Created by Christoph on 07.05.2018.
 */

public class StartAppReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        Intent launchIntent = pm.getLaunchIntentForPackage("com.example.clemensb.a177147");
        context.startActivity(launchIntent);
    }
}

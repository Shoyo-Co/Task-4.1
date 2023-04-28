package com.example.task41;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SnoozeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("SNOOZE_ACTION" .equals(intent.getAction())) {
            // Handle snooze action here
            Toast.makeText(context, "Snooze pressed", Toast.LENGTH_SHORT).show();
        }
    }
}

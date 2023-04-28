package com.example.task41;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
public class MainActivity extends AppCompatActivity{

    private EditText etWorkout;
    private EditText etRest;
    private Button btnStart;
    private Button btnStop;
    private TextView tvRemaining;
    private ProgressBar progressBar;
    private CountDownTimer timer;
    private NotificationManager mManager;
    private Object mProgressChannelName;

    //    private VideoView  videoView;
    private void startTimer() {
        long workoutDuration = Long.parseLong(etWorkout.getText().toString()) * 1000;
        long restDuration = Long.parseLong(etRest.getText().toString()) * 1000;
        timer = new CountDownTimer(workoutDuration + restDuration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;

                tvRemaining.setText(String.format("Time Remaining: %02d:%02d", minutes, seconds));
                int progress = (int) (100 - (millisUntilFinished * 100) / (workoutDuration + restDuration));
                progressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                tvRemaining.setText("Time Remaining: 00:00");
                progressBar.setProgress(100);
//                VideoView  videoView = VideoView.create(this, R.raw.levelup);
//                videoView.start();
                MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.levelup);
                mediaPlayer.start();
                Toast.makeText(MainActivity.this, "Time ends.", Toast.LENGTH_SHORT).show();
                showNotification();
            }
        };
        timer.start();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etWorkout = findViewById(R.id.input_workout);
        etRest = findViewById(R.id.input_rest);
        btnStart = findViewById(R.id.button_start);
        btnStop = findViewById(R.id.button_stop);
        tvRemaining = findViewById(R.id.remaining);
        progressBar = findViewById(R.id.progressbar);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTimer();
            }
        });
    }
    private void showNotification() {
        Intent snoozeIntent = new Intent(this, SnoozeReceiver.class);
        snoozeIntent.setAction("SNOOZE_ACTION");
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(this, 0, snoozeIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default")
//                .setSmallIcon(R.drawable.mario)
                .setContentTitle("Timer Ended")
                .setContentText("Your timer has ended.")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Your timer has ended."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.mario, "Snooze", snoozePendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(0, builder.build());
    }

}


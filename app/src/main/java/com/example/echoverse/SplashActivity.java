package com.example.echoverse;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private VideoView videoView;
    private MediaPlayer soundPlayer;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        videoView = findViewById(R.id.videoView);

        // الخطوة 1: اختبر الصوت
        testSoundFirst();

        // الخطوة 2: ابدأ الفيديو بعد الصوت
        handler.postDelayed(this::startVideo, 1000);
    }

    private void testSoundFirst() {
        try {
            // غير هذا الاسم لاسم ملفك
            String soundName = "kids_sound_wav"; // ← الاسم بدون امتداد

            int soundId = getResources().getIdentifier(soundName, "raw", getPackageName());

            if (soundId != 0) {
                soundPlayer = MediaPlayer.create(this, soundId);
                if (soundPlayer != null) {
                    soundPlayer.setVolume(0.7f, 0.7f);
                    soundPlayer.start();
                }
            } else {
                Toast.makeText(this, "ملف الصوت مش موجود", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "خطأ في الصوت: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void startVideo() {
        try {
            String videoName = "my_video";
            int videoId = getResources().getIdentifier(videoName, "raw", getPackageName());

            if (videoId != 0) {
                videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + videoId));
                videoView.start();

                // انتقل لشاشة Login بعد انتهاء الفيديو
                videoView.setOnCompletionListener(mp -> goToLogin());

                // أيضاً: إذا الفيديو طويل، اضبط وقت انتظار قصير
                handler.postDelayed(this::goToLogin, 5000); // انتظر 5 ثواني كحد أقصى

            } else {
                // إذا ما في فيديو، انتقل مباشرة
                goToLogin();
            }
        } catch (Exception e) {
            goToLogin();
        }
    }

    private void goToLogin() {
        // توقف عن الصوت إذا كان يعمل
        if (soundPlayer != null && soundPlayer.isPlaying()) {
            soundPlayer.stop();
            soundPlayer.release();
        }

        if (videoView != null && videoView.isPlaying()) {
            videoView.stopPlayback();
        }

        handler.removeCallbacksAndMessages(null);
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPlayer != null) {
            soundPlayer.release();
            soundPlayer = null;
        }
        if (videoView != null) {
            videoView.stopPlayback();
            videoView = null;
        }
        handler.removeCallbacksAndMessages(null);
    }
}
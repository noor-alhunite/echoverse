package com.example.echoverse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);

        // ربط زر مكتبة الأصوات (SoundLibraryActivity)
        findViewById(R.id.cardSoundLibrary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SoundLibraryActivity.class));
            }
        });

        // ربط زر الأنشطة اليومية (DailyActivitiesActivity)
        findViewById(R.id.cardDailyActivities).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DailyActivitiesActivity.class));
            }
        });

        // ربط زر الألعاب (GameActivity)
        findViewById(R.id.cardGames).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });

        // ربط زر المكافآت (RewardsActivity)
        findViewById(R.id.cardRewards).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RewardsActivity.class));
            }
        });

        // ربط أيقونة الإعدادات (SettingsActivity)
        findViewById(R.id.settingsIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
        // ربط زر Discover Sounds
        findViewById(R.id.cardDiscoverSounds).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DiscoverSoundsActivity.class));
            }
        });

        // ⭐⭐ إضافة زر Discover Sounds الجديد ⭐⭐
        findViewById(R.id.cardDiscoverSounds).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DiscoverSoundsActivity.class));
            }
        });
    }
}
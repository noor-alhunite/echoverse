package com.example.echoverse;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class DiscoverSoundsActivity extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private Button btnRecord, btnPlay, btnResults;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_sounds);

        btnRecord = findViewById(R.id.btn_record_frag);
        btnPlay = findViewById(R.id.btn_play_frag);
        btnResults = findViewById(R.id.btn_results_frag);

        fragmentManager = getSupportFragmentManager();

        if (checkPermissions()) {
            setupFragments();
        } else {
            requestPermissions();
        }
    }

    private void setupFragments() {
        // Load RecordFragment as default (مثل المثال في ملفاتك التعليمية)
        loadFragment(new RecordFragment(), "record_fragment");

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new RecordFragment(), "record_fragment");
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new PlayFragment(), "play_fragment");
            }
        });

        btnResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ResultsFragment(), "results_fragment");
            }
        });
    }

    private void loadFragment(Fragment fragment, String tag) {
        // ✅ نفس الكود من ملفاتك التعليمية Lec-Fragments-Dynamic
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // استخدم replace مثل المثال في الصفحة 1 من ملفاتك
        fragmentTransaction.replace(R.id.fragment_container, fragment, tag);

        // يمكن إضافة animation للانتقال السلس
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        fragmentTransaction.commit();
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                REQUEST_RECORD_AUDIO_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupFragments();
                Toast.makeText(this, "Permissions granted! Start recording", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissions denied! Cannot record audio", Toast.LENGTH_LONG).show();
            }
        }
    }
}
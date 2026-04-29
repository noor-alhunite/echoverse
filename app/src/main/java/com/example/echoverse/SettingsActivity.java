package com.example.echoverse;

import android.content.Context;
import android.content.Intent; // تم إضافة هذا الاستيراد
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "EchoVerseSettings";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_SFX_VOLUME = "sfx_volume";
    private static final String KEY_BGM_VOLUME = "bgm_volume";
    private static final String KEY_VIBRATION = "vibration_enabled";

    // مفتاح النقاط من RewardsActivity
    private static final String PREFS_REWARD = "EchoVersePrefs";
    private static final String KEY_POINTS = "current_points";

    private SharedPreferences settingsPrefs;
    private SharedPreferences rewardPrefs;

    private RadioGroup languageRadioGroup;
    private SeekBar sfxSeekBar;
    private SeekBar bgmSeekBar;
    private Switch vibrationSwitch;
    private TextView currentPointsTextView;
    private Button logoutButton; // تم إضافة هذا المتغير
    private Button resetProgressButton;
    private TextView versionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // تهيئة SharedPreferences
        settingsPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        rewardPrefs = getSharedPreferences(PREFS_REWARD, Context.MODE_PRIVATE);

        // ربط العناصر بالواجهة
        languageRadioGroup = findViewById(R.id.radio_group_language);
        sfxSeekBar = findViewById(R.id.seekbar_sfx_volume);
        bgmSeekBar = findViewById(R.id.seekbar_bgm_volume);
        vibrationSwitch = findViewById(R.id.switch_vibration);
        currentPointsTextView = findViewById(R.id.text_settings_current_points);
        logoutButton = findViewById(R.id.button_logout); // تم ربط الزر
        resetProgressButton = findViewById(R.id.button_reset_progress);
        versionTextView = findViewById(R.id.text_version_number);

        // 1. تحميل الإعدادات الحالية
        loadSettings();

        // 2. تعيين مستمعي الأحداث
        setupListeners();

        // 3. عرض رقم الإصدار
        displayVersion();
    }

    private void loadSettings() {
        // تحميل اللغة
        String currentLang = settingsPrefs.getString(KEY_LANGUAGE, "ar"); // الافتراضي عربي
        if (currentLang.equals("ar")) {
            languageRadioGroup.check(R.id.radio_arabic);
        } else {
            languageRadioGroup.check(R.id.radio_english);
        }

        // تحميل مستويات الصوت
        int sfxVolume = settingsPrefs.getInt(KEY_SFX_VOLUME, 80);
        sfxSeekBar.setProgress(sfxVolume);

        int bgmVolume = settingsPrefs.getInt(KEY_BGM_VOLUME, 50);
        bgmSeekBar.setProgress(bgmVolume);

        // تحميل حالة الاهتزاز
        boolean vibrationEnabled = settingsPrefs.getBoolean(KEY_VIBRATION, true);
        vibrationSwitch.setChecked(vibrationEnabled);

        // عرض النقاط الحالية
        int currentPoints = rewardPrefs.getInt(KEY_POINTS, 0);
        currentPointsTextView.setText(String.valueOf(currentPoints));
    }

    private void setupListeners() {
        // مستمع تغيير اللغة
        languageRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String lang = (checkedId == R.id.radio_arabic) ? "ar" : "en";
            saveSetting(KEY_LANGUAGE, lang);
            // يجب إعادة تشغيل النشاط لتطبيق اللغة الجديدة
            Toast.makeText(this, getString(R.string.settings_language_changed), Toast.LENGTH_SHORT).show();
            // هنا يجب إضافة منطق إعادة تشغيل النشاط أو التطبيق
        });

        // مستمع تغيير مستوى المؤثرات الصوتية
        sfxSeekBar.setOnSeekBarChangeListener(new SimpleSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                saveSetting(KEY_SFX_VOLUME, progress);
            }
        });

        // مستمع تغيير مستوى الموسيقى الخلفية
        bgmSeekBar.setOnSeekBarChangeListener(new SimpleSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                saveSetting(KEY_BGM_VOLUME, progress);
            }
        });

        // مستمع تغيير حالة الاهتزاز
        vibrationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveSetting(KEY_VIBRATION, isChecked);
        });

        // مستمع زر تسجيل الخروج (جديد)
        logoutButton.setOnClickListener(v -> showLogoutDialog());

        // مستمع زر إعادة تعيين التقدم
        resetProgressButton.setOnClickListener(v -> showResetDialog());
    }

    private void saveSetting(String key, String value) {
        settingsPrefs.edit().putString(key, value).apply();
    }

    private void saveSetting(String key, int value) {
        settingsPrefs.edit().putInt(key, value).apply();
    }

    private void saveSetting(String key, boolean value) {
        settingsPrefs.edit().putBoolean(key, value).apply();
    }

    private void displayVersion() {
        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            versionTextView.setText(getString(R.string.settings_version_format, versionName));
        } catch (Exception e) {
            versionTextView.setText(getString(R.string.settings_version_format, "N/A"));
        }
    }

    private void showResetDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.settings_reset_progress)
                .setMessage(R.string.settings_reset_warning)
                .setPositiveButton(R.string.yes, (dialog, which) -> resetProgress())
                .setNegativeButton(R.string.no, null)
                .show();
    }

    private void resetProgress() {
        // إعادة تعيين النقاط والمكافآت المفتوحة
        rewardPrefs.edit().clear().apply();

        // إعادة تعيين الإعدادات
        settingsPrefs.edit().clear().apply();

        // إعادة تحميل النشاط
        recreate();
        Toast.makeText(this, R.string.settings_progress_reset_success, Toast.LENGTH_LONG).show();
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.settings_logout)
                .setMessage(R.string.settings_logout_warning)
                .setPositiveButton(R.string.yes, (dialog, which) -> logoutUser())
                .setNegativeButton(R.string.no, null)
                .show();
    }

    private void logoutUser() {
        // هنا يجب إضافة منطق مسح بيانات الجلسة (Session data) إذا كانت موجودة

        // الانتقال إلى شاشة تسجيل الدخول (LoginActivity)
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // مسح جميع الأنشطة السابقة
        startActivity(intent);
        finish();
    }

    // كلاس مساعد لتبسيط التعامل مع SeekBar
    private abstract class SimpleSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }
}

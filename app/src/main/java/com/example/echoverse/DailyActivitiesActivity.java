package com.example.echoverse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DailyActivitiesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AchievementAdapter adapter;
    private List<DailyAchievement> achievements;
    private SharedPreferences sharedPreferences;
    private TextView textTotalCoins;

    @Override
    protected void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_activities);

        recyclerView = findViewById(R.id.recycler_view_achievements);
        textTotalCoins = findViewById(R.id.text_total_coins);

        // ربط الأزرار
        Button btnStart = findViewById(R.id.btn_start_activity);
        Button btnBack = findViewById(R.id.btn_back_to_menu);

        if (btnStart != null) {
            btnStart.setOnClickListener(v -> Toast.makeText(this, "بدء النشاط...", Toast.LENGTH_SHORT).show());
        }
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sharedPreferences = getSharedPreferences("AchievementsPrefs", Context.MODE_PRIVATE);

        // تهيئة قائمة الإنجازات
        initializeAchievements();

        // تحميل حالة الإنجازات المحفوظة
        loadAchievementProgress();

        // إعداد المحول (Adapter) وعرض القائمة
        adapter = new AchievementAdapter(this, achievements);
        recyclerView.setAdapter(adapter);

        // تحديث العملات الإجمالية (افتراضياً)
        updateTotalCoinsUI();
    }

    private void initializeAchievements() {
        achievements = new ArrayList<>();

        // الإنجاز 1: أكمل 3 جولات لعب
        achievements.add(new DailyAchievement(
                1,
                getString(R.string.achievement_title_1),
                getString(R.string.achievement_desc_1),
                3
        ));

        // الإنجاز 2: استمع إلى 5 أصوات مختلفة
        achievements.add(new DailyAchievement(
                2,
                getString(R.string.achievement_title_2),
                getString(R.string.achievement_desc_2),
                5
        ));

        // الإنجاز 3: أكمل إنجازين يوميين
        achievements.add(new DailyAchievement(
                3,
                getString(R.string.achievement_title_3),
                getString(R.string.achievement_desc_3),
                2
        ));
    }

    private void loadAchievementProgress() {
        int completedCount = 0;
        for (DailyAchievement achievement : achievements) {
            // تحميل العدد الحالي المحقق من SharedPreferences
            int currentCount = sharedPreferences.getInt("achievement_" + achievement.getId() + "_count", 0);
            achievement.setCurrentCount(currentCount);

            if (achievement.isCompleted()) {
                completedCount++;
            }
        }

        // تحديث الإنجاز الثالث (البطل اليومي) بناءً على الإنجازات المكتملة
        for (DailyAchievement achievement : achievements) {
            if (achievement.getId() == 3) {
                achievement.setCurrentCount(completedCount);
                break;
            }
        }
    }

    private void updateTotalCoinsUI() {
        int totalCoins = sharedPreferences.getInt("total_coins", 24); // قيمة افتراضية 24
        textTotalCoins.setText(String.valueOf(totalCoins));
    }

    // دالة عامة لتحديث تقدم الإنجازات (يمكن استدعاؤها من أي Activity)
    public static void updateAchievementProgress(Context context, int achievementId, int increment) {
        SharedPreferences prefs = context.getSharedPreferences("AchievementsPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int currentCount = prefs.getInt("achievement_" + achievementId + "_count", 0);
        int newCount = currentCount + increment;

        editor.putInt("achievement_" + achievementId + "_count", newCount);
        editor.apply();

        // ملاحظة: يجب إعادة تحميل DailyActivitiesActivity لرؤية التغيير
    }
}

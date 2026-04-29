package com.example.echoverse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder> {

    private final Context context;
    private final List<DailyAchievement> achievements;

    public AchievementAdapter(Context context, List<DailyAchievement> achievements) {
        this.context = context;
        this.achievements = achievements;
    }

    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // ربط ملف التخطيط item_achievement.xml
        View view = LayoutInflater.from(context).inflate(R.layout.item_achievement, parent, false);
        return new AchievementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        DailyAchievement achievement = achievements.get(position);

        // تعيين النصوص
        holder.title.setText(achievement.getTitleKey());
        holder.description.setText(achievement.getDescriptionKey());

        // تعيين العملات (قيمة ثابتة مؤقتة)
        int reward = (achievement.getId() == 1) ? 10 : (achievement.getId() == 2 ? 15 : 20);
        holder.rewardCoins.setText(reward + " Coins");

        // حساب التقدم
        int current = achievement.getCurrentCount();
        int target = achievement.getTargetCount();
        int progressPercentage = (target > 0) ? (current * 100 / target) : 0;

        // تعيين شريط التقدم
        holder.progressBar.setMax(target);
        holder.progressBar.setProgress(current);

        // تعيين نص التقدم
        String progressText = String.format("%d%%", progressPercentage);
        holder.progressText.setText(progressText);

        // تغيير لون البطاقة عند الإكمال (اختياري)
        if (achievement.isCompleted()) {
            holder.cardContainer.setCardBackgroundColor(context.getResources().getColor(R.color.success_light));
        } else {
            holder.cardContainer.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return achievements.size();
    }

    // ViewHolder Class
    public static class AchievementViewHolder extends RecyclerView.ViewHolder {
        CardView cardContainer;
        TextView title;
        TextView description;
        ProgressBar progressBar;
        TextView progressText;
        TextView rewardCoins;

        public AchievementViewHolder(@NonNull View itemView) {
            super(itemView);
            // ربط المكونات من item_achievement.xml
            cardContainer = (CardView) itemView;
            title = itemView.findViewById(R.id.text_achievement_title);
            description = itemView.findViewById(R.id.text_achievement_description);
            progressBar = itemView.findViewById(R.id.progress_bar_achievement);
            progressText = itemView.findViewById(R.id.text_achievement_progress);
            rewardCoins = itemView.findViewById(R.id.text_reward_coins);
        }
    }
}

package com.example.echoverse;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class RewardsActivity extends AppCompatActivity implements RewardAdapter.OnRewardPurchaseListener {

    private static final String PREFS_NAME = "EchoVersePrefs";
    private static final String KEY_POINTS = "current_points";
    private static final String KEY_REWARD_UNLOCKED_PREFIX = "reward_unlocked_";

    private TextView currentPointsTextView;
    private RecyclerView rewardsRecyclerView;
    private RewardAdapter rewardAdapter;
    private List<Reward> rewardList;
    private int currentPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        currentPointsTextView = findViewById(R.id.text_current_points);
        rewardsRecyclerView = findViewById(R.id.recycler_view_rewards);

        loadCurrentPoints();

        initializeRewards();

        rewardAdapter = new RewardAdapter(this, rewardList, currentPoints, this);
        rewardsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rewardsRecyclerView.setAdapter(rewardAdapter);
    }

    private void loadCurrentPoints() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        currentPoints = prefs.getInt(KEY_POINTS, 1000);
        updatePointsUI();
    }

    private void updatePointsUI() {
        currentPointsTextView.setText(String.valueOf(currentPoints));
    }

    private void saveCurrentPoints(int newPoints) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_POINTS, newPoints);
        editor.apply();
        currentPoints = newPoints;
        updatePointsUI();
        rewardAdapter.updateCurrentPoints(currentPoints);
    }

    private void initializeRewards() {
        rewardList = new ArrayList<>();

        int oceanSoundTitleId = R.string.reward_ocean_sound;
        int oceanSoundImageId = R.drawable.ic_ocean;
        int oceanSoundCost = 500;
        boolean isOceanUnlocked = getRewardUnlockedStatus(oceanSoundTitleId);
        rewardList.add(new Reward(oceanSoundTitleId, oceanSoundImageId, oceanSoundCost, isOceanUnlocked));

        int forestBgTitleId = R.string.reward_forest_background;
        int forestBgImageId = R.drawable.ic_forest;
        int forestBgCost = 750;
        boolean isForestUnlocked = getRewardUnlockedStatus(forestBgTitleId);
        rewardList.add(new Reward(forestBgTitleId, forestBgImageId, forestBgCost, isForestUnlocked));

        int starIconTitleId = R.string.reward_star_icon;
        int starIconImageId = R.drawable.ic_star;
        int starIconCost = 250;
        boolean isStarUnlocked = getRewardUnlockedStatus(starIconTitleId);
        rewardList.add(new Reward(starIconTitleId, starIconImageId, starIconCost, isStarUnlocked));
    }

    private boolean getRewardUnlockedStatus(int titleResId) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_REWARD_UNLOCKED_PREFIX + titleResId, false);
    }

    private void setRewardUnlockedStatus(int titleResId, boolean isUnlocked) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_REWARD_UNLOCKED_PREFIX + titleResId, isUnlocked);
        editor.apply();
    }

    @Override
    public void onRewardPurchased(Reward reward, int position) {
        if (currentPoints >= reward.getRequiredPoints()) {
            int newPoints = currentPoints - reward.getRequiredPoints();
            saveCurrentPoints(newPoints);

            reward.setUnlocked(true);
            setRewardUnlockedStatus(reward.getTitleResId(), true);

            rewardAdapter.notifyItemChanged(position);

            Toast.makeText(this, getString(R.string.purchase_success, getString(reward.getTitleResId())), Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, getString(R.string.not_enough_points), Toast.LENGTH_SHORT).show();
        }
    }
}

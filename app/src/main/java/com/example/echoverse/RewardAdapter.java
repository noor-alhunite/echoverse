package com.example.echoverse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.RewardViewHolder> {

    private final Context context;
    private final List<Reward> rewardList;
    private int currentPoints;

    public interface OnRewardPurchaseListener {
        void onRewardPurchased(Reward reward, int position);
    }

    private OnRewardPurchaseListener purchaseListener;

    public RewardAdapter(Context context, List<Reward> rewardList, int currentPoints, OnRewardPurchaseListener listener) {
        this.context = context;
        this.rewardList = rewardList;
        this.currentPoints = currentPoints;
        this.purchaseListener = listener;
    }

    public void updateCurrentPoints(int newPoints) {
        this.currentPoints = newPoints;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reward, parent, false);
        return new RewardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardViewHolder holder, int position) {
        Reward currentReward = rewardList.get(position);

        holder.rewardIcon.setImageResource(currentReward.getImageResource());
        holder.rewardTitle.setText(context.getString(currentReward.getTitleResId()));

        if (currentReward.isUnlocked()) {
            holder.rewardCost.setVisibility(View.GONE);
            holder.rewardStatus.setVisibility(View.VISIBLE);
            holder.rewardStatus.setText(context.getString(R.string.reward_status_unlocked));
            holder.rewardStatus.setTextColor(ContextCompat.getColor(context, R.color.green_correct));
            holder.cardReward.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
            holder.cardReward.setClickable(false);
        } else {
            holder.rewardStatus.setVisibility(View.GONE);
            holder.rewardCost.setVisibility(View.VISIBLE);

            String costText = context.getString(R.string.points_format, currentReward.getRequiredPoints());
            holder.rewardCost.setText(costText);

            if (currentPoints >= currentReward.getRequiredPoints()) {
                holder.rewardCost.setTextColor(ContextCompat.getColor(context, R.color.accent));
                holder.cardReward.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
                holder.cardReward.setClickable(true);
            } else {
                holder.rewardCost.setTextColor(ContextCompat.getColor(context, R.color.text_secondary));
                holder.cardReward.setCardBackgroundColor(ContextCompat.getColor(context, R.color.background_sky_light));
                holder.cardReward.setClickable(false);
            }
        }

        holder.cardReward.setOnClickListener(v -> {
            if (!currentReward.isUnlocked() && currentPoints >= currentReward.getRequiredPoints()) {
                if (purchaseListener != null) {
                    purchaseListener.onRewardPurchased(currentReward, position);
                }
            } else if (!currentReward.isUnlocked() && currentPoints < currentReward.getRequiredPoints()) {
                Toast.makeText(context, context.getString(R.string.not_enough_points), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return rewardList.size();
    }

    public static class RewardViewHolder extends RecyclerView.ViewHolder {
        final CardView cardReward;
        final ImageView rewardIcon;
        final TextView rewardTitle;
        final TextView rewardCost;
        final TextView rewardStatus;

        public RewardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardReward = itemView.findViewById(R.id.card_reward);
            rewardIcon = itemView.findViewById(R.id.image_reward_icon);
            rewardTitle = itemView.findViewById(R.id.text_reward_title);
            rewardCost = itemView.findViewById(R.id.text_reward_cost);
            rewardStatus = itemView.findViewById(R.id.text_reward_status);
        }
    }
}

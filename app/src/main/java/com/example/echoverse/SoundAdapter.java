package com.example.echoverse;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.SoundViewHolder> {

    private final Context context;
    private final List<SoundItem> soundList;
    private MediaPlayer mediaPlayer;

    public SoundAdapter(Context context, List<SoundItem> soundList) {
        this.context = context;
        this.soundList = soundList;
    }

    @NonNull
    @Override
    public SoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sound, parent, false);
        return new SoundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoundViewHolder holder, int position) {
        SoundItem currentItem = soundList.get(position);

        // 1. Set the Icon (Dynamically)
        holder.soundIcon.setImageResource(currentItem.getImageResId());

        // 2. Set the Name (Dynamically using the String Resource ID)
        holder.soundName.setText(context.getString(currentItem.getNameResId()));

        // 3. Set the click listener for the play button
        holder.playButton.setOnClickListener(v -> playSound(currentItem.getSoundResId()));

        holder.itemView.setOnClickListener(v -> playSound(currentItem.getSoundResId()));
    }

    @Override
    public int getItemCount() {
        return soundList.size();
    }

    private void playSound(int soundResId) {
        // Release previous player before starting a new one
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = MediaPlayer.create(context, soundResId);
        if (mediaPlayer != null) {
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release();
                mediaPlayer = null;
            });
        }
    }

    public void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static class SoundViewHolder extends RecyclerView.ViewHolder {
        final ImageView soundIcon;
        final TextView soundName;
        final ImageView playButton;

        public SoundViewHolder(@NonNull View itemView) {
            super(itemView);
            soundIcon = itemView.findViewById(R.id.image_sound_icon);
            soundName = itemView.findViewById(R.id.text_sound_name);
            playButton = itemView.findViewById(R.id.image_play_button);
        }
    }
}

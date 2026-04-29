package com.example.echoverse;

/**
 * Model class to represent a sound item in the game and library.
 * Contains resource IDs for the sound, image, and the multilingual name string.
 */
public class SoundItem {
    private final int soundResId;
    private final int imageResId;
    private final int nameResId; // Resource ID for the name (e.g., R.string.lion)

    public SoundItem(int soundResId, int imageResId, int nameResId) {
        this.soundResId = soundResId;
        this.imageResId = imageResId;
        this.nameResId = nameResId;
    }

    // Getters

    public int getSoundResId() {
        return soundResId;
    }

    public int getImageResId() {
        return imageResId;
    }

    public int getNameResId() {
        return nameResId;
    }
}

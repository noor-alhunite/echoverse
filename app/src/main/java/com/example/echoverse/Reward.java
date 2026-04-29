package com.example.echoverse;

public class Reward {
    private final int titleResId;
    private final int imageResource;
    private final int requiredPoints;
    private boolean isUnlocked;

    public Reward(int titleResId, int imageResource, int requiredPoints, boolean isUnlocked) {
        this.titleResId = titleResId;
        this.imageResource = imageResource;
        this.requiredPoints = requiredPoints;
        this.isUnlocked = isUnlocked;
    }

    public int getTitleResId() {
        return titleResId;
    }

    public int getImageResource() {
        return imageResource;
    }

    public int getRequiredPoints() {
        return requiredPoints;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }
}

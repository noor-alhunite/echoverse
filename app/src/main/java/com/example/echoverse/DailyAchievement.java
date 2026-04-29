package com.example.echoverse;

public class DailyAchievement {
    private int id;
    private String titleKey; // مفتاح النص في strings.xml
    private String descriptionKey; // مفتاح الوصف في strings.xml
    private int targetCount;
    private int currentCount;
    private boolean isCompleted;

    public DailyAchievement(int id, String titleKey, String descriptionKey, int targetCount) {
        this.id = id;
        this.titleKey = titleKey;
        this.descriptionKey = descriptionKey;
        this.targetCount = targetCount;
        this.currentCount = 0;
        this.isCompleted = false;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitleKey() {
        return titleKey;
    }

    public String getDescriptionKey() {
        return descriptionKey;
    }

    public int getTargetCount() {
        return targetCount;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    // Setters and Logic
    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
        if (this.currentCount >= targetCount) {
            this.isCompleted = true;
        }
    }

    public void incrementCount() {
        if (!isCompleted) {
            this.currentCount++;
            if (this.currentCount >= targetCount) {
                this.isCompleted = true;
            }
        }
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}

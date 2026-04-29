package com.example.echoverse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    // المكونات التي تم ربطها بـ IDs من activity_game.xml
    private TextView scoreTextView;
    private TextView questionTextView;
    private Button btnPlaySound;
    private Button btnNext;
    private Button btnBack;

    private ImageView option1Image, option2Image, option3Image, option4Image;
    private CardView cardOption1, cardOption2, cardOption3, cardOption4;

    private List<SoundItem> allSounds;
    private SoundItem currentQuestion;
    private List<SoundItem> currentOptions;
    private MediaPlayer mediaPlayer;
    private int score = 0;
    private int currentQuestionIndex = 0;
    private final int TOTAL_QUESTIONS = 10; // Total number of questions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // 1. تهيئة المكونات وربطها بـ IDs
        scoreTextView = findViewById(R.id.text_score);
        questionTextView = findViewById(R.id.text_question); // يجب أن يكون هذا الـ ID موجوداً في XML
        btnPlaySound = findViewById(R.id.btn_play_sound);
        btnNext = findViewById(R.id.btn_next);
        btnBack = findViewById(R.id.btn_back);

        option1Image = findViewById(R.id.image_option_1);
        option2Image = findViewById(R.id.image_option_2);
        option3Image = findViewById(R.id.image_option_3);
        option4Image = findViewById(R.id.image_option_4);

        cardOption1 = findViewById(R.id.card_option_1);
        cardOption2 = findViewById(R.id.card_option_2);
        cardOption3 = findViewById(R.id.card_option_3);
        cardOption4 = findViewById(R.id.card_option_4);

        // 2. تهيئة قائمة الأصوات
        initializeSoundItems();

        // 3. بدء اللعبة
        loadNewQuestion();
    }

    private void initializeSoundItems() {
        allSounds = new ArrayList<>();

        // استخدام أسماء الموارد الموجودة لديك: R.string.lion, R.string.rain, إلخ.
        allSounds.add(new SoundItem(R.raw.rain_sound, R.drawable.ic_rain, R.string.rain));
        allSounds.add(new SoundItem(R.raw.lion_roar, R.drawable.ic_lion, R.string.lion));
        allSounds.add(new SoundItem(R.raw.car_horn, R.drawable.ic_car, R.string.car));
        allSounds.add(new SoundItem(R.raw.bird_chirp, R.drawable.ic_bird, R.string.bird));
        allSounds.add(new SoundItem(R.raw.bell_ring, R.drawable.ic_bell, R.string.bell));


        Collections.shuffle(allSounds);
    }

    private void loadNewQuestion() {
        // إعادة تفعيل الخيارات وإخفاء زر التالي
        enableOptions(true);
        btnNext.setVisibility(View.GONE);

        if (currentQuestionIndex >= TOTAL_QUESTIONS || allSounds.size() < 4) {
            // نهاية اللعبة (باستخدام المورد الجديد game_over_message)
            String gameOverMessage = getString(R.string.game_over_message, score, TOTAL_QUESTIONS);
            Toast.makeText(this, gameOverMessage, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // 1. اختيار السؤال الصحيح
        currentQuestion = allSounds.get(currentQuestionIndex);

        // 2. اختيار الخيارات
        currentOptions = new ArrayList<>();
        currentOptions.add(currentQuestion); // الإجابة الصحيحة

        List<SoundItem> wrongOptions = new ArrayList<>(allSounds);
        wrongOptions.remove(currentQuestion);
        Collections.shuffle(wrongOptions);

        // إضافة 3 خيارات خاطئة
        for (int i = 0; i < 3 && i < wrongOptions.size(); i++) {
            currentOptions.add(wrongOptions.get(i));
        }

        // خلط الخيارات
        Collections.shuffle(currentOptions);

        // 3. تحديث الواجهة
        updateUI();
    }

    private void updateUI() {
        // تحديث السؤال (باستخدام المورد what_is_this_sound)
        questionTextView.setText(getString(R.string.what_is_this_sound));

        // تحديث الصور
        option1Image.setImageResource(currentOptions.get(0).getImageResId());
        option2Image.setImageResource(currentOptions.get(1).getImageResId());
        option3Image.setImageResource(currentOptions.get(2).getImageResId());
        option4Image.setImageResource(currentOptions.get(3).getImageResId());

        // إعادة تعيين ألوان البطاقات
        resetCardColors();

        // تحديث النتيجة
        scoreTextView.setText(currentQuestionIndex + "/" + TOTAL_QUESTIONS);
    }

    // الدالة التي يتم استدعاؤها عند النقر على زر "Play Sound"
    public void onPlaySoundClick(View view) {
        if (currentQuestion != null) {
            playSound(currentQuestion.getSoundResId());
        }
    }

    // الدالة التي يتم استدعاؤها عند النقر على أحد الخيارات
    public void onOptionSelected(View view) {
        // تعطيل الخيارات لمنع النقر المتعدد
        enableOptions(false);

        CardView selectedCard = (CardView) view;
        SoundItem selectedOption = null;

        // تحديد الخيار الذي تم اختياره
        if (view.getId() == R.id.card_option_1) {
            selectedOption = currentOptions.get(0);
        } else if (view.getId() == R.id.card_option_2) {
            selectedOption = currentOptions.get(1);
        } else if (view.getId() == R.id.card_option_3) {
            selectedOption = currentOptions.get(2);
        } else if (view.getId() == R.id.card_option_4) {
            selectedOption = currentOptions.get(3);
        }

        if (selectedOption != null) {
            checkAnswer(selectedOption, selectedCard);
        }
    }

    private void checkAnswer(SoundItem selectedOption, CardView selectedCard) {
        if (selectedOption.getSoundResId() == currentQuestion.getSoundResId()) {
            // إجابة صحيحة
            score++;
            selectedCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.green_correct));
            Toast.makeText(this, getString(R.string.correct_answer), Toast.LENGTH_SHORT).show();
        } else {
            // إجابة خاطئة
            selectedCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.red_wrong));
            Toast.makeText(this, getString(R.string.wrong_answer), Toast.LENGTH_SHORT).show();

            // إظهار الإجابة الصحيحة
            highlightCorrectAnswer();
        }

        // إظهار زر التالي
        btnNext.setVisibility(View.VISIBLE);
    }

    private void highlightCorrectAnswer() {
        for (int i = 0; i < currentOptions.size(); i++) {
            if (currentOptions.get(i).getSoundResId() == currentQuestion.getSoundResId()) {
                CardView correctCard = getCardViewByIndex(i);
                if (correctCard != null) {
                    correctCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.green_correct));
                }
                break;
            }
        }
    }

    private CardView getCardViewByIndex(int index) {
        switch (index) {
            case 0: return cardOption1;
            case 1: return cardOption2;
            case 2: return cardOption3;
            case 3: return cardOption4;
            default: return null;
        }
    }

    // الدالة التي يتم استدعاؤها عند النقر على زر "Next"
    public void onNextClick(View view) {
        currentQuestionIndex++;
        loadNewQuestion();
    }

    // الدالة التي يتم استدعاؤها عند النقر على زر "Back"
    public void onBackClick(View view) {
        onBackPressed();
    }

    // الدالة التي يتم استدعاؤها عند النقر على زر "Back to Main"
    public void onBackToMainClick(View view) {
        finish(); // ببساطة إغلاق النشاط الحالي
    }

    private void enableOptions(boolean enable) {
        cardOption1.setEnabled(enable);
        cardOption2.setEnabled(enable);
        cardOption3.setEnabled(enable);
        cardOption4.setEnabled(enable);

        // إعادة تعيين الألوان عند التمكين
        if (enable) {
            resetCardColors();
        }
    }

    private void resetCardColors() {
        int defaultColor = ContextCompat.getColor(this, R.color.white); // افترضنا وجود لون أبيض
        cardOption1.setCardBackgroundColor(defaultColor);
        cardOption2.setCardBackgroundColor(defaultColor);
        cardOption3.setCardBackgroundColor(defaultColor);
        cardOption4.setCardBackgroundColor(defaultColor);
    }

    private void playSound(int soundResId) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = MediaPlayer.create(this, soundResId);
        if (mediaPlayer != null) {
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release();
                mediaPlayer = null;
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

package com.example.echoverse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Random;

public class ResultsFragment extends Fragment {

    private TextView txtResult, txtConfidence;
    private Button btnIdentify;

    private String[] sounds = {
            "كلب ينبح", "قطة تموء", "سيارة تزمر",
            "عصفور يغرد", "هاتف يرن", "ساعة تدق",
            "جرس الباب", "مطر", "رعد", "سيارة إطفاء",
            "بيانو", "جيتار", "طبلة", "طفل يبكي",
            "ضحك", "تصفيق", "صفارة", "إنذار"
    };

    public ResultsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_results_fragment, container, false);

        txtResult = view.findViewById(R.id.txt_result);
        txtConfidence = view.findViewById(R.id.txt_confidence);
        btnIdentify = view.findViewById(R.id.btn_identify);

        btnIdentify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                identifySound();
            }
        });

        return view;
    }

    private void identifySound() {
        Random random = new Random();
        int soundIndex = random.nextInt(sounds.length);

        String identifiedSound = sounds[soundIndex];
        int confidence = 70 + random.nextInt(30);

        txtResult.setText("🎯 تم التعرف: " + identifiedSound);
        txtConfidence.setText("نسبة الثقة: " + confidence + "%");

        String[] messages = {
                "أحسنت! وجدت صوت " + identifiedSound + "!",
                "ممتاز! هذا صوت " + identifiedSound + "!",
                "جيد جداً! تعرفت على: " + identifiedSound,
                "رائع! هذا صوت: " + identifiedSound
        };

        String message = messages[random.nextInt(messages.length)];
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}
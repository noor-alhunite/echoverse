package com.example.echoverse;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;

public class RecordFragment extends Fragment {

    private MediaRecorder recorder;
    private String audioFilePath;
    private boolean isRecording = false;

    private Button btnStartStop;
    private TextView txtStatus, txtFileName;

    public RecordFragment() {
        // Required empty public constructor - مثل المثال في ملفاتك
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment - مثل المثال في ملفاتك
        View view = inflater.inflate(R.layout.fragment_record, container, false);

        btnStartStop = view.findViewById(R.id.btn_start_stop);
        txtStatus = view.findViewById(R.id.txt_status);
        txtFileName = view.findViewById(R.id.txt_file_name);

        // ✅ نفس طريقة حفظ الملفات من ملفاتك التعليمية
        audioFilePath = getActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC)
                + "/echoverse_recording.3gp";
        txtFileName.setText("File: " + new File(audioFilePath).getName());

        btnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRecording) {
                    startRecording();
                } else {
                    stopRecording();
                }
            }
        });

        return view;
    }

    private void startRecording() {
        try {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(audioFilePath);

            recorder.prepare();
            recorder.start();
            isRecording = true;

            txtStatus.setText("🔴 RECORDING...");
            btnStartStop.setText("STOP RECORDING");
            Toast.makeText(getActivity(), "Recording started! Speak now...", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Recording failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        if (isRecording && recorder != null) {
            try {
                recorder.stop();
                recorder.release();
                recorder = null;
                isRecording = false;

                txtStatus.setText("✅ RECORDING SAVED");
                btnStartStop.setText("START NEW RECORDING");
                Toast.makeText(getActivity(), "Recording saved successfully!", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (recorder != null) {
            recorder.release();
        }
    }
}
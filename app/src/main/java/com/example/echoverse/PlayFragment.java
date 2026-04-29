package com.example.echoverse;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;

public class PlayFragment extends Fragment {

    private MediaPlayer player;
    private String audioFilePath;

    private Button btnPlay, btnPause, btnStop;
    private TextView txtPlayStatus;
    private SeekBar seekBar;

    public PlayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_play_fragment, container, false);

        btnPlay = view.findViewById(R.id.btn_play);
        btnPause = view.findViewById(R.id.btn_pause);
        btnStop = view.findViewById(R.id.btn_stop);
        txtPlayStatus = view.findViewById(R.id.txt_play_status);
        seekBar = view.findViewById(R.id.seek_bar);

        audioFilePath = getActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC)
                + "/echoverse_recording.3gp";

        File audioFile = new File(audioFilePath);
        if (!audioFile.exists()) {
            txtPlayStatus.setText("No recording found. Record first!");
            btnPlay.setEnabled(false);
        }

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio();
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseAudio();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAudio();
            }
        });

        return view;
    }

    private void playAudio() {
        try {
            if (player == null) {
                player = new MediaPlayer();
                player.setDataSource(audioFilePath);
                player.prepare();
            }

            player.start();
            txtPlayStatus.setText("▶️ PLAYING...");
            Toast.makeText(getActivity(), "Playing recording...", Toast.LENGTH_SHORT).show();

            // Update seekbar
            seekBar.setMax(player.getDuration());

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Playback failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void pauseAudio() {
        if (player != null && player.isPlaying()) {
            player.pause();
            txtPlayStatus.setText("⏸️ PAUSED");
        }
    }

    private void stopAudio() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
            txtPlayStatus.setText("⏹️ STOPPED");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
    }
}
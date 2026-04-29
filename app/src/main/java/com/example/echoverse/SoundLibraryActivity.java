package com.example.echoverse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class SoundLibraryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SoundAdapter soundAdapter;
    private List<SoundItem> soundList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_library);

        recyclerView = findViewById(R.id.recycler_view_sounds);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 1. Initialize the unique list of sounds
        initializeSoundItems();

        // 2. Set up the Adapter
        soundAdapter = new SoundAdapter(this, soundList);
        recyclerView.setAdapter(soundAdapter);
    }

    private void initializeSoundItems() {
        soundList = new ArrayList<>();

        // Animals
        soundList.add(new SoundItem(R.raw.lion_roar, R.drawable.ic_library_lion, R.string.lion));
        soundList.add(new SoundItem(R.raw.cat_meow, R.drawable.ic_library_cat, R.string.cat));
        soundList.add(new SoundItem(R.raw.dog_bark, R.drawable.ic_library_dog, R.string.dog));
        soundList.add(new SoundItem(R.raw.bird_chirp, R.drawable.ic_library_bird, R.string.bird));

        // Nature
        soundList.add(new SoundItem(R.raw.rain_sound, R.drawable.ic_library_rain, R.string.rain));
        soundList.add(new SoundItem(R.raw.thunder_sound, R.drawable.ic_library_thunder, R.string.thunder));

        // Vehicles/Others
        soundList.add(new SoundItem(R.raw.car_horn, R.drawable.ic_library_car, R.string.car));
        soundList.add(new SoundItem(R.raw.ambulance_siren, R.drawable.ic_library_ambulance, R.string.ambulance));
        soundList.add(new SoundItem(R.raw.fire_truck_siren, R.drawable.ic_library_fire_truck, R.string.fire_truck));
        soundList.add(new SoundItem(R.raw.bell_ring, R.drawable.ic_library_bell, R.string.bell));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundAdapter != null) {
            soundAdapter.releaseMediaPlayer();
        }
    }
}

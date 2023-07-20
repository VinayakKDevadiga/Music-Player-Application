package com.example.mymusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class PlaySelectedActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_selected);

        TextView textView = findViewById(R.id.textView6);

        Intent it = getIntent();
        String selectedTitle = it.getStringExtra("selectedTitle");

        Button start = findViewById(R.id.starttbn);
        Button stop = findViewById(R.id.stopbtn);
        File fileDir = getFilesDir();
        File audioFile = new File(fileDir, selectedTitle);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioFile.getAbsolutePath());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setSelected(true);

                if (isPlaying ==false) {
                    mediaPlayer.seekTo(0);
                    mediaPlayer.start();
                    Toast.makeText(getApplicationContext(), "Service Created", Toast.LENGTH_SHORT).show();
                    start.setEnabled(false);
                    stop.setText("STOP");
                    isPlaying = true; // Set isPlaying to true when the song starts
                }


            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying==true) {
                    mediaPlayer.pause();
                    stop.setText("RESUME");
                    start.setEnabled(true);
                    isPlaying = false;
                    Toast.makeText(getApplicationContext(), "Service Destroyed", Toast.LENGTH_SHORT).show();
                }else{
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
                    mediaPlayer.start();
                    stop.setText("STOP");
                    start.setEnabled(false);
                    isPlaying = true;
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}

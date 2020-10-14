package com.example.myplaylistplayer;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class PlayerActivity extends AppCompatActivity implements Runnable {

    boolean isSongPlaying = true;
    MediaPlayer mediaPlayer;
    Button playPauseButton;
    int songNumber;
    int arrayLength;
    SeekBar seekBar;
    TextView durationTextView, timerTextView, songNameTextView;
    Button nextButton, previousButton;
    Intent intent;
    ArrayList<String> songs = new ArrayList<>();
    int songID;
    String songName;

    public void Onclick(View view){
        if (isSongPlaying){
            mediaPlayer.seekTo(seekBar.getProgress());
            mediaPlayer.pause();
            isSongPlaying = false;
            playPauseButton.setText("Play");
            new Thread(this).start();
        } else {
            mediaPlayer.seekTo(seekBar.getProgress());
            mediaPlayer.start();
            isSongPlaying = true;
            playPauseButton.setText("Pause");
            new Thread(this).start();
        }

    }

    public void onNextClicked(View view){
        mediaPlayer.stop();
        songNumber = songNumber + 1;
        if (songNumber > arrayLength){
            songNumber = 1;
        }
        String songName = "song" + Integer.toString(songNumber);
        int songID = getResources().getIdentifier(songName, "raw",  getPackageName());
        mediaPlayer = MediaPlayer.create(this, songID);
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(0);
        durationTextView.setText(updateTimer(mediaPlayer.getDuration()/1000));
        randomImageSelection();
        songNameTextView.setText(songs.get(songNumber-1));
        isSongPlaying = true;
        playPauseButton.setText("Pause");
        mediaPlayer.start();
        new Thread(this).start();
    }

    public void onPreviousClicked(View view){
        mediaPlayer.stop();
        songNumber = songNumber - 1;
        if (songNumber == 0){
            songNumber = arrayLength;
        }
        songName = "song" + Integer.toString(songNumber);
        songID = getResources().getIdentifier(songName, "raw",  getPackageName());
        mediaPlayer = MediaPlayer.create(this, songID);
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(0);
        durationTextView.setText(updateTimer(mediaPlayer.getDuration()/1000));
        randomImageSelection();
        songNameTextView.setText(songs.get(songNumber-1));
        isSongPlaying = true;
        playPauseButton.setText("Pause");
        mediaPlayer.start();
        new Thread(this).start();
    }

    private void clearMediaPlayer() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    public String updateTimer(int secondsLeft) {
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - (minutes * 60);

        String secondString = Integer.toString(seconds);

        if (seconds <= 9) {
            secondString = "0" + secondString;
        }
        return Integer.toString(minutes) + ":" + secondString;
    }

    public void randomImageSelection(){
        Random rand = new Random();
        ImageView backgroundImageView = findViewById(R.id.backgroundImageView);
        String imageName = "image" + Integer.toString(rand.nextInt(10) + 1);

        int resID = getResources().getIdentifier(imageName, "drawable",  getPackageName());
        backgroundImageView.setImageResource(resID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        playPauseButton = findViewById(R.id.playPauseButton);
        seekBar = findViewById(R.id.songSeekBar);
        durationTextView = findViewById(R.id.durationTextView);
        timerTextView = findViewById(R.id.timerTextView);
        intent = getIntent();
        nextButton = findViewById(R.id.nextSongButton);
        previousButton = findViewById(R.id.previousSongButton);
        songNameTextView = findViewById(R.id.songNameTextView);
        songs = intent.getStringArrayListExtra("songName");

        randomImageSelection();

        songNumber = intent.getIntExtra("song",0) + 1;
        arrayLength = intent.getIntExtra("arrayLength",0);
        songNameTextView.setText(songs.get(songNumber-1));
        final String[] songName = {"song" + Integer.toString(songNumber)};
        final int[] songID = {getResources().getIdentifier(songName[0], "raw", getPackageName())};
        mediaPlayer = MediaPlayer.create(this, songID[0]);

        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(0);
        durationTextView.setText(updateTimer(mediaPlayer.getDuration()/1000));

        mediaPlayer.start();
        new Thread(this).start();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                timerTextView.setText(updateTimer(i/1000));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
                if (mediaPlayer != null && isSongPlaying) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                    mediaPlayer.start();
                }
                if (mediaPlayer.getCurrentPosition() == mediaPlayer.getDuration()) {
                    mediaPlayer.stop();
                    songNumber = songNumber + 1;
                    if (songNumber > arrayLength) {
                        songNumber = 1;
                    }
                    String songName = "song" + Integer.toString(songNumber);
                    songNameTextView.setText(songs.get(songNumber));
                    int songID = getResources().getIdentifier(songName, "raw", getPackageName());
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), songID);
                    seekBar.setMax(mediaPlayer.getDuration());
                    seekBar.setProgress(0);
                    durationTextView.setText(updateTimer(mediaPlayer.getDuration() / 1000));
                    mediaPlayer.start();
                    randomImageSelection();
                }
            }
        });
    }

    public void run() {

        int currentPosition = mediaPlayer.getCurrentPosition();
        int total = mediaPlayer.getDuration();

        if (currentPosition == total){
            Toast.makeText(this, "Song Ended", Toast.LENGTH_SHORT).show();
        }
        while (mediaPlayer != null && mediaPlayer.isPlaying() && currentPosition < total) {
            try {
                Thread.sleep(1000);
                currentPosition = mediaPlayer.getCurrentPosition();
            } catch (Exception e) {
                return;
            }
            seekBar.setProgress(currentPosition);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
    }
}

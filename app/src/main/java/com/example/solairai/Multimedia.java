package com.example.solairai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class Multimedia extends AppCompatActivity {


    public void backToMain(View view) {
        Intent intent = new Intent(getApplicationContext(), MediaActivity.class);
        startActivity(intent);
        //this function is used to running other activity
    }




    MediaPlayer media2;
    //function used for playing media
    public void play(View view)
    {
        media2.start();
    }
    public void pause(View view)
    {
        media2.pause();
    }

    AudioManager audio;
    //audio controller



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multimedia);
        //all function inside main function will run at startup


        Intent intent = getIntent();
        //information is gathered from previous activity
        Toast.makeText(this, intent.getStringExtra("name"), Toast.LENGTH_SHORT).show();
        //and then toasted here




        VideoView video=(VideoView) findViewById(R.id.videoView);
        // take id value(video)
        video.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.diamond);
        // setting up video to videoview

        MediaController media=new MediaController(this);
        //function initialized for controlling video

        media.setAnchorView(video);
        // setting up anchor view
        video.setMediaController(media);
        //attaching media controls to video
        video.start();
        //autostart



        media2 = MediaPlayer.create(this, R.raw.mp3);
        //setting up audio file


        audio = (AudioManager) getSystemService(AUDIO_SERVICE);
        // declaring audio variable as a audio manager
        int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //defining max vol
        int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        //defining initial vol



        SeekBar volumeControl = (SeekBar) findViewById(R.id.SeekBar);
        //seekbar for volume control
        volumeControl.setMax(maxVolume);
        //setting max vol
        volumeControl.setProgress(currentVolume);
        //setting initial vol

        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
                //this function used to create functionalities in sound controller seekbar
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {


                audio.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
                 //connecting sound controller to seekbar
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });






        final SeekBar scrubSeekBar = (SeekBar) findViewById(R.id.scrubSeekBar);
        //defining seekbar for song progress

        scrubSeekBar.setMax(media2.getDuration());
        //defining max value to max duration of media

        scrubSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        //this function used to create functionalities in sound controller seekbar
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {



                media2.seekTo(i);
                  //changing media2 progress when we will move seekbar
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Timer().scheduleAtFixedRate(

                new TimerTask()
                //move seekbar as song progress will increased
            {
            @Override
            public void run() {

                scrubSeekBar.setProgress(media2.getCurrentPosition());
                // setting up seekbar current position =  song progress
            }
        }, 0, 300);

    }
}
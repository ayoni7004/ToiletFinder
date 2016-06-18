package com.example.xnote.toiletfinder;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.Marker;

public class Main3Activity extends AppCompatActivity {

    MediaPlayer mp,mp2,mp3,mp4 = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        // final int count = 0;
        Button w = (Button) findViewById(R.id.water);//물내리는 소리 mp
        Button wi = (Button) findViewById(R.id.su);//window
        Button di = (Button) findViewById(R.id.classic);//클래식소리 mp3
        Button s = (Button) findViewById(R.id.windy);//digital

        Button stop = (Button) findViewById(R.id.stop);
        Button stop2 = (Button) findViewById(R.id.stop2);
        Button stop3 = (Button) findViewById(R.id.stop3);
        Button stop4 = (Button) findViewById(R.id.stop4);

        mp = MediaPlayer.create(Main3Activity.this, R.raw.water);
        mp2 = MediaPlayer.create(Main3Activity.this, R.raw.window);
        mp3 = MediaPlayer.create(Main3Activity.this, R.raw.zzz);
        mp4 = MediaPlayer.create(Main3Activity.this, R.raw.digital);

        w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();

                new Thread();
            }
        });

        wi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp2.start();

                new Thread();
            }
        });

        di.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp3.start();

                new Thread();
            }
        });

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp4.start();

                new Thread();
            }
        });

        stop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mp.stop();


            }
        });

        stop2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mp2.stop();


            }
        });

        stop3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mp3.pause();


            }
        });

        stop4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mp4.pause();


            }
        });



    }


}

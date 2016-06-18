package com.example.xnote.toiletfinder;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main4Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        Button yo = (Button) findViewById(R.id.yo);
        yo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main4Activity.this, yo.class); // 요실금 진단으로 넘어간다.
                startActivity(intent);
                // 다음 화면으로 넘어간다
            }
        });

        Button ga = (Button) findViewById(R.id.ga);
        ga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main4Activity.this, ga.class); // 과민성대장증후군 진단으로 넘어간다.
                startActivity(intent);
                // 다음 화면으로 넘어간다
            }
        });

        Button yocha=(Button)findViewById(R.id.yocha);
        yocha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=ZSI9lCrU9UQ")); // 과민성대장증후군 진단으로 넘어간다.
                startActivity(intent);
            }
        });

        Button gacha=(Button)findViewById(R.id.gacha);
        gacha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=hwTDG7s3zRc")); // 과민성대장증후군 진단으로 넘어간다.
                startActivity(intent);
            }
        });


    }



}

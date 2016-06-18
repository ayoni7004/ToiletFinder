package com.example.xnote.toiletfinder;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

public class ga extends AppCompatActivity {

    int count2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ga);
    }

    public void onRadioButtonClicked2(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radioButton2:
                if (checked) ;
                count2++;
                break;

            case R.id.radioButton11:
                if (checked) ;
                count2++;
                break;

            case R.id.radioButton12:
                if (checked) ;
                count2++;
                break;

            case R.id.radioButton13:
                if (checked) ;
                count2++;
                break;

            case R.id.radioButton14:
                if (checked) ;
                count2++;
                break;


        }
        Toast.makeText(getApplicationContext(), +count2 + "개 입니다.", Toast.LENGTH_SHORT).show();

    }

}

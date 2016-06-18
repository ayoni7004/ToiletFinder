package com.example.xnote.toiletfinder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

public class yo extends Activity {

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yo);

    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radioButton:
                if (checked) ;
                count++;
                break;

            case R.id.rb2:
                if (checked) ;
                count++;
                break;

            case R.id.radioButton3:
                if (checked) ;
                count++;
                break;

            case R.id.radioButton4:
                if (checked) ;
                count++;
                break;

            case R.id.radioButton5:
                if (checked) ;
                count++;
                break;

            case R.id.radioButton6:
                if (checked) ;
                count++;
                break;

            case R.id.radioButton7:
                if (checked) ;
                count++;
                break;

            case R.id.radioButton8:
                if (checked) ;
                count++;
                break;

            case R.id.radioButton9:
                if (checked) ;
                count++;
                break;

            case R.id.radioButton10:
                if (checked) ;
                count++;
                break;

        }
        Toast.makeText(getApplicationContext(), +count+"개 입니다.",Toast.LENGTH_SHORT).show();

    }

}

package com.example.xnote.toiletfinder;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Register extends Info {

    EditText idText,passText,phoneText;

    Button mJoinbt,mCancelbt2;

    String Id;
    String Pass;
    String phone;



    Context mContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register);

        mContext = this;

        idText = (EditText) findViewById(R.id.id);
        passText = (EditText) findViewById(R.id.pass);
        phoneText = (EditText) findViewById(R.id.num);


        Button cancel = (Button) findViewById(R.id.cancel2);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cancel = new Intent(Register.this, MainActivity.class); // 취소를 누르면 메인으로
                startActivity(cancel);
                // 다음 화면으로 넘어간다
            }
        });


        mJoinbt = (Button)findViewById(R.id.join);
        mJoinbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JoinPost().execute(idText.getText().toString(),passText.getText().toString(),phoneText.getText().toString());
            }
        });

        Button mCancelBt2 = (Button)findViewById(R.id.cancel2);
        mCancelBt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class); // ok
                startActivity(intent);
                // 다음 화면으로 넘어간다
            }
        });


    }
    public class JoinPost extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            String response = null;
            try {
                url = new URL(Login.IP_ADDRESS+"userJoin");


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");


                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;");


                String body = "email="+params[0]+"&pwd="+params[1]+"&phone="+params[2];//"id="+mIDText+"&pw="+mPWText;
                //"uuid="+StaticData.UUID+"&token="+tokenstr;

                conn.setDoOutput(true);
                conn.setDoInput(true);

                OutputStream os = conn.getOutputStream();

                os.write(body.getBytes("utf-8"));

                os.flush();
                os.close();


                response = convertStreamToString(conn.getInputStream());

                conn.connect();
//                Log.e("logins", "response = " + response.toString());


            } catch (Exception e) {
                Log.e("logins", "post ERR " + e.getStackTrace()[0].getFileName() + " -> " + e.getStackTrace()[0].getLineNumber());
                e.printStackTrace();
            }
            return response.toString();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(null);
            try{
                JSONObject mData = new JSONObject(aVoid);
                Toast.makeText(mContext,mData.getString("data"),Toast.LENGTH_LONG).show();
                String res = mData.getString("data");
                if(res.equals("ok")){
                    Button mJoinbt = (Button) findViewById(R.id.join);
                    mJoinbt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Register.this, MainActivity.class); // ok
                            startActivity(intent);
                            // 다음 화면으로 넘어간다
                        }
                    });

                }else if(res.equals("err")){
                    //아이디에러
                }else{
                    //비번에러
                }
            }catch (Exception e){

            }
        }

        private String convertStreamToString(InputStream is){
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            StringBuilder sb = new StringBuilder();

            String line = null;

            try{
                while ((line = reader.readLine()) != null){
                    sb.append(line + "\n");
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
            finally{
                try{
                    is.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }

    }
}

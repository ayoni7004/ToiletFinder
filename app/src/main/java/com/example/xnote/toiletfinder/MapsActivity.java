package com.example.xnote.toiletfinder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    public GoogleMap mGoogleMap;
    public static final LatLng SEOUL = new LatLng(37.56, 126.97);
    private Marker mNowMarker;
    private ArrayList<Marker> mMarkers;
    private JSONObject mJsonData = null;
    private boolean mLodingBool = true;
    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mMarkers = new ArrayList<>();

        mContext = this;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        LocationManager mLocationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);


        if (!mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) &&
                !mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                ) {
            startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
            Toast.makeText(this, "무선 네트워크 사용을 클릭해주세요 .", Toast.LENGTH_LONG).show();
        }



    }

    @Override
    public void onMapReady(GoogleMap map) {

        mGoogleMap = map;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);

        mGoogleMap.setBuildingsEnabled(true);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 12));

        mGoogleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (mLodingBool) {
                    LatLng nowLocation = new LatLng(location.getLatitude(), location.getLongitude());

                    mNowMarker = mGoogleMap.addMarker(new MarkerOptions().position(nowLocation).title("현재위치"));
                    mNowMarker.showInfoWindow();

                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomIn());
                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(nowLocation)      // Sets the center of the map to Mountain View
                            .zoom(17)                   // Sets the zoom
//                        .bearing(90)                // Sets the orientation of the camera to east
                            .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    mLodingBool=false;
                }
            }
        });


        mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
//                    if(!mLodingBool){
//                        mNowMarker.setPosition(cameraPosition.target);
//                    }

            }
        });

        new APICall().execute();

    }

    public class APICall extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            //여기서 프로그래스바 실행
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            String response = null;
            try {
                url = new URL("http://openapi.seoul.go.kr:8088/4e466a487463616e35324351624875/json/GeoInfoPublicToiletWGS/4047/5046");


                HttpURLConnection conn = null;

                OutputStream os = null;
                InputStream is = null;
                ByteArrayOutputStream baos = null;

                conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(10 * 1000);
                conn.setReadTimeout(10 * 1000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoInput(true);

                int responseCode = conn.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK) {

                    is = conn.getInputStream();

                    response = convertStreamToString(is);

                }else{
                    Log.e("ayoni", ""+responseCode);
                }

            } catch (Exception e) {
                Log.e("logins", "post ERR " + e.getStackTrace()[0].getFileName() + " -> " + e.getStackTrace()[0].getLineNumber());
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(null);
            JSONArray mResult = null;
            try{
                try {
                    mJsonData = new JSONObject(aVoid);
                    mResult = mJsonData.getJSONObject("GeoInfoPublicToiletWGS").getJSONArray("row");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for(int count = 0;count <mResult.length();count++ ){
                    JSONObject mTempMarker = mResult.getJSONObject(count);
                   // mGoogleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.h)));
                    mMarkers.add(mGoogleMap.addMarker(new MarkerOptions().position(
                            new LatLng(Double.valueOf(mTempMarker.getString("LAT")).doubleValue(),
                                    Double.valueOf(mTempMarker.getString("LNG")).doubleValue())).
                            title(mTempMarker.getString("HNR_NAM")+"공중화장실").
                            icon(BitmapDescriptorFactory.fromResource(R.drawable.h)))
                    );
                    mMarkers.get(count).showInfoWindow();
                }
            }catch (Exception e){

            }
            new APICall2().execute();
            //프로그래스바 종료
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

    public class APICall2 extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            //여기서 프로그래스바 실행
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            String response = null;
            try {
                url = new URL("http://openapi.seoul.go.kr:8088/4e466a487463616e35324351624875/json/GeoInfoPublicToiletWGS/3047/4046");


                HttpURLConnection conn = null;

                OutputStream os = null;
                InputStream is = null;
                ByteArrayOutputStream baos = null;

                conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(10 * 1000);
                conn.setReadTimeout(10 * 1000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoInput(true);

                int responseCode = conn.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK) {

                    is = conn.getInputStream();

                    response = convertStreamToString(is);

                }else{
                    Log.e("ayoni", ""+responseCode);
                }

            } catch (Exception e) {
                Log.e("logins", "post ERR " + e.getStackTrace()[0].getFileName() + " -> " + e.getStackTrace()[0].getLineNumber());
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(null);
            JSONArray mResult = null;
            try{
                try {
                    mJsonData = new JSONObject(aVoid);
                    mResult = mJsonData.getJSONObject("GeoInfoPublicToiletWGS").getJSONArray("row");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for(int count = 0;count <mResult.length();count++ ){
                    JSONObject mTempMarker = mResult.getJSONObject(count);
                    mMarkers.add(
                            mGoogleMap.addMarker(
                                    new MarkerOptions().position(
                                            new LatLng(
                                                    Double.valueOf(mTempMarker.getString("LAT")).doubleValue(),
                                                    Double.valueOf(mTempMarker.getString("LNG")).doubleValue()
                                            )
                                    ).title(mTempMarker.getString("HNR_NAM")+" 공중화장실").
                                            icon(BitmapDescriptorFactory.fromResource(R.drawable.h))
                            )
                    );
                    mMarkers.get(count).showInfoWindow();
                }
            }catch (Exception e){

            }
           new APICall3().execute();

            //프로그래스바 종료
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

    public class APICall3 extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            //여기서 프로그래스바 실행
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            String response = null;
            try {
                url = new URL("http://openapi.seoul.go.kr:8088/4e466a487463616e35324351624875/json/GeoInfoPublicToiletWGS/2047/3046");


                HttpURLConnection conn = null;

                OutputStream os = null;
                InputStream is = null;
                ByteArrayOutputStream baos = null;

                conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(10 * 1000);
                conn.setReadTimeout(10 * 1000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoInput(true);

                int responseCode = conn.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK) {

                    is = conn.getInputStream();

                    response = convertStreamToString(is);

                }else{
                    Log.e("ayoni", ""+responseCode);
                }

            } catch (Exception e) {
                Log.e("logins", "post ERR " + e.getStackTrace()[0].getFileName() + " -> " + e.getStackTrace()[0].getLineNumber());
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(null);
            JSONArray mResult = null;
            try{
                try {
                    mJsonData = new JSONObject(aVoid);
                    mResult = mJsonData.getJSONObject("GeoInfoPublicToiletWGS").getJSONArray("row");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for(int count = 0;count <mResult.length();count++ ){
                    JSONObject mTempMarker = mResult.getJSONObject(count);
                    mMarkers.add(
                            mGoogleMap.addMarker(
                                    new MarkerOptions().position(
                                            new LatLng(
                                                    Double.valueOf(mTempMarker.getString("LAT")).doubleValue(),
                                                    Double.valueOf(mTempMarker.getString("LNG")).doubleValue()
                                            )
                                    ).title(mTempMarker.getString("HNR_NAM")+" 공중화장실").
                                            icon(BitmapDescriptorFactory.fromResource(R.drawable.h)).infoWindowAnchor(0.5f,0.5f).visible(true))
                    );
                    mMarkers.get(count).showInfoWindow();
                }
            }catch (Exception e){

            }
            new APICall4().execute();

            //프로그래스바 종료
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

    public class APICall4 extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            //여기서 프로그래스바 실행
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            String response = null;
            try {
                url = new URL("http://openapi.seoul.go.kr:8088/4e466a487463616e35324351624875/json/GeoInfoPublicToiletWGS/1047/2046");


                HttpURLConnection conn = null;

                OutputStream os = null;
                InputStream is = null;
                ByteArrayOutputStream baos = null;

                conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(10 * 1000);
                conn.setReadTimeout(10 * 1000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoInput(true);

                int responseCode = conn.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK) {

                    is = conn.getInputStream();

                    response = convertStreamToString(is);

                }else{
                    Log.e("ayoni", ""+responseCode);
                }

            } catch (Exception e) {
                Log.e("logins", "post ERR " + e.getStackTrace()[0].getFileName() + " -> " + e.getStackTrace()[0].getLineNumber());
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(null);
            JSONArray mResult = null;
            try{
                try {
                    mJsonData = new JSONObject(aVoid);
                    mResult = mJsonData.getJSONObject("GeoInfoPublicToiletWGS").getJSONArray("row");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for(int count = 0;count <mResult.length();count++ ){
                    JSONObject mTempMarker = mResult.getJSONObject(count);
                    mMarkers.add(
                            mGoogleMap.addMarker(
                                    new MarkerOptions().position(
                                            new LatLng(
                                                    Double.valueOf(mTempMarker.getString("LAT")).doubleValue(),
                                                    Double.valueOf(mTempMarker.getString("LNG")).doubleValue()
                                            )
                                    ).title(mTempMarker.getString("HNR_NAM")+" 공중화장실").icon(BitmapDescriptorFactory.fromResource(R.drawable.h))
                            )
                    );
                    mMarkers.get(count).showInfoWindow();
                }
            }catch (Exception e){

            }
            new APICall5().execute();
            //프로그래스바 종료
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

    public class APICall5 extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            //여기서 프로그래스바 실행
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            String response = null;
            try {
                url = new URL("http://openapi.seoul.go.kr:8088/4e466a487463616e35324351624875/json/GeoInfoPublicToiletWGS/47/1046");


                HttpURLConnection conn = null;

                OutputStream os = null;
                InputStream is = null;
                ByteArrayOutputStream baos = null;

                conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(10 * 1000);
                conn.setReadTimeout(10 * 1000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoInput(true);

                int responseCode = conn.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK) {

                    is = conn.getInputStream();

                    response = convertStreamToString(is);

                }else{
                    Log.e("ayoni", ""+responseCode);
                }

            } catch (Exception e) {
                Log.e("logins", "post ERR " + e.getStackTrace()[0].getFileName() + " -> " + e.getStackTrace()[0].getLineNumber());
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(null);
            JSONArray mResult = null;
            try{
                try {
                    mJsonData = new JSONObject(aVoid);
                    mResult = mJsonData.getJSONObject("GeoInfoPublicToiletWGS").getJSONArray("row");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for(int count = 0;count <mResult.length();count++ ){
                    JSONObject mTempMarker = mResult.getJSONObject(count);
                    mMarkers.add(
                            mGoogleMap.addMarker(
                                    new MarkerOptions().position(
                                            new LatLng(
                                                    Double.valueOf(mTempMarker.getString("LAT")).doubleValue(),
                                                    Double.valueOf(mTempMarker.getString("LNG")).doubleValue()
                                            )
                                    ).title(mTempMarker.getString("HNR_NAM")+" 공중화장실").icon(BitmapDescriptorFactory.fromResource(R.drawable.h))
                            )
                    );
                    mMarkers.get(count).showInfoWindow();
                }
            }catch (Exception e){

            }
            new APICall6().execute();
            //프로그래스바 종료
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

    public class APICall6 extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            //여기서 프로그래스바 실행
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            String response = null;
            try {
                url = new URL("http://openapi.seoul.go.kr:8088/4e466a487463616e35324351624875/json/GeoInfoPublicToiletWGS/0/46");


                HttpURLConnection conn = null;

                OutputStream os = null;
                InputStream is = null;
                ByteArrayOutputStream baos = null;

                conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(10 * 1000);
                conn.setReadTimeout(10 * 1000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoInput(true);

                int responseCode = conn.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK) {

                    is = conn.getInputStream();

                    response = convertStreamToString(is);

                }else{
                    Log.e("ayoni", ""+responseCode);
                }

            } catch (Exception e) {
                Log.e("logins", "post ERR " + e.getStackTrace()[0].getFileName() + " -> " + e.getStackTrace()[0].getLineNumber());
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(null);
            JSONArray mResult = null;
            try{
                try {
                    mJsonData = new JSONObject(aVoid);
                    mResult = mJsonData.getJSONObject("GeoInfoPublicToiletWGS").getJSONArray("row");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for(int count = 0;count <mResult.length();count++ ){
                    JSONObject mTempMarker = mResult.getJSONObject(count);
                    mMarkers.add(
                            mGoogleMap.addMarker(
                                    new MarkerOptions().position(
                                            new LatLng(
                                                    Double.valueOf(mTempMarker.getString("LAT")).doubleValue(),
                                                    Double.valueOf(mTempMarker.getString("LNG")).doubleValue()
                                            )
                                    ).title(mTempMarker.getString("HNR_NAM")+" 공중화장실").icon(BitmapDescriptorFactory.fromResource(R.drawable.h))
                            )
                    );
                    mMarkers.get(count).showInfoWindow();
                }
            }catch (Exception e){

            }

            //프로그래스바 종료
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



package com.example.poonamgupta2801.jsondemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    int data;
    public class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            HttpURLConnection urlConnection=null;

            try {

                url=new URL(urls[0]);
                urlConnection=(HttpURLConnection) url.openConnection ();
                urlConnection.connect ();
                InputStream inputStream=urlConnection.getInputStream ();
                InputStreamReader inputStreamReader= new InputStreamReader ( inputStream );
                data=inputStreamReader.read ();

                while(data!=-1){

                    char currentData= (char) data;
                    result+=currentData;
                    data=  inputStreamReader.read ();
                }
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace ();
            } catch (IOException e) {
                e.printStackTrace ();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String result){
          super.onPostExecute ( result);


            try {
                JSONObject jsonObject= new JSONObject (result  );

                String weatherInfo= jsonObject.getString("weather");

                JSONArray jsonArray= new JSONArray(weatherInfo);

                for(int i=0; i<jsonArray.length (); i++){

                    JSONObject jsonPart= jsonArray.getJSONObject ( i);

                    Log.i("main",jsonPart.getString ( "main" ));

                    Log.i("description",jsonPart.getString ( "description" ));

                }


            } catch (JSONException e) {
                e.printStackTrace ();
            }

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        DownloadTask downloadTask= new DownloadTask ();
        String result= null;

        try {

            downloadTask.execute ( "http://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22" ).get ();

        } catch (InterruptedException e) {
            e.printStackTrace ();
        } catch (ExecutionException e) {
            e.printStackTrace ();
        }


    }
}

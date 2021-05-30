package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;

import static android.app.PendingIntent.getActivity;


//   1. We need to get an API
//     2. We need to get retrieve Json data as it is in Json format
//     3. Get the weather details that we need from the JSON data
//     4. Display our weather details


/// for the first step we need to:
// make a weather class that extends AsyncTask class and implement the doInBackground method
// Create a method and name it search to put the API in it, here we create a String and
// Go to Open weather then to current weather then Doc ..click on London link then delete example from url and copy the link and paste to your app
// Go to doInBackground and make a URL variable then catch exceptions
// make HttpConnection under the url variable and use that url to open connection
public class MainActivity extends AppCompatActivity {


    TextView weatherTextResult;
    EditText findCityText;


    Button btn;



    public void search(View view) {

        closeKeyboard();

        // cityToFindBtn=findViewById( R.id. )

        btn = findViewById( R.id.button );

        findCityText = findViewById( R.id.editText1 );

        weatherTextResult = findViewById( R.id.textView );
        final String cityToFind;

        cityToFind = findCityText.getText().toString();


        String c;

        // This weather instance is part of the inner Weather class down below which extends the AsyncTask
        //We use it to connect to the url we want, However all the operations are in the Weather class method ( DoInBackGround)
        // SO here we are only using the class to execute the code via a String variable ( content )
        Weather weather = new Weather();


        try {



            c = weather.execute( "https://openweathermap.org/data/2.5/weather?q="+cityToFind+"&appid=b6907d289e10d714a6e88b30761fae22" ).get();
            // we check if data retrieved or not



            Log.i( "content", c );

            //JSON
            JSONObject jsonObject = new JSONObject( c );

            String weatherData = jsonObject.getString( "weather" );

            String mainTemp = jsonObject.getString( "main" );//this is a different main

            String mainHumidity= jsonObject.getString( "main" );

            String windData = jsonObject.getString( "wind" );



            double visibility ;




            Log.i( "weatherData", weatherData );


            // second step starts here

            // we got the weather object however the weather data are in an array
            // that's why we need to make an array for these weather data

            JSONArray Array = new JSONArray( weatherData );//this is the weather Array

            String main="" ;
            String description="" ;
            String temp ="";

            String humidity="";

            String windSpeed="";

            for (int i = 0; i < Array.length(); i++) {

                JSONObject weatherPart = Array.getJSONObject( i );

                main = weatherPart.getString( "main" );

                description = weatherPart.getString( "description" );

            }

                Log.i( "main", main );
                Log.i( "description", description );


                JSONObject mainPart = new JSONObject( mainTemp );
                temp = mainPart.getString( "temp" );

                JSONObject mainH= new JSONObject( mainHumidity );

               humidity=mainH.getString( "humidity" );

               JSONObject wind= new JSONObject( windData );

               windSpeed= wind.getString( "speed" );



                visibility=Double.parseDouble(jsonObject.getString( "visibility" ) );

                int visibilityInKilometer =(int) visibility/1000;

                String result = "main : " + main +"\n"+ "description  : " + description + "\n"+"temp  : " + temp+ "\n"+"Visibility: "+visibilityInKilometer+" kms"+"\n"+"Humidity : "+humidity+"\n"+"Wind Speed : "+windSpeed + "km/h";

                weatherTextResult.setText( result );


        } catch (Exception e) {
            e.printStackTrace();

        }



    }




    public static class Weather extends AsyncTask<String, Void, String> {// first string means url is in string format, second string means the return must be string too


            @Override

            protected String doInBackground(String... urlAddress) {


                URL url;

                HttpsURLConnection connection;


                try {
                    url = new URL( urlAddress[0] );
                    connection = (HttpsURLConnection) url.openConnection();

                    // establish connection with urlAddress
                    connection.connect();
                    // retrieve data from url

                    InputStream in = connection.getInputStream();
                    InputStreamReader isr = new InputStreamReader( in );

                    //retrieve data and return as string

                    int data = isr.read();

                    String content = "";

                    char ch;


                    while (data != -1) {
                        ch = (char) data;
                        content = content + ch;
                        data = isr.read();


                    }
                    return content;


                } catch (MalformedURLException e) {
                    e.printStackTrace();
               } catch (IOException e) {
                    e.printStackTrace();
                }


                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute( s );

            }


        }

        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate( savedInstanceState );
            setContentView( R.layout.activity_main );




        }



    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    }


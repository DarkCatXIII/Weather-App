package com.play.ata.weather;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class SettingActivity extends AppCompatActivity {
    private EditText etCity;
    private String nowcity;
    private String count;
    private NotificationManager manager;
    private boolean isNotify=false;
    private int NotifyID=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        etCity = (EditText) findViewById(R.id.etCity);
    }

    public void back(View view) {
        startActivity(new Intent(SettingActivity.this, MainActivity.class));
        finish();
    }


    public void CityOk(View view) {

        nowcity = etCity.getText().toString();


        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22" + nowcity + "%2C%20eg%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject all = new JSONObject(response);
                    JSONObject query = all.getJSONObject("query");
                    count = query.getString("count");

      if (count!="0") {
                    JSONObject results = query.getJSONObject("results");
                    JSONObject channel = results.getJSONObject("channel");
                    JSONObject location = channel.getJSONObject("location");
                    nowcity = location.getString("city");

          SharedPreferences sharedPreferences = getSharedPreferences("City", MODE_PRIVATE);
          SharedPreferences.Editor editor = sharedPreferences.edit();
          editor.putString("city", nowcity);
          editor.commit();
          Toast.makeText(SettingActivity.this, nowcity, Toast.LENGTH_SHORT).show();


        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this)
                    .setMessage("This City Is Wrong")
                    .setCancelable(false)
                    .setPositiveButton("Ok", null);
            builder.show();

        }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    public boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
        if (isConnected) {

            return true;
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No internet Connection");
            builder.setMessage("Please turn on internet connection to continue");
            builder.setNeutralButton("Go To Setting", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                }
            });
            builder.setNegativeButton("close",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }
            );
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        isNetworkConnectionAvailable();
    }


    public void Notifyset(View view) {
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 00);

        Intent intent=new Intent(getApplicationContext(),Notification_reciver.class);

        PendingIntent pendingIntent= PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
    }

    public void Notifyoff(View view) {
        if(isNotify){
            manager.cancel(NotifyID);
        }
    }
}
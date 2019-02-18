package com.play.ata.weather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class OneTime extends AppCompatActivity {
    private TextView etCity2; private String nowcity,count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_time);
        etCity2= (TextView) findViewById(R.id.etCity2);
    }


    public void Go(View view) {
       startActivity(new Intent(OneTime.this, MainActivity.class));
    }

    public void CityOk(View view) {
        nowcity = etCity2.getText().toString();


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
                        Toast.makeText(OneTime.this, nowcity, Toast.LENGTH_SHORT).show();


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(OneTime.this)
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
                AlertDialog.Builder builder = new AlertDialog.Builder(OneTime.this)
                        .setTitle("Site Is Wrong")
                        .setMessage(error.getMessage())
                        .setCancelable(false)
                        .setPositiveButton("Ok", null);
                builder.show();
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}

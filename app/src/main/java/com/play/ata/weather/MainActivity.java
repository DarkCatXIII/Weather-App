package com.play.ata.weather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity  {
    private RelativeLayout r1; ImageView ivWeather; private ImageButton bRefresh;
    private TextView highNow, lowNow, speedMPH, timeSet, timeRise; private TextView nowType, txtdate,tvCity;
    private TextView d1date, d2date, d3date, d4date, d5date, d6date, d7date, d8date, d9date;
    private TextView d1max, d2max, d3max, d4max, d5max, d6max, d7max, d8max, d9max;
    private TextView d1min, d2min, d3min, d4min, d5min, d6min, d7min ,d8min, d9min;
    private String datenow, date1, date2, date3, date4, date5, date6, date7, date8, date9; private String text,currentTemp, ImageCode;
    private String high, high1, high2, high3, high4, high5, high6, high7, high8, high9; private AdView mAdView; private TextView tvTempNow;
    private String low, low1, low2, low3, low4, low5, low6, low7, low8, low9; private String speed,sunset,sunrise; private String city;
    String id="1";String id1="2";String id2="3";String id3="4";String id4="5";String id5="6";String id6="7";String id7="8";String id8="9";String id9="10";


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); r1= (RelativeLayout) findViewById(R.id.mainweather);
        r1.setBackgroundResource(R.drawable.fo);
        r1.invalidate(); tvCity= (TextView) findViewById(R.id.tvCity);
        ivWeather = (ImageView) findViewById(R.id.ivWeather);
        bRefresh= (ImageButton) findViewById(R.id.bRefresh);
        highNow= (TextView) findViewById(R.id.highNow); lowNow= (TextView) findViewById(R.id.lowNow); speedMPH= (TextView) findViewById(R.id.speedMPH); timeSet= (TextView) findViewById(R.id.timeSet); timeRise= (TextView) findViewById(R.id.timeRise); nowType= (TextView) findViewById(R.id.nowType); txtdate= (TextView) findViewById(R.id.txtdate);
        d1date= (TextView) findViewById(R.id.d1date); d2date= (TextView) findViewById(R.id.d2date); d3date= (TextView) findViewById(R.id.d3date); d4date= (TextView) findViewById(R.id.d4date); d5date= (TextView) findViewById(R.id.d5date); d6date= (TextView) findViewById(R.id.d6date); d7date= (TextView) findViewById(R.id.d7date); d8date= (TextView) findViewById(R.id.d8date); d9date= (TextView) findViewById(R.id.d9date);
        d1max= (TextView) findViewById(R.id.d1max); d2max= (TextView) findViewById(R.id.d2max); d3max= (TextView) findViewById(R.id.d3max); d4max= (TextView) findViewById(R.id.d4max); d5max= (TextView) findViewById(R.id.d5max); d6max= (TextView) findViewById(R.id.d6max); d7max= (TextView) findViewById(R.id.d7max); d8max= (TextView) findViewById(R.id.d8max); d9max= (TextView) findViewById(R.id.d9max);
        d1min= (TextView) findViewById(R.id.d1min); d2min= (TextView) findViewById(R.id.d2min); d3min= (TextView) findViewById(R.id.d3min); d4min= (TextView) findViewById(R.id.d4min); d5min= (TextView) findViewById(R.id.d5min); d6min= (TextView) findViewById(R.id.d6min); d7min= (TextView) findViewById(R.id.d7min); d8min= (TextView) findViewById(R.id.d8min); d9min= (TextView) findViewById(R.id.d9min);
        tvTempNow= (TextView) findViewById(R.id.tvTempNow);
        //===========================================================================================================================================================================================
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //===================================================================================================================================================================================================

    }
     
    @Override
    protected void onStart() {
        super.onStart();
        isNetworkConnectionAvailable();

        SharedPreferences sharedPreferences=getSharedPreferences("City",MODE_PRIVATE);
        city= sharedPreferences.getString("city",null);
        if(city == null ){startActivity(new Intent(MainActivity.this,OneTime.class));}

        java.util.Calendar calendar= java.util.Calendar.getInstance();
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 7);
        calendar.set(java.util.Calendar.MINUTE, 00);

        Intent intent=new Intent(getApplicationContext(),Notification_reciver.class);

        PendingIntent pendingIntent= PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
    }

    public void checkNetworkConnection(){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setNeutralButton("Go To Setting",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {startActivity(new Intent(Settings.ACTION_SETTINGS));}});
        builder.setNegativeButton("close",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyHelper helper=new MyHelper(MainActivity.this);
                SQLiteDatabase SQLDB=helper.getReadableDatabase();
                Cursor RDB=SQLDB.rawQuery("select * from WEATHER",null);
                String H=""; String L=""; String T=""; String D="";String SP="";String SS="";String SR="";String C="";String N="";String Co="";
                if(RDB.moveToNext()){
                    C +=RDB.getString(RDB.getColumnIndex("CITY"));N +=RDB.getString(RDB.getColumnIndex("NOW"));
                    H +=RDB.getString(RDB.getColumnIndex("HIGH"));Co +=RDB.getString(RDB.getColumnIndex("CODE"));
                    L +=RDB.getString(RDB.getColumnIndex("LOW"));
                    T +=RDB.getString(RDB.getColumnIndex("TEXT"));
                    D +=RDB.getString(RDB.getColumnIndex("DATE"));
                    SP +=RDB.getString(RDB.getColumnIndex("SPEED"));
                    SS +=RDB.getString(RDB.getColumnIndex("SUNSET"));
                    SR +=RDB.getString(RDB.getColumnIndex("SUNRISE"));
                    highNow.setText(Integer.valueOf((int) (((Double.valueOf(H))-32)/1.8)).toString());
                    lowNow.setText(Integer.valueOf((int) (((Double.valueOf(L))-32)/1.8)).toString());
                    nowType.setText(T); speedMPH.setText(SP);timeSet.setText(SS);
                    txtdate.setText(D);timeRise.setText(SR);tvCity.setText(C);
                    tvTempNow.setText(Integer.valueOf((int) (((Double.valueOf(currentTemp))-32)/1.8)).toString());
                    int resourceID=getResources().getIdentifier("drawable/icon_"+ImageCode,null,getPackageName());
                    @SuppressWarnings("deprecation")
                    Drawable WeathericonDrawable = getResources().getDrawable(resourceID);
                    ivWeather.setImageDrawable(WeathericonDrawable);

                    int resourceID2=getResources().getIdentifier("drawable/b_"+ImageCode,null,getPackageName());
                    @SuppressWarnings("deprecation")
                    Drawable WeatherbackgroundDrawable = getResources().getDrawable(resourceID2);
                    r1.setBackgroundDrawable(WeatherbackgroundDrawable);}

                Cursor RDB1=SQLDB.rawQuery("select * from WEATHERALL WHERE ID=?",new String[]{id1});
                    if(RDB1.moveToNext()){
                    String H1=""; String L1=""; String D1="";
                    H1 +=RDB1.getString(RDB1.getColumnIndex("HIGH"));
                    L1 +=RDB1.getString(RDB1.getColumnIndex("LOW"));
                    D1 +=RDB1.getString(RDB1.getColumnIndex("DATE"));
                    d1date.setText(D1); d1max.setText(Integer.valueOf((int) (((Double.valueOf(H1))-32)/1.8)).toString()); d1min.setText(Integer.valueOf((int) (((Double.valueOf(L1))-32)/1.8)).toString());}

                Cursor RDB2=SQLDB.rawQuery("select * from WEATHERALL WHERE ID=?",new String[]{id2});
                    if(RDB2.moveToNext()){
                    String H2=""; String L2=""; String D2="";
                    H2 +=RDB2.getString(RDB2.getColumnIndex("HIGH"));
                    L2 +=RDB2.getString(RDB2.getColumnIndex("LOW"));
                    D2 +=RDB2.getString(RDB2.getColumnIndex("DATE"));
                    d2date.setText(D2); d2max.setText(Integer.valueOf((int) (((Double.valueOf(H2))-32)/1.8)).toString()); d2min.setText(Integer.valueOf((int) (((Double.valueOf(L2))-32)/1.8)).toString());}

                Cursor RDB3=SQLDB.rawQuery("select * from WEATHERALL WHERE ID=?",new String[]{id3});
                   if(RDB3.moveToNext()){
                    String H3=""; String L3=""; String D3="";
                    H3 +=RDB3.getString(RDB3.getColumnIndex("HIGH"));
                    L3 +=RDB3.getString(RDB3.getColumnIndex("LOW"));
                    D3 +=RDB3.getString(RDB3.getColumnIndex("DATE"));
                    d3date.setText(D3); d3max.setText(Integer.valueOf((int) (((Double.valueOf(H3))-32)/1.8)).toString()); d3min.setText(Integer.valueOf((int) (((Double.valueOf(L3))-32)/1.8)).toString());}

                Cursor RDB4=SQLDB.rawQuery("select * from WEATHERALL WHERE ID=?",new String[]{id4});
                    if(RDB4.moveToNext()){
                    String H4=""; String L4=""; String D4="";
                    H4 +=RDB4.getString(RDB4.getColumnIndex("HIGH"));
                    L4 +=RDB4.getString(RDB4.getColumnIndex("LOW"));
                    D4 +=RDB1.getString(RDB4.getColumnIndex("DATE"));
                    d4date.setText(D4); d4max.setText(Integer.valueOf((int) (((Double.valueOf(H4))-32)/1.8)).toString()); d4min.setText(Integer.valueOf((int) (((Double.valueOf(L4))-32)/1.8)).toString());}

                Cursor RDB5=SQLDB.rawQuery("select * from WEATHERALL WHERE ID=?",new String[]{id5});
                    if(RDB5.moveToNext()){
                    String H5=""; String L5=""; String D5="";
                    H5 +=RDB5.getString(RDB5.getColumnIndex("HIGH"));
                    L5 +=RDB5.getString(RDB5.getColumnIndex("LOW"));
                    D5 +=RDB5.getString(RDB5.getColumnIndex("DATE"));
                    d5date.setText(D5); d5max.setText(Integer.valueOf((int) (((Double.valueOf(H5))-32)/1.8)).toString()); d5min.setText(Integer.valueOf((int) (((Double.valueOf(L5))-32)/1.8)).toString());}

                Cursor RDB6=SQLDB.rawQuery("select * from WEATHERALL WHERE ID=?",new String[]{id6});
                    if(RDB6.moveToNext()){
                    String H6=""; String L6=""; String D6="";
                    H6 +=RDB6.getString(RDB6.getColumnIndex("HIGH"));
                    L6 +=RDB6.getString(RDB6.getColumnIndex("LOW"));
                    D6 +=RDB6.getString(RDB6.getColumnIndex("DATE"));
                    d6date.setText(D6); d6max.setText(Integer.valueOf((int) (((Double.valueOf(H6))-32)/1.8)).toString()); d6min.setText(Integer.valueOf((int) (((Double.valueOf(L6))-32)/1.8)).toString());}

                Cursor RDB7=SQLDB.rawQuery("select * from WEATHERALL WHERE ID=?",new String[]{id7});
                    if(RDB7.moveToNext()){
                    String H7=""; String L7=""; String D7="";
                    H7 +=RDB7.getString(RDB7.getColumnIndex("HIGH"));
                    L7 +=RDB7.getString(RDB7.getColumnIndex("LOW"));
                    D7 +=RDB7.getString(RDB7.getColumnIndex("DATE"));
                    d7date.setText(D7); d7max.setText(Integer.valueOf((int) (((Double.valueOf(H7))-32)/1.8)).toString()); d7min.setText(Integer.valueOf((int) (((Double.valueOf(L7))-32)/1.8)).toString());}

                Cursor RDB8=SQLDB.rawQuery("select * from WEATHERALL WHERE ID=?",new String[]{id8});
                    if(RDB8.moveToNext()){
                    String H8=""; String L8=""; String D8="";
                    H8 +=RDB8.getString(RDB8.getColumnIndex("HIGH"));
                    L8 +=RDB8.getString(RDB8.getColumnIndex("LOW"));
                    D8 +=RDB8.getString(RDB8.getColumnIndex("DATE"));
                    d8date.setText(D8); d8max.setText(Integer.valueOf((int) (((Double.valueOf(H8))-32)/1.8)).toString()); d8min.setText(Integer.valueOf((int) (((Double.valueOf(L8))-32)/1.8)).toString());}

                Cursor RDB9=SQLDB.rawQuery("select * from WEATHERALL WHERE ID=?",new String[]{id9});
                    if(RDB9.moveToNext()){
                    String H9=""; String L9=""; String D9="";
                    H9 +=RDB9.getString(RDB9.getColumnIndex("HIGH"));
                    L9 +=RDB9.getString(RDB9.getColumnIndex("LOW"));
                    D9 +=RDB9.getString(RDB9.getColumnIndex("DATE"));
                    d9date.setText(D9); d9max.setText(Integer.valueOf((int) (((Double.valueOf(H9))-32)/1.8)).toString()); d9min.setText(Integer.valueOf((int) (((Double.valueOf(L9))-32)/1.8)).toString());}
                       Log.d("wm","not work");
                Toast.makeText(MainActivity.this, "Offline Mode", Toast.LENGTH_SHORT).show();
            }
        }
        );
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public boolean isNetworkConnectionAvailable(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
        if(isConnected) {
            getWeather();
            return true;
        }
        else{
            checkNetworkConnection();
            return false;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_button, menu);
    }

    public void showPop(View view) {
        PopupMenu popupMenu=new PopupMenu(this, view);
        MenuInflater inflater=popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_button, popupMenu.getMenu());
        PopupMenuH popupop =new PopupMenuH(getApplicationContext());
        popupMenu.setOnMenuItemClickListener(popupop);
        popupMenu.show();
    }

    private class PopupMenuH implements android.widget.PopupMenu.OnMenuItemClickListener, android.support.v7.widget.PopupMenu.OnMenuItemClickListener {
        Context context;

        PopupMenuH(Context context) {
            this.context = context;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            if(item.getItemId()==R.id.aSetting){
                startActivity(new Intent(MainActivity.this,SettingActivity.class));
                return true;
            }else if(item.getItemId()==R.id.aExit){
                finish();
                return true;
            }

            return false;
        }
    }

    public void make(View view) {
           ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

           NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
           boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
           if (isConnected) {
               bRefresh.setEnabled(false);
               ForTheThread forTheThread = new ForTheThread();
               forTheThread.execute();
               DataBase();
               DataBaseall();
           } else {
               Toast.makeText(this, "NO CONNECTION", Toast.LENGTH_SHORT).show();
           }

    }

    public void getWeather() {
        SharedPreferences sharedPreferences=getSharedPreferences("City",MODE_PRIVATE);
        city= sharedPreferences.getString("city",null);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+city+"%2C%20eg%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject all=new JSONObject(response);
                            JSONObject query=all.getJSONObject("query");
                            JSONObject results=query.getJSONObject("results");
                            JSONObject channel=results.getJSONObject("channel");
                            JSONObject wind=channel.getJSONObject("wind");
                            JSONObject location=channel.getJSONObject("location");
                            city= location.getString("city"); tvCity.setText(city);
                            speed=wind.getString("speed");
                            speedMPH.setText(speed);
                            JSONObject astronomy=channel.getJSONObject("astronomy");
                            sunset=astronomy.getString("sunset");sunrise=astronomy.getString("sunrise");
                            timeSet.setText(sunset);  timeRise.setText(sunrise);
                            JSONObject item=channel.getJSONObject("item");
                            JSONObject condition=item.getJSONObject("condition");
                            text = condition.getString("text");
                            currentTemp=condition.getString("temp");
                            ImageCode=condition.getString("code");
                            nowType.setText(text); tvTempNow.setText(Integer.valueOf((int) (((Double.valueOf(currentTemp))-32)/1.8)).toString());

                            int resourceID=getResources().getIdentifier("drawable/icon_"+ImageCode,null,getPackageName());
                            @SuppressWarnings("deprecation")
                            Drawable WeathericonDrawable = getResources().getDrawable(resourceID);
                            ivWeather.setImageDrawable(WeathericonDrawable);

                            int resourceID2=getResources().getIdentifier("drawable/b_"+ImageCode,null,getPackageName());
                            @SuppressWarnings("deprecation")
                            Drawable WeatherbackgroundDrawable = getResources().getDrawable(resourceID2);
                            r1.setBackgroundDrawable(WeatherbackgroundDrawable);

                            JSONArray forecast=item.getJSONArray("forecast");
                            JSONObject day=forecast.getJSONObject(0);
                            high = day.getString("high");
                            low = day.getString("low");
                            datenow=day.getString("day")+", "+day.getString("date");
                            txtdate.setText(datenow); highNow.setText(Integer.valueOf((int) (((Double.valueOf(high))-32)/1.8)).toString());
                            lowNow.setText(Integer.valueOf((int) (((Double.valueOf(low))-32)/1.8)).toString());

                            JSONObject day1=forecast.getJSONObject(1);
                            high1 = day1.getString("high");
                            low1 = day1.getString("low");
                            date1= day1.getString("day");
                            d1date.setText(date1); d1max.setText(Integer.valueOf((int) (((Double.valueOf(high1))-32)/1.8)).toString()); d1min.setText(Integer.valueOf((int) (((Double.valueOf(low1))-32)/1.8)).toString());

                            JSONObject day2=forecast.getJSONObject(2);
                            high2 = day2.getString("high");
                            low2 = day2.getString("low");
                            date2= day2.getString("day");
                            d2date.setText(date2); d2max.setText(Integer.valueOf((int) (((Double.valueOf(high2))-32)/1.8)).toString()); d2min.setText(Integer.valueOf((int) (((Double.valueOf(low2))-32)/1.8)).toString());

                            JSONObject day3=forecast.getJSONObject(3);
                            high3 = day3.getString("high");
                            low3 = day3.getString("low");
                            date3= day3.getString("day");
                            d3date.setText(date3); d3max.setText(Integer.valueOf((int) (((Double.valueOf(high3))-32)/1.8)).toString()); d3min.setText(Integer.valueOf((int) (((Double.valueOf(low3))-32)/1.8)).toString());

                            JSONObject day4=forecast.getJSONObject(4);
                            high4 = day4.getString("high");
                            low4 = day4.getString("low");
                            date4= day4.getString("day");
                            d4date.setText(date4); d4max.setText(Integer.valueOf((int) (((Double.valueOf(high4))-32)/1.8)).toString()); d4min.setText(Integer.valueOf((int) (((Double.valueOf(low4))-32)/1.8)).toString());

                            JSONObject day5=forecast.getJSONObject(5);
                            high5 = day5.getString("high");
                            low5 = day5.getString("low");
                            date5= day5.getString("day");
                            d5date.setText(date5); d5max.setText(Integer.valueOf((int) (((Double.valueOf(high5))-32)/1.8)).toString()); d5min.setText(Integer.valueOf((int) (((Double.valueOf(low5))-32)/1.8)).toString());

                            JSONObject day6=forecast.getJSONObject(6);
                            high6 = day6.getString("high");
                            low6 = day6.getString("low");
                            date6= day6.getString("day");
                            d6date.setText(date6); d6max.setText(Integer.valueOf((int) (((Double.valueOf(high6))-32)/1.8)).toString()); d6min.setText(Integer.valueOf((int) (((Double.valueOf(low6))-32)/1.8)).toString());

                            JSONObject day7=forecast.getJSONObject(7);
                            high7 = day7.getString("high");
                            low7 = day7.getString("low");
                            date7= day7.getString("day");
                            d7date.setText(date7); d7max.setText(Integer.valueOf((int) (((Double.valueOf(high7))-32)/1.8)).toString()); d7min.setText(Integer.valueOf((int) (((Double.valueOf(low7))-32)/1.8)).toString());

                            JSONObject day8=forecast.getJSONObject(8);
                            high8 = day8.getString("high");
                            low8 = day8.getString("low");
                            date8= day8.getString("day");
                            d8date.setText(date8); d8max.setText(Integer.valueOf((int) (((Double.valueOf(high8))-32)/1.8)).toString()); d8min.setText(Integer.valueOf((int) (((Double.valueOf(low8))-32)/1.8)).toString());

                            JSONObject day9=forecast.getJSONObject(9);
                            high9 = day9.getString("high");
                            low9 = day9.getString("low");
                            date9= day9.getString("day");
                            d9date.setText(date9); d9max.setText(Integer.valueOf((int) (((Double.valueOf(high9))-32)/1.8)).toString()); d9min.setText(Integer.valueOf((int) (((Double.valueOf(low9))-32)/1.8)).toString());
                            Log.d("car","work");
                        } catch (JSONException e) {e.printStackTrace();}
                        bRefresh.setEnabled(true);
                        DataBase();
                        DataBaseall();


                    }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}});
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void DataBase(){

        MyHelper myHelper=new MyHelper(MainActivity.this);
        SQLiteDatabase SQLDB=myHelper.getReadableDatabase();
        String count = "SELECT count(*) FROM WEATHER";
        Cursor mcursor = SQLDB.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount>0){
            ContentValues row=new ContentValues();
            row.put("id",id);
            row.put("HIGH",high);
            row.put("LOW",low);
            row.put("CODE",ImageCode);
            row.put("NOW",currentTemp);
            row.put("TEXT",text);
            row.put("DATE",datenow);
            row.put("CITY",city);
            row.put("SPEED",speed);
            row.put("SUNSET",sunset);
            row.put("SUNRISE",sunrise);
            SQLDB.update("WEATHER",row,"id=?",new String[]{id });
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
       }
        else{
            ContentValues row=new ContentValues();
            row.put("ID","1");
            row.put("HIGH",high);
            row.put("LOW",low);
            row.put("CODE",ImageCode);
            row.put("NOW",currentTemp);
            row.put("TEXT",text);
            row.put("DATE",datenow);
            row.put("SPEED",speed);
            row.put("SUNSET",sunset);
            row.put("SUNRISE",sunrise);
            SQLDB.insert("Weather",null,row);
            Toast.makeText(this, "CONNECT", Toast.LENGTH_SHORT).show();
        }

    }

    public void DataBaseall(){

        MyHelper myHelper=new MyHelper(MainActivity.this);
        SQLiteDatabase SQLDB=myHelper.getReadableDatabase();
        String count = "SELECT count(*) FROM WEATHERALL";
        Cursor mcursor = SQLDB.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount>0){
            ContentValues row=new ContentValues();
            row.put("id",id1);
            row.put("HIGH",high1);
            row.put("LOW",low1);
            row.put("DATE",date1);
            SQLDB.update("WEATHERALL",row,"id=?",new String[]{id1});
            row.put("id",id2);
            row.put("HIGH",high2);
            row.put("LOW",low2);
            row.put("DATE",date2);
            SQLDB.update("WEATHERALL",row,"id=?",new String[]{id2});
            row.put("id",id3);
            row.put("HIGH",high3);
            row.put("LOW",low3);
            row.put("DATE",date3);
            SQLDB.update("WEATHERALL",row,"id=?",new String[]{id3});
            row.put("id",id4);
            row.put("HIGH",high4);
            row.put("LOW",low4);
            row.put("DATE",date4);
            SQLDB.update("WEATHERALL",row,"id=?",new String[]{id4});
            row.put("id",id5);
            row.put("HIGH",high5);
            row.put("LOW",low5);
            row.put("DATE",date5);
            SQLDB.update("WEATHERALL",row,"id=?",new String[]{id5});
            row.put("id",id6);
            row.put("HIGH",high6);
            row.put("LOW",low6);
            row.put("DATE",date6);
            SQLDB.update("WEATHERALL",row,"id=?",new String[]{id6});
            row.put("id",id7);
            row.put("HIGH",high7);
            row.put("LOW",low7);
            row.put("DATE",date7);
            SQLDB.update("WEATHERALL",row,"id=?",new String[]{id7});
            row.put("id",id8);
            row.put("HIGH",high8);
            row.put("LOW",low8);
            row.put("DATE",date8);
            SQLDB.update("WEATHERALL",row,"id=?",new String[]{id8});
            row.put("id",id9);
            row.put("HIGH",high9);
            row.put("LOW",low9);
            row.put("DATE",date9);
            SQLDB.update("WEATHERALL",row,"id=?",new String[]{id9});
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        }
        else{
            ContentValues row=new ContentValues();

            row.put("id",id1);
            row.put("HIGH",high1);
            row.put("LOW",low1);
            row.put("DATE",date1);
            SQLDB.insert("WEATHERALL",null,row);
            row.put("id",id2);
            row.put("HIGH",high2);
            row.put("LOW",low2);
            row.put("DATE",date2);
            SQLDB.insert("WEATHERALL",null,row);
            row.put("id",id3);
            row.put("HIGH",high3);
            row.put("LOW",low3);
            row.put("DATE",date3);
            SQLDB.insert("WEATHERALL",null,row);
            row.put("id",id4);
            row.put("HIGH",high4);
            row.put("LOW",low4);
            row.put("DATE",date4);
            SQLDB.insert("WEATHERALL",null,row);
            row.put("id",id5);
            row.put("HIGH",high5);
            row.put("LOW",low5);
            row.put("DATE",date5);
            SQLDB.insert("WEATHERALL",null,row);
            row.put("id",id6);
            row.put("HIGH",high6);
            row.put("LOW",low6);
            row.put("DATE",date6);
            SQLDB.insert("WEATHERALL",null,row);
            row.put("id",id7);
            row.put("HIGH",high7);
            row.put("LOW",low7);
            row.put("DATE",date7);
            SQLDB.insert("WEATHERALL",null,row);
            row.put("id",id8);
            row.put("HIGH",high8);
            row.put("LOW",low8);
            row.put("DATE",date8);
            SQLDB.insert("WEATHERALL",null,row);
            row.put("id",id9);
            row.put("HIGH",high9);
            row.put("LOW",low9);
            row.put("DATE",date9);
            SQLDB.insert("WEATHERALL",null,row);
            Toast.makeText(this, "CONNECT", Toast.LENGTH_SHORT).show();
        }

    }

    class ForTheThread extends AsyncTask</*1*/Void,/*2*/Void,/*3*/TextView> {
        @Override
        protected TextView doInBackground(Void... params) {

            getWeather();

            return null;


        }
    }
}


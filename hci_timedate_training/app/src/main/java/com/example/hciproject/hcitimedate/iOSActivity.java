package com.example.hciproject.hcitimedate;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class iOSActivity extends ActionBarActivity implements View.OnClickListener {
    public String[] goal_dates;
    public String[] goal_times;
    public String[] input_dates = new String[20];
    public String[] input_times = new String[20];
    public int counter = 0;
    OutputStreamWriter outputWriter;
    TextView txtDate, txtTime;
    Button btnDatePicker, btnTimePicker, ok;
    private int mYear, mMonth, mDay, mHour, mMinute;
    long startTime, endTime;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    int secs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
        setContentView(R.layout.activity_i_os);
        for (int i =0; i < 20; i++)
        {
            input_times[i]="";
            input_dates[i]="";
        }
        Bundle extras = getIntent().getExtras();

        Intent intent = getIntent();
        //timer = new Chronometer(MainActivity.this);

        btnDatePicker=(Button)findViewById(R.id.btn_date_ios);
        btnTimePicker=(Button)findViewById(R.id.btn_time_ios);
        ok = (Button) findViewById(R.id.ok_ios);
        txtDate=(TextView)findViewById(R.id.in_date_ios);
        txtTime=(TextView)findViewById(R.id.in_time_ios);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Instructions for IOS UI.");
        builder.setPositiveButton("Start", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //startTime = System.nanoTime();
                //if (run.equals("2")) {
                //    ctimer = new MyCountDown(11000, 1000);
                //}
                // TODO: start timer
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();

        //timer = (TextView) findViewById(R.id.timer);

        goal_dates = intent.getStringExtra(MainActivity.GOAL_DATES).split(",");
        goal_times = intent.getStringExtra(MainActivity.GOAL_TIMES).split(",");

        //timer = (TextView) findViewById(R.id.timer);

        //builder.setMessage("You're about to start trials on a new interface. There will be "+ goal_times.length /2 +" trials for this interface.\n\nReady to begin?\n\nPlease enter\n" + getTitle(counter));
        getSupportActionBar().setTitle(getTitle(counter));

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_i_o, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onClick(View v) {

        if (v == btnDatePicker) {
            // Get Current Date
            startTime = System.nanoTime();

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            int month = monthOfYear + 1;
                            if (month < 10)
                            {
                                if (dayOfMonth < 10)
                                {
                                    input_dates[counter] = "0" + month + "-0" + dayOfMonth + "-" + "2016";
                                }
                                else
                                {
                                    input_dates[counter] = "0" + month + "-" + dayOfMonth + "-" + "2016";
                                }
                            }
                            else
                            {
                                if (dayOfMonth < 10)
                                {
                                    input_dates[counter] = month + "-0" + dayOfMonth + "-" + "2016";
                                }
                                else
                                {
                                    input_dates[counter] = month + "-" + dayOfMonth + "-" + "2016";
                                }
                            }
                            //input_dates[counter] = monthOfYear+1 + "-" + dayOfMonth + "-" + "2016";
                            Log.v("Counter:",String.valueOf(counter));
                            Log.v("Input Date:", input_dates[counter]);
                            txtDate.setText(getMonth(monthOfYear +1) + " " + dayOfMonth);
                        }
                    }, 2016, 6, 1);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            if (hourOfDay > 12) {
                                hourOfDay = hourOfDay - 12;
                                if (hourOfDay < 10)
                                {
                                    if (minute < 10)
                                    {
                                        txtTime.setText("0" + hourOfDay + ":0" + minute + " PM");
                                    }
                                    else
                                    {
                                        txtTime.setText("0" + hourOfDay + ":" + minute + " PM");
                                    }
                                }
                                else
                                {
                                    if (minute < 10)
                                    {
                                        txtTime.setText(hourOfDay + ":0" + minute + " PM");
                                    }
                                    else
                                    {
                                        txtTime.setText(hourOfDay + ":" + minute + " PM");
                                    }
                                }
                                //txtTime.setText(hourOfDay + ":" + minute + " PM");
                            } else
                            {
                                if (hourOfDay < 10)
                                {
                                    if (minute < 10)
                                    {
                                        txtTime.setText("0" + hourOfDay + ":0" + minute + " AM");
                                    }
                                    else
                                    {
                                        txtTime.setText("0" + hourOfDay + ":" + minute + " AM");
                                    }
                                }
                                else
                                {
                                    if (minute < 10)
                                    {
                                        txtTime.setText(hourOfDay + ":0" + minute + " AM");
                                    }
                                    else
                                    {
                                        txtTime.setText(hourOfDay + ":" + minute + " AM");
                                    }
                                }
                                //txtTime.setText(hourOfDay + ":" + minute + " AM");
                            }

                            input_times[counter] = txtTime.getText().toString();
                            Log.v("Counter:",String.valueOf(counter));
                            Log.v("Input Date:", input_times[counter]);
                        }
                    }, 12, 0, false);
            timePickerDialog.show();
        }

        if (v == ok)
        {
            //Check if accurate
            boolean result = false;
            //01-01-2016
            String[] date = txtDate.getText().toString().split(" ");
            //if (input_times[counter])
            if (input_times[counter].equals(goal_times[counter]) && goal_dates[counter].equals(input_dates[counter]))
            {
                result = true;
            }
            else
            {
                result = false;
            }
            endTime = System.nanoTime();
            //counter++;

            long minutesElapsed = ((endTime - startTime)/1000000000)/60;
            long secondsElapsed = (endTime - startTime)/1000000000;
            long millisElapsed = ((endTime - startTime)%1000000000)/1000000;
            String time = "";
            if (minutesElapsed < 10)
            {
                time = time + "0" + minutesElapsed;
            }
            else
            {
                time = time + minutesElapsed;
            }
            if (secondsElapsed < 10)
            {
                time = time + ":0" + secondsElapsed;
            }
            else
            {
                time = time + ":" + secondsElapsed;
            }
            if (millisElapsed < 10)
            {
                time = time + ".00" + millisElapsed;
            }
            else if (millisElapsed < 100)
            {
                time = time + ".0" + millisElapsed;
            }
            else
            {
                time = time + "." +millisElapsed;
            }

            //timer.setText(time);
                setResult(RESULT_OK);
                finish();
        }
    }

    public String getMonth(int month) {
        String monthString;
        switch (month) {
            case 1:  monthString = "January";
                break;
            case 2:  monthString = "February";
                break;
            case 3:  monthString = "March";
                break;
            case 4:  monthString = "April";
                break;
            case 5:  monthString = "May";
                break;
            case 6:  monthString = "June";
                break;
            case 7:  monthString = "July";
                break;
            case 8:  monthString = "August";
                break;
            case 9:  monthString = "September";
                break;
            case 10: monthString = "October";
                break;
            case 11: monthString = "November";
                break;
            case 12: monthString = "December";
                break;
            default: monthString = "Invalid month";
                break;
        }
        return monthString;
    }

    public String getTitle(int counter) {
        String month = getMonth(Integer.parseInt(goal_dates[counter].split("-")[0]));
        Log.v("month", month);
        String day = (goal_dates[counter].split("-")[1]);
        String datetime = month + " " +day+ ", " + goal_times[counter];

        return datetime;
    }
}

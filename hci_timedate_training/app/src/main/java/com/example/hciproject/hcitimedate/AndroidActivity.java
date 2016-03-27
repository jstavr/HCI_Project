package com.example.hciproject.hcitimedate;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.CountDownTimer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;


public class AndroidActivity extends ActionBarActivity implements
        View.OnClickListener {

        public String[] goal_dates;
        public String[] goal_times;
        public String[] input_dates = new String[20];
        public String[] input_times = new String[20];
        public int counter = 0;
        public int secs;
        //public int last = 1;
        Button btnDatePicker, btnTimePicker, ok;
        TextView txtDate, txtTime;
        long startTime, endTime;
        private int mYear, mMonth, mDay, mHour, mMinute;
        DatePickerDialog datePickerDialog;
        TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android);
        for (int i =0; i < 20; i++)
        {
            input_times[i]="";
            input_dates[i]="";
        }
        Intent intent = getIntent();
        //intent.putExtra("ID",participant_id);
        Bundle extras = getIntent().getExtras();

        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        ok = (Button) findViewById(R.id.ok);
        txtDate=(TextView)findViewById(R.id.in_date);
        txtTime=(TextView)findViewById(R.id.in_time);



        goal_dates = intent.getStringExtra(MainActivity.GOAL_DATES).split(",");
        goal_times = intent.getStringExtra(MainActivity.GOAL_TIMES).split(",");


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Instructions for Android UI.");
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
        getSupportActionBar().setTitle(getTitle(counter));

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_android, menu);
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
                    }, 12, 00, false);
            timePickerDialog.show();
        }
        if (v == ok)
        {
            //Check if accurate
            boolean result = false;
            //01-01-2016
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
            System.out.println("Counter = " + counter);

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

//            Intent intent = getIntent()
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
        String datetime = month + " " + day + ", " + goal_times[counter];

        return datetime;
    }

}

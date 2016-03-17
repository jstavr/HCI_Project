package com.calendarapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import java.io.File;

import java.io.IOException;
import java.util.Calendar;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import android.os.Environment;
public class MainActivity extends Activity implements
        View.OnClickListener {

    public String[] goal_dates = {"01-01-2016","29-05-1991"};
    public String[] goal_times = {"08:07 AM","10:03 PM"};
    public int counter = 0;
    public int last = 1;
    Button btnDatePicker, btnTimePicker, ok;
    TextView txtDate, txtTime, timer, goalDate, goalTime;
    long startTime, endTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    OutputStreamWriter outputWriter;
    //File file = new File(context.getilesDir(), filename);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //String MY_FILE_NAME = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mytextfile.txt";
        // Create a new output file stream
        try {
            File output = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "mytextfile.txt");
            FileOutputStream fileOut = new FileOutputStream(output);

            //FileOutputStream fileOut=openFileOutput(my, MODE_PRIVATE);
            outputWriter=new OutputStreamWriter(fileOut);
            //outputWriter.write(textmsg.getText().toString());

            //display file saved message
        } catch (Exception e) {
            e.printStackTrace();
        }
        //timer = new Chronometer(MainActivity.this);
        setContentView(R.layout.activity_main);

        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        ok = (Button) findViewById(R.id.ok);
        txtDate=(TextView)findViewById(R.id.in_date);
        txtTime=(TextView)findViewById(R.id.in_time);
        goalDate=(TextView)findViewById(R.id.goal_date);
        goalTime=(TextView)findViewById(R.id.goal_time);

        timer = (TextView) findViewById(R.id.timer);


        goalDate.setText(goal_dates[counter]);
        goalTime.setText(goal_times[counter]);
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        ok.setOnClickListener(this);
        setTitle(goal_dates[counter] + " " + goal_times[counter]);

    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {
            // Get Current Date
            startTime = System.nanoTime();
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            if (hourOfDay > 12) {
                                hourOfDay = hourOfDay - 12;
                                txtTime.setText(hourOfDay + ":" + minute + " PM");
                            }
                            else
                            {
                                txtTime.setText(hourOfDay + ":" + minute + " AM");
                            }
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if (v == ok)
        {

            //Check if accurate
            endTime = System.nanoTime();
            counter++;

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
            timer.setText(time);
            try {
                outputWriter.write(time + "\n");
            }
            catch (Exception e)
            {

            }
//            Intent intent = getIntent();
            if (counter <= last) {
                goalDate.setText(goal_dates[counter]);
                goalTime.setText(goal_times[counter]);

                txtTime.setText("");
                txtDate.setText("");
                setTitle(goal_dates[counter] + " " + goal_times[counter]);
            }
            else
            {
                try {
                    outputWriter.close();
                }
                catch (IOException e)
                {

                }
                finish();
            }
  //
   //         startActivity(intent);
        }
    }


}

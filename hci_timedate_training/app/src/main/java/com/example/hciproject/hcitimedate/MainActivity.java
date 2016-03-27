package com.example.hciproject.hcitimedate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import android.widget.RadioGroup;
public class MainActivity extends ActionBarActivity {

    public final static String GOAL_DATES = "com.mycompany.myfirstapp.DATES";
    public final static String GOAL_TIMES = "com.mycompany.myfirstapp.TIMES";
    public final static String LEFT = "com.mycompany.myfirstapp.LEFT";
    public final static String TASK_NO = "1";

    //month-date-year
//    String dates = "01-01-2016,05-29-2016,03-23-2016,04-17-2016,11-30-2016,08-02-2016,10-01-2016,09-14-2016,04-01-2016,06-11-2016";
//    String times = "08:07 AM,10:03 PM,6:00 PM,2:30 PM,11:45 AM,8:00 AM,12:00 PM,7:00 PM,11:11 AM,1:15 PM";


    // Provides a balance of distances July is in the middle, 01 at the top, 12 at the bottom, 00 at the top, PM at bottom
    String startDate = "07-1-2016"; // Starting position should offer no advantage to any interface
    String startTime = "12:00 PM";

    String dates;
    String times;

    static final int FIRST_REQUEST = 1;  // The request code
    int ANDROID = 10;
    int IOS = 20;
    int NEW = 30;
    static final int SECOND_REQUEST = 2;  // The request code
    static final int THIRD_REQUEST = 3;  // The request code
    static final int FORTH_REQUEST = 4;  // The request code
    static final int FIFTH_REQUEST = 5;  // The request code
    static final int SIXTH_REQUEST = 6;  // The request code
    boolean left = true;
    int first;
    int second;
    int third;
    int forth;
    int fifth;
    int sixth;
    CheckBox checkbox;
    Button start, startAndroid, startIOS, startNew;
    // android, ios, new
    int [] order = {1, 2, 3};



    ////// For random date and time

    Random randnum;

    private String randomDates(int numDates){
        randnum.setSeed(123456789);
        String dates = "";
        for(int i = 0; i < numDates - 1; i++)
        {
            dates += randomDate() + ",";
        }
        dates += randomDate();
        return dates;
    }

    private String randomTimes(int numTimes){
        randnum.setSeed(987654321);
        String times = "";
        boolean onFives = true;
        for(int i = 0; i < numTimes - 1; i++)
        {
            times += randomTime(onFives) + ",";
            if(i % 2 == 0)
                onFives = !onFives;
        }
        times += randomTime(onFives);
        return times;
    }

    private String randomTime(boolean onFives)
    {
        String time;
        int hour = randBetween(0, 23);
        int ampm = randBetween(0,1);
        int min;

        if(onFives) {
            min = 5*randBetween(0, 11);
        }
        else
            min = randBetween(0, 59);

        SimpleDateFormat dfDateTime  = new SimpleDateFormat("hh:mm a");
        GregorianCalendar gc = new GregorianCalendar(2016, 1, 1, hour, min);

        return dfDateTime.format(gc.getTime());
    }


    private String randomDate()
    {

        SimpleDateFormat dfDateTime  = new SimpleDateFormat("MM-dd-yyyy");
        int year = 2016;
        int month = randBetween(0, 11);

        GregorianCalendar gc = new GregorianCalendar(year, month, 1);
        int day = randBetween(1, gc.getActualMaximum(gc.DAY_OF_MONTH));

        gc.set(year, month, day);

        return dfDateTime.format(gc.getTime());
    }

    private int randBetween(int min, int max) {
        return randnum.nextInt(max - min + 1) + min;
    }

    //////////




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create date and times
        randnum = new Random();
        dates = randomDates(20);
        times = randomTimes(20);
        System.out.println(dates);
        System.out.println(times);

        checkbox=(CheckBox)findViewById(R.id.lefthanded);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                boolean checked = ((CheckBox) buttonView).isChecked();
                switch(buttonView.getId()) {
                    case R.id.lefthanded:
                        if (checked){
                            left = true;
                        }
                        else
                        {
                            left = false;
                        }
                        break;
                }
            }
        });
        start = (Button)findViewById(R.id.our_start);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkbox = (CheckBox) findViewById(R.id.lefthanded);
                checkbox.setVisibility(View.GONE);
                start.setVisibility(View.GONE);
            }
        });
        startAndroid = (Button)findViewById(R.id.start_android);
        startIOS = (Button)findViewById(R.id.start_ios);
        startNew = (Button)findViewById(R.id.start_new);
        startAndroid.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startAndroid();
            }
        });

        startIOS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startiOS();
            }
        });

        startNew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startNew();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void startAndroid() {
        Intent intent = new Intent(this, AndroidActivity.class);
        intent.putExtra(GOAL_DATES, dates);
        intent.putExtra(GOAL_TIMES, times);
        //intent.putExtra("ID",participant_id);
        //intent.putExtra("run", run);
        //intent.putExtra("request",request);
        startActivityForResult(intent, FIRST_REQUEST);
    }

    public void startiOS() {
        Intent intent = new Intent(this, iOSActivity.class);
        intent.putExtra(GOAL_DATES, dates);
        intent.putExtra(GOAL_TIMES, times);
        //intent.putExtra("ID",participant_id);
        //intent.putExtra("run", run);
        //intent.putExtra("request",request);
        startActivityForResult(intent, FIRST_REQUEST);
    }

    public void startNew() {
        Intent intent = new Intent(this, NewActivity.class);
        intent.putExtra(LEFT,left);
        intent.putExtra(GOAL_DATES, dates);
        intent.putExtra(GOAL_TIMES, times);
        //intent.putExtra("ID",participant_id);
        //intent.putExtra("run", run);
        //intent.putExtra("request",request);
        startActivityForResult(intent, FIRST_REQUEST);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle extras = getIntent().getExtras();
        String run = "1";
        if (extras != null) {
            run = extras.getString("run");
            Log.v("run",String.valueOf("run"));
        }
        // Check which request we're responding to
        if (requestCode == FIRST_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                switch (second) {

                    case 1:
                        Log.v("second", "android");
                        //startAndroid(SECOND_REQUEST, run);
                        break;
                    case 2:
                        Log.v("second", "ios");
                        //startiOS(SECOND_REQUEST, run);
                        break;
                    case 3:
                        Log.v("third", "new");
                        //startNew(SECOND_REQUEST, run);
                        break;
                    default:
                        break;
                }
            }

        }
        // Check which request we're responding to
        if (requestCode == SECOND_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                switch (third) {
                    case 1:
                        Log.v("third", "android");
                        //startAndroid(THIRD_REQUEST, run);
                        break;
                    case 2:
                        Log.v("third", "ios");
                        //startiOS(THIRD_REQUEST, run );
                        break;
                    case 3:
                        Log.v("third", "new");
                        //startNew(THIRD_REQUEST, run);
                        break;
                    default:
                        break;
                }
            }

        }
        // Check which request we're responding to
        if (requestCode == THIRD_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                run = "2";
                switch (forth) {
                    case 1:
                        Log.v("forth", "android");
                        //startAndroid(FORTH_REQUEST, run);
                        break;
                    case 2:
                        Log.v("forth", "ios");
                        //startiOS(FORTH_REQUEST, run );
                        break;
                    case 3:
                        Log.v("forth", "new");
                        //startNew(FORTH_REQUEST, run);
                        break;
                    default:
                        break;
                }
            }

        }
        if (requestCode == FORTH_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                run = "2";
                switch (fifth) {
                    case 1:
                        Log.v("fifth", "android");
                        //startAndroid(FIFTH_REQUEST, run);
                        break;
                    case 2:
                        Log.v("forth", "ios");
                        //startiOS(FIFTH_REQUEST, run );
                        break;
                    case 3:
                        Log.v("forth", "new");
                        //startNew(FIFTH_REQUEST, run);
                        break;
                    default:
                        break;
                }
            }

        }
        if (requestCode == FIFTH_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                run = "2";
                switch (sixth) {
                    case 1:
                        Log.v("sixth", "android");
                        //startAndroid(SIXTH_REQUEST, run);
                        break;
                    case 2:
                        Log.v("sixth", "ios");
                        //startiOS(SIXTH_REQUEST, run );
                        break;
                    case 3:
                        Log.v("sixth", "new");
                        //startNew(SIXTH_REQUEST, run);
                        break;
                    default:
                        break;
                }
            }

        }
        if (requestCode == SIXTH_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Well done! Please hand the phone back.");
                builder.setPositiveButton("Complete", new DialogInterface.OnClickListener() {
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
                //finish();
                }
            }

        }
    }

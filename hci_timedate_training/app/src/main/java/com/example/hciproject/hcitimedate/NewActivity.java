package com.example.hciproject.hcitimedate;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.MotionEvent;
import android.app.Activity;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.content.Context;
import android.graphics.Paint.Align;
import android.graphics.Shader.TileMode;
import android.os.Handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
//import android.widget.Button;


public class NewActivity extends ActionBarActivity {

    public String[] goal_dates;
    public String[] goal_times;
    public boolean left;
    public int counter = 0;
    public boolean okTouched;
    private static Context context;
    String run = "1";
    public int request = 0;
    String setHour = "12", setMin = "00", setDay = "01", setMonth="07";
    String setAM = "AM";


    DrawingView dv ;
    private Paint mPaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();


        goal_dates = intent.getStringExtra(MainActivity.GOAL_DATES).split(",");
        goal_times = intent.getStringExtra(MainActivity.GOAL_TIMES).split(",");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            left = extras.getBoolean(MainActivity.LEFT);
            run = extras.getString("run");
            request = extras.getInt("request");
            Log.v("left",String.valueOf(left));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Instructions for New UI.");
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
        //builder.setMessage("You're about to start trials on a new interface. There will be "+ goal_times.length /2 +" trials for this interface.\n\nReady to begin?\n\nPlease enter\n" + getTitle(counter));

        getSupportActionBar().setTitle(getTitle(counter));


        dv = new DrawingView(this);
        setContentView(dv);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);

        NewActivity.context = this;

    }

    public static Context getAppContext() {
        return NewActivity.context;
    }

    public class DrawingView extends View {

        public int width;
        public  int height;
        private Bitmap  mBitmap;
        private Canvas  mCanvas;
        private Path    mPath;
        private Paint   mBitmapPaint;
        Context context;
        private Paint circlePaint;
        private Path circlePath;

        private Paint textPaint;
        private Paint layoutPaint;

        private Shader monthShader;
        private Shader dayShader;
        private Shader hourShader;
        private Shader ampmShader;
        private Shader minuteShader;

        private float monthEnd;
        private float dayEnd;
        private float hourEnd;
        private float ampmEnd;
        private float minuteEnd;

        private float titleSize;
        private float titleSpacing;
        private float numberSize;
        private float topOffset;
        private float monthSize;
        private float monthSpacing;
        float daysSpacing;
        float ampmSpacing;
        float minuteSpacing;
        private float ruleSize;

        private int swiping;
        private float startTouchX, startTouchY;

        private float mWidth;
        private float mHeight;

        private int[] selections;

        private int touchRegion; // 0 for no touch, 1 for month, 2 for day, 3 for hour, 4 for ampm, 5 for minute
        private int lastTouchRegion;
        private long startTouchTime;
        private long releaseTime;
        private boolean justReleased;

        private int[] daysPerMonth;

        private boolean startTouchTimerCancelled;

        private boolean leftHanded;

        private float okButtonTopLeftX, okButtonTopLeftY, okButtonBottomRightX, okButtonBottomRightY;
        private boolean hasTouched;
        //public boolean okTouched;

        public DrawingView(Context c) {
            super(c);
            context=c;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);

            circlePaint = new Paint();
            circlePath = new Path();
            circlePaint.setAntiAlias(true);
            circlePaint.setColor(Color.BLUE);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeJoin(Paint.Join.MITER);
            circlePaint.setStrokeWidth(4f);

            textPaint = new Paint();
            textPaint.setAntiAlias(true);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(30);
            textPaint.setAntiAlias(true);
            textPaint.setTextAlign(Align.LEFT);

            layoutPaint = new Paint();
            layoutPaint.setColor(Color.WHITE);
            layoutPaint.setAntiAlias(true);
            layoutPaint.setStyle(Paint.Style.FILL);
            layoutPaint.setTextAlign(Align.CENTER);

//            monthShader = new LinearGradient(0, 0, monthEnd, 0, Color.rgb(221,120,101), Color.rgb(210,92,72), TileMode.CLAMP);
//            dayShader = new LinearGradient(monthEnd, 0, minuteEnd, 0, Color.rgb(221,160,104), Color.rgb(210,138,77), TileMode.CLAMP);
//            hourShader = new LinearGradient(dayEnd, 0, hourEnd, 0, Color.rgb(124,209,138), Color.rgb(88,203,115), TileMode.CLAMP);
//            ampmShader = new LinearGradient(hourEnd, 0, ampmEnd, 0, Color.rgb(124,209,138), Color.rgb(88,203,115), TileMode.CLAMP);
//            minuteShader = new LinearGradient(ampmEnd, 0, minuteEnd, 0, Color.rgb(181,221,105), Color.rgb(166,209,77), TileMode.CLAMP);

            selections = new int[5];

            daysPerMonth = new int[]{31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

            leftHanded = left;

            selections[0] = 6;
            selections[1] = 1;
            selections[2] = 12;
            selections[3] = 0;
            selections[4] = 1;


            touchRegion = 1;
            lastTouchRegion = 1;

            swiping = 0;
            startTouchX = 0;
            startTouchY = 0;

            startTouchTime = 0;
            releaseTime = 0;
            justReleased = false;

            startTouchTimerCancelled = false;

            okTouched = false;
            hasTouched = false;


        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);




            mWidth = getWidth();
            mHeight = getHeight();


            // Draw layout

            float division = (float)getWidth()/8;

            monthEnd = division;
            dayEnd = monthEnd + division;
            hourEnd = dayEnd + division;
            minuteEnd = hourEnd + division;
            ampmEnd = minuteEnd + division;



            //// Spacings

            titleSize = (float)getHeight() / 30;
            titleSpacing = (float)1.5*titleSize;
            numberSize = (float)getHeight() / 45;


            float regionStart;
            float mainTimeStartPos;
            if(leftHanded) {
                regionStart = 0;
                mainTimeStartPos = getWidth() / 2;
            }
            else {
                regionStart = getWidth() / 2;
                mainTimeStartPos = 0;
            }



            topOffset = 2.5f * titleSpacing;
            monthSize = (float)getHeight() / 35;
            monthSpacing = (float)2.25*monthSize;
            daysSpacing = (float)1.25*numberSize;
            ampmSpacing = ( 12 * monthSpacing) / 2;
            minuteSpacing = 31*daysSpacing / 60;
            ruleSize = .25f*minuteSpacing;

//            monthShader = new LinearGradient(getWidth() / 2, 0, getWidth(), 0, Color.rgb(221, 120, 101), Color.rgb(210, 92, 72), TileMode.CLAMP);
//            dayShader = new LinearGradient(getWidth() / 2, 0, getWidth(), 0, Color.rgb(221, 160, 104), Color.rgb(210, 138, 77), TileMode.CLAMP);
//            hourShader = new LinearGradient(getWidth() / 2, 0, getWidth(), 0, Color.rgb(124, 209, 138), Color.rgb(88, 203, 115), TileMode.CLAMP);
//            minuteShader = new LinearGradient(getWidth() / 2, 0, getWidth(), 0, Color.rgb(181, 221, 105), Color.rgb(166, 209, 77), TileMode.CLAMP);
//            ampmShader = new LinearGradient(getWidth() / 2, 0, getWidth(), 0, Color.rgb(150, 209, 150), Color.rgb(128, 203, 145), TileMode.CLAMP);

//            monthShader = new LinearGradient(getWidth() / 2, 0, getWidth(), 0, Color.rgb(221, 120, 101), Color.rgb(195, 76, 56), TileMode.CLAMP);
//            dayShader = new LinearGradient(getWidth() / 2, 0, getWidth(), 0, Color.rgb(105, 181, 221), Color.rgb(87, 160, 199), TileMode.CLAMP);
//            hourShader = new LinearGradient(getWidth() / 2, 0, getWidth(), 0, Color.rgb(124, 209, 138), Color.rgb(88, 203, 115), TileMode.CLAMP);
//            minuteShader = new LinearGradient(getWidth() / 2, 0, getWidth(), 0, Color.rgb(181, 221, 105), Color.rgb(166, 209, 77), TileMode.CLAMP);
//            ampmShader = new LinearGradient(getWidth() / 2, 0, getWidth(), 0, Color.rgb(248, 172, 103), Color.rgb(224, 147, 76), TileMode.CLAMP);

            monthShader = new LinearGradient(regionStart, 0, regionStart + getWidth()/2, 0, Color.rgb(221, 120, 101), Color.rgb(210, 92, 72), TileMode.CLAMP);
            dayShader = new LinearGradient(regionStart, 0, regionStart + getWidth()/2, 0, Color.rgb(105, 181, 221), Color.rgb(62, 144, 184), TileMode.CLAMP);
            hourShader = new LinearGradient(regionStart, 0, regionStart + getWidth()/2, 0, Color.rgb(124, 209, 138), Color.rgb(77, 192, 103), TileMode.CLAMP);
            minuteShader = new LinearGradient(regionStart, 0, regionStart + getWidth()/2, 0, Color.rgb(181, 221, 105), Color.rgb(138, 180, 52), TileMode.CLAMP);
            ampmShader = new LinearGradient(regionStart, 0, regionStart + getWidth()/2, 0, Color.rgb(248, 172, 103), Color.rgb(204, 128, 59), TileMode.CLAMP);

//            monthShader = new LinearGradient(getWidth() / 2, 0, getWidth(), 0, Color.rgb(221, 120, 101), Color.rgb(221, 120, 101), TileMode.CLAMP);
//            dayShader = new LinearGradient(getWidth() / 2, 0, getWidth(), 0, Color.rgb(105, 181, 221), Color.rgb(105, 181, 221), TileMode.CLAMP);
//            hourShader = new LinearGradient(getWidth() / 2, 0, getWidth(), 0, Color.rgb(124, 209, 138), Color.rgb(124, 209, 138), TileMode.CLAMP);
//            minuteShader = new LinearGradient(getWidth() / 2, 0, getWidth(), 0, Color.rgb(181, 221, 105), Color.rgb(181, 221, 105), TileMode.CLAMP);
//            ampmShader = new LinearGradient(getWidth() / 2, 0, getWidth(), 0, Color.rgb(248, 172, 103), Color.rgb(248, 172, 103), TileMode.CLAMP);






            textPaint.setTextAlign(Align.LEFT);




            if(touchRegion != 0) {
//                drawRegion(touchRegion - 1, false, division * 2, ampmEnd + division, canvas);
                drawRegion(touchRegion - 1, false, regionStart, getWidth() / 2, canvas);

            }
//            else if(touchRegion == 0)
//            {
//                okButton.setVisibility(View.VISIBLE);
//            }




            ///// Titles

            textPaint.setTextSize(0.8f*titleSize);

            //textPaint.setTypeface(Typeface.create("normal", Typeface.BOLD));
            textPaint.setTypeface(Typeface.create("normal", Typeface.NORMAL));

            // Month
            float divs = getWidth() / 2 / 7.0f;
            float currentPOS = regionStart;
            textPaint.setTextAlign(Align.CENTER);

            float touchMid = 1.5f * divs;
            float nonTouchMid = .5f * divs;
            float touchSpace = 3.0f * divs;
            float nonTouchSpace  = divs;

            float spacing = nonTouchSpace;
            float spacingMid = nonTouchMid;
            float offset = 1.5f*titleSpacing;
            String text = "month";

            Shader shader = new Shader();
            layoutPaint.setColor(Color.WHITE);




//            if (touchRegion != 0) {
            if (touchRegion == 1) {
                text = "month";
                spacing = touchSpace;
                spacingMid = touchMid;
            } else {
                text = "m";
                spacing = nonTouchSpace;
                spacingMid = nonTouchMid;
            }
            shader = monthShader;
            layoutPaint.setShader(shader);
            canvas.drawRect(currentPOS, 0, currentPOS + spacing, offset, layoutPaint);
            canvas.drawText(text, currentPOS + spacingMid, titleSpacing, textPaint);
            currentPOS += spacing;


            if (touchRegion == 2) {
                text = "day";
                spacing = touchSpace;
                spacingMid = touchMid;
            } else {
                text = "d";
                spacing = nonTouchSpace;
                spacingMid = nonTouchMid;
            }
            shader = dayShader;
            layoutPaint.setShader(shader);
            canvas.drawRect(currentPOS, 0, currentPOS + spacing, offset, layoutPaint);
            canvas.drawText(text, currentPOS + spacingMid, titleSpacing, textPaint);
            currentPOS += spacing;


            if (touchRegion == 3) {
                text = "hour";
                spacing = touchSpace;
                spacingMid = touchMid;
            } else {
                text = "h";
                spacing = nonTouchSpace;
                spacingMid = nonTouchMid;
            }
            shader = hourShader;
            layoutPaint.setShader(shader);
            canvas.drawRect(currentPOS, 0, currentPOS + spacing, offset, layoutPaint);
            canvas.drawText(text, currentPOS + spacingMid, titleSpacing, textPaint);
            currentPOS += spacing;


            if (touchRegion == 4) {
                text = "minute";
                spacing = touchSpace;
                spacingMid = touchMid;
            } else {
                text = "m";
                spacing = nonTouchSpace;
                spacingMid = nonTouchMid;
            }
            shader = minuteShader;
            layoutPaint.setShader(shader);
            canvas.drawRect(currentPOS, 0, currentPOS + spacing, offset, layoutPaint);
            canvas.drawText(text, currentPOS + spacingMid, titleSpacing, textPaint);
            currentPOS += spacing;


            if (touchRegion == 5) {
                text = "am/pm";
                spacing = touchSpace;
                spacingMid = touchMid;
            } else {
                text = "ap";
                spacing = nonTouchSpace;
                spacingMid = nonTouchMid;
            }
            shader = ampmShader;
            layoutPaint.setShader(shader);
            canvas.drawRect(currentPOS, 0, currentPOS + spacing, offset, layoutPaint);
            canvas.drawText(text, currentPOS + spacingMid, titleSpacing, textPaint);

            // Little bars underneath

            layoutPaint.setShader(new Shader());
            layoutPaint.setColor(Color.argb(255 / 2, 255, 255, 255));

            currentPOS = regionStart;


            for(int i = 1; i < 6; i++ ) {
                if (i != touchRegion) {
                    canvas.drawRect(currentPOS, offset - ruleSize / 2, currentPOS + nonTouchSpace, offset + ruleSize / 2, layoutPaint);
                    currentPOS += nonTouchSpace;
                } else {
                    currentPOS += touchSpace;
                }
            }
//            }

            layoutPaint.setShader(new Shader());







            // Main date

            textPaint.setTypeface(Typeface.create("normal", Typeface.NORMAL));
            textPaint.setColor(Color.rgb(2 * 255 / 4, 2 * 255 / 4, 2 * 255 / 4));
            textPaint.setTextAlign(Align.CENTER);
            textPaint.setTextSize(getHeight() / 25);

            String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "Novemeber", "December"};

            String[] amorpm = {"AM", "PM"};
            canvas.drawText(months[selections[0]] + " " + Integer.toString(selections[1]), mainTimeStartPos + getWidth() / 4, getHeight() / 2 - titleSize, textPaint);



            if(selections[3] < 10)
                canvas.drawText( Integer.toString(selections[2]) + ":0" + Integer.toString(selections[3]) + " " + amorpm[selections[4]], mainTimeStartPos + getWidth() / 4, getHeight() / 2 + titleSize, textPaint);
            else
                canvas.drawText(Integer.toString(selections[2]) + ":" + Integer.toString(selections[3]) + " " + amorpm[selections[4]], mainTimeStartPos + getWidth() / 4, getHeight() / 2 + titleSize, textPaint);



            ///// Ok button

            layoutPaint.setStyle(Paint.Style.STROKE);
            layoutPaint.setStrokeWidth(ruleSize);
            layoutPaint.setStrokeMiter(ruleSize);
            layoutPaint.setColor(Color.rgb(3*255/4, 3*255/4, 3*255/4));

            if(okTouched)
                textPaint.setColor(Color.rgb(180, 20, 180));
            else
                textPaint.setColor(Color.rgb(50, 180, 80));


            float okButtonWidth = 2*titleSize;
            float okButtonHeight = 2*titleSize;
            float okTextPositionX = regionStart + getWidth() / 4;
            float okTextPositionY = getHeight() / 2 + okButtonHeight / 4;
            okButtonTopLeftX = okTextPositionX - okButtonWidth /2;
            okButtonTopLeftY = okTextPositionY - okButtonHeight;
            okButtonBottomRightX = okTextPositionX + okButtonWidth/2;
            okButtonBottomRightY = okTextPositionY;

            textPaint.setTextSize(2*titleSize);
            if(touchRegion == 0){
                canvas.drawText("Ok",okTextPositionX, okTextPositionY, textPaint);
                //canvas.drawRect(okButtonTopLeftX, okButtonTopLeftY, okButtonBottomRightX, okButtonBottomRightY, layoutPaint);
            }

            layoutPaint.setStyle(Paint.Style.FILL);
            layoutPaint.setColor(Color.WHITE);
            textPaint.setColor(Color.WHITE);
        }



        private void drawRegion(int region, boolean highlightable, float startX, float regionSize, Canvas canvas) {


            Shader shader = new Shader();

            if (region == 0)
                shader = monthShader;
            else if (region == 1)
                shader = dayShader;
            else if (region == 2)
                shader = hourShader;
            else if (region == 3)
                shader = minuteShader;
            else if (region == 4)
                shader = ampmShader;


            int textColour = Color.WHITE;
            int rulerColour = Color.argb(255 / 2, 255, 255, 255);
            int selectionColour = Color.rgb(62, 122, 140);


            // Column

            layoutPaint.setColor(Color.WHITE);
            layoutPaint.setShader(shader);
            canvas.drawRect(startX, 0, startX + regionSize, getHeight(), layoutPaint);

            layoutPaint.setShader(new Shader());

            // Column highlight if touchRegion

            if (highlightable && region != touchRegion - 1) {
                layoutPaint.setColor(Color.argb(255 / 2, 255, 255, 255));
                canvas.drawRect(startX, 0, startX + regionSize, getHeight(), layoutPaint);
            }


            layoutPaint.setColor(textColour);
            textPaint.setColor(textColour);
            textPaint.setTextAlign(Align.CENTER);


            //// Creating the content of each column

            textPaint.setTextSize(numberSize);
            textPaint.setTypeface(Typeface.create("normal", Typeface.BOLD));

            layoutPaint.setColor(rulerColour);


            // Months

            float monthCharOffset = (float) 0.5 * monthSpacing + (float) 0.5 * numberSize;

            if (region == 0) {

                monthEnd = startX + regionSize;

                textPaint.setTextSize(monthSize);

                // Draw selection
                layoutPaint.setColor(selectionColour);
                canvas.drawRect(startX, topOffset + selections[0] * monthSpacing, monthEnd, topOffset + (selections[0] + 1) * monthSpacing, layoutPaint);
                layoutPaint.setColor(rulerColour);



                float division = regionSize;


                String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "Novemeber", "December"};


                for (int i = 0; i < 12; i++) {
                    canvas.drawText(months[i], startX + division / 2, topOffset + i * monthSpacing + monthCharOffset, textPaint);
                    canvas.drawRect(startX, topOffset - ruleSize / 4 + i * monthSpacing, monthEnd, topOffset + ruleSize / 4 + i * monthSpacing, layoutPaint);

                }

                canvas.drawRect(startX, topOffset - ruleSize / 4 + 12 * monthSpacing, monthEnd, topOffset + ruleSize / 4 + 12 * monthSpacing, layoutPaint);
            } else if (region == 1) {


                // Days

//                dayEnd = startX + regionSize;
//                textPaint.setTextSize(numberSize);
//
//                float rulerNumOffset = numberSize / 2;
//
//                float division = (float)0.5 * regionSize;
//
//                for (int i = 1; i <= daysPerMonth[selections[0]]; i++) {
//                    if (i == selections[1]) {
//                        layoutPaint.setColor(selectionColour);
//                        textPaint.setColor(selectionColour);
//                        canvas.drawText(Integer.toString(i), dayEnd - (float) 1.5 * division, topOffset + (i - 1) * daysSpacing + rulerNumOffset, textPaint);
//                        canvas.drawRect(dayEnd - (float) 1.0 * division, topOffset + (i - 1) * daysSpacing - ruleSize / 2, dayEnd, topOffset + (i - 1) * daysSpacing + ruleSize / 2, layoutPaint);
//                        layoutPaint.setColor(rulerColour);
//                        textPaint.setColor(textColour);
//                    } else {
//                        if (i % 2 == 0) {
//                            canvas.drawText(Integer.toString(i), dayEnd - (float) 1.5 * division, topOffset + (i - 1) * daysSpacing + rulerNumOffset, textPaint);
//                            canvas.drawRect(dayEnd - (float) 1.0 * division, topOffset + (i - 1) * daysSpacing - ruleSize / 2, dayEnd, topOffset + (i - 1) * daysSpacing + ruleSize / 2, layoutPaint);
//                        } else {
//                            canvas.drawRect(dayEnd - (float) 0.5 * division, topOffset + (i - 1) * daysSpacing - ruleSize / 4, dayEnd, topOffset + (i - 1) * daysSpacing + ruleSize / 4, layoutPaint);
//                        }
//                    }
//
//                }



                dayEnd = startX + regionSize;
                textPaint.setTextSize(numberSize);
                float rulerNumOffset = numberSize / 2;
                float dayCharOffset = (float) 0.25 * numberSize;


                // Draw selection
                layoutPaint.setColor(selectionColour);
                canvas.drawRect(startX, topOffset + (selections[1] - 1) * daysSpacing, dayEnd, topOffset + selections[1] * daysSpacing, layoutPaint);
                layoutPaint.setColor(rulerColour);



                float division = regionSize;

                canvas.drawRect(startX, topOffset - ruleSize / 4, dayEnd, topOffset + ruleSize / 4, layoutPaint);
                for (int i = 1; i <= daysPerMonth[selections[0]]; i++) {
                    canvas.drawText(Integer.toString(i), startX + division / 2, topOffset + i * daysSpacing - dayCharOffset, textPaint);
                    canvas.drawRect(startX, topOffset - ruleSize / 4 + i * daysSpacing, dayEnd, topOffset + ruleSize / 4 + i * daysSpacing, layoutPaint);

                }


            } else if (region == 2) {

                // Hours
                hourEnd = startX + regionSize;

                float division = regionSize;

                layoutPaint.setColor(selectionColour);
                canvas.drawRect(startX, topOffset + (selections[2] - 1) * monthSpacing, hourEnd, topOffset + (selections[2]) * monthSpacing, layoutPaint);
                layoutPaint.setColor(rulerColour);

                for (int i = 0; i < 12; i++) {
                    canvas.drawText(Integer.toString(i + 1), hourEnd - division / 2, topOffset + i * monthSpacing + monthCharOffset, textPaint);
                    canvas.drawRect(startX, topOffset - ruleSize / 4 + i * monthSpacing, hourEnd, topOffset + ruleSize / 4 + i * monthSpacing, layoutPaint);

                }

                canvas.drawRect(startX, topOffset - ruleSize / 4 + 12 * monthSpacing, hourEnd, topOffset + ruleSize / 4 + 12 * monthSpacing, layoutPaint);

            }


            // Minutes

            else if (region == 3) {

                textPaint.setTextSize(numberSize);

                float division = (float)0.5 * regionSize;
                minuteEnd = startX + regionSize;

                float rulerNumOffset = numberSize/2;

                for (int i = 0; i <= 58; i++) {
                    if (i == selections[3]) {
                        layoutPaint.setColor(selectionColour);
                        textPaint.setColor(selectionColour);
                        canvas.drawText(Integer.toString(i), minuteEnd - (float) 1.5 * division, topOffset + i * minuteSpacing + rulerNumOffset, textPaint);
                        canvas.drawRect(minuteEnd - (float) 1.0 * division, topOffset + i * minuteSpacing - ruleSize / 2, minuteEnd, topOffset + i * minuteSpacing + ruleSize / 2, layoutPaint);
                        layoutPaint.setColor(rulerColour);
                        textPaint.setColor(textColour);
                    } else {
                        if (i % 5 == 0) {
                            canvas.drawText(Integer.toString(i), minuteEnd - (float) 1.5 * division, topOffset + i * minuteSpacing + rulerNumOffset, textPaint);
                            canvas.drawRect(minuteEnd - (float) 1.0 * division, topOffset + i * minuteSpacing - ruleSize / 2, minuteEnd, topOffset + i * minuteSpacing + ruleSize / 2, layoutPaint);
                        } else {
                            canvas.drawRect(minuteEnd - (float) 0.5 * division, topOffset + i * minuteSpacing - ruleSize / 4, minuteEnd, topOffset + i * minuteSpacing + ruleSize / 4, layoutPaint);
                        }
                    }
                }

                if (selections[3] == 59) {
                    layoutPaint.setColor(selectionColour);
                    textPaint.setColor(selectionColour);
                    canvas.drawText(Integer.toString(59), minuteEnd - (float) 1.5 * division, topOffset + 59 * minuteSpacing + rulerNumOffset, textPaint);
                    canvas.drawRect(minuteEnd - (float) 1.0 * division, topOffset + 59 * minuteSpacing - ruleSize / 2, minuteEnd, topOffset + 59 * minuteSpacing + ruleSize / 2, layoutPaint);
                    layoutPaint.setColor(rulerColour);
                    textPaint.setColor(textColour);
                } else {
                    canvas.drawText(Integer.toString(59), minuteEnd - (float) 1.5 * division, topOffset + 59 * minuteSpacing + rulerNumOffset, textPaint);
                    canvas.drawRect(minuteEnd - (float) 1.0 * division, topOffset + 59 * minuteSpacing - ruleSize / 2, minuteEnd, topOffset + 59 * minuteSpacing + ruleSize / 2, layoutPaint);
                }
            }



            else if (region == 4) {

                // AM PM

                float division = regionSize;

                ampmEnd = startX + regionSize;

                layoutPaint.setColor(selectionColour);
                canvas.drawRect(startX, topOffset + selections[4] * ampmSpacing, ampmEnd, topOffset + (selections[4] + 1) * ampmSpacing, layoutPaint);
                layoutPaint.setColor(rulerColour);

                textPaint.setTextSize(monthSize);
                canvas.drawRect(startX, topOffset - ruleSize / 4, ampmEnd, topOffset + ruleSize / 4, layoutPaint);

//                if (selections[4] == 0)
//                    textPaint.setColor(selectionColour);

                canvas.drawText("A", ampmEnd - division / 2, topOffset + ampmSpacing / 2, textPaint);
                canvas.drawText("M", ampmEnd - division / 2, topOffset + ampmSpacing / 2 + monthSize, textPaint);

                canvas.drawRect(startX, topOffset + ampmSpacing - ruleSize / 4, ampmEnd, topOffset + ampmSpacing + ruleSize / 4, layoutPaint);

//                if (selections[4] == 1)
//                    textPaint.setColor(selectionColour);
//                else
//                    textPaint.setColor(textColour);

                canvas.drawText("P", ampmEnd - division / 2, topOffset + (float) 1.5 * ampmSpacing, textPaint);
                canvas.drawText("M", ampmEnd - division / 2, topOffset + (float) 1.5 * ampmSpacing + monthSize, textPaint);

                canvas.drawRect(startX, topOffset + 2 * ampmSpacing - ruleSize / 4, ampmEnd, topOffset + 2 * ampmSpacing + ruleSize / 4, layoutPaint);

                textPaint.setColor(textColour);
            }
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void updateSelection(float y){

//            float dy = Math.abs(y - mY);

            if (touchRegion == 1) {
                if(y < topOffset)
                    selections[0] = 0;
                else if(y >= topOffset + 11 * monthSpacing)
                    selections[0] = 11;
                else
                    selections[0] = (int)(y - topOffset) / (int)monthSpacing;
            }
//            else if (touchRegion == 2) {
//                if(y < topOffset)
//                    selections[1] = 1;
//                else if(y >= topOffset + (daysPerMonth[selections[0]] - 1) * daysSpacing)
//                    selections[1] = daysPerMonth[selections[0]];
//                else
//                    selections[1] = (int)((y - topOffset + daysSpacing/2.0) / daysSpacing) + 1;
//            }
            else if (touchRegion == 2) {
                if(y < topOffset)
                    selections[1] = 1;
                else if(y >= topOffset + (daysPerMonth[selections[0]] - 1) * daysSpacing)
                    selections[1] = daysPerMonth[selections[0]];
                else
                    selections[1] = (int)((y - topOffset + daysSpacing) / daysSpacing);
            }
            else if (touchRegion == 3) {
                if(y < topOffset)
                    selections[2] = 1;
                else if(y >= topOffset + 11 * monthSpacing)
                    selections[2] = 12;
                else
                    selections[2] = (int)(y - topOffset) / (int)monthSpacing + 1;
            }
            else if (touchRegion == 4){
                if(y < topOffset)
                    selections[3] = 0;
                else if(y >= topOffset + 59 * minuteSpacing)
                    selections[3] = 59;
//                else if (dy > 0.25f*minuteSpacing)
//                    selections[3] = 5*Math.round((int)((y - topOffset + minuteSpacing/2.0) / minuteSpacing)/5);
                else
                    selections[3] = (int)((y - topOffset + minuteSpacing/2.0) / minuteSpacing);
            }
            else if (touchRegion == 5){
                if(y <= topOffset + ampmSpacing)
                    selections[4] = 0;
                else if(y > topOffset + ampmSpacing)
                    selections[4] = 1;
            }

        }

        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;

            startTouchY = y;
            startTouchX = x;

            hasTouched = true;

            startTouchTimerCancelled = false;
            if(touchRegion != 0)
                new Handler().postDelayed(startTouchTimer, 200 );


        }

        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= 0.5f*minuteSpacing || dy >= 0.5f*minuteSpacing) {


                if(swiping == 0) // don't know if it's swiping yet
                {
                    if(dx > dy) {
                        if (x - mX > mWidth / 50 /*&& x - startTouchX > mWidth / 10*/) {
                            swiping = 1;
                            startTouchTimerCancelled = true;
                            swipeRight();
                        } else if (x - mX < -mWidth / 50 /*&& x - startTouchX < - mWidth / 10*/) {
                            swiping = 2;
                            startTouchTimerCancelled = true;
                            swipeLeft();
                        }
                    }
                }
                else if(swiping == 4) // not swiping
                    updateSelection(y);


                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                mX = x;
                mY = y;

                circlePath.reset();
                circlePath.addCircle(mX, mY, 30, Path.Direction.CW);


            }
        }

        private void touch_up() {
            if(hasTouched) {
                mPath.lineTo(mX, mY);
                circlePath.reset();
                // kill this so we don't double draw
                mPath.reset();

                if (swiping == 0) {
                    if (touchRegion == 0) {
                        if (testOkButtonTouch(startTouchX, startTouchY))
                            okTouch();
                    } else {
                        updateSelection(startTouchY);
                        startChange();
                    }

                } else if (swiping == 4) {
                    if (touchRegion == 0) {
                        if (testOkButtonTouch(startTouchX, startTouchY))
                            okTouch();
                    } else
                        startChange();
                }

                swiping = 0;

                startTouchTimerCancelled = true;
            }


        }

        private boolean testOkButtonTouch(float x, float y){
            return x > okButtonTopLeftX && x < okButtonBottomRightX && y > okButtonTopLeftY && y < okButtonBottomRightY;
        }

        private void okTouch(){
            System.out.println("Yay you touched the button!");
            if(!okTouched) {
                okTouched = true;
                new Handler().postDelayed(end, 200);
            }
        }

        private void startChange(){
            new Handler().postDelayed(moveToNextSelectionRegion, 200);
        }

        private void swipeLeft(){

            touchRegion++;
            if(touchRegion == 6)
                touchRegion = 0;
        }

        private void swipeRight(){
            touchRegion--;
            if(touchRegion == -1)
                touchRegion = 5;
        }

        public void reset()
        {
            selections[0] = 6;
            selections[1] = 1;
            selections[2] = 12;
            selections[3] = 0;
            selections[4] = 1;


            touchRegion = 1;
            lastTouchRegion = 1;

            swiping = 0;
            startTouchX = 0;
            startTouchY = 0;

            startTouchTime = 0;
            releaseTime = 0;
            justReleased = false;

            startTouchTimerCancelled = true;

            okTouched = false;
            hasTouched = false;
            invalidate();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }
    }

    private Runnable end = new Runnable() {
        public void run() {
            boolean result = false;


            //endTime = System.nanoTime();

            System.out.println("END");
            System.out.println(goal_times.length);

            //Chris here the input_times should have the value that the participant gave
            // year, month, day, hour (24 hour clock), minute



//                Intent intent = new Intent();
                setResult(RESULT_OK);
                finish();
        }
    };

    private Runnable moveToNextSelectionRegion = new Runnable() {
        public void run() {
            dv.swipeLeft();
            dv.invalidate();
        }
    };

    private Runnable startTouchTimer = new Runnable() {
        public void run() {

            if(!dv.startTouchTimerCancelled && dv.swiping == 0) {
                dv.swiping = 4; // no longer swiping
                dv.updateSelection(dv.startTouchY);
                dv.invalidate();
            }

        }
    };

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

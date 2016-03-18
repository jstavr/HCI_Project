package calendar.calendartouchapp2_2;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
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


public class MainActivity extends Activity {

    DrawingView dv ;
    private Paint mPaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        private int swiping;
        private float startTouchX, startTouchY;
        private float lastTestX, lastTestY;

        private float mWidth;
        private float mHeight;

        private int[] selections;
        private boolean[] selected;

        private int touchRegion; // 0 for no touch, 1 for month, 2 for day, 3 for hour, 4 for ampm, 5 for minute
        private int lastTouchRegion;
        private long startTouchTime;
        private long releaseTime;
        private boolean justReleased;

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
            selections[0] = 5;
            selections[1] = 10;
            selections[2] = 2;
            selections[3] = 45;
            selections[4] = 1;

            selected = new boolean[5];
            for(int i = 0; i < 5; i++)
            {
                selected[i] = false;
            }

            touchRegion = 1;
            lastTouchRegion = 1;


            swiping = 0;
            startTouchX = 0;
            startTouchY = 0;
            lastTestX = 0;
            lastTestY = 0;

            startTouchTime = 0;
            releaseTime = 0;
            justReleased = false;


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

            if(justReleased && releaseTime - System.currentTimeMillis() > 200)
            {
                justReleased = false;
                touchRegion++;
                if(touchRegion == 6)
                    touchRegion = 0;
            }


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
            int ruleSize = 8;

            topOffset = 2 * titleSpacing;
            monthSize = (float)getHeight() / 35;
            monthSpacing = (float)2.25*monthSize;
            daysSpacing = (float)1.25*numberSize;
            ampmSpacing = ( 12 * monthSpacing) / 2;
            minuteSpacing = 31*daysSpacing / 60;



//            drawRegion(0, true, division, 0, canvas);
//            drawRegion(1, true, division, monthEnd, canvas);
//            drawRegion(2, true, division, dayEnd, canvas);
//            drawRegion(3, true, division, hourEnd, canvas);
//            drawRegion(4, true, division, minuteEnd, canvas);

//            drawRegion(0, true, getWidth() / 2, getWidth() / 2, canvas);
//            drawRegion(1, true, getWidth() / 2, getWidth() / 2, canvas);
//            drawRegion(2, true, getWidth() / 2, getWidth() / 2, canvas);
//            drawRegion(3, true, getWidth() / 2, getWidth() / 2, canvas);
//            drawRegion(4, true, getWidth() / 2, getWidth() / 2, canvas);


            textPaint.setTextAlign(Align.LEFT);

            if(touchRegion != 0) {
//                drawRegion(touchRegion - 1, false, division * 2, ampmEnd + division, canvas);
                drawRegion(touchRegion - 1, false, getWidth() / 2, getWidth() / 2, canvas);

            }


            //// White Spacers

//            monthEnd = division;
//            dayEnd = monthEnd + division;
//            hourEnd = dayEnd + division;
//            minuteEnd = hourEnd + division;
//            ampmEnd = minuteEnd + division;
//
//
//            layoutPaint.setColor(Color.WHITE);
//
//            float spacerWidth = (float) getWidth() / 100;
//            canvas.drawRect(monthEnd - spacerWidth / 2, 0, monthEnd + spacerWidth / 2, getHeight(), layoutPaint);
//            canvas.drawRect(dayEnd - spacerWidth / 2, 0, dayEnd + spacerWidth / 2, getHeight(), layoutPaint);
//            canvas.drawRect(hourEnd - spacerWidth / 2, 0, hourEnd + spacerWidth / 2, getHeight(), layoutPaint);
//            canvas.drawRect(minuteEnd - spacerWidth / 2, 0, minuteEnd + spacerWidth / 2, getHeight(), layoutPaint);






            ///// Titles

            textPaint.setTextSize(titleSize);

            textPaint.setTypeface(Typeface.create("normal", Typeface.BOLD));

//            textPaint.setTextAlign(Align.CENTER);
//            canvas.drawText("m", monthEnd - division / 2, titleSpacing, textPaint);
//            canvas.drawText("d", dayEnd - division / 2, titleSpacing, textPaint);
//            canvas.drawText("h", hourEnd - division / 2, titleSpacing, textPaint);
//            //canvas.drawText("am pm", ampmEnd - division / 2, titleSpacing, textPaint);
//            canvas.drawText("min", minuteEnd - division / 2, titleSpacing, textPaint);

            textPaint.setTextAlign(Align.LEFT);
            if(touchRegion == 1)
                canvas.drawText("month", getWidth() / 2 + division / 2, titleSpacing, textPaint);
            else if(touchRegion == 2)
                canvas.drawText("day", getWidth() / 2 + division / 2, titleSpacing, textPaint);
            else if(touchRegion == 3)
                canvas.drawText("hour", getWidth() / 2 + division / 2, titleSpacing, textPaint);
            else if(touchRegion == 4)
                canvas.drawText("minute", getWidth() / 2 + division / 2, titleSpacing, textPaint);
            else if(touchRegion == 5)
                canvas.drawText("am or pm", getWidth() / 2 + division / 2, titleSpacing, textPaint);

            textPaint.setTypeface(Typeface.create("normal", Typeface.NORMAL));
            textPaint.setColor(Color.rgb(2 * 255 / 4, 2 * 255 / 4, 2 * 255 / 4));
            textPaint.setTextAlign(Align.CENTER);
            textPaint.setTextSize(getHeight() / 25);

            String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "Novemeber", "December"};

            String[] amorpm = {"AM", "PM"};
            canvas.drawText(months[selections[0]] + " " + Integer.toString(selections[1]), getWidth() / 4, getHeight() / 2 - titleSize, textPaint);
            canvas.drawText(Integer.toString(selections[2]) + ":" + Integer.toString(selections[3]) + " " + amorpm[selections[4]], getWidth() / 4, getHeight() / 2 + titleSize, textPaint);









        }


        private void drawRegion(int region, boolean highlightable, float regionSize, float startX, Canvas canvas) {
            int ruleSize = 8;

            Shader shader = new Shader();

            if (region == 0)
                shader = new LinearGradient(startX, 0, startX + regionSize, 0, Color.rgb(221,120,101), Color.rgb(210,92,72), TileMode.CLAMP);
            else if (region == 1)
                shader = new LinearGradient(startX, 0, startX + regionSize, 0, Color.rgb(221,160,104), Color.rgb(210,138,77), TileMode.CLAMP);
            else if (region == 2)
                shader = new LinearGradient(startX, 0, startX + regionSize, 0, Color.rgb(124,209,138), Color.rgb(88,203,115), TileMode.CLAMP);
            else if (region == 3)
                shader = new LinearGradient(startX, 0, startX + regionSize, 0, Color.rgb(181,221,105), Color.rgb(166,209,77), TileMode.CLAMP);
            else if (region == 4)
                shader = new LinearGradient(startX, 0, startX + regionSize, 0, Color.rgb(150,209,150), Color.rgb(128,203,145), TileMode.CLAMP);


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

                dayEnd = startX + regionSize;
                textPaint.setTextSize(numberSize);

                float rulerNumOffset = numberSize / 2;

                float division = (float)0.5 * regionSize;

                for (int i = 1; i <= 31; i++) {
                    if (i == selections[1]) {
                        layoutPaint.setColor(selectionColour);
                        textPaint.setColor(selectionColour);
                        canvas.drawText(Integer.toString(i), dayEnd - (float) 1.5 * division, topOffset + (i - 1) * daysSpacing + rulerNumOffset, textPaint);
                        canvas.drawRect(dayEnd - (float) 1.0 * division, topOffset + (i - 1) * daysSpacing - ruleSize / 2, dayEnd, topOffset + (i - 1) * daysSpacing + ruleSize / 2, layoutPaint);
                        layoutPaint.setColor(rulerColour);
                        textPaint.setColor(textColour);
                    } else {
                        if (i % 2 == 0) {
                            canvas.drawText(Integer.toString(i), dayEnd - (float) 1.5 * division, topOffset + (i - 1) * daysSpacing + rulerNumOffset, textPaint);
                            canvas.drawRect(dayEnd - (float) 1.0 * division, topOffset + (i - 1) * daysSpacing - ruleSize / 2, dayEnd, topOffset + (i - 1) * daysSpacing + ruleSize / 2, layoutPaint);
                        } else {
                            canvas.drawRect(dayEnd - (float) 0.5 * division, topOffset + (i - 1) * daysSpacing - ruleSize / 4, dayEnd, topOffset + (i - 1) * daysSpacing + ruleSize / 4, layoutPaint);
                        }
                    }

                }
            } else if (region == 2) {

                // Hours
                hourEnd = startX + regionSize;

                float division = regionSize;

                layoutPaint.setColor(selectionColour);
                canvas.drawRect(startX, topOffset + (selections[2] - 1) * monthSpacing, hourEnd, topOffset + (selections[2]) * monthSpacing, layoutPaint);
                layoutPaint.setColor(textColour);

                for (int i = 0; i < 12; i++) {
                    canvas.drawText(Integer.toString(i + 1), hourEnd - division / 2, topOffset + i * monthSpacing + monthCharOffset, textPaint);
                    canvas.drawRect(startX, topOffset - ruleSize / 4 + i * monthSpacing, hourEnd, topOffset + ruleSize / 4 + i * monthSpacing, layoutPaint);

                }

                canvas.drawRect(dayEnd, topOffset - ruleSize / 4 + 12 * monthSpacing, hourEnd, topOffset + ruleSize / 4 + 12 * monthSpacing, layoutPaint);

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
                        layoutPaint.setColor(textColour);
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

                canvas.drawText(Integer.toString(59), minuteEnd - (float) 1.5 * division, topOffset + 59 * minuteSpacing + rulerNumOffset, textPaint);
                canvas.drawRect(minuteEnd - (float) 1.0 * division, topOffset + 59 * minuteSpacing - ruleSize / 2, minuteEnd, topOffset + 59 * minuteSpacing + ruleSize / 2, layoutPaint);
            }



            else if (region == 4) {

                // AM PM

                float division = regionSize;

                ampmEnd = startX + regionSize;

                textPaint.setTextSize(monthSize);
                canvas.drawRect(startX, topOffset - ruleSize / 4, ampmEnd, topOffset + ruleSize / 4, layoutPaint);

                if (selections[4] == 0)
                    textPaint.setColor(selectionColour);

                canvas.drawText("A", ampmEnd - division / 2, topOffset + ampmSpacing / 2, textPaint);
                canvas.drawText("M", ampmEnd - division / 2, topOffset + ampmSpacing / 2 + monthSize, textPaint);

                canvas.drawRect(startX, topOffset + ampmSpacing - ruleSize / 4, ampmEnd, topOffset + ampmSpacing + ruleSize / 4, layoutPaint);

                if (selections[4] == 1)
                    textPaint.setColor(selectionColour);
                else
                    textPaint.setColor(textColour);

                canvas.drawText("P", ampmEnd - division / 2, topOffset + (float) 1.5 * ampmSpacing, textPaint);
                canvas.drawText("M", ampmEnd - division / 2, topOffset + (float) 1.5 * ampmSpacing + monthSize, textPaint);

                canvas.drawRect(startX, topOffset + 2 * ampmSpacing - ruleSize / 4, ampmEnd, topOffset + 2 * ampmSpacing + ruleSize / 4, layoutPaint);

                textPaint.setColor(textColour);
            }





        }



        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;



        private void updateSelection(float y){
            if (touchRegion == 1) {
                if(y < topOffset)
                    selections[0] = 0;
                else if(y >= topOffset + 11 * monthSpacing)
                    selections[0] = 11;
                else
                    selections[0] = (int)(y - topOffset) / (int)monthSpacing;
            }
            else if (touchRegion == 2) {
                if(y < topOffset)
                    selections[1] = 1;
                else if(y >= topOffset + 30 * daysSpacing)
                    selections[1] = 31;
                else
                    selections[1] = (int)((y - topOffset + daysSpacing/2.0) / daysSpacing) + 1;
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

            startTouchTime = System.currentTimeMillis();


        }

        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {


                System.out.println("New move");
                System.out.println("mWidth " + mWidth / 50);
                System.out.println("dx " + dx);
                System.out.println("x - startTouchX " + Float.toString(x - startTouchX));

                if(swiping == 0) // don't know if it's swiping yet
                {
                    if(mX - x > mWidth / 50 /*&& x - startTouchX > mWidth / 10*/ ) {
                        swiping = 1;
                        swipeRight();
                    }
                    else if(mX - x < - mWidth / 50 /*&& x - startTouchX < - mWidth / 10*/ ) {
                        swiping = 2;
                        swipeLeft();
                    }
                    else if(System.currentTimeMillis() - startTouchTime > 200 /*|| (dx < mWidth / 50 && Math.abs(startTouchY - y) > mHeight / 100)*/) {
                        swiping = 4;
                        updateSelection(startTouchY);
                        System.out.println("Not swiping");
                    }

//                    testSwiping(dx, dy);
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
            mPath.lineTo(mX, mY);
            circlePath.reset();
            // kill this so we don't double draw
            mPath.reset();

            if(swiping == 0) {
                updateSelection(startTouchY);
                justReleased = true;
                releaseTime = System.currentTimeMillis();
            }
            else if(swiping == 4)
            {
                justReleased = true;
                releaseTime = System.currentTimeMillis();
            }

            swiping = 0;


        }

//        private void testSwiping(float dx, float dy) {
//            if()
//        }

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
}

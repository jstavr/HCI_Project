package calendar.calendartouchapp;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
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


        private int[] selections;

        private int touchRegion; // 0 for no touch, 1 for month, 2 for day, 3 for hour, 4 for ampm, 5 for minute
        private int lastTouchRegion;

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

            monthShader = new LinearGradient(0, 0, monthEnd, 0, Color.rgb(221,120,101), Color.rgb(210,92,72), TileMode.CLAMP);
            dayShader = new LinearGradient(monthEnd, 0, minuteEnd, 0, Color.rgb(221,160,104), Color.rgb(210,138,77), TileMode.CLAMP);
            hourShader = new LinearGradient(dayEnd, 0, hourEnd, 0, Color.rgb(124,209,138), Color.rgb(88,203,115), TileMode.CLAMP);
            ampmShader = new LinearGradient(hourEnd, 0, ampmEnd, 0, Color.rgb(124,209,138), Color.rgb(88,203,115), TileMode.CLAMP);
            minuteShader = new LinearGradient(ampmEnd, 0, minuteEnd, 0, Color.rgb(181,221,105), Color.rgb(166,209,77), TileMode.CLAMP);

            selections = new int[5];
            selections[0] = 5;
            selections[1] = 10;
            selections[2] = 2;
            selections[3] = 1;
            selections[4] = 45;

            touchRegion = 0;
            lastTouchRegion = 0;




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



            // Draw layout

            float division = (float)getWidth()/7;

            monthEnd = division;
            dayEnd = monthEnd + 2*division;
            hourEnd = dayEnd + division;
            ampmEnd = hourEnd + division;
            minuteEnd = getWidth();

            monthShader = new LinearGradient(0, 0, monthEnd, 0, Color.rgb(221,120,101), Color.rgb(210,92,72), TileMode.CLAMP);
            dayShader = new LinearGradient(monthEnd, 0, minuteEnd, 0, Color.rgb(221,160,104), Color.rgb(210,138,77), TileMode.CLAMP);
            hourShader = new LinearGradient(dayEnd, 0, hourEnd, 0, Color.rgb(124,209,138), Color.rgb(88,203,115), TileMode.CLAMP);
            ampmShader = new LinearGradient(hourEnd, 0, ampmEnd, 0, Color.rgb(150,209,150), Color.rgb(128,203,145), TileMode.CLAMP);
            minuteShader = new LinearGradient(ampmEnd, 0, minuteEnd, 0, Color.rgb(181,221,105), Color.rgb(166,209,77), TileMode.CLAMP);


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


            //// Creating the Columns

            layoutPaint.setColor(Color.WHITE);

            layoutPaint.setShader(monthShader);
            canvas.drawRect(0, 0, monthEnd, getHeight(), layoutPaint);
            layoutPaint.setShader(dayShader);
            canvas.drawRect(monthEnd, 0, dayEnd, getHeight(), layoutPaint);
            layoutPaint.setShader(hourShader);
            canvas.drawRect(dayEnd, 0, hourEnd, getHeight(), layoutPaint);
            layoutPaint.setShader(ampmShader);
            canvas.drawRect(hourEnd, 0, ampmEnd, getHeight(), layoutPaint);
            layoutPaint.setShader(minuteShader);
            canvas.drawRect(ampmEnd, 0, minuteEnd, getHeight(), layoutPaint);

            layoutPaint.setShader(new Shader()); // back to the default shader


            // Touch Region

            layoutPaint.setColor(Color.argb(255 / 2, 255, 255, 255));
            if (touchRegion == 1)
                canvas.drawRect(0, 0, monthEnd, getHeight(), layoutPaint);
            else if (touchRegion == 2)
                canvas.drawRect(monthEnd, 0, dayEnd, getHeight(), layoutPaint);
            else if (touchRegion == 3)
                canvas.drawRect(dayEnd, 0, hourEnd, getHeight(), layoutPaint);
            else if (touchRegion == 4)
                canvas.drawRect(hourEnd, 0, ampmEnd, getHeight(), layoutPaint);
            else if (touchRegion == 5)
                canvas.drawRect(ampmEnd, 0, minuteEnd, getHeight(), layoutPaint);


            // Colours

            int textColour = Color.WHITE;
            int selectionColour = Color.rgb(62,122,140);



            // Draw selections

            layoutPaint.setColor(selectionColour);
            canvas.drawRect(0, topOffset + selections[0] * monthSpacing, monthEnd, topOffset + (selections[0] + 1) * monthSpacing, layoutPaint);

            canvas.drawRect(dayEnd, topOffset + (selections[2] - 1) * monthSpacing, hourEnd, topOffset + selections[2] * monthSpacing, layoutPaint);
            //canvas.drawRect(hourEnd, topOffset + selections[3] * ampmSpacing, ampmEnd, topOffset + (selections[3] + 1) * ampmSpacing, layoutPaint);





            layoutPaint.setColor(textColour);
            textPaint.setColor(textColour);
            textPaint.setTextAlign(Align.CENTER);


            //// Creating the content of each column

            textPaint.setTextSize(numberSize);
            textPaint.setTypeface(Typeface.create("normal", Typeface.BOLD));

            layoutPaint.setColor(Color.argb(255 / 2, 255, 255, 255));




            // Months


            textPaint.setTextSize(monthSize);


            float monthCharOffset = (float)0.5*monthSpacing + (float)0.5*numberSize;


            String[] months = {"J", "F", "M", "A", "M", "J", "J", "A", "S", "O", "N", "D" };


            for(int i = 0; i < 12; i++)
            {
                canvas.drawText(months[i], division/2, topOffset + i * monthSpacing + monthCharOffset, textPaint);
                canvas.drawRect(0, topOffset - ruleSize/4 + i * monthSpacing, monthEnd, topOffset + ruleSize/4 + i * monthSpacing, layoutPaint);

            }

            canvas.drawRect(0, topOffset - ruleSize / 4 + 12 * monthSpacing, monthEnd, topOffset + ruleSize / 4 + 12 * monthSpacing, layoutPaint);




            // Days

            textPaint.setTextSize(numberSize);

            float rulerNumOffset = numberSize/2;

            for(int i = 1; i <= 31; i++)
            {
                if(i == selections[1]) {
                    layoutPaint.setColor(selectionColour);
                    textPaint.setColor(selectionColour);
                    canvas.drawText(Integer.toString(i), dayEnd - (float) 1.5 * division, topOffset + (i - 1) * daysSpacing + rulerNumOffset, textPaint);
                    canvas.drawRect(dayEnd - (float) 1.0 * division, topOffset + (i - 1) * daysSpacing - ruleSize / 2, dayEnd, topOffset + (i - 1) * daysSpacing + ruleSize / 2, layoutPaint);
                    layoutPaint.setColor(textColour);
                    textPaint.setColor(textColour);
                }
                else{
                    if(i % 2 == 0) {
                        canvas.drawText(Integer.toString(i), dayEnd - (float) 1.5 * division, topOffset + (i - 1) * daysSpacing + rulerNumOffset, textPaint);
                        canvas.drawRect(dayEnd - (float)1.0*division, topOffset + (i - 1) * daysSpacing - ruleSize/2, dayEnd, topOffset + (i - 1) * daysSpacing + ruleSize/2, layoutPaint);
                    }
                    else {
                        canvas.drawRect(dayEnd - (float)0.5*division, topOffset + (i - 1) * daysSpacing - ruleSize/4, dayEnd, topOffset + (i - 1) * daysSpacing + ruleSize/4, layoutPaint);
                    }
                }

            }


            // Hours


            for(int i = 0; i < 12; i++)
            {
                canvas.drawText(Integer.toString(i+1), hourEnd - division/2, topOffset + i * monthSpacing + monthCharOffset, textPaint);
                canvas.drawRect(dayEnd, topOffset - ruleSize/4 + i * monthSpacing, hourEnd, topOffset + ruleSize/4 + i * monthSpacing, layoutPaint);

            }

            canvas.drawRect(dayEnd, topOffset - ruleSize / 4 + 12 * monthSpacing, hourEnd, topOffset + ruleSize / 4 + 12 * monthSpacing, layoutPaint);

            // AM PM

            textPaint.setTextSize(monthSize);
            canvas.drawRect(hourEnd, topOffset - ruleSize / 4, ampmEnd, topOffset + ruleSize / 4, layoutPaint);

            if(selections[3] == 0)
                textPaint.setColor(selectionColour);

            canvas.drawText("A", ampmEnd - division / 2, topOffset + ampmSpacing / 2, textPaint);
            canvas.drawText("M", ampmEnd - division / 2, topOffset + ampmSpacing / 2 +  monthSize, textPaint);

            canvas.drawRect(hourEnd, topOffset + ampmSpacing - ruleSize / 4, ampmEnd, topOffset + ampmSpacing + ruleSize / 4, layoutPaint);

            if(selections[3] == 1)
                textPaint.setColor(selectionColour);
            else
                textPaint.setColor(textColour);

            canvas.drawText("P", ampmEnd - division / 2, topOffset + (float)1.5 * ampmSpacing, textPaint);
            canvas.drawText("M", ampmEnd - division / 2, topOffset + (float) 1.5 * ampmSpacing + monthSize, textPaint);

            canvas.drawRect(hourEnd, topOffset + 2 * ampmSpacing - ruleSize / 4, ampmEnd, topOffset + 2 * ampmSpacing + ruleSize / 4, layoutPaint);

            textPaint.setColor(textColour);


            // Minutes

            textPaint.setTextSize(numberSize);

            for(int i = 0; i <= 58; i++)
            {
                if(i == selections[4]) {
                    layoutPaint.setColor(selectionColour);
                    textPaint.setColor(selectionColour);
                    canvas.drawText(Integer.toString(i), minuteEnd - (float) 1.5 * division, topOffset + i * minuteSpacing + rulerNumOffset, textPaint);
                    canvas.drawRect(minuteEnd - (float) 1.0 * division, topOffset + i * minuteSpacing - ruleSize / 2, minuteEnd, topOffset + i * minuteSpacing + ruleSize / 2, layoutPaint);
                    layoutPaint.setColor(textColour);
                    textPaint.setColor(textColour);
                }
                else {
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



            //// White Spacers

            layoutPaint.setColor(textColour);

            float spacerWidth = (float) getWidth() / 100;
            canvas.drawRect(monthEnd - spacerWidth / 2, 0, monthEnd + spacerWidth / 2, getHeight(), layoutPaint);
            canvas.drawRect(dayEnd - spacerWidth / 2, 0, dayEnd + spacerWidth / 2, getHeight(), layoutPaint);
            canvas.drawRect(hourEnd - spacerWidth / 2, 0, hourEnd + spacerWidth / 2, getHeight(), layoutPaint);
            canvas.drawRect(ampmEnd - spacerWidth / 2, 0, ampmEnd + spacerWidth / 2, getHeight(), layoutPaint);


            ///// Titles

            textPaint.setTextSize(titleSize);
            //textPaint.setTypeface(Typeface.create("normal", Typeface.BOLD));

            canvas.drawText("m", monthEnd - division / 2, titleSpacing, textPaint);
            canvas.drawText("day", dayEnd - division, titleSpacing, textPaint);
            canvas.drawText("hour", hourEnd - division / 2, titleSpacing, textPaint);
            //canvas.drawText("am pm", ampmEnd - division / 2, titleSpacing, textPaint);
            canvas.drawText("minute", minuteEnd - division, titleSpacing, textPaint);






            ///// Draw curve
            //canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath(mPath, mPaint);


            // Draw current selection above finger
            float fingerNumSpacing = titleSpacing + numberSize;
            if(touchRegion != 0) {
                textPaint.setTextSize(numberSize);
                textPaint.setColor(Color.MAGENTA);
                String[] amorpm = {"am", "pm"};
                if (touchRegion == 1)
                    canvas.drawText(months[selections[0]], mX, mY - fingerNumSpacing, textPaint);
                else if (touchRegion == 2)
                    canvas.drawText(Integer.toString(selections[1]), mX, mY - fingerNumSpacing, textPaint);
                else if (touchRegion == 3)
                    canvas.drawText(Integer.toString(selections[2]), mX, mY - fingerNumSpacing, textPaint);
                else if (touchRegion == 4)
                    canvas.drawText(amorpm[selections[3]], mX, mY - fingerNumSpacing, textPaint);
                else if (touchRegion == 5)
                    canvas.drawText(Integer.toString(selections[4]), mX, mY - fingerNumSpacing, textPaint);
            }



            canvas.drawPath(circlePath, circlePaint);


        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;


        private void touchRegionTest(float x) {
            lastTouchRegion = touchRegion;
            if(x <= monthEnd )
                touchRegion = 1;
            else if(x > monthEnd && x <= dayEnd )
                touchRegion = 2;
            else if(x > dayEnd && x <= hourEnd )
                touchRegion = 3;
            else if(x > hourEnd && x <= ampmEnd )
                touchRegion = 4;
            else if(x > ampmEnd && x <= minuteEnd )
                touchRegion = 5;
        }

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
                if(y <= topOffset + ampmSpacing)
                    selections[3] = 0;
                else if(y > topOffset + ampmSpacing)
                    selections[3] = 1;
            }
            else if (touchRegion == 5){
                if(y < topOffset)
                    selections[4] = 0;
                else if(y >= topOffset + 59 * minuteSpacing)
                    selections[4] = 59;
                else
                    selections[4] = (int)((y - topOffset + minuteSpacing/2.0) / minuteSpacing);
            }
        }

        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;

            touchRegionTest(x);
            updateSelection(y);


        }

        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                mX = x;
                mY = y;

                circlePath.reset();
                circlePath.addCircle(mX, mY, 30, Path.Direction.CW);

                touchRegionTest(x);
                updateSelection(y);

//                // Update selection when touch region changes
//                if(lastTouchRegion != 0 && lastTouchRegion != touchRegion)
//                {
//                    lastTouchRegion
//                }
            }
        }

        private void touch_up() {
            mPath.lineTo(mX, mY);
            circlePath.reset();
            // commit the path to our offscreen
            mCanvas.drawPath(mPath,  mPaint);
            // kill this so we don't double draw
            mPath.reset();


            touchRegion = 0;
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

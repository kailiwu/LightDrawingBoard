package com.example.asus.lightdrawingboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * DrawingBoard
 *
 * @author: ASUS
 * @time: 2016/2/21 10:14
 */
public class DrawingBoard extends SurfaceView implements SurfaceHolder.Callback,View.OnTouchListener{
    public final static String TAG = "DrawingBorad";

    private onLightLevelListener listener;

    private Canvas canvas;
    private Paint paint;
    private Paint paintMin;
    private Path path;
    private Path pathMin;
    public float flagMin;
    public int color;

    public DrawingBoard(Context context) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        init();

    }

    public DrawingBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        init();
    }

    /*初始化画笔，路径
     */
    public void init() {
        paint = new Paint();
        paint.setAntiAlias(true);//画笔的抗锯齿
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(5);//画笔的粗细
        paint.setStyle(Paint.Style.STROKE);//画笔的风格

        paintMin = new Paint();
        paintMin.setColor(Color.BLACK);
        paintMin.setAntiAlias(true);
        paintMin.setStrokeWidth(2);
        paintMin.setStyle(Paint.Style.STROKE);

        path = new Path();
        pathMin = new Path();

        flagMin = 0;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        draw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /*初始化画布，背景为白色，画四条直线将屏幕划分为8个区域
    */
    public void draw() {
        canvas = getHolder().lockCanvas();

        canvas.drawColor(Color.WHITE);
        paint.setColor(Color.GRAY);
        canvas.drawLine(0, 0, this.getWidth(), this.getHeight(), paint);
        canvas.drawLine(this.getWidth() / 2, 0, this.getWidth() / 2, this.getHeight(), paint);
        canvas.drawLine(this.getWidth(), 0, 0, this.getHeight(), paint);
        canvas.drawLine(0, this.getHeight() / 2, this.getWidth(), this.getHeight() / 2, paint);
        canvas.drawLine(0, this.getHeight() / 5, this.getWidth(), this.getHeight() / 5, paintMin);
        canvas.drawLine(this.getWidth() / 5, 0, this.getWidth() / 5, this.getHeight() / 5, paintMin);
        canvas.drawLine(this.getWidth() * 2 / 5, 0, this.getWidth() * 2 / 5, this.getHeight() / 5, paintMin);
        canvas.drawLine(this.getWidth() * 3 / 5, 0, this.getWidth() * 3 / 5, this.getHeight() / 5, paintMin);
        canvas.drawLine(this.getWidth() * 4 / 5, 0, this.getWidth() * 4 / 5, this.getHeight() / 5, paintMin);

        paint.setColor(getColor());
        canvas.drawPath(path, paint);

        getHolder().unlockCanvasAndPost(canvas);
    }

    public void drawMin() {
        canvas = getHolder().lockCanvas();

        canvas.drawPath(pathMin, paintMin);

        getHolder().unlockCanvasAndPost(canvas);
    }


    /*onTouch方法：获取实时的点的坐标，并且画出路径
    */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getPointerCount() == 1) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(event.getX(), event.getY());
                    pathMin.moveTo(event.getX() / 5 + this.getWidth() * flagMin / 5, event.getY() / 5);
                    draw();
                    drawMin();
                    break;
                case MotionEvent.ACTION_MOVE:
                    path.lineTo(event.getX(), event.getY());
                    pathMin.lineTo(event.getX() / 5 + this.getWidth() * flagMin / 5, event.getY() / 5);
                    draw();
                    drawMin();
                    break;
            }
        }
        return true;
    }


    public void clean() {
        flagMin += 0.5;
        path.reset();
        draw();
        drawMin();
    }

    /*清除整个画布
     */
    public void cleanCanvace() {
        flagMin = 0;
        path.reset();
        pathMin.reset();
        draw();
    }

    /*根据当前的光照强度改变画笔以及路径的颜色
     */
    public int getColor() {
        if(listener.onLightlevel()<10){
            return Color.RED;
        }else if(listener.onLightlevel()<20){
            return Color.rgb(255,215,0);
        }else if(listener.onLightlevel()<30){
            return Color.YELLOW;
        }else if(listener.onLightlevel()<40){
            return Color.GREEN;
        }else if(listener.onLightlevel()<50){
            return Color.rgb(127,255,212);
        }else if(listener.onLightlevel()<60){
            return Color.BLUE;
        }else if(listener.onLightlevel()<70){
            return Color.rgb(160,32,240);
        }else {
            return Color.BLACK;
        }
    }

    /*设置监听器
    */
    public void setOnLightLevelListener(onLightLevelListener listener){
        this.listener=listener;
    }

    /*光照强度监听器
     */
    public interface onLightLevelListener{
        float onLightlevel();
    }
}

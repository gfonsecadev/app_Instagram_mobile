package com.example.instagram.tools;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.core.view.Event;

public class ZoomScale {
    private ImageView imageView;
    private float scale = 1f;
    private float x, y, dx, dy,getX,getY,getScalex,getScaley;
    private Context context;
    private ScaleGestureDetector scaleGestureDetector;


    public ZoomScale(ImageView imageView, Context context) {
        this.imageView = imageView;
        getScalex=imageView.getScaleX();
        getScaley=imageView.getScaleY();
        getX=27;
        getY=120;
        this.context = context;
        scaleGestureDetector = new ScaleGestureDetector(context, new GetZoom());
    }


    public void zoom(MotionEvent event){


        scaleGestureDetector.onTouchEvent(event);

                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    x=event.getX();
                    y=event.getY();

                }



                if(event.getAction()==MotionEvent.ACTION_MOVE){
                    dx=event.getX()-x;
                    dy=event.getY()-y;

                    imageView.setX(imageView.getX()+dx);
                    imageView.setY(imageView.getY()+dy);
                    x=event.getX();
                    y=event.getY();

                }

                if (event.getAction()==MotionEvent.ACTION_UP){
                    imageView.setX(getX);
                    imageView.setY(getY);
                    imageView.setScaleX(getScalex);
                    imageView.setScaleY(getScaley);
                    scale=1f;
                }





            }






    public class GetZoom extends ScaleGestureDetector.SimpleOnScaleGestureListener {


    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        scale = scale * detector.getScaleFactor();
        scale = Math.max(0.1f, Math.min(scale, 5f));
        imageView.setScaleY(scale);
        imageView.setScaleX(scale);

        return true;
    }

}


}


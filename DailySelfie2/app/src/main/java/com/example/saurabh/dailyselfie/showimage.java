package com.example.saurabh.dailyselfie;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;


public class showimage extends MainActivity implements GestureDetector.OnGestureListener{

    GestureDetector detector;
    int position;
    ImageView imageView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showimage);
        Intent intent = getIntent();
        Uri imageUri = Uri.parse(intent.getStringExtra("imageURI"));
        imageView1 = (ImageView)findViewById(R.id.imagetobeshown);
        imageView1.setImageURI(imageUri);
        position = intent.getIntExtra("position",0);
        detector = new GestureDetector(showimage.this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return detector.onTouchEvent(event);
    }
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        float diffX = e2.getX() - e1.getX();
        float diffY = e2.getY() - e1.getY();
        if(diffX < 0 && Math.abs(diffX) - Math.abs(diffY) > 0)
        {
            if(position < listFile.length-1)
            {
                imageView1.setImageURI(Uri.fromFile(listFile[position++ +1]));
            }
            else
            {
                Toast.makeText(getApplicationContext(),"No more Images",Toast.LENGTH_SHORT).show();
            }
        }
        else
        if(diffX > 0 && Math.abs(diffX) - Math.abs(diffY) > 0)
        {
            if(position > 0)
                imageView1.setImageURI(Uri.fromFile(listFile[position-- -1]));
            else
            {
                Toast.makeText(getApplicationContext(),"No more Images",Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }
}

package com.example.saurabh.dailyselfie;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class compilation extends Activity {

    ImageView view ;
    File file,fileArray[];
   Timer timer = new Timer();
    int c = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compilation);
        view = (ImageView)findViewById(R.id.new_image);
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DailySelfie/Selfie");
        if(file.isDirectory())
        {
            fileArray = file.listFiles();
        }
        //AnimationDrawable animationDrawable = new AnimationDrawable();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(c < fileArray.length)
                        view.setImageURI(Uri.fromFile(fileArray[c++]));
                        else
                        {
                            finish();
                        }
                    }
                });
            };
        },500,500);
        Log.i("compilation ","finished");
       // for(int i=0;i<fileArray.length;i++)
       // {

      //  }
    }



}

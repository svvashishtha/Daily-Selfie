package com.example.saurabh.dailyselfie;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;


public class showimage extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showimage);
        Intent intent = getIntent();
        Uri imageUri = Uri.parse(intent.getStringExtra("imageURI"));
        ImageView imageView = (ImageView)findViewById(R.id.imagetobeshown);
        imageView.setImageURI(imageUri);
    }
}

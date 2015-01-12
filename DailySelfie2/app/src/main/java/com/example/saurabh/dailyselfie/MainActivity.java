package com.example.saurabh.dailyselfie;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends Activity {
    private Uri fileUri;
    File[] listFile;
    ArrayList<String> images;
    ImageAdapter adapter;
    GridView gridView;
    public AlarmManager mAlarmManager;
    private static final long INITIAL_ALARM_DELAY = 2 * 60 * 1000L;
    protected static final long JITTER = 5000L;
    private Intent mNotificationReceiverIntent, mLoggerReceiverIntent;
    private PendingIntent mNotificationReceiverPendingIntent,
            mLoggerReceiverPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        images = new ArrayList<String>();
        new extractImages().execute(images);
        gridView = (GridView) findViewById(R.id.gridView);
        adapter = new ImageAdapter(MainActivity.this, images);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String uri = images.get(i);
                Intent intent = new Intent(MainActivity.this,showimage.class);
                intent.putExtra("imageURI",uri);
                startActivity(intent);
            }
        });

            }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Create an Intent to broadcast to the AlarmNotificationReceiver
        mNotificationReceiverIntent = new Intent(MainActivity.this,
                AlarmNotificationReceiver.class);

        // Create an PendingIntent that holds the NotificationReceiverIntent
        mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
                MainActivity.this, 0, mNotificationReceiverIntent, 0);

        // Create an Intent to broadcast to the AlarmLoggerReceiver
        mLoggerReceiverIntent = new Intent(MainActivity.this,
                AlarmLoggerReceiver.class);

        // Create PendingIntent that holds the mLoggerReceiverPendingIntent
        mLoggerReceiverPendingIntent = PendingIntent.getBroadcast(
                MainActivity.this, 0, mLoggerReceiverIntent, 0);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_camera) {
            openCamera();
            return true;
        }
        else
        if(id == R.id.delete)
        {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DailySelfie/Selfie");
            if (file.isDirectory()) {
                listFile = file.listFiles();
                for (int i = 0; i < listFile.length; i++) {
                     listFile[i].delete();
                   }
                images.clear();
                adapter.notifyDataSetChanged();
            }
        }
        else
        if(id == R.id.alarm)
        {

            mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime() ,60 * 1000
                    ,
                    mNotificationReceiverPendingIntent);

            // Set repeating alarm to fire shortly after previous alarm
            mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(),
                    2 * 60 * 1000,
                    mLoggerReceiverPendingIntent);
                Toast toast = Toast.makeText(this,"Alarm has been set",Toast.LENGTH_SHORT);
                toast.show();
        }
        else
        if(id == R.id.alarmCancel)
        {
            mAlarmManager.cancel(mNotificationReceiverPendingIntent);
            mAlarmManager.cancel(mLoggerReceiverPendingIntent);

            Toast toast = Toast.makeText(this,"Alarm has been cancelled",Toast.LENGTH_SHORT);
            toast.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = Uri.fromFile(getOutputMediaFile());
        camera.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(camera, 1);
    }

    @Override
    protected void onActivityResult(int RequestCode, int ResultCode, Intent data) {

         if (ResultCode != RESULT_CANCELED) {
                    Log.i("Image saved at",fileUri.getEncodedPath());
                    images.add(fileUri.getEncodedPath());
                    adapter.notifyDataSetChanged();
            }
        }
    private static File getOutputMediaFile() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File root = Environment.getExternalStorageDirectory();
            File mediaStorageDir = new File(root.getAbsolutePath() + "/DailySelfie", "Selfie");
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.i("file error", "failed to create directory");
                    return null;
                }
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "SELFIE" + timeStamp + ".jpg");
            Log.i("the return path:",Uri.fromFile(mediaFile).toString());
            return mediaFile;
        } else {
            Log.i("storage error", "no external storage");
            return null;
        }
    }
class extractImages extends AsyncTask<ArrayList<String>,Integer,Integer>{
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast toast = Toast.makeText(getApplicationContext(),"Loading Images...",Toast.LENGTH_SHORT);
        toast.show();
    }
    @Override
    protected Integer doInBackground(ArrayList<String>... arrayLists) {

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DailySelfie/Selfie");
        if (file.isDirectory()) {
            listFile = file.listFiles();
            for (int i = 0; i < listFile.length; i++) {
                images.add(listFile[i].getAbsolutePath());
            }
        }
        Log.i("Asynctask","Loading images");
        return 0;
    }}
}
    class ImageAdapter extends BaseAdapter {
        Context mContext;
        private List<String> mThumbIds;
        public ImageAdapter(Context c,List <String>ids) {
            mThumbIds = ids;
            mContext = c;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = (ImageView)convertView;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(105, 105));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(1, 1, 1, 1);
            }
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mThumbIds.get(position), options);

            // Set inSampleSize
            options.inSampleSize = 4;
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            Bitmap myBitmap = BitmapFactory.decodeFile(mThumbIds.get(position), options);
            imageView.setImageBitmap(myBitmap);
            return imageView;
        }

        public long getItemId(int position) {
            return position;
        }

        public int getCount() {
            return mThumbIds.size();
        }

        public Object getItem(int position) {
            return null;
        }

    }

package com.garydty.a10366827.activities;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.garydty.a10366827.R;

import java.io.File;
import java.io.FileOutputStream;

public class PictureConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_picture_confirmation);

        final byte[] imageData = getIntent().getByteArrayExtra("picture");
        final ImageView mPreviewImage = (ImageView) findViewById(R.id.preview_image_holder);
        final Bitmap image = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        mPreviewImage.setImageBitmap(image);

        Button confirmButton = (Button) findViewById(R.id.confirm_upload_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new StorePhotoTask().execute(imageData);
                createDirectoryAndSaveFile(image, System.currentTimeMillis() + ".jpg");
                Toast.makeText(getApplicationContext(), "image saved.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        Button cancelButton = (Button) findViewById(R.id.cancel_upload_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/LeagueHelper");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/LeagueHelper/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/sdcard/LeagueHelper/"), fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



//    class StorePhotoTask extends AsyncTask<byte[], Void, Void> {
//        @Override
//        protected Void doInBackground(byte[]... jpeg) {
//            if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
//                Log.e("StorePhotoTask", "Media not mounted.");
//                return null;
//            }
//
//            File photo=
//                    new File(Environment.getExternalStorageDirectory(),
//                            System.currentTimeMillis() + ".jpg");
//
////            if (photo.exists()) {
////                photo.delete();
////            }
//
//            try {
//                FileOutputStream fos=new FileOutputStream(photo.getPath());
//
//                fos.write(jpeg[0]);
//                fos.close();
//            }
//            catch (java.io.IOException e) {
//                Log.e("PictureDemo", "Exception in photoCallback", e);
//            }
//
//            return null;
//        }
//    }
}

package com.garydty.a10366827.activities;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.garydty.a10366827.R;

public class PictureConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_picture_confirmation);

        byte[] imageData = getIntent().getByteArrayExtra("picture");
        final ImageView mPreviewImage = (ImageView) findViewById(R.id.preview_image_holder);
        Bitmap image = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        mPreviewImage.setImageBitmap(image);

        Button confirmButton = (Button) findViewById(R.id.confirm_upload_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
}

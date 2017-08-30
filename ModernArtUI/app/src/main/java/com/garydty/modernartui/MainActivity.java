package com.garydty.modernartui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EpicFrameLayout frame1 = new EpicFrameLayout(
                (FrameLayout) findViewById(R.id.frame1),
                Color.rgb(255, 160, 160),
                Color.rgb(255, 255, 0)
        );

        final EpicFrameLayout frame2 = new EpicFrameLayout(
                (FrameLayout) findViewById(R.id.frame2),
                Color.rgb(120, 80, 0),
                Color.rgb(255, 80, 230)
        );

        final EpicFrameLayout frame3 = new EpicFrameLayout(
                (FrameLayout) findViewById(R.id.frame3),
                Color.rgb(0, 255, 0),
                Color.rgb(120, 255, 255)
        );

        final EpicFrameLayout frame4 = new EpicFrameLayout(
                (FrameLayout) findViewById(R.id.frame4),
                Color.rgb(255, 255, 0),
                Color.rgb(0, 100, 200)
        );

//        final EpicFrameLayout greyFrame = new EpicFrameLayout(
//                (FrameLayout) findViewById(R.id.frameGrey),
//                Color.rgb(200, 200, 200),
//                Color.rgb(200, 200, 200)
//        );

        SeekBar seeker = (SeekBar) findViewById(R.id.seekBar);
        seeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                textView.setText("Value: " + i);
//                frame1.setBackgroundColor(Color.rgb(i, 0, i));
//                if(frameLayout1 != null)
                frame1.updateColour(i);
                frame2.updateColour(i);
                frame3.updateColour(i);
                frame4.updateColour(i);
//                greyFrame.updateColour(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.more_information) {
            showMoreInfoDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void openMomaSite(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.moma_address)));
        startActivity(browserIntent);
    }

    private void showMoreInfoDialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        builder1.setTitle(getString(R.string.more_info_title));
        builder1.setMessage(getString(R.string.more_info_desc));
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Visit MOMA",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        openMomaSite();
                    }
                });

        builder1.setNegativeButton(
                "Not Now",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}

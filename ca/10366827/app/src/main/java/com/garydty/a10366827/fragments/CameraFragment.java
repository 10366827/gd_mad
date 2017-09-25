package com.garydty.a10366827.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.garydty.a10366827.R;
import com.garydty.a10366827.activities.PictureConfirmationActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Gary Doherty on 19/09/2017.
 */

public class CameraFragment extends Fragment implements Camera.PictureCallback {

    private static final String TAG = "CameraFragment";
    private static final int CAMERA_ID = 0;

    private CameraDisplay mCameraDisplay;
    private Camera mCamera;

    public static CameraFragment newInstance() {
        Log.i(TAG, "constructor");
        return new CameraFragment();
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton takePicButton = view.findViewById(R.id.take_photo_btn);
        takePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTakePicClick();
            }
        });
        if(mCamera == null)
            initCamera();
    }

    private void initCamera() {
        mCamera = getCameraInstance(CAMERA_ID);
        android.hardware.Camera.CameraInfo cameraInfo = null;

        if (mCamera != null) {
            // Get mCamera info only if the mCamera is available
            cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(CAMERA_ID, cameraInfo);
        }

        // Get the rotation of the screen to adjust the mCameraDisplay image accordingly.
        final int displayRotation = getActivity().getWindowManager().getDefaultDisplay()
                .getRotation();

        if (getView() == null) {
            return;
        }

        final FrameLayout preview = getActivity().findViewById(R.id.user_photo);
        preview.removeAllViews();
        ((ViewGroup)preview.getParent()).removeView(mCameraDisplay);

        if (mCameraDisplay == null) {
            // Create the Preview view and set it as the content of this Activity.
            mCameraDisplay = new CameraDisplay(getActivity(), mCamera, cameraInfo, displayRotation);
        } else {
            mCameraDisplay.setCamera(mCamera, cameraInfo, displayRotation);
        }
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            public void run() {
                preview.addView(mCameraDisplay);
            }
        }, 350);
//        preview.addView(mCameraDisplay);
    }

    private void onTakePicClick() {
        //  Fixes the image result from being the wrong orientation
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setRotation(mCameraDisplay.getDisplayOrientation());

        //  Set output quality, using a medium quality by dividing supportedSizes by 2
        List<Camera.Size> supportedSizes = parameters.getSupportedPictureSizes();
        if(supportedSizes != null && !supportedSizes.isEmpty()) {
            Camera.Size sizePicture = supportedSizes.get(supportedSizes.size() / 2);
            parameters.setPictureSize(sizePicture.width, sizePicture.height);
        }

        mCamera.setParameters(parameters);

        //  Call the auto-focus, then call takePicture
        mCamera.autoFocus(new Camera.AutoFocusCallback(){
            public void onAutoFocus(boolean success, Camera camera){
                camera.takePicture(new Camera.ShutterCallback(){
                           public void onShutter(){

                           }
                        },
                        null,
                        CameraFragment.this
                );
            }
        });
    }

    //  Called when takePicture completes, as long as the fragment is passed for the callback
    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
//        new CameraDisplay.StorePhotoTask().execute(bytes);
        Toast.makeText(getContext(), "" + bytes.length, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getContext(), PictureConfirmationActivity.class);
        intent.putExtra("picture", bytes);
        startActivity(intent);
//        mCamera.startPreview();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCamera == null) {
            initCamera();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop mCamera access
        releaseCamera();
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(int cameraId) {
        Camera c = null;
        try {
            c = Camera.open(cameraId); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.d(TAG, "Camera " + cameraId + " is not available: " + e.getMessage());
        }
        return c; // returns null if mCamera is unavailable
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();        // release the mCamera for other applications
            mCamera = null;
        }
    }

    @Override
    public void onSaveInstanceState( Bundle outState ) {

    }

    class StorePhotoTask extends AsyncTask<byte[], String, String> {
        @Override
        protected String doInBackground(byte[]... jpeg) {
            File photo=
                    new File(Environment.getExternalStorageDirectory(),
                            Calendar.getInstance().getTime().toString() + ".jpg");

            if (photo.exists()) {
                photo.delete();
            }

            try {
                FileOutputStream fos=new FileOutputStream(photo.getPath());

                fos.write(jpeg[0]);
                fos.close();
            }
            catch (java.io.IOException e) {
                Log.e("PictureDemo", "Exception in photoCallback", e);
            }

            return(null);
        }
    }
}

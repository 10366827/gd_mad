package com.garydty.a10366827.fragments;

import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.garydty.a10366827.R;

/**
 * Created by Gary Doherty on 19/09/2017.
 */

public class CameraFragment extends Fragment {

    private static final String TAG = "CameraPreview";

    /**
     * Id of the camera to access. 0 is the first camera.
     */
    private static final int CAMERA_ID = 0;

    private CameraDisplay preview;
    private Camera camera;

    public static CameraFragment newInstance() {
        return new CameraFragment();
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton backButton = view.findViewById(R.id.take_photo_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackClick();
            }
        });
        initCamera();
    }

    private void initCamera() {
        camera = getCameraInstance(CAMERA_ID);
        android.hardware.Camera.CameraInfo cameraInfo = null;

        if (camera != null) {
            // Get camera info only if the camera is available
            cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(CAMERA_ID, cameraInfo);
        }

        // Get the rotation of the screen to adjust the preview image accordingly.
        final int displayRotation = getActivity().getWindowManager().getDefaultDisplay()
                .getRotation();

        if (getView() == null) {
            return;
        }

        FrameLayout preview = getView().findViewById(R.id.user_photo);
        preview.removeAllViews();

        if (this.preview == null) {
            // Create the Preview view and set it as the content of this Activity.
            this.preview = new CameraDisplay(getActivity(), camera, cameraInfo, displayRotation);
        } else {
            this.preview.setCamera(camera, cameraInfo, displayRotation);
        }

        preview.addView(this.preview);
    }

    private void onBackClick() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (camera == null) {
            initCamera();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop camera access
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
        return c; // returns null if camera is unavailable
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();        // release the camera for other applications
            camera = null;
        }
    }

    @Override
    public void onSaveInstanceState( Bundle outState ) {

    }
}

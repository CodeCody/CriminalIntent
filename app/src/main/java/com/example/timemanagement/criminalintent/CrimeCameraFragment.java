package com.example.timemanagement.criminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraDevice;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by TimeManagement on 7/10/2015.
 */
@SuppressWarnings("deprecation")
public class CrimeCameraFragment extends Fragment
{
    private static final String TAG="CrimeCameraFragment";
    private CameraDevice mCamera2;
    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private View mProgressContainer;
    public static final String EXTRA_PHOTO_FILENAME="com.bignerdranch.android.criminalintent.photo_filename";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.fragment_crime_camera,parent,false);

        Button takePicture=(Button)v.findViewById(R.id.crime_camera_takePictureButton);
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCamera!=null)
                {

                    mCamera.takePicture(mShutterCallback,null,mJpegCallback);
                }
            }
        });

        mSurfaceView=(SurfaceView)v.findViewById(R.id.crime_camera_surfaceView);
        SurfaceHolder holder=mSurfaceView.getHolder();

        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try{
                    if(mCamera!=null)
                    mCamera.setPreviewDisplay(holder);
                }
                catch(IOException io)
                {
                    Log.e(TAG, "Error setting up preview display", io);
                }
            }
            @SuppressWarnings("deprecation")
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                if(mCamera==null)
                    return;

                Camera.Parameters parameters=mCamera.getParameters();
                Camera.Size s=getBestsupportedSize(parameters.getSupportedPreviewSizes(),width,height);
                parameters.setPreviewSize(s.width, s.height);
                s=getBestsupportedSize(parameters.getSupportedPictureSizes(),width,height);
                parameters.setPictureSize(s.width,s.height);
                mCamera.setParameters(parameters);

                try
                {
                    mCamera.startPreview();
                }
                catch (Exception e)
                {
                    Log.e(TAG,"Could not start preview",e);
                    mCamera.release();
                    mCamera=null;
                }

            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if(mCamera!=null)
                {
                    mCamera.stopPreview();
                }
            }
        });

        mProgressContainer=v.findViewById(R.id.crime_camera_progressContainer);
        mProgressContainer.setVisibility(View.INVISIBLE);
        return v;
    }




    private Camera.ShutterCallback mShutterCallback=new Camera.ShutterCallback()
    {
        public void onShutter()
        {
            //display the progress indicator
            mProgressContainer.setVisibility(View.VISIBLE);
        }
    };

    private Camera.PictureCallback mJpegCallback= new Camera.PictureCallback()
    {
        @Override
        public void onPictureTaken(byte[]data,Camera camera)
        {
            String filename= UUID.randomUUID().toString()+".jpg";
            //Save the jpeg data to disk
            FileOutputStream os=null;
            boolean success=true;

            try
            {
                os=getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
                os.write(data);
            }
            catch(Exception e)
            {
                Log.e(TAG,"Error writing to file "+filename,e);
                success=false;
            }
            finally {
                try
                {
                    if(os!=null)
                        os.close();
                }
                catch(Exception e)
                {
                    Log.e(TAG,"Error closing file"+filename,e);
                    success=false;
                }
            }

            if(success)
            {
                Intent i=new Intent();
                i.putExtra(EXTRA_PHOTO_FILENAME,filename);
                getActivity().setResult(Activity.RESULT_OK,i);
            }
            else
            {
                getActivity().setResult(Activity.RESULT_CANCELED);
            }

            getActivity().finish();
        }
    };

    private Camera.Size getBestsupportedSize(List<Camera.Size>sizes,int width,int height)
    {
        Camera.Size bestSize=sizes.get(0);
        int largestArea=bestSize.width* bestSize.height;
        for(Camera.Size s :sizes)
        {
            int area=s.width* s.height;
            if(area > largestArea)
            {
                bestSize=s;
                largestArea=area;
            }
        }
        return bestSize;
    }
    @Override
    public void onResume()
    {
        super.onResume();
        mCamera=Camera.open(0);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(mCamera!=null)
        {
            mCamera.release();
            mCamera=null;
        }

    }

}

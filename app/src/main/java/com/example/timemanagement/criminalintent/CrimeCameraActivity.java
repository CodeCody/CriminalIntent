package com.example.timemanagement.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by TimeManagement on 7/10/2015.
 */
public class CrimeCameraActivity extends SingleFragmentActivity
{


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        //Hide the window title
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //Hide the status bar and other OS-level chrome
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment createFragment()
    {
        return new CrimeCameraFragment();
    }
}

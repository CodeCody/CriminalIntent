package com.example.timemanagement.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by TimeManagement on 3/30/2015.
 */
public class CrimeListActivity extends SingleFragmentActivity
{
    @Override
    protected Fragment createFragment()
    {
        return new CrimeListFragment();
    }
}

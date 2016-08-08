package com.example.timemanagement.criminalintent;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by TimeManagement on 4/5/2015.
 */
public class CrimePagerActivity extends AppCompatActivity
{
    private ViewPager mViewPager;
    private ArrayList<Crime> mCrimes;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        /*
        A ViewPager is like an AdapterView(the superclass of ListView). An AdapterView reuires an Adapter to provide views. A ViewPager requires a PagerAdapter.
        You can use FragmentStatePagerAdapter a subclass of PagerAdapter,to take care of many of the details. FragmentStatePagerAdapter will boil down to two
        simple methods. getCount() and getItem(int). When getItem is called for a position in your array of crimes, you will return a CrimeFragment configured to
        display the crime at that position.
         */
        mViewPager=new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        //get data set from crime lab,the array list of crimes.
        mCrimes=CrimeLab.get(this).getCrimes();
       //get activiy instance of fragmentManager
        FragmentManager fm=getSupportFragmentManager();
        //FragmentStatePagerAdapter is your agent for managing the conversation with the ViewPager
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            //This method fetches the Crime instance for the given position in the dataset. It then uses that Crime's ID to create and return
            //a properly configured CrimeFragment.
            @Override
            public Fragment getItem(int pos) {
                Crime crime=mCrimes.get(pos);
                return CrimeFragment.newInstance(crime.getmId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) {


            }

            @Override
            public void onPageSelected(int pos) {
                Crime crime=mCrimes.get(pos);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        //get crime id from extra
        UUID crimeId=(UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        //iterate through arraylist
        for(int i=0;i<mCrimes.size();i++)
        {
            if(mCrimes.get(i).getmId().equals(crimeId))//find a match
            {
                mViewPager.setCurrentItem(i);//set current item to the match,and break loop
                break;
            }
        }


    }
}

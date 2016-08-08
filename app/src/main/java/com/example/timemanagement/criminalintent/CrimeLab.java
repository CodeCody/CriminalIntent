package com.example.timemanagement.criminalintent;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by TimeManagement on 3/29/2015.
 */
/*
A singleton exists as long as the application stays in memory,so storing the list in a singleton will keep the crime data available
no matter what happens with activities,fragments,and their lifecycles.
 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private Context mAppContext;
    private ArrayList<Crime> mCrimes;
    private static  final String TAG="CrimeLab";
    private static final String FILENAME="crimes.json";
    private CriminalIntentJSONSerializer mSerializer;



    /*
    The Crime lab constructor requires a Context parameter. This is common in Android; having a Context parameter allows the singleton to start
    activities,access project resources,find your application's private storage,and more.
     */
    private CrimeLab(Context appContext)
    {
        mAppContext=appContext;
        mSerializer=new CriminalIntentJSONSerializer(mAppContext,FILENAME);

        try
        {
            mCrimes=mSerializer.loadCrimes();
        }
        catch(Exception e)
        {
            mCrimes=new ArrayList<Crime>();
            Log.e(TAG,"Error loading crimes: ",e);
        }

    }

    public boolean saveCrimes() {
        try {
            mSerializer.saveCrimes(mCrimes);
            Log.d(TAG, "crimes saved to file");
            return true;
        } catch(Exception e)
        {
            Log.e(TAG,"Error saving crimes");
            return false;
        }
    }
    public void addCrime(Crime c)
    {
        mCrimes.add(c);
    }

    public void deleteCrime(Crime c)
    {
        mCrimes.remove(c);
    }
    //Pass in a context and get the applicationContext to ensure that the context will exist throughout the application
    //Whenever you have an application wide singleton always use getApplicationContext()
    public static CrimeLab get(Context c) {
        if (sCrimeLab == null)
            sCrimeLab = new CrimeLab(c.getApplicationContext());

        return sCrimeLab;
    }
    //method to return ArrayList of crimes
    public ArrayList<Crime> getCrimes()
    {
        return mCrimes;
    }
    //method to return crime by ID,if not found return null
    public Crime getCrime(UUID id)
    {
        for(Crime c : mCrimes)
        {
            if (c.getmId().equals(id))
                return c;
        }
        return null;
    }


}

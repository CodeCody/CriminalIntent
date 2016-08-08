package com.example.timemanagement.criminalintent;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.text.SimpleDateFormat;
/**
 * Created by TimeManagement on 3/28/2015.
 */
public class CrimeFragment extends Fragment {
    private Crime mCrime;//crime object
    private EditText mTitleField;//text field for title
    private Button mDateButton;//button with date within
    private Button mReportButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private CheckBox mSolvedCheckBox;//solved check box
    private Button mSuspectButton;

    public static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_PHOTO=2;
    public static final int REQUEST_CONTACT=2;
    private static final String DIALOG_DATE = "date";
    private static final String TAG="CrimeFragment";
    private static final String DIALOG_IMAGE="image";
    private String filename=null;

    SimpleDateFormat mFormat = new SimpleDateFormat("EEEE,MMMM dd,yyyy");
    SimpleDateFormat mTime = new SimpleDateFormat("HH:MM:SS");


    //Fragment lifecycle methods are declared public instead of protected because they will be called
    //by whichever activity is hosting the fragment. It has a Bundle object which saves and retreives Fragment's state.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//call super class on create
        UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        CrimeLab.get(getActivity()).saveCrimes();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        showPhoto();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        PictureUtils.cleanImageView(mPhotoView);
    }



    //This method is where you inflate the layout for the fragment's view and return the inflated view to the hosting activity.
    //The LayoutInflater and ViewGroup are necessary to inflate the layout. The Bundle will contain data that this method can use to recreate
    //the view from a saved state.
    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState)
    {

        //You explicitly call LayoutInflater.inflater to inflate fragment's view. Pass in layout resource id
        //The second parameter is your view's parent which is usually needed to configure the widgets properly.
        //The third parameter tells the layout inflater whether to add the inflated view to the view's parent. False because you will add it in code.
        View v=inflater.inflate(R.layout.fragment_crime,parent,false);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            if(NavUtils.getParentActivityName(getActivity())!=null)
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);//setDisplayHomeAsUpEnabled(true);
        }
        //if cameara is not available
        PackageManager pm=getActivity().getPackageManager();
        boolean hasCamera=pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
        if(!hasCamera)
        {

        }
        mTitleField=(EditText)v.findViewById(R.id.crime_title);//get resource id
        mTitleField.setText(mCrime.getmTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {//set listener
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //when text changes set the charsequence to the crime title
            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mCrime.setmTitle(c.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
       //format date



        mDateButton=(Button)v.findViewById(R.id.crime_date);//get resource id
       updateDate(2);
        mDateButton.setOnClickListener((new View.OnClickListener() {
            public void onClick(View v) {

                FragmentManager fm = getActivity().getSupportFragmentManager();
                PickerFragment dialog = PickerFragment.getInstance(mCrime.getmId());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
             /*   DatePickerFragment dialog=DatePickerFragment.newInstance(mCrime.getmDate());
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
                dialog.show(fm,DIALOG_DATE); */
            }
        }));

        mSolvedCheckBox=(CheckBox)v.findViewById(R.id.crime_solved);//get resource id
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

         mPhotoButton=(ImageButton)v.findViewById(R.id.crime_imageButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
        public void onClick(View v)
            {
                Intent i=new Intent(getActivity(),CrimeCameraActivity.class);
                startActivityForResult(i, REQUEST_PHOTO);
            }
        });

        mPhotoView=(ImageView)v.findViewById(R.id.crime_imageView);
        mPhotoView.setOnClickListener(new View.OnClickListener()
        {
            @Override
        public void onClick(View v)
            {
                Photo p=mCrime.getmPhoto();
                if(p==null)
                    return;

                if(filename!=null && new File(filename).exists()) {
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    String path = getActivity().getFileStreamPath(p.getmFilename()).getAbsolutePath();
                    ImageFragment.newInstance(path).show(manager, DIALOG_IMAGE);
                }
            }


        });

        mPhotoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((AppCompatActivity) getActivity()).startSupportActionMode(mActionModeCallback);
                return true;

            }
        });

        mReportButton=(Button)v.findViewById(R.id.crime_reportButton);
        mReportButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
        public void onClick(View v)
            {
                Intent i=new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.crime_report_subject));
                i=Intent.createChooser(i,getString(R.string.send_report));
                startActivity(i);
            }
        });

        mSuspectButton=(Button)v.findViewById(R.id.crime_suspectButton);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm=((AppCompatActivity)getActivity()).getPackageManager();

                Intent i=new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                List<ResolveInfo> activities=pm.queryIntentActivities(i,0);
                boolean isIntentSafe=activities.size()>0;
                if(isIntentSafe)
                startActivityForResult(i,REQUEST_CONTACT);
            }
        });

        return v;//return view
    }

    private void showPhoto()
    {
        //Rest the image buttons image based on our photo
        Photo p=mCrime.getmPhoto();
        BitmapDrawable b=null;
        if(p!=null)
        {
            if(filename!=null)
            {
                filename = getActivity().getFileStreamPath(p.getmFilename()).getAbsolutePath();
                if (new File(filename).exists())
                {
                    b = PictureUtils.getScaledDrawable(getActivity(), filename);
                }
            }
        }

        mPhotoView.setImageDrawable(b);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                if(NavUtils.getParentActivityName(getActivity()) != null)
                {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


/*
Every fragment can have a Bundle object attached to it. This bundle contains key-value pairs that work just like the intent extras of an Activity.
Each pair is known as an argument. To create fragment arguments,you first create a Bundle object. Next you type specific "put" methods of Bundle to add
arguments to the bundle.
 */
   public static CrimeFragment newInstance(UUID crimeId)
   {
       Bundle args=new Bundle();
       args.putSerializable(EXTRA_CRIME_ID,crimeId);
       CrimeFragment fragment=new CrimeFragment();
       fragment.setArguments(args);

       return fragment;
   }

    private void updateDate(int choice)
    {
        Date date=mCrime.getmDate();
        StringBuilder dateString=new StringBuilder();
        if(choice==0)
            dateString.append(mFormat.format(date)).append("  ").append(mTime.format(date));
        else if(choice == 1)
            dateString.append(mFormat.format(date)).append("  ").append(mTime.format(date));
        else
        {
            dateString.append(mFormat.format(date)).append("  ").append(mTime.format(date));

            mDateButton.setText(dateString.toString());
           // DateField.setText(mFormat.format(date));
           // TimeField.setText(mTime.format(date));
        }

    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        if(resultCode != Activity.RESULT_OK)
            return;
        if(requestCode==REQUEST_DATE)
        {
            Date date=(Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setmDate(date);
            updateDate(0);
        }
        else if(requestCode==REQUEST_TIME)
        {
            Date date=(Date)data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setmDate(date);
            updateDate(1);
        }
        else if(requestCode==REQUEST_PHOTO)
        {
            //Create a new Photo object and attach it to the crime

                if (filename != null && new File(filename).exists() != false)
                    deletePhoto(filename);

            filename=data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
            if(filename!=null)
            {
                Photo p=new Photo(filename);
                mCrime.setPhoto(p);
                showPhoto();
            }
        }
        else if(requestCode==REQUEST_CONTACT)
        {
            Uri contactUri=data.getData();

            String[] queryFields=new String[]{ContactsContract.Contacts.DISPLAY_NAME};
            //Perform your query-the contectUri is like a "where'
            //clause here
            Cursor c =getActivity().getContentResolver().query(contactUri,queryFields,null,null,null);

            //Double
            if(c.getCount()==0)
            {
                c.close();
                return;
            }

            //Pull out the first column of the first row of data
            //that is your suspects name
            c.moveToFirst();
            mCrime.setmSuspect(c.getString(0));
            mSuspectButton.setText(mCrime.getmSuspect());
            c.close();
        }
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.photo_delete, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_item_delete_photo:
                    deletePhoto(filename);
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
          //  mActionMode = null;
        }
    };

    private void deletePhoto(String filename)
    {
        mPhotoView.setImageDrawable(null);
        PictureUtils.cleanImageView(mPhotoView);
        if(filename!=null) {

            File file = new File(filename);
            file.delete();


        }
    }

    private String getCrimeReport()
    {
        String solvedString=null;
        if(mCrime.isSolved())
            solvedString=getString(R.string.crime_report_solved);
        else
            solvedString=getString(R.string.crime_report_unsolved);

        String dateFormat="EEE,MMM,dd";
        String dateString=DateFormat.format(dateFormat,mCrime.getmDate()).toString();

        String suspect=mCrime.getmSuspect();
        if(suspect==null)
            suspect=getString(R.string.crime_report_no_suspect);
        else
            suspect=getString(R.string.crime_report_suspect,suspect);

        String report=getString(R.string.crime_report,mCrime.getmTitle(),dateString,solvedString,suspect);

        return report;

    }
}

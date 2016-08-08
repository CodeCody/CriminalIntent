package com.example.timemanagement.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Date;
import java.util.UUID;

/**
 * Created by TimeManagement on 5/27/2015.
 */
public class PickerFragment extends android.support.v4.app.DialogFragment
{

    private Button datePicker;
    private Button timePicker;
    private Crime mCrime;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_choice,null);
        UUID crimeId=(UUID)getArguments().getSerializable("ID");
        mCrime=CrimeLab.get(getActivity()).getCrime(crimeId);
        datePicker=(Button)v.findViewById(R.id.button2);
        timePicker=(Button)v.findViewById(R.id.button);


        datePicker.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager manager=getActivity().getSupportFragmentManager();
                DatePickerFragment dialog=DatePickerFragment.newInstance(mCrime.getmDate());
                dialog.setTargetFragment(PickerFragment.this, 0);
                dialog.show(manager,"date");


            }

        });

        timePicker.setOnClickListener(new View.OnClickListener()
        {
            @Override
        public void onClick(View v)
            {
                android.support.v4.app.FragmentManager manager=getActivity().getSupportFragmentManager();
                TimePickerFragment dialog=TimePickerFragment.getInstance(mCrime.getmDate());
                dialog.setTargetFragment(PickerFragment.this,1);
                dialog.show(manager,"time");
            }
        });

        return new AlertDialog.Builder(getActivity()).setView(v).create();

    }

 public static PickerFragment getInstance(UUID crimeId)
 {
     Bundle args=new Bundle();
     args.putSerializable("ID",crimeId);
     PickerFragment fragment=new PickerFragment();
     fragment.setArguments(args);
     return fragment;
 }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
         getTargetFragment().onActivityResult(requestCode,resultCode,data);
    }



}

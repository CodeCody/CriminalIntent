package com.example.timemanagement.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

/**
 * Created by TimeManagement on 5/26/2015.
 */
public class TimePickerFragment extends android.support.v4.app.DialogFragment
{
    private Calendar time;
    private Date mDate;
    public static final String EXTRA_TIME="time";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View v=getActivity().getLayoutInflater().inflate(R.layout.dialog_time,null);
        time=Calendar.getInstance();
        mDate=(Date)getArguments().getSerializable(EXTRA_TIME);
        //time(mDate);
        int hour=Calendar.HOUR;
        int min=Calendar.MINUTE;
        int sec=Calendar.SECOND;

        TimePicker timePicker=(TimePicker)v.findViewById(R.id.timePicker);
        timePicker.setCurrentHour(Calendar.HOUR);
        timePicker.setCurrentMinute(Calendar.MINUTE);


        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            public void onTimeChanged(TimePicker view, int hour, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR, hour);
                calendar.set(Calendar.MINUTE, minute);
                mDate = calendar.getTime();
            }
        });





        return new AlertDialog.Builder(getActivity()).setView(v).setTitle("Set Time").setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK);
                    }
                }).create();
    }

    public void sendResult(int resultCode)
    {
        Intent i=new Intent();
        i.putExtra(EXTRA_TIME,mDate);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }

    public static TimePickerFragment getInstance(Date date)
    {
        Bundle args=new Bundle();
        args.putSerializable(EXTRA_TIME,date);
        TimePickerFragment fragment=new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }
}

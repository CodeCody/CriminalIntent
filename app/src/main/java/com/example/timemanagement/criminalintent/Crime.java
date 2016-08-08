package com.example.timemanagement.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by TimeManagement on 3/28/2015.
 */
public class Crime {

    private UUID mId;//give each crime object a unique identification number
    private java.text.DateFormat mFormat;//Dateformat object for date

    private static final String JSON_ID="id";
    private static final String JSON_TITLE="title";
    private static final String JSON_SOLVED="solved";
    private static final String JSON_DATE="date";
    private static final String JSON_PHOTO="photo";
    private static final String JSON_SUSPECT="suspect";
    private Photo mPhoto=null;


    public Date getmDate()
    {
        return mDate;
    }//retreive date

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }//set date

    public boolean isSolved() {
        return solved;
    }//return boolean value of solved

    public void setSolved(boolean solved) {
        this.solved = solved;
    }//set boolean value

    private String mTitle;//title
    private Date mDate;//date object for date
    private boolean solved;//variable for solved or not
    private String mSuspect;
    public Crime()
    {
        mId=UUID.randomUUID();//assign mId a random id
        mDate=new Date();//get today's date
        mFormat=new SimpleDateFormat("EEEE,MMMM dd,yyyy");
    }

    public Crime(JSONObject json) throws JSONException
    {
        mId=UUID.fromString(json.getString(JSON_ID));
        if(json.has(JSON_TITLE))
        {
            mTitle=json.getString(JSON_TITLE);
        }

        solved=json.getBoolean(JSON_SOLVED);
        mDate=new Date(json.getLong(JSON_DATE));

        if(json.has(JSON_PHOTO))
        {
            mPhoto=new Photo(json.getJSONObject(JSON_PHOTO));
        }
        if(json.has(JSON_SUSPECT))
        {
            mSuspect=json.getString(JSON_SUSPECT);
        }

    }

    public UUID getmId() {
        return mId;
    }//return id

    public String getmTitle() {
        return mTitle;
    }//return title

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }//set title

    @Override
    public String toString()
    {
        return mTitle;
    }

    public JSONObject toJSON() throws JSONException
    {
        JSONObject json=new JSONObject();
        json.put(JSON_ID,mId.toString());
        json.put(JSON_TITLE,mTitle);
        json.put(JSON_SOLVED,solved);
        json.put(JSON_DATE,mDate.getTime());
        json.put(JSON_SUSPECT,mSuspect);

        if(mPhoto!=null)
        {
            json.put(JSON_PHOTO,mPhoto.toJSON());
        }


        return json;
    }

    public Photo getmPhoto()
    {
        return mPhoto;
    }

    public void setPhoto(Photo p)
    {
        mPhoto=p;
    }

    public void setmSuspect(String suspect)
    {
        mSuspect=suspect;
    }

    public String getmSuspect()
    {
        return mSuspect;
    }


}

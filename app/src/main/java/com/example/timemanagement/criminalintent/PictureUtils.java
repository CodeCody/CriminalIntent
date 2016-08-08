package com.example.timemanagement.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.widget.ImageView;

/**
 * Created by TimeManagement on 7/21/2015.
 */
public class PictureUtils
{
    /*
    Get a BitmapDrawable from a local file that is scaled down to fit the current Window size
     */

    @SuppressWarnings("deprecation")
    public static BitmapDrawable getScaledDrawable(Activity a ,String path)
    {
        Display display=a.getWindowManager().getDefaultDisplay();
        Matrix matrix=new Matrix();
        matrix.postRotate(90);
        float destWidth=display.getWidth();
        float destHeight=display.getHeight();
        Bitmap bitmap2=null;

        //Read in the dimensions of the image on disk

        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path, options);
        float srcWidth=options.outWidth;
        float srcHeight=options.outHeight;

        int inSampleSize=1;
        if(srcHeight > destHeight || srcWidth > destWidth)
        {
            if(srcWidth > srcHeight)
            {
                inSampleSize=Math.round(srcHeight/destHeight);
            }
            else
            {
                inSampleSize=Math.round((srcWidth/srcHeight));
            }
        }

        options=new BitmapFactory.Options();
        options.inSampleSize=inSampleSize;

        Bitmap bitmap=BitmapFactory.decodeFile(path,options);
      bitmap2=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return new BitmapDrawable(a.getResources(),bitmap2);

    }


    public static void cleanImageView(ImageView imageView)
    {
        if(!(imageView.getDrawable() instanceof BitmapDrawable))
            return;

        //Clean up the views image for the sake of memory
        BitmapDrawable b=(BitmapDrawable)imageView.getDrawable();

        b.getBitmap().recycle();
        imageView.setImageDrawable(null);
    }
}

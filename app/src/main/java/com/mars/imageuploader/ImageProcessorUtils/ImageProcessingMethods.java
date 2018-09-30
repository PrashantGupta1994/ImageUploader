package com.mars.imageuploader.ImageProcessorUtils;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;

public class ImageProcessingMethods {

    public static byte[] bytes(Bitmap mB){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mB.compress(Bitmap.CompressFormat.JPEG, 40, stream);
        //mB.recycle();
        return stream.toByteArray();
    }

    public static int calculateNoOfColumns(Context ctx) {
        Context context = new WeakReference<>(ctx).get();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }
}

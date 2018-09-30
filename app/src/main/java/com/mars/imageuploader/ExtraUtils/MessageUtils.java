package com.mars.imageuploader.ExtraUtils;


import android.content.Context;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MessageUtils {

    /**
     * Message and intent name helper.
     * */
    public static final String INTENT_TAG = "URL";

    public static void toast(Context context, CharSequence chars, int mLength){
        WeakReference<Context> weakContext = new WeakReference<>(context);
        Toast.makeText(weakContext.get(), chars, mLength).show();
    }
}

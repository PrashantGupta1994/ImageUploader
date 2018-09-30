package com.mars.imageuploader.ExtraUtils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.lang.ref.WeakReference;

public class ConnectionUtils {

    /***
     * API constants
     * */
    public static final String S_NAME = "MarsPlay";
    public static final String DO_NOT_SHOW_AGAIN = "idk";

    public static final String IMAGE_TAG = "mars";
    public static final String JSON = ".json";
    public static final String JPEG = ".jpg";

    public static final String CLOUD_NAME = "test-cloud-007";
    public static final String CLOUD_KEY = "224597545485481";
    public static final String CLOUD_SECRET = "1y_3I_yHTPsMtFNUVRMW0woY7b8";
    public static final String URL_BASE = "http://res.cloudinary.com/"+ CLOUD_NAME +"/image/";

    public static final String URL_DOWNLOAD_IMAGE_ID = URL_BASE + "list/" + IMAGE_TAG + JSON;
    public static final String URL_DOWNLOAD_IMAGE = URL_BASE + "upload/" ;

    // connection detector
    public static boolean isConnected(Context ctx){
        Context context = new WeakReference<>(ctx).get();

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert manager != null;
        NetworkInfo connection = manager.getActiveNetworkInfo();
        return connection != null && connection.isConnectedOrConnecting();
    }
}

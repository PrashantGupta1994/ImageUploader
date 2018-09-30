package com.mars.imageuploader.ExtraUtils;


public class APIUtils {

    public static final String S_NAME = "MarsPlay";
    public static final String DO_NOT_SHOW_AGAIN = "idk";

    public static final String IMAGE_TAG = S_NAME;
    public static final String JSON = ".json";
    public static final String JPEG = ".jpg";

    public static final String CLOUD_NAME = "test-cloud-007";
    public static final String CLOUD_KEY = "224597545485481";
    public static final String CLOUD_SECRET = "1y_3I_yHTPsMtFNUVRMW0woY7b8";
    public static final String URL_BASE = "http://res.cloudinary.com/"+ CLOUD_NAME +"/image/";

    public static final String URL_DOWNLOAD_IMAGE_ID = URL_BASE + "list/" + IMAGE_TAG + JSON;
    public static final String URL_DOWNLOAD_IMAGE = URL_BASE + "upload/" ;
}

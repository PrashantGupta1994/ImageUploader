package com.mars.imageuploader.ExtraUtils;

import java.util.HashMap;
import java.util.Map;


public class CloudUtils {
    /**
     * Cloud access key and name
     * */
    private static CloudUtils Instance;

    public static synchronized CloudUtils getInstance() {
        if (Instance == null){
            Instance = new CloudUtils();
        }
        return Instance;
    }

    private CloudUtils() {
    }

    public Map config(){
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", ConnectionUtils.CLOUD_NAME);
        config.put("api_key", ConnectionUtils.CLOUD_KEY);
        config.put("api_secret", ConnectionUtils.CLOUD_SECRET);
        return config;
    }
}

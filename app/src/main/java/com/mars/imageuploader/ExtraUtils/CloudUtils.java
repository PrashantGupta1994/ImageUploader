package com.mars.imageuploader.ExtraUtils;

import java.util.HashMap;
import java.util.Map;


public class CloudUtils {
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
        config.put("cloud_name", APIUtils.CLOUD_NAME);
        config.put("api_key", APIUtils.CLOUD_KEY);
        config.put("api_secret", APIUtils.CLOUD_SECRET);
        return config;
    }
}

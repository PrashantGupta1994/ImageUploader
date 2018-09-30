package com.mars.imageuploader.Shell;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.cloudinary.android.MediaManager;
import com.mars.imageuploader.ExtraUtils.CloudUtils;


public class ShellActivity extends AppCompatActivity {

    // base class for any common feature among other activity.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // full screen
    public void makeWindowFullScreen(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

}

package com.mars.imageuploader.Shell;

import android.app.Application;

//import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Prashant on 30-09-2018.
 */

public class Shell extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //if (LeakCanary.isInAnalyzerProcess(this)) {
        //    return;
        //}
        //LeakCanary.install(this);
    }
}

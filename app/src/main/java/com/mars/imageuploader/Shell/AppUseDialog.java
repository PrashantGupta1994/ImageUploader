package com.mars.imageuploader.Shell;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.mars.imageuploader.R;

import java.lang.ref.WeakReference;

import static com.mars.imageuploader.ExtraUtils.APIUtils.DO_NOT_SHOW_AGAIN;

public class AppUseDialog {

    public static void appUseDialog(SharedPreferences s, Context context) {
        Context c = new WeakReference<>(context).get();
        if (!s.getBoolean(DO_NOT_SHOW_AGAIN, false)){
            initialDialog(s, c);
        }
    }


    private static void initialDialog(SharedPreferences s, Context c){
        Dialog dialog = new Dialog(c);
        final SharedPreferences.Editor mEditor = s.edit();
        mEditor.putBoolean(DO_NOT_SHOW_AGAIN, false);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.app_use_dialog);

        ((CheckBox)dialog.findViewById(R.id.main_check)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mEditor.putBoolean(DO_NOT_SHOW_AGAIN, isChecked);
                mEditor.apply();
            }
        });
        dialog.show();
    }
}

package com.mars.imageuploader.Activities;

import android.os.Bundle;

import com.mars.imageuploader.ExtraUtils.MessageUtils;
import com.mars.imageuploader.ImageProcessorUtils.MarsPlayImageZoomView;
import com.mars.imageuploader.R;
import com.mars.imageuploader.Shell.ShellActivity;
import com.squareup.picasso.Picasso;

public class FullImageActivity extends ShellActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        // init custom zoom imageview
        MarsPlayImageZoomView mView = findViewById(R.id.main_image);

        // get URL of selected image from image thumbnail
        String URL = getIntent().getStringExtra(MessageUtils.INTENT_TAG);
        Picasso.get().load(URL).placeholder(R.drawable.ic_loading).error(R.drawable.ic_error).into(mView);

        MessageUtils.toast(this, "Pinch in/out for image zoom!", 1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

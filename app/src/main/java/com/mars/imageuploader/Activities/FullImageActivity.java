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

        MarsPlayImageZoomView mView = findViewById(R.id.main_image);

        String URL = getIntent().getStringExtra(MessageUtils.INTENT_TAG);
        Picasso.get().load(URL).placeholder(R.drawable.ic_loading).error(R.drawable.ic_error).into(mView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

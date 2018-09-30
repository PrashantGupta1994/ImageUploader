package com.mars.imageuploader.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.mars.imageuploader.ExtraUtils.APIUtils;
import com.mars.imageuploader.ExtraUtils.CloudUtils;
import com.mars.imageuploader.ExtraUtils.MessageUtils;
import com.mars.imageuploader.R;
import com.mars.imageuploader.Shell.ShellActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import static com.mars.imageuploader.ExtraUtils.APIUtils.S_NAME;
import static com.mars.imageuploader.ImageProcessorUtils.ImageSizeReducer.compressImage;
import static com.mars.imageuploader.Shell.AppUseDialog.appUseDialog;

public class ImageActivity extends ShellActivity implements View.OnClickListener{

    private Uri mCroppedImageUri = null;

    private ImageButton mAddImage;
    private ImageButton mSendImage;
    private ImageButton mNext;
    private LinearLayout mParent;
    private ImageView mImageView;

    public SharedPreferences mSharedPreferences;

    private final static int REQUEST_CODE = 101;
    private final String[] mPermissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    public void makeWindowFullScreen() {
        super.makeWindowFullScreen();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        initConstants();
        appUseDialog(mSharedPreferences, this);
    }

    private void initConstants(){
        try {
            MediaManager.init(this, CloudUtils.getInstance().config());
        }catch (IllegalStateException ignored){
        }finally {
            mSharedPreferences = getSharedPreferences(S_NAME, MODE_PRIVATE);
            mAddImage = findViewById(R.id.main_fab);
            mSendImage = findViewById(R.id.main_fab_send);
            mImageView = findViewById(R.id.main_image);
            mParent = findViewById(R.id.main_buttons);
            mNext = findViewById(R.id.main_fab_next);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAddImage.setOnClickListener(this);
        mSendImage.setOnClickListener(this);
        mNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_fab:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(arePermissionsEnabled()){
                        CropImage.startPickImageActivity(ImageActivity.this);
                    }else{
                        requestMultiplePermissions();
                    }
                }
                break;

            case R.id.main_fab_send:
                if (mImageView.getDrawable() != null && mCroppedImageUri != null){
                    MessageUtils.toast(ImageActivity.this, "Processing Image!", 0);
                    byte[] mArray = compressImage(mCroppedImageUri);

                    mParent.setVisibility(View.GONE);
                    sendImage(mArray);
                }else {
                    MessageUtils.toast(ImageActivity.this, "No images available!", 0);
                }
                break;

            case R.id.main_fab_next:
                startActivity(new Intent(ImageActivity.this, AllImages.class));
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean arePermissionsEnabled(){
        for(String permission : mPermissions){
            if(checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestMultiplePermissions(){
        List<String> remainingPermissions = new ArrayList<>();

        for (String permission : mPermissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(permission)){
                    showRequestPermissionRationaleDialog();
                    return;
                }else {
                    remainingPermissions.add(permission);
                }
            }
        }

        requestPermissions(remainingPermissions.toArray(new String[remainingPermissions.size()]), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /** handle result of pick image chooser */
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            startCropImageActivity(imageUri);
        }

        /** handle result of CropImageActivity */
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mCroppedImageUri = result.getUri();
                mImageView.setImageURI(mCroppedImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                MessageUtils.toast(this, "Cropping failed: " + result.getError(), 0);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    CropImage.startPickImageActivity(ImageActivity.this);
                }
                break;
        }
    }

    private void showRequestPermissionRationaleDialog(){
        new AlertDialog.Builder(this)
                .setMessage("Permissions needed to use application features.")
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettingsConfigActivity();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    private void startAppSettingsConfigActivity() {
        startActivity(new Intent()
                .setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .addCategory(Intent.CATEGORY_DEFAULT)
                .setData(Uri.parse("package:" + this.getPackageName()))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        );
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    private void sendImage(byte[] arr){
        MediaManager.get().upload(arr).option("tags", APIUtils.IMAGE_TAG).callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {
                MessageUtils.toast(ImageActivity.this, "Uploading Image!", 0);
            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {
            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                MessageUtils.toast(ImageActivity.this, "File Uploaded!", 0);
                mImageView.setImageURI(null);
                mParent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                MessageUtils.toast(ImageActivity.this, error.getDescription(), 1);
                mParent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
            }
        }).dispatch();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}

package com.mars.imageuploader.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mars.imageuploader.ExtraUtils.ConnectionUtils;
import com.mars.imageuploader.ImageProcessorUtils.ImageProcessingMethods;
import com.mars.imageuploader.ExtraUtils.MessageUtils;
import com.mars.imageuploader.R;
import com.mars.imageuploader.Shell.ShellActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.mars.imageuploader.ExtraUtils.ConnectionUtils.isConnected;

public class AllImages extends ShellActivity{

    // constants
    private final static String TAG = AllImages.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RequestQueue mRequestQueue = null;
    private List<String> mImageUrl = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_images);

        // init constants and others
        mRecyclerView = findViewById(R.id.main_list);
        mRequestQueue = Volley.newRequestQueue(this);

        // get image URLs from cloud
        if (isConnected(this))
            getDataFromCloud();
        else
            MessageUtils.toast(this, "No connection available!", 1);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     *  Get image URLs from cloud using JsonObject request
        Sample date below -
     {
     "resources": [
     {
     "public_id": "mxudzvbexfn10dtl2xzs",
     "version": 1538306390,
     "format": "jpg",
     "width": 960,
     "height": 1280,
     "type": "upload",
     "created_at": "2018-09-30T11:19:50Z"
     }
     **/
    private void getDataFromCloud(){
        JsonObjectRequest mReq = new JsonObjectRequest(Request.Method.GET, ConnectionUtils.URL_DOWNLOAD_IMAGE_ID, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray mArray = response.getJSONArray("resources");
                    JSONObject mObject;
                    for (int a = 0; a < mArray.length(); a++){
                        mObject = mArray.getJSONObject(a);
                        String ID = mObject.getString("public_id");
                        mImageUrl.add(ID.equals("") ? "NA" : ConnectionUtils.URL_DOWNLOAD_IMAGE + ID + ConnectionUtils.JPEG);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.toString());
                }
                updateGridImages(mImageUrl);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MessageUtils.toast(AllImages.this, "Something went wrong!\n " +
                            "If image is uploaded, cloud takes 30 sec. to process the image URL through API. Try again after sometime!", 1);
            }
        });
        mReq.setTag(TAG);
        mRequestQueue.add(mReq);
    }

    // fill data to list after list check
    private void updateGridImages(List<?> mList){
        if (!mList.isEmpty()) {
            int mColumn = ImageProcessingMethods.calculateNoOfColumns(this);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(AllImages.this, mColumn);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(new SpacingItemDecoration(mColumn, 5, true));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(new ImageThumbs(AllImages.this, mImageUrl));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    // remove callbacks or streams
    @Override
    protected void onDestroy() {
        mRequestQueue.cancelAll(TAG);
        mRecyclerView.setAdapter(null);
        super.onDestroy();
    }

    // RecyclerView for image thumbnails
    private class ImageThumbs extends RecyclerView.Adapter<ImageThumbs.ViewHolder>{

        private Context context;
        private List<String> mList = new ArrayList<>();

        public ImageThumbs(Context context, List<String> mList) {
            this.mList = mList;
            this.context = new WeakReference<>(context).get();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.image_thumbs, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Picasso.get()
                    .load(mList.get(position))
                    .placeholder(R.drawable.ic_loading)
                    .error(R.drawable.ic_error)
                    .into(holder.mView);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private ImageView mView;

            public ViewHolder(View itemView) {
                super(itemView);
                mView = itemView.findViewById(R.id.m_view);
                mView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.m_view){
                    String URL = mList.get(getAdapterPosition());
                    context.startActivity(new Intent(context, FullImageActivity.class).putExtra(MessageUtils.INTENT_TAG, URL));
                }
            }
        }
    }

    /** spacing for grid.
     *  Taken from - @auther from Github
     */
    public class SpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int column;
        private int spacing;
        private boolean spaceEdging;

        public SpacingItemDecoration(int column, int spacing, boolean spaceEdging) {
            this.column = column;
            this.spacing = spacing;
            this.spaceEdging = spaceEdging;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % this.column;

            if (spaceEdging) {
                outRect.left = spacing - column * spacing / this.column;
                outRect.right = (column + 1) * spacing / this.column;

                if (position < this.column) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / this.column;
                outRect.right = spacing - (column + 1) * spacing / this.column;
                if (position >= this.column) {
                    outRect.top = spacing;
                }
            }
        }
    }
}

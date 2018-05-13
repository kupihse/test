package com.example.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alexvasilkov.gestures.views.GestureImageView;
import com.example.activities.FullScreenImageActivity;
import com.example.application.R;
import com.example.layouts.SingleImageLayout;
import com.liuguangqiang.swipeback.SwipeBackLayout;

import java.util.ArrayList;

/**
 * Created by 31719 on 20.02.2018.
 */

public class FullScreenImagePagerAdapter extends PagerAdapter {
    private Activity _activity;
    private ArrayList<Bitmap> _images;

    // constructor
    public FullScreenImagePagerAdapter(Activity activity,
                                       ArrayList<Bitmap> images) {
        this._activity = activity;
        this._images = images;
    }

    @Override
    public int getCount() {
        return this._images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final GestureImageView imgDisplay;


        View viewLayout = _activity.getLayoutInflater().inflate(R.layout.layout_full, container,
                false);

        SwipeBackLayout layout = viewLayout.findViewById(R.id.swipe_back);
        layout.setDragEdge(SwipeBackLayout.DragEdge.BOTTOM);

        imgDisplay = viewLayout.findViewById(R.id.imgDisplay);
        Bitmap bitmap = _images.get(position);
        imgDisplay.setImageBitmap(bitmap);

        container.addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);

    }

}

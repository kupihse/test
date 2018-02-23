package com.example.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.alexvasilkov.gestures.views.GestureImageView;
import com.example.application.R;
import com.liuguangqiang.swipeback.SwipeBackLayout;

import java.util.ArrayList;

/**
 * Created by 31719 on 20.02.2018.
 */

public class FullScreenImageAdapter extends PagerAdapter {
    private Activity _activity;
    private ArrayList<Bitmap> _images;

    // constructor
    public FullScreenImageAdapter(Activity activity,
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
        Button btnClose;

        View viewLayout = _activity.getLayoutInflater().inflate(R.layout.layout_full, container,
                false);


        SwipeBackLayout layout = viewLayout.findViewById(R.id.swipe_back);
        layout.setDragEdge(SwipeBackLayout.DragEdge.BOTTOM);

        imgDisplay = viewLayout.findViewById(R.id.imgDisplay);
        btnClose = viewLayout.findViewById(R.id.btnClose);
        Bitmap bitmap = _images.get(position);
        imgDisplay.setImageBitmap(bitmap);


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _activity.finish();
            }
        });

        container.addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);

    }
}

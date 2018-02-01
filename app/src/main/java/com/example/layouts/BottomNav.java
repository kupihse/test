package com.example.layouts;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.activities.AddProductActivity;
import com.example.activities.ScrollingActivity;
import com.example.application.R;
import com.example.s1k0de.entry.EntryFormActivity;

/**
 * Created by Andreyko0 on 31/01/2018.
 */

public class BottomNav extends BottomNavigationView {
    public BottomNav(Context ctx) {
        super(ctx);
        init(ctx);
    }
    public BottomNav(Context ctx, AttributeSet set) {
        super(ctx, set);
        init(ctx);
    }
    public BottomNav(Context ctx, AttributeSet set, int defStyle) {
        super(ctx, set, defStyle);
        init(ctx);
    }

    void init(Context context) {
        inflate(context, R.layout.bottom_nav_view, this);
//        this.setOnNavigationItemSelectedListener(this);
    }
//
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.bottom_nav_list:
//                getContext().startActivity(new Intent(this.getContext(), ScrollingActivity.class));
//                return true;
//            case R.id.bottom_nav_search:
//                getContext().startActivity(new Intent(this.getContext(), AddProductActivity.class));
//                return true;
//            case R.id.bottom_nav_user:
//                getContext().startActivity(new Intent(this.getContext(), EntryFormActivity.class));
//                return true;
//        }
//        return false;
//    }

}

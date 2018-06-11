package com.example;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.R;
import com.example.storages.ImageStorage;
import com.example.storages.WishList;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Andreyko0 on 05/02/2018.
 */

public class HSEOutlet extends Application {
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Log.d("App: ", "started");
        if (!ImageStorage.init(getApplicationContext())) {
            Toast toast = Toast.makeText(getApplicationContext(), "No cache dir", Toast.LENGTH_SHORT);
            toast.show();
            try {
                Thread.sleep(toast.getDuration());
            } catch (InterruptedException e) {
            } finally {
                System.exit(0);
            }
        }

        EventBus.getDefault().register(WishList.getInstance());
//        CurrentUser.init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        EventBus.getDefault().unregister(WishList.getInstance());
        super.onTerminate();
    }
}

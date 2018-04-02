package com.example;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.example.storages.CurrentUser;
import com.example.storages.ImageStorage;

/**
 * Created by Andreyko0 on 05/02/2018.
 */

public class HSEOutlet extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
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

        CurrentUser.init(getApplicationContext());

    }
}

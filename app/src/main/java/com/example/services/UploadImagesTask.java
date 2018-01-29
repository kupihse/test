package com.example.services;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.graphics.BitmapCompat;
import android.widget.Toast;

import com.example.application.R;
import com.example.models.SendableImage;
import com.example.storages.ImageStorage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Andreyko0 on 29/01/2018.
 */

public class UploadImagesTask extends AsyncTask<String,Void,Void> {
    private int got = 0;
    private int sent = 0;

    private Context context;

    public UploadImagesTask setContext(Context context) {
        this.context = context;
        return this;
    }

    public Void doInBackground(String... ids) {
        this.got = ids.length;
        for (String id: ids) {
            Bitmap bmp = ImageStorage.get(id);

            int imSize = BitmapCompat.getAllocationByteCount(bmp);
            int imSizeKB = imSize / 1024;
            int quality;
            if (imSizeKB > 512) {
                quality = 51200 / imSizeKB;
            } else {
                quality = 100;
            }
            SendableImage img = SendableImage.encode(id, bmp, quality);
            Services.images.add(img).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    // #todo
                    UploadImagesTask.this.sent++;
                    UploadImagesTask.this.publishProgress();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    // #todo
                }
            });
        }

        return null;
    }

    public void onProgressUpdate(Void... progress) {
        Toast.makeText(this.context, "Uploaded " + this.sent+"/"+this.got + " images", Toast.LENGTH_SHORT).show();
//        setProgressPercent(progress[0]);
    }

    public void onPostExecute(Void result) {
//        showDialog("Downloaded " + result + " bytes");
        Toast.makeText(this.context, "Uploaded1 " + this.sent+"/"+this.got + " images", Toast.LENGTH_SHORT).show();
    }
}

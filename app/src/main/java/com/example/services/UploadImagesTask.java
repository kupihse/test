package com.example.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.storages.ImageStorage;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Andreyko0 on 29/01/2018.
 */

public class UploadImagesTask extends AsyncTask<List<String>, Integer, Void> {
    private int got = 0;

    private Context context;

    public UploadImagesTask(Context context) {
        this.context = context;
    }

    protected Void doInBackground(List<String>... ids) {

        if (ids.length == 0) {
            throw new IllegalArgumentException("Provide 1 argument");
        }
        this.got = ids[0].size();
        int i = 0;
        for (String id : ids[0]) {
            i++;
//            Bitmap bmp = ImageStorage.get(id);
//            int imSize = bmp.getByteCount();
//            int imSizeKB = imSize / 1024;
//            int quality;
//            if (imSizeKB > 512) {
//                quality = 51200 / imSizeKB;
//            } else {
//                quality = 100;
//            }
            publishProgress(99, 50, -1);
//            SendableImage img = SendableImage.encode(id, bmp, quality);
            final int x = i;

            File file = ImageStorage.diskCache.getFile(id);
            if (!file.exists()) {
                Log.d("UPLOAD", "fail");
            }
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            Services.images.upload(id, body).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    // 1 –– success, 0 –– fail
                    publishProgress(1, x);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    publishProgress(0, x);
                }
            });
        }

        return null;
    }

    protected void onProgressUpdate(Integer... progress) {
        if (progress[0] == 99) {
            Log.d("QUALITY", ": " + progress[1]);
            Log.d("SIZE", ": " + progress[2]);
            return;
        }
        String successOrFail = progress[0] == 1 ? "Success: " : "Fail: ";
        Toast.makeText(this.context,
                successOrFail
                        + progress[1]
                        + "/"
                        + this.got, Toast.LENGTH_SHORT).show();
    }

}

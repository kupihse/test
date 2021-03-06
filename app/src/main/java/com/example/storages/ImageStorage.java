package com.example.storages;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.cache.images.DiskCache;
import com.example.cache.images.MemoryCache;
import com.example.services.Services;

import java.lang.ref.WeakReference;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Andreyko0 on 25/01/2018.
 */

public class ImageStorage {
    public static MemoryCache memoryCache = new MemoryCache();
    public static DiskCache diskCache = new DiskCache();


    public static boolean init(Context context) {
        memoryCache.init();
        return diskCache.init(context);
    }

    private static int calculateQuality(Bitmap img) {
        int imSize = img.getByteCount();
        int imSizeKB = imSize / 1024;
        if (imSizeKB > 512) {
            return 51200 / imSizeKB;
        }
        return 100;
    }

    public static String add(Bitmap img) {
        String id = UUID.randomUUID().toString();

        int s1 = img.getByteCount();
        Log.d("SIZE", "b4 resize: " + s1);

        Bitmap compressed = diskCache.getResizedBitmap(img, 1280);
        int s2 = compressed.getByteCount();
        Log.d("SIZE", "after resize: " + s2);

        diskCache.compress(id, compressed, 40);

        memoryCache.set(id, diskCache.get(id));

        return id;
    }

    public static boolean delete(String id) {
        memoryCache.delete(id);
        return diskCache.delete(id);
    }

    public static void set(String id, Bitmap img) {
        memoryCache.set(id, img);
        diskCache.compress(id, img, 40);
    }

    public static Bitmap get(String id) {
        Bitmap img = memoryCache.get(id);
        if (img != null) {
            return img;
        }
        img = diskCache.get(id);
        if (img == null) {
            return null;
        }
        memoryCache.set(id, img);
        return img;
    }

//
//    public static boolean has(String id) {
//        return storage.containsKey(id);
//    }
//

    public static void inject(ImageView view, String id) {
//        Bitmap img = ImageStorage.get(id);
//        if (img != null) {
//            view.setImageBitmap(img);
//            return;
//        }
//
//        final String imId = id;
//        final ImageView imgPicture = view;
//        Services.images.download(imId).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (!response.isSuccessful()) {
//                    // maybe #todo
//                    Log.d("DOWNLOAD", "unsuccessful");
//
//                    return;
//                }
//                ResponseBody body = response.body();
//                if (body == null) {
//                    // maybe #todo
//                    Log.d("DOWNLOAD", "body null");
//                    return;
//                }
//                Bitmap bitmap = BitmapFactory.decodeStream(body.byteStream());
//                int s = bitmap.getAllocationByteCount();
//                Log.d("SIZE", "download: " + s);
//                Toast.makeText(imgPicture.getContext().getApplicationContext(), "s:" + s, Toast.LENGTH_SHORT).show();
//                ImageStorage.set(imId, bitmap);
//                Log.d("GOT", "loaded");
//                imgPicture.setImageBitmap(bitmap);
//                Log.d("DOWNLOAD", "ok");
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.d("DOWNLOAD", "fail");
//            }
//        });

        Bitmap img = memoryCache.get(id);
        if (img != null) {
            view.setImageBitmap(img);
            return;
        }
        new InjectTask(view, id);
    }


    public static class InjectTask extends AsyncTask<Void, Bitmap, Void> {

        private String imId;
        private WeakReference<ImageView> imView;

        public InjectTask(ImageView view, String id) {
            imView = new WeakReference<>(view);
            imId = id;
            execute();
        }

        @Override
        public Void doInBackground(Void... p) {
            Bitmap img = diskCache.get(imId);
            if (img != null) {
                memoryCache.set(imId, img);
                publishProgress(img);
                return null;
            }

            Services.images.download(imId).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (!response.isSuccessful()) return;

                    ResponseBody body = response.body();
                    if (body == null) return;

                    Bitmap bitmap = BitmapFactory.decodeStream(body.byteStream());
                    ImageStorage.set(imId, bitmap);
                    publishProgress(bitmap);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("DOWNLOAD", "fail");
                }
            });

            return null;
        }

        @Override
        public void onProgressUpdate(Bitmap... img) {
            ImageView view = imView.get();
            if (view == null || view.getContext() == null) return;
            view.setImageBitmap(img[0]);
        }

    }
}

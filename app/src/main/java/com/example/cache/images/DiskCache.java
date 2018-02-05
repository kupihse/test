package com.example.cache.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Andreyko0 on 04/02/2018.
 */

public class DiskCache {
    private File cacheDir;
    private String IMG_DIR = "image_cache/";



    public File getFile(String id) {
        return new File(cacheDir, id + ".jpg");
    }

    public boolean init(Context context) {
        cacheDir = context.getExternalCacheDir();
        if (cacheDir == null) {
            return false;
        }
        Log.d("FPATH:", cacheDir.getAbsolutePath());
        cacheDir = new File(cacheDir, IMG_DIR);
        if (!cacheDir.exists()) {
            return cacheDir.mkdir();
        }
        return true;
    }

    public boolean set(String id, Bitmap bmp) {
        return this.set(id, bmp, 100);
    }

    // здесь сжимаем фотку
    public boolean set(String id, Bitmap bmp, int quality) {
        try {
            File f = getFile(id);
            f.createNewFile();
            Log.d("WRITE TO FILE", f.getAbsolutePath());

            FileOutputStream stream = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, stream);
            stream.flush();
            stream.close();

            Log.d("WRITE TO FILE", "finished");

            return true;
        } catch (IOException e) {
            Log.d("FILE EXC", e.getMessage());
            return false;
        }
    }

    public Bitmap get(String id) {

        Log.d("GET FILE", "started");

        File f = getFile(id);
        if (!f.exists()) {

            Log.d("DISK:", "no file");

            return null;
        }
        try {
            FileInputStream stream = new FileInputStream(f);
            return BitmapFactory.decodeStream(stream);
        } catch (IOException e) {
            return null;
        }
    }

    public boolean has(String id) {
        return getFile(id).exists();
    }

    public boolean delete(String id) {
        return getFile(id).delete();
    }

}

package com.example.storages;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Andreyko0 on 02/02/2018.
 */

public class DiskLruCache {
    private static File cacheDir;
    private static String IMG_DIR = "/image_cache";

    public static void init(File dir) {
        Log.d("FPATH:", dir.getAbsolutePath());
        cacheDir = dir;
    }

    public static void setFile(String id, Bitmap bmp) {
        try {

            File f = new File(cacheDir, id + ".jpg");
            FileOutputStream stream = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            Log.d("FILE EXC", e.getMessage());
        }
    }

}

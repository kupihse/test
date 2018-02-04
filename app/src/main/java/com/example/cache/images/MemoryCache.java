package com.example.cache.images;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by Andreyko0 on 04/02/2018.
 */

public class MemoryCache {

    private LruCache<String, Bitmap> lruCache;

    public void init() {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        lruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public Bitmap get(String id) {
        return lruCache.get(id);
    }

    public void set(String id, Bitmap img) {
        lruCache.put(id, img);
    }

    public void delete(String id) {
        lruCache.remove(id);
    }

    public boolean has(String id) {
        return lruCache.get(id) != null;
    }

}

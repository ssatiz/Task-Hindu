package com.example.webservice.utilities;

import android.graphics.Bitmap;
 
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;
/**
 *
 * @author  AppDevelopment Inc
 * @version 1.0
 * @since Dec 5, 2014
 * @created by Satheeshkumar
 * @modified Dec 5, 2014
 * @modified by Satheeshkumar
 *
 * Copyright (c) 
 */
 
public class LruBitmapCache extends LruCache<String, Bitmap> implements
        ImageCache {
    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
 
        return cacheSize;
    }
 
    public LruBitmapCache() {
        this(getDefaultLruCacheSize());
    }
 
    public LruBitmapCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }
 
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }
 
    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }
 
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}

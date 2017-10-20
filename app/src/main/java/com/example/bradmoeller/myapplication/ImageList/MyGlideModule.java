package com.example.bradmoeller.myapplication.ImageList;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by bradmoeller on 9/20/17.
 */

@GlideModule
public class MyGlideModule extends AppGlideModule {

    // How many MB to use for the image cache on disk
    private final int DISK_CACHE_MB = 250;

    // How many screens worth of memory to use for the image cache in ram
    private final int RAM_CACHE_SCREENS = 2;

    // How many screens worth of memory to use for the shared bitmap pool in ram
    private final int BITMAP_POOL_SCREENS = 2;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);

        builder.setDefaultRequestOptions(
                new RequestOptions()
                        .format(DecodeFormat.PREFER_RGB_565)
                        .disallowHardwareConfig());

        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, 1024 * 1024 * DISK_CACHE_MB ));

        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(RAM_CACHE_SCREENS)
                .setBitmapPoolScreens(BITMAP_POOL_SCREENS)
                .build();
        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));
    }
}
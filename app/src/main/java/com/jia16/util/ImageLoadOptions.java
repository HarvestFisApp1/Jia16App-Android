package com.jia16.util;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 加载图片的options的工具类
 */
public interface ImageLoadOptions {

    //设置圆角的options
    DisplayImageOptions rounded_options = new DisplayImageOptions.Builder()
//            .showImageOnLoading(R.drawable.ic_default)     //加载中显示的图片
//            .showImageOnFail(R.drawable.ic_default)       //加载失败显示的图片
//            .showImageForEmptyUri(R.drawable.ic_drawer)  //加载的URL为空显示的图片
            .cacheInMemory(false)                // 是否在内存缓存
            .cacheOnDisk(true)                 //是否在硬盘缓存
            .imageScaleType(ImageScaleType.EXACTLY) //内部会对图片进一步压缩
            .bitmapConfig(Bitmap.Config.RGB_565)    //使用比较 节省内存的颜色模式
            .considerExifParams(true)   //识别图片的方向信息
            // .displayer(new FadeInBitmapDisplayer(500)).build()    //设置渐渐显示的效果
            .displayer(new RoundedBitmapDisplayer(20)).build();     //设置圆角显示


    //设置渐渐电话的options
    DisplayImageOptions fadeIn_options = new DisplayImageOptions.Builder()
//            .showImageOnLoading(R.drawable.ic_default)     //加载中显示的图片
//            .showImageOnFail(R.drawable.ic_default)       //加载失败显示的图片
//            .showImageForEmptyUri(R.drawable.ic_drawer)  //加载的URL为空显示的图片
            .cacheInMemory(false)                // 是否在内存缓存
            .cacheOnDisk(true)                 //是否在硬盘缓存
            .imageScaleType(ImageScaleType.EXACTLY) //内部会对图片进一步压缩
            .bitmapConfig(Bitmap.Config.RGB_565)    //使用比较 节省内存的颜色模式
            .considerExifParams(true)   //识别图片的方向信息
            //.displayer(new RoundedBitmapDisplayer(20)).build()    //设置圆角显示
            .displayer(new FadeInBitmapDisplayer(500)).build();     //设置渐渐显示的效果

}

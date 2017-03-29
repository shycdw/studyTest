package com.david.study.app;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
/*import android.support.multidex.MultiDex;*/
import android.text.TextUtils;

import com.david.study.R;
import com.david.study.util.SPUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * 自定义Application，用于一些初始化工作，保存变量（官方推荐用这个方法）,这里我还将值保存到了SP中。
 * 举个栗子：页面启动A->B->C,此时若是切出去玩游戏，打电话之类的，假设当内存资源不足了，那么应用就会
 * 被系统回收，此时若是重新切回应用，Android会在新的进程里重新创建C，如果关闭C，则会重新创建B，
 * 以此类推，这样的话，假如在A中进行全局变量的赋值，那么到了后面就不会有
 *
 * @author DavidChen
 *         Created by DavidChen on 2017/2/27.
 */

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    private static Context sContext;

    /**
     * 城市名
     */
    private String mCity = null;

    /**
     * 省名
     */
    private String mProvince = null;

    /**
     * 城市编码
     */
    private String mCityCode = null;

    /**
     * 经度
     */
    private double mLongitude;

    /**
     * 地址
     */
    private String mAddress;

    /**
     * 纬度
     */
    private double mLatitude;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();

        // 初始化ImageLoader
        initImageLoader();
    }

    /**
     * 初始化Universal-Image-Loader
     */
    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.default_image)
                .showImageOnFail(R.mipmap.default_image)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                // ARGB_8888:原图，RGB_565:压缩后的模糊图
                .build();
        ImageLoaderConfiguration config =
                new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(defaultOptions)
                        .diskCacheFileCount(100)
                        // 缓存一百张图片
                        /*.writeDebugLogs()*/   // 不写debug日志
                        .build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        /*MultiDex.install(this);*/
    }

    public static Context getContext() {
        return sContext;
    }

    /**
     * 保存当前城市名，会保存到sp中
     * @param city 城市名
     */
    public void setCity(String city) {
        mCity = city;
        SPUtils.put(sContext, Config.KEY_CITY, city);
    }

    /**
     * 获取城市名
     * @return 城市名
     */
    public String getCity() {
        if (TextUtils.isEmpty(mCity)) {
            mCity = (String) SPUtils.get(sContext, Config.KEY_CITY, Config.DEFAULT_CITY);
        }
        return mCity;
    }

    /**
     * 保存当前省名，会保存到sp中
     * @param province 省名
     */
    public void setProvince(String province) {
        mProvince = province;
        SPUtils.put(sContext, Config.KEY_PROVINCE, province);
    }

    /**
     * 获取省名
     * @return 省名
     */
    public String getProvince() {
        if (TextUtils.isEmpty(mProvince)) {
            mProvince = (String) SPUtils.get(sContext, Config.KEY_PROVINCE, Config.DEFAULT_PROVINCE);
        }
        return mProvince;
    }

    public void setCityCode(String cityCode) {
        mCityCode = cityCode;
    }

    public String getCityCode() {
        if (TextUtils.isEmpty(mCityCode)) {
            mCityCode = Config.DEFAULT_CITY_CODE;
        }
        return mCityCode;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public String getAddress() {
        return mAddress;
    }

    public double getLatitude() {
        return mLatitude;
    }
}

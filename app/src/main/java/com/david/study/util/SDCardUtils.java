package com.david.study.util;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by DavidChen on 2015/12/18.
 * sd卡工具类
 */
public class SDCardUtils {

    private SDCardUtils() {
        throw new UnsupportedOperationException("can not be instantiate");
    }

    /**
     * 判断SD卡是否可用
     * @return
     */
    public static boolean isSDCardEnable()
    {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取sd卡路径
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     * @return
     */
    public static long getSDCardAllSize() {
        if (isSDCardEnable())
        {
            StatFs statFs = new StatFs(getSDCardPath());
            //获取空闲的数据块的数量
            long availableBlocks = (long) statFs.getAvailableBlocks() - 4;
            //获取单个数据块的大小（byte）
            long freeBlocks = statFs.getBlockSize();
            return freeBlocks * availableBlocks;
        }

        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     * @param filePath
     * @return
     */
    public static long getFreeBytes(String filePath)
    {
        //如果是sd卡下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath()))
        {
            filePath = getSDCardPath();
        } else {
            //如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs statFs = new StatFs(filePath);
        long availableBlocks = (long) statFs.getAvailableBlocks() - 4;
        return statFs.getBlockSize() * availableBlocks;
    }

    /**
     * 获取系统存储路径
     * @return
     */
    public static String getRootDirectoryPath()
    {
        return Environment.getRootDirectory().getAbsolutePath();
    }

}

package com.iplay.feastbooking.assistance;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by gu_y-pc on 2018/1/22.
 */

public class DataCleanManager {

    public static boolean clearInternalCache(Context context) {
        return deleteFolderFile(context.getCacheDir(), true);
    }

    public static boolean cleanExternalCache(Context context) {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED) ? deleteFilesByDirectory(context.getExternalCacheDir()) : false;
    }

    public static boolean clearApplicationCache(Context context) {
        return clearInternalCache(context) && cleanExternalCache(context);
    }

    public static long getCacheSize(Context context) {
        long cacheSize, externalCacheSize = 0, internalCacheSize = 0;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {
                externalCacheSize = getFolderSize(context.getExternalCacheDir());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            internalCacheSize = getFolderSize(context.getCacheDir());
        } catch (Exception e) {
            e.printStackTrace();
        }
        cacheSize = externalCacheSize + internalCacheSize;
        return cacheSize;
    }

    public static String getFormattedCacheSize(Context context) {
        return getFormatSize(getCacheSize(context));
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    // 获取文件
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static boolean deleteFolderFile(File file, boolean deleteThisPath) {
        boolean result = true;
        if (file != null) {
            try {
                if (file.isDirectory()) {// 如果下面还有文件
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        result &= deleteFolderFile(files[i], true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        return file.delete();
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            return file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }else {
            result = false;
        }
        return result;
    }

    private static boolean deleteFilesByDirectory(File directory) {
        boolean isSuccess = true;
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                isSuccess &= item.delete();
            }
        }
        return isSuccess;
    }

}

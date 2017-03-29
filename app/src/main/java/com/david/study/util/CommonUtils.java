package com.david.study.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * 其他通用工具类
 * Created by DavidChen on 2016/2/29.
 */
public class CommonUtils {

    private static final String TAG = "CommonUtils";

    /**
     * 此为调用系统自带剪辑工具
     * @param uri
     * @param outputX
     * @param outputY
     * @param requestCode
     */
    private void cropImageUri(Activity activity, Uri uri, int outputX, int outputY, int requestCode){

        Intent intent = new Intent("com.android.camera.action.CROP");

        intent.setDataAndType(uri, "image/*");

        intent.putExtra("crop", "true");

        intent.putExtra("aspectX", 1);

        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", outputX);

        intent.putExtra("outputY", outputY);

        intent.putExtra("scale", true);

        L.i(TAG, uri.getPath());
        /*intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);//此方法在截图之后会覆盖原图（前提是你传入的uri和参数一样）*/

        intent.putExtra("return-data", true);

        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        intent.putExtra("noFaceDetection", true); // no face detection

        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 50*1000);

        activity.startActivityForResult(intent, requestCode);

    }

    /**
     * 判断字符串是否属于正常值
     * @param str
     * @return
     */
    public static boolean isNormal(Object str){
        return! (str == null || "null".equals(str) || "NULL".equals(str)
                || "".equals(str) || "undefined".equals(str));
    }

    /**
     * 获取人民币符号。注：用中文输入法打出的“￥”是一横，而价格上应该是双横
     * @return 双杠的“￥”
     */
    public static String getRMBSymbol() {
        char symbol=165;
        return String.valueOf(symbol);
    }

    public static String hideMobileMiddle4(String mobile) {
        return mobile.substring(0, mobile.length() - 9) + "*****"
                + mobile.substring(mobile.length() - 4, mobile.length());
    }
}

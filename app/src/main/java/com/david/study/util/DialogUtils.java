package com.david.study.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * dialog相关
 * Created by DavidChen on 2015/11/4.
 */
public class DialogUtils {

    /**
     * 显示一个dialog
     * @param ctx   context
     * @param msg   the message to show
     * @param goHome if true ，show positive button
     */
    public static void showDialog(final Context ctx, String msg, boolean goHome)
    {
        //创建一个AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx)
                .setMessage(msg)
                .setCancelable(false);
        if (goHome)
        {
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent();
                }
            });
        }
    }
}

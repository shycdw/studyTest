package com.david.study.util;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by DavidChen on 2015/12/18.
 * 打开或关闭软键盘
 */
public class KeyBoardUtils {

    /**
     * 打开软键盘
     * @param editText  输入框
     * @param context   上下文
     */
    public static void openKeyBoard(EditText editText, Context context)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     * @param editText  输入框
     * @param context   上下文
     */
    public static void closeKeyBoard(EditText editText, Context context)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

}

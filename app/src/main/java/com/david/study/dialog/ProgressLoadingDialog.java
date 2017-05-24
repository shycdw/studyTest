package com.david.study.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.david.study.R;

/**
 * 加载进度dialog，无限循环。
 * 这里一定要用Builder来构造，第一，构造方法已经private，第二，Builder中指定了其theme
 * 之前很多次做ProgressDialog，经常会有白色背景出现无法去掉，其实是因为用了本地构造器，而在Builder中指定了无背景色的theme，
 * 而且，默认的theme,一般是粗线条的,不太美观。
 * Created by DavidChen on 2017/5/24.
 */

public class ProgressLoadingDialog extends AlertDialog {

    private ProgressLoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_loading_progress, null);
        setContentView(view);
    }

    public static class Builder {
        private Context mContext;

        public Builder(Context context) {
            this.mContext = context;
        }

        public ProgressLoadingDialog build() {
            return new ProgressLoadingDialog(mContext, R.style.AppTheme_Dialog_NoTitleAndBackGround);
        }
    }
}

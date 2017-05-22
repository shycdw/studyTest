package com.david.study.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * ViewHolder
 * Created by DavidChen on 2017/4/19.
 */

public class ViewHolder {

    private SparseArray<View> mViews;
    private View mConvertView;

    private ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mViews = new SparseArray<>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    /**
     * 获取ViewHolder对象，如果convertView中已经存在则使用convertView中的tag，不存在则新建
     */
    public static ViewHolder get(Context context, View convertView, ViewGroup parent,
                                 int layoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        }
        return (ViewHolder) convertView.getTag();
    }

    /**
     * 获取需要的View，如果已经存在引用则直接获取，如果不存在则重新加载并保存
     * @param viewId id
     * @param <T> 需要的具体view类型
     * @return 需要的View
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }
}

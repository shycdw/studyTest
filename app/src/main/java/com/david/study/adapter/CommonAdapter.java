package com.david.study.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * adapter基类,参考了几个大牛的封装
 * Created by DavidChen on 2017/4/19.
 */

public abstract class CommonAdapter<T> extends BaseAdapter {

    private Context mContext;

    private List<T> mData;

    private int mLayoutId;

    private CommonAdapter(Context context, List<T> data, int layoutId) {
        this.mContext = context;
        this.mData = data;
        this.mLayoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = getViewHolder(position, convertView, parent);
        bindData(viewHolder, getItem(position));
        return viewHolder.getConvertView();
    }

    /**
     * 绑定数据到view上
     * @param viewHolder ViewHolder
     * @param bean 数据
     */
    abstract void bindData(ViewHolder viewHolder, T bean);

    private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mLayoutId, position);
    }
}

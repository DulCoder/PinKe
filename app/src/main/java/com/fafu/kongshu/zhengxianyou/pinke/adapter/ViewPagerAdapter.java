package com.fafu.kongshu.zhengxianyou.pinke.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by zhengxianyou on 2016/10/29.
 * 创建viewpager适配器，设置相关属性
 */

public class ViewPagerAdapter extends PagerAdapter {
    private List<View> views;
    private Context mContext;

    public ViewPagerAdapter(List<View> views, Context mContext) {
        this.views = views;
        this.mContext = mContext;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

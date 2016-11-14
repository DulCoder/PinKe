package com.fafu.kongshu.zhengxianyou.pinke;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.fafu.kongshu.zhengxianyou.pinke.adapter.ViewPagerAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * 引导页面，包含三个view和引导的小点
 */
public class GuideActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private List<View> mViews;
    private ViewPagerAdapter mAdapter;

    //定义ImageView
    private ImageView[]dots;
    private int []ids = {R.id.iv1,R.id.iv2,R.id.iv3};

    private Button btn_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        init();
        initDots();
    }

    //初始化view
    private void init() {

        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        LayoutInflater inflater = LayoutInflater.from(this) ;
        mViews = new ArrayList<>();
        mViews.add(inflater.inflate(R.layout.view1,null));
        mViews.add(inflater.inflate(R.layout.view2,null));
        mViews.add(inflater.inflate(R.layout.view3,null));

        //进入主界面
        btn_in = (Button) mViews.get(2).findViewById(R.id.btn_in);
        btn_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                finish();            //跳转后销毁Activity
            }
        });

        mAdapter = new ViewPagerAdapter(mViews,this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);
    }

    //初始化ImageView
    private void initDots(){
        dots = new ImageView[mViews.size()];

        for (int i = 0;i < mViews.size();i++){
            dots[i] = (ImageView) findViewById(ids[i]);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    //在页面被选中时设置导航点为高亮，未被选中的点为暗灰色
    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < ids.length; i++) {

            if (position == i){
                dots[i].setImageResource(R.drawable.login_point_selected);
            }else {
                dots[i].setImageResource(R.drawable.login_point);

            }
        }
    }
    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
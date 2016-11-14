package com.fafu.kongshu.zhengxianyou.pinke.fragment;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fafu.kongshu.zhengxianyou.pinke.DisplayActivity;
import com.fafu.kongshu.zhengxianyou.pinke.R;
import com.fafu.kongshu.zhengxianyou.pinke.adapter.DisplayAdapter;
import com.fafu.kongshu.zhengxianyou.pinke.bean.Note;
import com.fafu.kongshu.zhengxianyou.pinke.config.Config;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * Created by zhengxianyou on 2016/10/30.
 * 首界面显示的信息
 */

public class DisplayFragment extends Fragment implements View.OnClickListener {
    private DisplayActivity displayActivity;

    private ListView mListView;
    private List<Note> notes = new ArrayList<>();
    private DisplayAdapter adapter;

    private View mView;

    private RelativeLayout layout_action;
    private LinearLayout layout_all;
    private TextView tv_car;
    private Button btn_add;

    private Button btn_search_all;
    private Button btn_fare;
    private Button btn_car;
    private PopupWindow morePop;

    private int mScreenWidth;
    private int mScreenHeight;
    private boolean flag = false;

    /**
     * 返回创建fragment实例
     */
    public static DisplayFragment newInstance() {

        DisplayFragment displayFragment = new DisplayFragment();
        return displayFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        displayActivity = (DisplayActivity) context;   //获取上下文对象
        Log.e("test", "onAttach");


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("test", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        mView = inflater.inflate(R.layout.fragment_display, container, false);

        adapter = new DisplayAdapter(displayActivity, notes);

        displayActivity.setHandler(2);                //设置导航栏文字颜色

        //获取当前屏幕宽高
        DisplayMetrics metric = new DisplayMetrics();
        displayActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;

        initView();
        Log.e("test", "createView");

        loadData();

        return mView;

    }


    /**
     * 初始化view
     */
    private void initView() {
        mListView = (ListView) mView.findViewById(R.id.display_listView);
        mListView.setClickable(true);

//        mListView的滑动监听
//        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                switch (scrollState) {
//                    // 当不滚动时
//                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
//                        // 判断滚动到底部
//                        if (mListView.getLastVisiblePosition() == (mListView.getCount() - 1)) {
//
//                        }
//                        // 判断滚动到顶部
//
//                        if(mListView.getFirstVisiblePosition() == 0){
//                        }
//
//                        break;
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (firstVisibleItem + visibleItemCount == totalItemCount && !flag) {
//                    flag = true;
//                } else
//                    flag = false;
//            }
//        });

        layout_action = (RelativeLayout) mView.findViewById(R.id.layout_action);
        layout_all = (LinearLayout) mView.findViewById(R.id.layout_all);
        // 默认是加载全部信息
        tv_car = (TextView) mView.findViewById(R.id.tv_car);
        btn_add = (Button) mView.findViewById(R.id.btn_add);

        btn_add.setOnClickListener(this);
        layout_all.setOnClickListener(this);
    }

    /**
     * 加载数据
     */
    private void loadData() {
        BmobQuery<Note> query = new BmobQuery<>();
        query.setLimit(300);
        query.order("-updatedAt");

        query.findObjects(new FindListener<Note>() {

            @Override
            public void done(List<Note> list, BmobException e) {

                notes = list;
                Config.setList(notes);
                adapter = new DisplayAdapter(displayActivity, notes);
                mListView.setAdapter(adapter);
            }

        });


    }

    /**
     * 点击事件响应逻辑
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_all:                  //显示下拉框，选择查询条件
                showListPop();
                break;

            case R.id.btn_add:                     //跳转到添加界面
                inflateAddFragment();
                break;

            case R.id.btn_fare:                    //筛选已有车需要找拼友的信息
                tv_car.setText("Search Fare");
                morePop.dismiss();
                queryFare();
                break;

            case R.id.btn_car:
                tv_car.setText("Search Car");      //筛选需要搭车的信息
                morePop.dismiss();
                queryCar();
                break;

            case R.id.btn_search_all:              //加载所有的的信息
                tv_car.setText("Search All");
                notes.clear();
                loadData();
                morePop.dismiss();
                break;

        }
    }

    /**
     * 筛选需要搭车的信息
     */
    private void queryCar() {
        notes.clear();
        BmobQuery<Note> query = new BmobQuery<>();
        query.setLimit(150);
        query.addWhereEqualTo("title", "Search Car");
        query.order("-updatedAt");

        query.findObjects(new FindListener<Note>() {
            @Override
            public void done(List<Note> list, BmobException e) {
                notes = list;
                adapter = new DisplayAdapter(displayActivity, notes);
                mListView.setAdapter(adapter);
            }
        });
    }

    /**
     * 筛选已有车需要找拼友的信息
     */
    private void queryFare() {
        notes.clear();
        BmobQuery<Note> query = new BmobQuery<>();
        query.setLimit(150);
        query.addWhereEqualTo("title", "Search Fare");
        query.order("-updatedAt");

        query.findObjects(new FindListener<Note>() {
            @Override
            public void done(List<Note> list, BmobException e) {
                notes = list;
                adapter = new DisplayAdapter(displayActivity, notes);
                mListView.setAdapter(adapter);
            }
        });
    }

    /**
     * 跳转到添加信息界面
     */
    private void inflateAddFragment() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, AddFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    /**
     * 展开下拉列表
     */
    private void showListPop() {
        View view = LayoutInflater.from(displayActivity).inflate(R.layout.pop_search, null);
        // 注入
        btn_search_all = (Button) view.findViewById(R.id.btn_search_all);
        btn_fare = (Button) view.findViewById(R.id.btn_fare);
        btn_car = (Button) view.findViewById(R.id.btn_car);
        btn_search_all.setOnClickListener(this);
        btn_fare.setOnClickListener(this);
        btn_car.setOnClickListener(this);
        morePop = new PopupWindow(view, mScreenWidth, 600);

        morePop.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    morePop.dismiss();
                    return true;
                }
                return false;
            }
        });

        morePop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        morePop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        morePop.setTouchable(true);
        morePop.setFocusable(true);
        morePop.setOutsideTouchable(true);
        morePop.setBackgroundDrawable(new BitmapDrawable());
        // 动画效果 从顶部弹下
        morePop.setAnimationStyle(R.style.MenuPop);
        float scale = displayActivity.getResources().getDisplayMetrics().density;
        int dip2px = (int) (scale * 2.0F + 0.5f);
        morePop.showAsDropDown(layout_action, 0, dip2px);
    }

    /**
     * 生命周期
     */
    @Override
    public void onResume() {
        super.onResume();
        Config.setIsDisplayFragmentAlive(true);       //设置DisplayFragment状态为激活

        Log.e("test", "resume");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("test", "pause");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.e("test", "onDestroyView");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Config.setIsDisplayFragmentAlive(false);

        Log.e("test", "destroy");
    }

}

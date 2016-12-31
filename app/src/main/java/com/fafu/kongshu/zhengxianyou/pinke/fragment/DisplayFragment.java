package com.fafu.kongshu.zhengxianyou.pinke.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fafu.kongshu.zhengxianyou.pinke.AddActivity;
import com.fafu.kongshu.zhengxianyou.pinke.DisplayActivity;
import com.fafu.kongshu.zhengxianyou.pinke.R;
import com.fafu.kongshu.zhengxianyou.pinke.adapter.DatabaseAdapter;
import com.fafu.kongshu.zhengxianyou.pinke.adapter.DisplayAdapter;
import com.fafu.kongshu.zhengxianyou.pinke.bean.Note;
import com.fafu.kongshu.zhengxianyou.pinke.config.Config;
import com.fafu.kongshu.zhengxianyou.pinke.sqlitedb.NoteMeteData;
import com.fafu.kongshu.zhengxianyou.pinke.view.DragLayout;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by zhengxianyou on 2016/12/31.
 */

public class DisplayFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View view;
    private DragLayout dl;
    private ListView lv;
    private ImageView iv_icon, iv_bottom;

    private DisplayActivity displayActivity;

    private ListView mListView;
    private List<Note> notes = new ArrayList<>();
    private DisplayAdapter adapter;

    private RelativeLayout layout_action;
    private LinearLayout layout_all;
    private TextView tv_car;
    private TextView my_id;
    private Button btn_add;

    private Button btn_search_all;
    private Button btn_fare;
    private Button btn_car;
    private PopupWindow morePop;

    private int mScreenWidth;
    private static final int REFRESH_COMPLETE = 0X110;
    private static final int CHOOSE_ICON = 0X111;
    private static final int DISPLAY_DATA = 0X112;
    private SwipeRefreshLayout mSwipeLayout;
    private int mScreenHeight;
    private DatabaseAdapter mDatabaseAdapter;



    public static DisplayFragment newInstance() {

        DisplayFragment fragment = new DisplayFragment();

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        displayActivity = (DisplayActivity) context;   //获取上下文对象

        Log.e("test", "onAttach");


    }

    /**
     * 异步刷新页面
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    loadData();
                    mSwipeLayout.setRefreshing(false);
                    break;
                case CHOOSE_ICON:
                    ChooseIcon();            //更新头像

                    break;
                case DISPLAY_DATA:

                    break;

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_display,container,false);

        mDatabaseAdapter = new DatabaseAdapter(displayActivity);

        adapter = new DisplayAdapter(displayActivity, notes);

        displayActivity.setHandler(2);                //设置导航栏文字颜色

        //获取当前屏幕宽高
        DisplayMetrics metric = new DisplayMetrics();
        displayActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;

        initDragLayout();
        initView();

        Log.e("test", "createView");
        if (Config.isRefresh()) {
            new Thread(runnable).start();
        } else {
            nativeData();
        }

        return view;
    }

    private void initDragLayout() {
        dl = (DragLayout) view.findViewById(R.id.dl);
        dl.setDragListener(new DragLayout.DragListener() {
            @Override
            public void onOpen() {
//                lv.smoothScrollToPosition(new Random().nextInt(30));
            }

            @Override
            public void onClose() {
                shake();
            }

            @Override
            public void onDrag(float percent) {
                ViewHelper.setAlpha(iv_icon, 1 - percent);
            }
        });
    }

    /**
     * 初始化View
     */
    private void initView() {
        iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        iv_bottom = (ImageView) view.findViewById(R.id.iv_bottom);
        lv = (ListView) view.findViewById(R.id.lv);
        lv.setAdapter(new ArrayAdapter<>(getActivity(),
                R.layout.item_text, new String[] {"更换头像","更改昵称","修改密码"}));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                Toast.makeText(getActivity(), "此功能待加入...",Toast.LENGTH_SHORT).show();
            }
        });

        handler.sendEmptyMessage(CHOOSE_ICON);      //更新头像

        my_id = (TextView) view.findViewById(R.id.my_id);
        my_id.setText(Config.getNickName());

        mListView = (ListView) view.findViewById(R.id.display_listView);
        mListView.setClickable(true);

        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.id_swipe_ly);

        mSwipeLayout.setOnRefreshListener(this);       //下拉刷新
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        layout_action = (RelativeLayout) view.findViewById(R.id.layout_action);
        layout_all = (LinearLayout) view.findViewById(R.id.layout_all);
        // 默认是加载全部信息
        tv_car = (TextView) view.findViewById(R.id.tv_car);
        btn_add = (Button) view.findViewById(R.id.btn_add);

        btn_add.setOnClickListener(this);
        layout_all.setOnClickListener(this);

    }



    /**
     * 选择头像
     */
    private void ChooseIcon() {

        String icon = Config.getMyIcon();
        switch (icon) {

            case "icon1":
                iv_icon.setImageResource(R.drawable.icon1);
                iv_bottom.setImageResource(R.drawable.icon1);

                break;

            case "icon2":
                iv_icon.setImageResource(R.drawable.icon2);
                iv_bottom.setImageResource(R.drawable.icon2);

                break;

            case "icon3":
                iv_icon.setImageResource(R.drawable.icon3);
                iv_bottom.setImageResource(R.drawable.icon3);

                break;

            case "icon4":
                iv_icon.setImageResource(R.drawable.icon4);
                iv_bottom.setImageResource(R.drawable.icon4);

                break;

            case "icon5":
                iv_icon.setImageResource(R.drawable.icon5);
                iv_bottom.setImageResource(R.drawable.icon5);

                break;

            case "icon6":
                iv_icon.setImageResource(R.drawable.icon6);
                iv_bottom.setImageResource(R.drawable.icon6);

                break;

            case "icon7":
                iv_icon.setImageResource(R.drawable.icon7);
                iv_bottom.setImageResource(R.drawable.icon7);

                break;

            case "icon8":
                iv_icon.setImageResource(R.drawable.icon8);
                iv_bottom.setImageResource(R.drawable.icon8);

                break;

            case "icon9":
                iv_icon.setImageResource(R.drawable.icon9);
                iv_bottom.setImageResource(R.drawable.icon9);

                break;

            default:
                break;
        }
    }

    /**
     * 返回时头像左右摇动
     */
    private void shake() {
        iv_icon.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake));
    }

    /**
     * 网络加载数据
     */
    private void loadData() {
        try {
            BmobQuery<Note> query = new BmobQuery<>();
            query.setLimit(300);
            query.order("-updatedAt");

            query.findObjects(new FindListener<Note>() {

                @Override
                public void done(List<Note> list, BmobException e) {
                    notes.clear();
                    notes = list;
                    Config.setList(notes);     //添加地图界面的marker


                    adapter = new DisplayAdapter(displayActivity, notes);
                    mListView.setAdapter(adapter);

                    //加载前先清空表中数据
                    mDatabaseAdapter.rawDelete(NoteMeteData.DisplayNoteTable.TABLE_NAME);
                    //把数据添加到本地数据库
                    int size = notes.size();
                    for (int i = 0; i < size; i++) {
                        mDatabaseAdapter.rawAdd(notes.get(i), NoteMeteData.DisplayNoteTable.TABLE_NAME);
                    }

                }

            });
            Log.e("test", "loadData");
        } catch (Exception e) {
            nativeData();
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
           loadData();

        }
    };

    /**
     * 本地加载数据
     */
    private void nativeData() {

        notes.clear();
        ArrayList<Note> list = mDatabaseAdapter.rawQueryAll(NoteMeteData.DisplayNoteTable.TABLE_NAME);
        notes.addAll(list);

        adapter = new DisplayAdapter(displayActivity, notes);
        mListView.setAdapter(adapter);

        Log.e("test", "nativeData");
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
                inflateAddActivity();
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
    private void inflateAddActivity() {

        startActivity(new Intent(displayActivity, AddActivity.class));
    }

    /**
     * 展开下拉列表
     */
    private void showListPop() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_search, null);
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

        view.setOnTouchListener(new View.OnTouchListener()// 需要设置，点击之后取消popupview，即使点击外面，也可以捕获事件
        {
            public boolean onTouch(View v, MotionEvent event)
            {
                if (morePop.isShowing())
                {
                    morePop.dismiss();
                }
                return false;
            }
        });

        morePop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        morePop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        morePop.setTouchable(true);
        morePop.setFocusable(true);
        morePop.setOutsideTouchable(true);
//        morePop.setBackgroundDrawable(new BitmapDrawable());
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
        Config.setIsRefresh(false);

        Log.e("test", "onDestroyView");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Config.setIsDisplayFragmentAlive(false);

        Log.e("test", "destroy");
    }


    //下拉刷新
    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
    }

}

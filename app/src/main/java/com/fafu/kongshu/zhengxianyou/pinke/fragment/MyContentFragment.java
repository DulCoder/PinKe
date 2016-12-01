package com.fafu.kongshu.zhengxianyou.pinke.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fafu.kongshu.zhengxianyou.pinke.DisplayActivity;
import com.fafu.kongshu.zhengxianyou.pinke.PinKeApplication;
import com.fafu.kongshu.zhengxianyou.pinke.R;
import com.fafu.kongshu.zhengxianyou.pinke.adapter.DatabaseAdapter;
import com.fafu.kongshu.zhengxianyou.pinke.adapter.NoteAdapter;
import com.fafu.kongshu.zhengxianyou.pinke.bean.MyUser;
import com.fafu.kongshu.zhengxianyou.pinke.bean.Note;
import com.fafu.kongshu.zhengxianyou.pinke.config.Config;
import com.fafu.kongshu.zhengxianyou.pinke.sqlitedb.NoteMeteData;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.fafu.kongshu.zhengxianyou.pinke.PinKeApplication.editor;

/**
 * Created by zhengxianyou on 2016/10/30.
 */

public class MyContentFragment extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private DisplayActivity displayActivity;
    private View mView;

    private ListView mListView;
    private List<Note> notes = new ArrayList<>();
    private NoteAdapter adapter;
    private DatabaseAdapter mDatabaseAdapter;

    private static final int DEL_ITEM = 0x1;
    private boolean isNeedRefresh = false;             //是否需要刷新
    private String objectId = null;
    private SwipeRefreshLayout mSwipeLayout;
    private static final int REFRESH_COMPLETE = 0X100;


    /**
     * 返回创建fragment实例
     */
    public static MyContentFragment newInstance() {
        MyContentFragment myContentFragment = new MyContentFragment();
        return myContentFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        displayActivity = (DisplayActivity) context;   //获取上下文对象
        Log.e("test", "My onAttach");
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

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_content_my, container, false);
        mDatabaseAdapter = new DatabaseAdapter(displayActivity);

        adapter = new NoteAdapter(displayActivity, notes);
        Log.e("test", "My createView");

        displayActivity.setHandler(3);

        initView();

        isNeedRefresh = PinKeApplication.sp.getBoolean("isNeedRefresh", true);
        if (isNeedRefresh) {
            loadData();
            editor.putBoolean("isNeedRefresh", false);
            editor.commit();
        } else {
            nativeData();
        }

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Config.setIsMyContentFragmentAlive(true);
        Log.e("test", "My resume");

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Config.setIsMyContentFragmentAlive(false);
        Log.e("test", "My onDestroyView");

    }

    /**
     * 初始化view
     */
    private void initView() {
        mListView = (ListView) mView.findViewById(R.id.note_listView);
        mListView.setOnItemClickListener(this);   //给ListView注册监听事件
        registerForContextMenu(mListView);        //给ListView注册上下文菜单

        mSwipeLayout = (SwipeRefreshLayout) mView.findViewById(R.id.id_swipe_ly);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

    }

    /**
     * 加载网络数据
     */
    private void loadData() {
        try {
            MyUser user = BmobUser.getCurrentUser(MyUser.class);
            BmobQuery<Note> query = new BmobQuery<>();
            query.setLimit(150);
            query.addWhereEqualTo("author", user);     //查找当前用户发布的消息
            query.order("-updatedAt");

            query.findObjects(new FindListener<Note>() {

                @Override
                public void done(List<Note> list, BmobException e) {

                    notes = list;

                    //把数据添加到本地数据库
                    int size = list.size();
                    Note note = null;
                    for (int i = 0; i < size; i++) {
                        note = list.get(i);
                        mDatabaseAdapter.rawAdd(note, NoteMeteData.MyNoteTable.TABLE_NAME);
                    }

                    nativeData();

//                    adapter = new NoteAdapter(displayActivity, notes);
//                    mListView.setAdapter(adapter);
                }
            });
        } catch (Exception e) {
            nativeData();
        }
    }

    /**
     * 本地加载数据
     */
    private void nativeData() {

        notes.clear();
        ArrayList<Note> list = mDatabaseAdapter.queryAll();
        notes.addAll(list);

        adapter = new NoteAdapter(displayActivity, notes);
        mListView.setAdapter(adapter);

        Log.e("test", "nativeData");
    }


    /**
     * 上下文菜单，长按删除item
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(1, DEL_ITEM, 100, "删除");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DEL_ITEM:

                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int position = info.position;               //得到被点击的item
                Note note = notes.get(position);
                objectId = note.getNoteId();

                note.delete(objectId, new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        mDatabaseAdapter.delete(objectId);
                        nativeData();
                        Config.setIsRefresh(true);

                    }

                });

                break;
        }
        return true;
    }

    /**
     * item点击事件
     * 跳转到编辑界面，并把item内容传递过去
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String objectId, title, start, end, description, time, phone;
        Note note = notes.get(position);
        try {

            objectId = note.getNoteId();

            //获取objectId
            title = note.getTitle();                         //获取标题关键字
            start = note.getOrigin();                        //获取起点
            end = note.getDestination();                     //获取目的地
            description = note.getContent();                 //获取item内容
            time = note.getTime();                           //获取出发时间
            phone = note.getPhoneNumber();                   //获取联系方式

            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, MyDetailFragment.newInstance(objectId, title, start, end, description, time, phone))
                    .commit();
        } catch (Exception e) {

            Log.e("TAG", e.getMessage().toString());
        }

    }


    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
    }
}

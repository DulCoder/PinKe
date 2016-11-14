package com.fafu.kongshu.zhengxianyou.pinke.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fafu.kongshu.zhengxianyou.pinke.DisplayActivity;
import com.fafu.kongshu.zhengxianyou.pinke.R;
import com.fafu.kongshu.zhengxianyou.pinke.adapter.NoteAdapter;
import com.fafu.kongshu.zhengxianyou.pinke.bean.MyUser;
import com.fafu.kongshu.zhengxianyou.pinke.bean.Note;
import com.fafu.kongshu.zhengxianyou.pinke.config.Config;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by zhengxianyou on 2016/10/30.
 */

public class MyContentFragment extends Fragment implements AdapterView.OnItemClickListener {
    private DisplayActivity displayActivity;
    private View mView;

    private ListView mListView;
    private List<Note> notes = new ArrayList<>();
    private NoteAdapter adapter;

    private static final int DEL_ITEM = 0x1;

    /**
    *返回创建fragment实例
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("test", "My onCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (Config.getMyContentFragmentView() == null) {
            mView = inflater.inflate(R.layout.fragment_content_my, container, false);

            adapter = new NoteAdapter(displayActivity, notes);
            Log.e("test", "My createView");

            displayActivity.setHandler(3);


            loadData();
            initView();
            loadData();
        }else {
            Log.e("test", "My no no no createView");

            mView = Config.getMyContentFragmentView();
            displayActivity.setHandler(3);

        }
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Config.setIsMyContentFragmentAlive(true);

        Log.e("test","My resume");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("test","My pause");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Config.setMyContentFragmentView(mView);
        Log.e("test","My destroy");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Config.setIsMyContentFragmentAlive(false);
        Config.setMyContentFragmentView(mView);
        Log.e("test","My onDestroyView");

    }

    private void initView() {
        mListView = (ListView)mView.findViewById(R.id.note_listView);
        mListView.setOnItemClickListener(this);   //给ListView注册监听事件
        registerForContextMenu(mListView);        //给ListView注册上下文菜单

    }

    /**
     * 加载数据
     */
    private void loadData() {
        MyUser user = BmobUser.getCurrentUser(MyUser.class);
        BmobQuery<Note> query = new BmobQuery<>();
        query.setLimit(150);
        query.addWhereEqualTo("author",user);     //查找当前用户发布的消息
        query.order("-updatedAt");

        query.findObjects(new FindListener<Note>() {

            @Override
            public void done(List<Note> list, BmobException e) {

                notes = list;
                adapter = new NoteAdapter(displayActivity,notes);
                mListView.setAdapter(adapter);
            }
        });
    }

    /**
     * 上下文菜单，长按删除item
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(1,DEL_ITEM,100,"删除");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case DEL_ITEM:

                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                View view = info.targetView;  //得到被点击的view
                String objectId = (String) view.getTag();//获取到被点击item的数据id

                Note note = new Note();
                note.delete(objectId,new UpdateListener(){

                    @Override
                    public void done(BmobException e) {
                        loadData();            //删除数据后更新界面
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
        try {

        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_start = (TextView) view.findViewById(R.id.tv_start);
        TextView tv_end = (TextView) view.findViewById(R.id.destination);
        TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
        TextView tv_description = (TextView) view.findViewById(R.id.tv_description);
        TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);

        String objectId = (String) view.getTag();                    //获取objectId
        String title = tv_title.getText().toString();                //获取标题关键字
        String start = tv_start.getText().toString();                //获取起点
        String end = tv_end.getText().toString();                    //获取目的地
        String description = tv_description.getText().toString();    //获取item内容
        String time = tv_time.getText().toString();                  //获取出发时间
        String phone = tv_phone.getText().toString();                //获取出发时间


         getFragmentManager()
                 .beginTransaction()
                 .replace(R.id.fragment_container,MyDetailFragment.newInstance(objectId,title,start,end,description,time,phone))
                 .addToBackStack(null)
                 .commit();
        }catch (Exception e){

            Log.e("TAG",e.getMessage().toString());
        }

    }
}

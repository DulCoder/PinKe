package com.fafu.kongshu.zhengxianyou.pinke.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fafu.kongshu.zhengxianyou.pinke.DisplayActivity;
import com.fafu.kongshu.zhengxianyou.pinke.R;
import com.fafu.kongshu.zhengxianyou.pinke.bean.Note;
import com.fafu.kongshu.zhengxianyou.pinke.config.Config;
import com.fafu.kongshu.zhengxianyou.pinke.utils.Utils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by zhengxianyou on 2016/10/31
 * 详细界面，用于对已发布的item信息进行修改
 */

public class MyDetailFragment extends Fragment implements View.OnClickListener {
    private View mView;
    private DisplayActivity displayActivity = new DisplayActivity();

    private TextView tv_title;
    private EditText et_description, et_start, et_end, et_time, et_phone;
    private String title, description, start, end, time, phone;
    private String currentLocation;
    private Double latitude, longitude;
    private String objectId;
    private Button btn_save, btn_cancel;
    private DisplayActivity mDisplayActivity;

    /**
     * 返回创建fragment实例
     */
    public static MyDetailFragment newInstance(String objectId, String title, String start, String end, String description, String time, String phone) {
        Bundle bundle = new Bundle();
        bundle.putString("objectId", objectId);
        bundle.putString("title", title);
        bundle.putString("start", start);
        bundle.putString("end", end);
        bundle.putString("description", description);
        bundle.putString("time", time);
        bundle.putString("phone", phone);
        MyDetailFragment myDetailFragment = new MyDetailFragment();
        myDetailFragment.setArguments(bundle);
        return myDetailFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDisplayActivity = (DisplayActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_detail_my, container, false);
        displayActivity.setHandler(0);

        initView();
        return mView;
    }

    /**
     * 初始化view
     */
    private void initView() {
        tv_title = (TextView) mView.findViewById(R.id.tv_title);
        et_start = (EditText) mView.findViewById(R.id.et_start);
        et_end = (EditText) mView.findViewById(R.id.et_end);
        et_time = (EditText) mView.findViewById(R.id.et_time);
        et_phone = (EditText) mView.findViewById(R.id.et_phone);
        et_description = (EditText) mView.findViewById(R.id.et_content);
        btn_save = (Button) mView.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        btn_cancel = (Button) mView.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);

        title = getArguments().getString("title");
        start = getArguments().getString("start");
        end = getArguments().getString("end");
        description = getArguments().getString("description");
        time = getArguments().getString("time");
        phone = getArguments().getString("phone");


        objectId = getArguments().getString("objectId");
        Log.e("CCC", objectId);
        tv_title.setText(title);
        et_start.setText(start);
        et_end.setText(end);
        et_description.setText(description);
        et_time.setText(time);
        et_phone.setText(phone);
        currentLocation = Config.getTempLocation().toString();
        latitude = Config.getLatitude();
        longitude = Config.getLongitude();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_save:

                if (!TextUtils.isEmpty(description) && !TextUtils.isEmpty(start) && !TextUtils.isEmpty(end) && !TextUtils.isEmpty(time) && phone.length() == 11) {
                    Note note = new Note();
                    note.setTitle(tv_title.getText().toString());
                    note.setOrigin(et_start.getText().toString());
                    note.setDestination(et_end.getText().toString());
                    note.setContent(et_description.getText().toString());
                    note.setTime(et_time.getText().toString());
                    note.setPhoneNumber(et_phone.getText().toString());
                    note.setCurrentLocation(currentLocation);
                    note.setLatitude(latitude);
                    note.setLongitude(longitude);


                    note.update(objectId, new UpdateListener() {

                        @Override
                        public void done(BmobException e) {

                            if (e != null) {
                                Config.setMyContentFragmentView(null);
                                goBack();
                            } else {
                                String s = String.valueOf(e.getErrorCode());
                                Log.e("MyDetailFragment", s + e.getMessage().toString());
                            }
                        }
                    });
                } else {
                    Utils.toast(displayActivity, "请把信息填写完整");
                }

                break;

            case R.id.btn_cancel:
                goMyContent();
                break;
        }
    }

    private void goMyContent() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, MyContentFragment.newInstance(), null)
                .commit();
    }

    private void goBack() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, DisplayFragment.newInstance(), null)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDisplayActivity.setHandler(1);
    }
}

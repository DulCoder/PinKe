package com.fafu.kongshu.zhengxianyou.pinke.fragment;

/**
 * Created by zhengxianyou on 2016/10/29.
 * * 找回密码界面，
 * 通过注册账号是填写的邮箱找回
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fafu.kongshu.zhengxianyou.pinke.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class Forget_Fragment extends Fragment implements View.OnClickListener {
    private View view;
    private EditText et_find;
    private Button btn_find;
    private String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_forget, container, false);
        initView();
        return view;
    }

    /**
     * 初始化view
     */
    private void initView() {
        et_find = (EditText) view.findViewById(R.id.et_find);
        btn_find = (Button) view.findViewById(R.id.btn_find);
        btn_find.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_find:
                findPassword();
                break;
        }
    }

    /**
     * 找回密码相关逻辑
     */
    private void findPassword() {
        email = et_find.getText().toString();
        if (TextUtils.isEmpty(email) | !email.contains("@")) {  //判断邮箱格式是否正确
            toast("邮箱输入错误");
        } else {
            BmobUser.resetPasswordByEmail(email, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    toast("验证邮件已经发送至邮箱");
                }
            });
        }
    }

    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
}
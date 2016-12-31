package com.fafu.kongshu.zhengxianyou.pinke.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fafu.kongshu.zhengxianyou.pinke.DisplayActivity;
import com.fafu.kongshu.zhengxianyou.pinke.LoginActivity;
import com.fafu.kongshu.zhengxianyou.pinke.PinKeApplication;
import com.fafu.kongshu.zhengxianyou.pinke.R;
import com.fafu.kongshu.zhengxianyou.pinke.bean.MyUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static com.fafu.kongshu.zhengxianyou.pinke.PinKeApplication.editor;

/**
 * Created by zhengxianyou on 2016/10/29.
 */

public class Login_Fragment extends Fragment implements View.OnClickListener {
    private View v;

    private EditText et_login, et_password;
    private Button btn_logup, btn_login;
    private TextView tv_forget;
    private LoginActivity mRing_upActivity;
    private CheckBox cb_choice;
    private RelativeLayout rl_login;

    /**
     *返回创建fragment实例
     */
    public static Login_Fragment newInstance() {
        Login_Fragment mFragment = new Login_Fragment();
        return mFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mRing_upActivity = (LoginActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_login, container, false);

        initView();
        getJudge();
        return v;
    }

    private void getJudge() {
        boolean isChecked = PinKeApplication.sp.getBoolean("isChecked",false);
        if (isChecked){
            String name = PinKeApplication.sp.getString("name",null);
            String password = PinKeApplication.sp.getString("password",null);

            et_login.setText(name);
            et_password.setText(password);
            cb_choice.setChecked(true);
        }

    }

    /**
     * 初始化view
     */
    private void initView() {

        et_login = (EditText) v.findViewById(R.id.et_login);
        et_password = (EditText) v.findViewById(R.id.et_password);

        btn_logup = (Button) v.findViewById(R.id.btn_logup);
        btn_logup.setOnClickListener(this);

        btn_login = (Button) v.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        tv_forget = (TextView) v.findViewById(R.id.tv_forget);
        tv_forget.setOnClickListener(this);

        cb_choice = (CheckBox) v.findViewById(R.id.cb_choice);
        cb_choice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    cb_choice.setChecked(true);
                }else {

                }
            }
        });

        rl_login = (RelativeLayout) v.findViewById(R.id.rl_login);

    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_login:
                login();

                break;
            case R.id.btn_logup:
                //跳转到注册界面
                goRegister();

                break;
            case R.id.tv_forget:
                //跳转到找回密码界面
                goForget();

                break;

        }
    }

    /**
     * 跳转到找回密码界面
     */
    private void goForget() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_ring_up_container, new Forget_Fragment())
                .addToBackStack(null)
                .commit();
    }
    /**
     * 跳转到注册界面
     */
    private void goRegister() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_ring_up_container, Register_Fragment.newInstance())
                .commit();
    }


    /**
     * 登录相关逻辑
     */
    String name, password;

    private void login() {
        name = et_login.getText().toString();
        password = et_password.getText().toString();
        final MyUser user = new MyUser();

        user.setUsername(name);
        user.setPassword(password);
        user.login(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                try {
                    if (e == null) {
                        PinKeApplication.editor.putBoolean("isChecked",cb_choice.isChecked());
                        if (cb_choice.isChecked()){
                            editor.putString("name",name);
                            editor.putString("password",password);
                            editor.commit();
                        }
                       String icon =  myUser.getMyIcon();
                        String nickName = myUser.getNickName();

                        rl_login.setVisibility(View.VISIBLE);

                        Intent intent = new Intent(mRing_upActivity, DisplayActivity.class);
                        intent.putExtra("name",name);
                        intent.putExtra("icon",icon);
                        intent.putExtra("nickName",nickName);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        Log.e("err1", e.getErrorCode() + "" + e.getMessage());
                    }

                } catch (Exception exception) {
                    Log.e("err", exception.getMessage() + "");
                    toast("正在登录");
                }
            }
//                try {
//                    if (user.getEmailVerified()) {
//                        if (e == null) {
//                            toast("登录成功:");
//                            //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
//                            //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
////                            Intent i = new Intent(getActivity(), MainActivity.class);
////                            i.putExtra("username", name);
//                            Log.e("err",e.getErrorCode()+""+e.getMessage());
//
////                            startActivity(i);
//                            startActivity(new Intent(mRing_upActivity, MainActivity.class));
//                            getActivity().finish();
//                        } else {
//                            Log.e("err",e.getErrorCode()+""+e.getMessage());
//                            toast("error");
//                        }
//                    } else {
//                        toast("请前往邮箱验证");
//                    }
//                } catch (Exception exception) {
//                    Log.e("err",exception.getMessage()+"");
//                    startActivity(new Intent(mRing_upActivity, MainActivity.class));
//                    toast("正在登录");
//                }
//            }
        });

    }

    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();

    }
}

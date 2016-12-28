package com.fafu.kongshu.zhengxianyou.pinke;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.fafu.kongshu.zhengxianyou.pinke.adapter.DatabaseAdapter;
import com.fafu.kongshu.zhengxianyou.pinke.bean.MyUser;
import com.fafu.kongshu.zhengxianyou.pinke.bean.Note;
import com.fafu.kongshu.zhengxianyou.pinke.config.Config;
import com.fafu.kongshu.zhengxianyou.pinke.sqlitedb.NoteMeteData;
import com.fafu.kongshu.zhengxianyou.pinke.utils.Utils;

import java.text.DateFormat;
import java.util.Date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class AddActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Spinner mSpinner;
    private static final String[] item_spinner = {"Search Car", "Search Fare"};
    private ArrayAdapter<String> mAdapter;
    private EditText et_description, et_start, et_end, et_time, et_phone;
    private Button btn_save, btn_cancel;
    private String choice;
    private String currentLocation;
    private Double latitude, longitude;
    private DatabaseAdapter mDatabaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        mDatabaseAdapter = new DatabaseAdapter(this);

        currentLocation = Config.getTempLocation().toString();
        latitude = Config.getLatitude();
        longitude = Config.getLongitude();
        Utils.toast(this, currentLocation);

        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        mSpinner = (Spinner) findViewById(R.id.mSpinner);
        //将可选内容与ArrayAdapter连接起来
        mAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, item_spinner);

        //设置下拉列表的风格
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //mAdapter 添加到spinner中
        mSpinner.setAdapter(mAdapter);

        //添加事件Spinner事件监听
        mSpinner.setOnItemSelectedListener(this);

        //设置默认值
        mSpinner.setVisibility(View.VISIBLE);

        et_description = (EditText)findViewById(R.id.et_description);
        et_start = (EditText)findViewById(R.id.et_start);
        et_end = (EditText)findViewById(R.id.et_end);
        et_time = (EditText)findViewById(R.id.et_time);
        et_phone = (EditText)findViewById(R.id.et_phone);
        btn_save = (Button)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        btn_cancel = (Button)findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_save:           //保存数据后退出本界面

                save();
                break;

            case R.id.btn_cancel:         //取消退出本界面
                goBack();
                break;

        }
    }


    private void save() {
        Date date = new Date();
        DateFormat format = DateFormat.getDateTimeInstance();
        String datetime = format.format(date);            //获取当前时间

        String origin = et_start.getText().toString();
        String end = et_end.getText().toString();
        String time = et_time.getText().toString();
        String content = et_description.getText().toString();
        String phone = et_phone.getText().toString();
        String icon = Config.getMyIcon();
        String nickName = Config.getNickName();
        Log.e("DATE", datetime + nickName + icon);
        if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(origin) && !TextUtils.isEmpty(end) && !TextUtils.isEmpty(time) && phone.length() == 11) {
            MyUser user = BmobUser.getCurrentUser(MyUser.class);
            final Note note = new Note();
            note.setTitle(choice);
            note.setContent(content);
            note.setOrigin(origin);
            note.setDestination(end);
            note.setTime(time);
            note.setPhoneNumber(phone);
            note.setCurrentLocation(currentLocation);
            note.setDatetime(datetime);
            note.setMyIcon(icon);
            note.setNickName(nickName);
            note.setLatitude(latitude);
            note.setLongitude(longitude);
            note.setAuthor(user);                             //设置作者字段以便查询
            try {
                note.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        mDatabaseAdapter.rawAdd(note, NoteMeteData.MyNoteTable.TABLE_NAME);
                        Config.setIsRefresh(true);
                        goBack();
                    }
                });
            }catch (Exception e){

            }
        } else {
            Utils.toast(this, "请把信息填写完整");
        }

    }

    /**
     * 返回上一界面
     */
    private void goBack() {
      finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        choice = item_spinner[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}

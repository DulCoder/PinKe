package com.fafu.kongshu.zhengxianyou.pinke.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fafu.kongshu.zhengxianyou.pinke.R;
import com.fafu.kongshu.zhengxianyou.pinke.bean.Note;

import java.util.List;

import static com.fafu.kongshu.zhengxianyou.pinke.R.id.iv_icon;

/**
 * Created by zhengxianyou on 2016/10/23.
 */

public class NoteAdapter extends BaseAdapter {
    private List<Note> mList;
    private Context mContext;

    public NoteAdapter(Context context, List<Note> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
        convertView = LayoutInflater.from(mContext).inflate(R.layout.note_item_list, parent, false);
        viewHolder = new ViewHolder();
        viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
        viewHolder.tv_start = (TextView) convertView.findViewById(R.id.tv_start);
        viewHolder.tv_end = (TextView) convertView.findViewById(R.id.destination);
        viewHolder.tv_description = (TextView) convertView.findViewById(R.id.tv_description);
        viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
        viewHolder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
        viewHolder.tv_location = (TextView) convertView.findViewById(R.id.tv_location);
        viewHolder.tv_contact = (TextView) convertView.findViewById(R.id.tv_contact);
        viewHolder.tv_contact.setVisibility(View.GONE);
        viewHolder.btn_call = (Button) convertView.findViewById(R.id.btn_call);
        viewHolder.btn_call.setVisibility(View.GONE);
        viewHolder.iv_icon = (ImageView) convertView.findViewById(iv_icon);
        viewHolder.tv_nick = (TextView) convertView.findViewById(R.id.tv_nick);
        viewHolder.tv_datetime = (TextView) convertView.findViewById(R.id.tv_datetime);

            convertView.setTag(viewHolder);

        }
        viewHolder = (ViewHolder) convertView.getTag();
        try {
        Note note = mList.get(position);
//        convertView.setTag(note.getObjectId());
        viewHolder.tv_title.setText(note.getTitle());
        viewHolder.tv_start.setText(note.getOrigin());
        viewHolder.tv_end.setText(note.getDestination());
        viewHolder.tv_description.setText(note.getContent());
        viewHolder.tv_time.setText(note.getTime());
        viewHolder.tv_phone.setText(note.getPhoneNumber());
        viewHolder.tv_location.setText(note.getCurrentLocation());
        viewHolder.tv_nick.setText(note.getNickName());
        viewHolder.tv_datetime.setText(note.getDatetime());

        String icon = note.getMyIcon();
        switch (icon){

            case "icon1":
                viewHolder.iv_icon.setImageResource(R.drawable.icon1);

                break;

            case "icon2":
                viewHolder.iv_icon.setImageResource(R.drawable.icon2);

                break;

            case "icon3":
                viewHolder.iv_icon.setImageResource(R.drawable.icon3);

                break;

            case "icon4":
                viewHolder.iv_icon.setImageResource(R.drawable.icon4);

                break;

            case "icon5":
                viewHolder.iv_icon.setImageResource(R.drawable.icon5);

                break;

            case "icon6":
                viewHolder.iv_icon.setImageResource(R.drawable.icon6);

                break;

            case "icon7":
                viewHolder.iv_icon.setImageResource(R.drawable.icon7);

                break;

            case "icon8":
                viewHolder.iv_icon.setImageResource(R.drawable.icon8);

                break;

            case "icon9":
                viewHolder.iv_icon.setImageResource(R.drawable.icon9);

                break;

            default:

                break;
        }
        }catch (Exception e){
            Log.e("EEE",e.getMessage().toString());
        }
        return convertView;
    }


    private static class ViewHolder {

        TextView tv_title;
        TextView tv_start;
        TextView tv_end;
        TextView tv_description;
        TextView tv_time;
        TextView tv_phone;
        TextView tv_contact;
        Button btn_call;
        TextView tv_location;
        ImageView iv_icon;
        TextView tv_nick;
        TextView tv_datetime;

    }
}

package com.example.myapplication;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.drawable.Drawable;

public class MyAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MyData> arrData;
    private LayoutInflater inflater;
    private int pos;
    private BtnClickListener mClickListener;

    public MyAdapter(Context c, ArrayList<MyData> arr, BtnClickListener listener ) {
        this.context = c;
        this.arrData = arr;
        inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mClickListener = listener;
    }

    public void modifyItem(int position, MyData mydata){
        arrData.set(position, mydata);
    }

    public int getCount() {
        return arrData.size();
    }

    public Object getItem(int position) {
        return arrData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void removeItem(int index){
        arrData.remove(index);
    }

    public void addItem(Drawable icon, String Name, String Ph_num) {
        MyData item = new MyData(icon, Name, Ph_num, "", BitmapFactory.decodeResource(context.getResources(),
                R.drawable.user));
        arrData.add(item);
    }

    public View getView(final int position,View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = inflater.inflate(R.layout.list, parent, false);
        }

        ImageView image = (ImageView)convertView.findViewById(R.id.image);

        Drawable draw = arrData.get(position).getImage();
        image.setImageDrawable(draw);

        TextView name = (TextView)convertView.findViewById(R.id.name);
        name.setText(arrData.get(position).getName());

        TextView delBtn = (TextView) convertView.findViewById(R.id.delbtn);
        delBtn.setTag(position);
        delBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(mClickListener != null)
                    mClickListener.onBtnClick((Integer) v.getTag());
            }
        });
        return convertView;
    }
    public void modifyto(ArrayList<MyData> newarrData){
        this.arrData = newarrData;
    }
    }
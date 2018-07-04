package com.example.myapplication;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class C_Adapter extends BaseAdapter{
    private ArrayList<ListViewItem>  listViewItemList = new ArrayList<ListViewItem>();

    public C_Adapter() {

    }
    @Override
    public int getCount(){
        return listViewItemList.size();
    }

    public ArrayList<ListViewItem> getListViewItemList() {
        return listViewItemList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos = position;
        final Context context = parent.getContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_layout, parent, false);
        }
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView1) ;
        TextView NameTextView = (TextView) convertView.findViewById(R.id.name) ;
        TextView PhTextView = (TextView) convertView.findViewById(R.id.ph_num) ;

        ListViewItem listViewitem = listViewItemList.get(position);
        iconImageView.setImageDrawable(listViewitem.getIconDrawable());
        NameTextView.setText(listViewitem.getName());
        PhTextView.setText(listViewitem.getPh_num());

        return convertView;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    public void addItem(Drawable icon, String Name, String Ph_num) {
        ListViewItem item = new ListViewItem();

        item.setIconDrawable(icon);
        item.setName(Name);
        item.setPh_num(Ph_num);

        listViewItemList.add(item);
    }
    public void removeItem(int index){
        listViewItemList.remove(index);
    }
    public void remove_all(){
        for(int i=0;i<getCount();i++)
            removeItem(i);
    }
    public void modifyItem(int index,Drawable icon, String Name,String Ph_num){
        ListViewItem item = new ListViewItem();

        item.setIconDrawable(icon);
        item.setName(Name);
        item.setPh_num(Ph_num);
        listViewItemList.set(index, item);
    }
    public void modifyicon(int index, Drawable icon){
        if(index==9999) return;
        listViewItemList.get(index).setIconDrawable(icon);
    }
}
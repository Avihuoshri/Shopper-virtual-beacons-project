package com.arielu.shopper.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.TreeMap;

public class ListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listTitles;
    private TreeMap<String,Item[]> list;

    public ListAdapter(Context context,List<String> listTitles,TreeMap<String, Item[]> list) {
        this.context = context;
        this.listTitles = listTitles;
        this.list = list;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return list.get(listTitles.get(i)).length;
    }

    @Override
    public Object getGroup(int i) {
        return listTitles.get(i);
    }

    @Override
    public Item getChild(int i, int i1) {
        return list.get(listTitles.get(i))[i1];
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String title = (String) getGroup(i);
        if(view==null){
            LayoutInflater layoutInflater =(LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_group,null);
        }
        TextView listTitleView = view.findViewById(R.id.group_title);
        listTitleView.setText(title);
        return view ;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if(view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item, null);
        }
        Item currItem = getChild(i,i1);
        ((TextView)view.findViewById(R.id.item_name)).setText(currItem.getName());
        ((TextView)view.findViewById(R.id.item_price)).setText("\u20AA"+currItem.getPrice());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


}

package com.arielu.shopper.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.arielu.shopper.demo.classes.Product;
import com.arielu.shopper.demo.models.SessionProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

public class ListAdapter2 extends BaseExpandableListAdapter {
    private Context context;
    private TreeMap<String,ArrayList<SessionProduct>> list;

    public ListAdapter2(Context context,TreeMap<String, ArrayList<SessionProduct>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return list.get(list.keySet().toArray()[i]).size();
    }

    @Override
    public Object getGroup(int i) {
        return list.keySet().toArray()[i];
    }

    @Override
    public Product getChild(int i, int i1) {
        return list.get(list.keySet().toArray()[i]).get(i1);
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
        Product currItem = getChild(i,i1);
        ((TextView)view.findViewById(R.id.item_name)).setText(currItem.getProductName());
        ((TextView)view.findViewById(R.id.item_price)).setText("\u20AA"+currItem.getProductPrice());
        ((ImageView)view.findViewById(R.id.item_image)).setImageBitmap(currItem.ProductImage());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }




}

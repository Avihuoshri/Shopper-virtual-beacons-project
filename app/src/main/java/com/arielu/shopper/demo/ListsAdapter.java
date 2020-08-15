package com.arielu.shopper.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.arielu.shopper.demo.classes.Shopping_list;
import com.arielu.shopper.demo.models.SessionProduct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import androidx.core.content.ContextCompat;

public class ListsAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private ArrayList<Shopping_list> list,displayList;
    private ArrayList<Integer> selectedItems;
    private final int blue,white;

    public ListsAdapter(Context context, ArrayList<Shopping_list> list) {
        this.context = context;
        this.list = list;
        displayList = list;
        selectedItems = new ArrayList<>();
        blue = ContextCompat.getColor(context,R.color.blue);
        white = ContextCompat.getColor(context, R.color.white);
    }

    @Override
    public int getCount() {
        return displayList.size();
    }

    @Override
    public Object getItem(int i) {
        return displayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
            view = LayoutInflater.from(context).inflate(R.layout.list_listname,null);
        Shopping_list shopping_list = (Shopping_list) getItem(i);
        ((TextView)view.findViewById(R.id.text_list_name)).setText(shopping_list.getShopping_list_title());
        if (selectedItems.contains(i))
            view.setBackgroundColor(blue);
        else view.setBackgroundColor(white);
        return view;
    }

    public List<String> remove(){
        ArrayList<String> listIds = new ArrayList();
        Collections.sort(selectedItems,Collections.reverseOrder());
        for (int position:selectedItems) {
            Shopping_list shopping_list = list.get(position);
            if (!shopping_list.getShopping_list_id().equals(""))
                listIds.add(shopping_list.getShopping_list_id());
            list.remove(position);

        }
        selectedItems.clear();
        this.displayList = list;
        notifyDataSetChanged();
        return listIds;
    }

    public boolean select(int position){
        if (selectedItems.contains(position)){
            selectedItems.remove(selectedItems.indexOf(position));
            return false;
        }
        selectedItems.add(position);
        return true;
    }
    public void cancel(){
        selectedItems.clear();
        notifyDataSetChanged();
    }
    public int allSelectedItems(){
        return selectedItems.size();
    }
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                ArrayList<Shopping_list> filterData = new ArrayList<>();
                int count=0;
                charSequence = charSequence.toString().toLowerCase();
                for (Shopping_list shopping_list: list) {
                    if (shopping_list.getShopping_list_title().toLowerCase().contains(charSequence.toString())) {
                        filterData.add(shopping_list);
                        count+= 1;
                    }
                }
                results.count = count;
                results.values=filterData;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                displayList = (ArrayList<Shopping_list>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

}

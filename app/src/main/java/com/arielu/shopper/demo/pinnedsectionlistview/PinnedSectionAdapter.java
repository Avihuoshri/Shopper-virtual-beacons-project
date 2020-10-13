package com.arielu.shopper.demo.pinnedsectionlistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.arielu.shopper.demo.R;
import com.arielu.shopper.demo.models.SessionProduct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import androidx.core.content.ContextCompat;

public class PinnedSectionAdapter extends BaseExpandableListAdapter implements PinnedSectionListView.PinnedSection, AbsListView.OnScrollListener, Filterable {
    private Context context;
    private TreeMap<Integer,List<Integer>> selectedItems;
    private TreeMap<String,ArrayList<SessionProduct>> displayList, list;
    private int countSelectedItems=0;
    private final int blue,white;

    public PinnedSectionAdapter(Context context, TreeMap<String, ArrayList<SessionProduct>> list) {
        this.context = context;
        this.displayList = list;
        this.list = list;
        blue = ContextCompat.getColor(context,R.color.blue);
        white = ContextCompat.getColor(context, R.color.white);
        selectedItems=new TreeMap<>();
    }

    @Override
    public int getGroupCount() {
        return displayList.size();
    }

    @Override
    public int getChildrenCount(int i) {
            if (displayList.containsKey(""+getGroup(i)))
        return displayList.get(getGroup(i)).size();
            return 0;
    }

    @Override
    public Object getGroup(int i) {
        if (displayList.size()>i)
        return displayList.keySet().toArray()[i];
        return 0;
    }

    @Override
    public Object getChild(int i, int i1) {
        return displayList.get(displayList.keySet().toArray()[i]).get(i1);
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
        if (view==null){
            LayoutInflater layoutInflater =(LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_group,null);
        }
        TextView listTitleView = view.findViewById(R.id.group_title);
        listTitleView.setText(title);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if(view==null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item, null);
        }
        SessionProduct currItem = (SessionProduct) getChild(i,i1);
        if(currItem instanceof SessionProduct)
            ((TextView)view.findViewById(R.id.item_quantity)).setText("X"+((SessionProduct)currItem).getQuantity());
        ((TextView)view.findViewById(R.id.item_name)).setText(currItem.getProductName());
        ((TextView)view.findViewById(R.id.item_price)).setText("\u20AA"+currItem.getProductPrice());
        ((ImageView)view.findViewById(R.id.item_image)).setImageBitmap(currItem.ProductImage());
        if (selectedItems.containsKey(i)&&selectedItems.get(i).contains(i1))
            view.setBackgroundColor(blue);
        else
            view.setBackgroundColor(white);
        if (currItem.getIsBought())
            view.setAlpha((float) 0.3);
        else view.setAlpha(1);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
    public void configurePinnedSection(View v, int position, int alpha) {
        TextView header = v.findViewById(R.id.group_title);
        header.setAlpha(alpha);
        v.setAlpha(alpha);
        final String title = (String) getGroup(position);
        header.setText(title);
    }
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (view instanceof PinnedSectionListView) {
            ((PinnedSectionListView) view).configureSectionTop(firstVisibleItem);
            ((PinnedSectionListView) view).configureSectionBottom(firstVisibleItem+visibleItemCount-1);
        }
    }
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub

    }
    public void remove(){
        SortedSet<Integer> sorted = new TreeSet<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });
        sorted.addAll(selectedItems.keySet());
        for (int group:sorted) {
            String currGroup = "" + this.list.keySet().toArray()[group];
            ArrayList list = this.list.get(currGroup);
            Collections.sort(selectedItems.get(group),Collections.<Integer>reverseOrder());
            for (int child:selectedItems.get(group)) {
                if (list.size()<=1)
                    this.list.remove(currGroup);
                else
                    list.remove(child);
            }
        }
        selectedItems.clear();
        countSelectedItems=0;
        this.displayList = list;
        notifyDataSetChanged();
    }
    public void done(){
        for (int group:selectedItems.keySet()) {
            String currGroup = "" + this.list.keySet().toArray()[group];
            ArrayList list = this.list.get(currGroup);
            for (int child:selectedItems.get(group)) {
                SessionProduct product = (SessionProduct) list.get(child);
                product.setIsBought(true);
            }
        }
        selectedItems.clear();
        countSelectedItems=0;
        this.displayList = list;
        notifyDataSetChanged();
    }
    public void cancel(){
        selectedItems.clear();
        countSelectedItems=0;
        notifyDataSetChanged();
    }
public int totalSelectedItems(){
        return countSelectedItems;
}
    public boolean select(int group, int child){
        ArrayList<Integer> list;
        if (selectedItems.containsKey(group)){
            list = (ArrayList<Integer>) selectedItems.get(group);
            if (list.contains(child)){
                if (list.size()==1)
                    selectedItems.remove(group);
                else
                    list.remove(child);
                countSelectedItems-=1;
                return false;
            }else list.add(child);
        }else{
            list = new ArrayList<>();
            list.add(child);
            selectedItems.put(group,list);
        }
        countSelectedItems+=1;
        return true;
    }
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                TreeMap<String,ArrayList<SessionProduct>> filterData = new TreeMap<>();
                int count=0;
                charSequence = charSequence.toString().toLowerCase();
                for (String group: list.keySet()) {
                    if (group.toLowerCase().contains(charSequence.toString())) {
                        filterData.put(group, list.get(group));
                        count+= list.get(group).size();
                    }
                    else
                        for (SessionProduct product: list.get(group)) {
                            if (product.getProductName().toLowerCase().contains(charSequence.toString())){
                                if (filterData.containsKey(group))
                                    filterData.get(group).add(product);
                                else {
                                    ArrayList list = new ArrayList();
                                    list.add(product);
                                    filterData.put(group, list);
                                }
                                count+=1;
                            }
                        }
                }
                results.count = count;
                results.values=filterData;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                displayList = (TreeMap<String, ArrayList<SessionProduct>>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }
}

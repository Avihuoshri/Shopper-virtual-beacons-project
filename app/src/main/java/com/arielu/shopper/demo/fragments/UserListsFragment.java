package com.arielu.shopper.demo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.arielu.shopper.demo.ChooseListActivity;
import com.arielu.shopper.demo.ListsAdapter;
import com.arielu.shopper.demo.R;
import com.arielu.shopper.demo.UserShoppingListActivity;
import com.arielu.shopper.demo.classes.Shopping_list;
import com.arielu.shopper.demo.database.Firebase2;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class UserListsFragment extends Fragment {
    public static final String MY_LISTS="MY_LISTS";
    private  ChooseListActivity parent;
    private ListView listview;
    public LinearLayout FAB;
    private ListsAdapter listsAdapter;
    private ArrayList<Shopping_list> list;
    private FirebaseAuth mAuth;
    private int blue,white;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_lists,container,false);
        listview = view.findViewById(R.id.lists);
        FAB = view.findViewById(R.id.add_list_button);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parent = ((ChooseListActivity)getActivity());
        mAuth = FirebaseAuth.getInstance();
        blue = ContextCompat.getColor(getActivity().getApplicationContext(),R.color.blue);
        white = ContextCompat.getColor(getActivity().getApplicationContext(), R.color.white);
        list = new ArrayList<>();
        listsAdapter = new ListsAdapter(this.getActivity(),list);
        listview.setAdapter(listsAdapter);
        Firebase2.getUserLists(mAuth.getCurrentUser().getUid(),(lists) -> {
            list.addAll((List<Shopping_list>)lists);
            listsAdapter.notifyDataSetChanged();
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (parent.isSelectOn){

                    if (listsAdapter.select(i))
                        view.setBackgroundColor(blue);
                    else view.setBackgroundColor(white);
                    if (listsAdapter.allSelectedItems()==0)
                        cancel();
                    else parent.toolbar.setTitle(listsAdapter.allSelectedItems()+" Selected");
                }else {
                    Intent intent = new Intent(getActivity(), UserShoppingListActivity.class);

                    Shopping_list listItem = list.get(i);
                    intent.putExtra("list", listItem);
                    startActivity(intent);
                }
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!parent.isSelectOn) {
                    parent.selectMode(true);
                    listsAdapter.select(i);
                    parent.toolbar.setTitle(listsAdapter.allSelectedItems() + " Selected");
                    view.setBackgroundColor(blue);
                }
                return true;
            }
        });
    }
    public void remove(){
        Toast.makeText(getActivity().getApplicationContext(),listsAdapter.allSelectedItems()+" items were deleted.",Toast.LENGTH_SHORT).show();
        ArrayList<String> listIds = (ArrayList<String>) listsAdapter.remove();
        Firebase2.removeListItems(listIds);
        Firebase2.setUserLists(mAuth.getCurrentUser().getUid(),list);
    }
    public void cancel(){
        parent.toolbar.setTitle("List's");
        parent.selectMode(false);
        listsAdapter.cancel();
    }
public void getFilter(String text){
        listsAdapter.getFilter().filter(text);
}
public void addList(String listName){
    if (listName.isEmpty()) {
        Toast.makeText(getActivity(), "can't create list without a name", Toast.LENGTH_SHORT).show();
        return;
    }
    boolean add = true;
    for (Shopping_list list:list) {
        add = !list.getShopping_list_title().equals(listName);
    }
    if (add) {
        Shopping_list result = new Shopping_list("", mAuth.getCurrentUser().getUid(), listName);
        list.add(result);
        Firebase2.pushUserList(mAuth.getCurrentUser().getUid(), result);
        listsAdapter.notifyDataSetChanged();
    }else Toast.makeText(getActivity(),"couldn't add list because of already existing list with same name",Toast.LENGTH_SHORT).show();
}
    public static UserListsFragment newInstance() {

        Bundle args = new Bundle();
        UserListsFragment fragment = new UserListsFragment();
        fragment.setArguments(args);
        return fragment;
    }
}

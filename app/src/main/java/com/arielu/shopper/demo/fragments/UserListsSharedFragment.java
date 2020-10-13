package com.arielu.shopper.demo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.arielu.shopper.demo.ChooseListActivity;
import com.arielu.shopper.demo.ListsAdapter;
import com.arielu.shopper.demo.R;
import com.arielu.shopper.demo.UserShoppingListActivity;
import com.arielu.shopper.demo.classes.Shopping_list;
import com.arielu.shopper.demo.database.Firebase2;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UserListsSharedFragment extends Fragment {
    public static final String SHARED_LISTS="SHARED_LISTS";
    private ArrayList<Shopping_list> list;
    private ListView listView;
    private ListsAdapter listsAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_shared_lists,container,false);
        listView = view.findViewById(R.id.lists);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ChooseListActivity chooseListActivity = ((ChooseListActivity)getActivity());
            list = new ArrayList<>();
            listsAdapter = new ListsAdapter(this.getActivity(), list);
            listView.setAdapter(listsAdapter);
        Firebase2.getUserSharedLists(FirebaseAuth.getInstance().getCurrentUser().getUid(),(lists) -> {
            list.addAll(lists);
            listsAdapter.notifyDataSetChanged();
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), UserShoppingListActivity.class);

                Shopping_list listItem = list.get(i);
                intent.putExtra("list", listItem);
                startActivity(intent);
            }
        });
    }

    public void getFilter(String text){
        listsAdapter.getFilter().filter(text);
    }
    public static UserListsSharedFragment newInstance() {

        Bundle args = new Bundle();
        UserListsSharedFragment fragment = new UserListsSharedFragment();
        fragment.setArguments(args);
        return fragment;
    }
}

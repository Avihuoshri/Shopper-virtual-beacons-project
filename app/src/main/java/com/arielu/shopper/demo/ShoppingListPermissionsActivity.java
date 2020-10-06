package com.arielu.shopper.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.arielu.shopper.demo.classes.Shopping_list;
import com.arielu.shopper.demo.database.Firebase2;
import com.arielu.shopper.demo.models.Permission;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListPermissionsActivity extends AppCompatActivity implements DialogAddPermission.DialogListener {

    //private String listID;
    //private String listName;
    private Shopping_list listObj;
    private List<String> permissions;
    private List<Permission> permTemp;

    // UI
    private ListView lv_permissions;
    private ArrayAdapter<String>  lv_permissions_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_permissions);

        //this.listID = getIntent().getStringExtra("listID");
        //this.listName = getIntent().getStringExtra("listName");
        this.listObj = (Shopping_list) getIntent().getSerializableExtra("list");

        this.permissions = new ArrayList<>();
        this.permTemp = new ArrayList<>();

        // arrange listview and its adapter.
        lv_permissions = findViewById(R.id.lv_permissions);
        lv_permissions_adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                permissions);
        lv_permissions.setAdapter(lv_permissions_adapter);

        // Firebase DB get permission list for list id

        Firebase2.getListPermissions(this.listObj.getShopping_list_id(), data -> {
            //permissions.clear();
            //permissions.addAll(data);
            if(data == null) return;
            permTemp.addAll(data);
            for (Permission p : data) {
                Firebase2.getUserNameByID(p.getUserID(), (data2) -> {
                    permissions.add(data2[1]);
                    lv_permissions_adapter.notifyDataSetChanged();
                });
            }

            Log.d("Permissions_List", permissions.toString());
        });
    }

    public void onAddPermisssionClick(View view)
    {
        DialogFragment newFragment = new DialogAddPermission();
        newFragment.show(getSupportFragmentManager(), "Add Permission");
    }



    @Override
    public void addPermission(String userPhoneNumber) {
        Firebase2.getUserIDByPhone(userPhoneNumber, (data) -> {
            if(data == null)
            { // a user with such phone number was not found.
                Toast.makeText(ShoppingListPermissionsActivity.this, "a user with such phone number was not found.", Toast.LENGTH_SHORT).show();
                return;
            }

            Permission fakePermission = new Permission(data[1], Permission.PermissionType.Member);
            Firebase2.setNewPermission(this.listObj.getShopping_list_id(),this.listObj.getShopping_list_title(), fakePermission);

            Firebase2.getUserNameByID(data[1], (data2) -> {
                        permissions.add(data2[1]);
                        lv_permissions_adapter.notifyDataSetChanged();
            });

            // to remove:
            //Firebase2.removePermission(this.listID, data[1]);

        });
    }
}

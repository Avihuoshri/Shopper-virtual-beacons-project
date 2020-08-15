package com.arielu.shopper.demo.database;


import android.util.Log;

import com.arielu.shopper.demo.classes.Branch;
import com.arielu.shopper.demo.classes.Product;
import com.arielu.shopper.demo.classes.Shopping_list;
import com.arielu.shopper.demo.models.Message;
import com.arielu.shopper.demo.models.Permission;
import com.arielu.shopper.demo.models.SessionProduct;
import com.arielu.shopper.demo.models.StoreProductRef;
import com.arielu.shopper.demo.models.User;
import com.arielu.shopper.demo.models.UserSessionData;
import com.arielu.shopper.demo.utilities.DelegateonDataChangeFunction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.QueryParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

final public class Firebase2 {

    public static void getListItems(String listID, Task task)
    {
        firebaseDBGetRequest("shopping_list_items/"+listID, task, (dataSnapshot) -> {
            GenericTypeIndicator<List<SessionProduct>> genericTypeIndicator = new GenericTypeIndicator<List<SessionProduct>>() {};
            try{
                List<SessionProduct> items_data = dataSnapshot.getValue(genericTypeIndicator);
                return items_data;
            }
            catch (Exception e)
            {
                return new ArrayList<SessionProduct>();
            }
        });
    }
    public static void removeListItems(List<String> listIDs){
        for (String listID:listIDs) {
            removeListItems(listID);
        }
    }

    public static void removeListItems(String listID){
        firebaseDBSetRequest("shopping_list_items/"+listID,null);
    }


    /**
     * Retrieve from firebase DB the lists that other people shared or gave permissions to the user.
     */
    public static void getUserSharedLists(String uID, Task<List<Shopping_list>> task)
    {

        firebaseDBGetRequest("user_shared_shopping_lists/"+uID, task, dataSnapshot -> {
            Log.d("firebase_shared_lists",dataSnapshot.toString());
            List<Shopping_list> lists = new ArrayList<>();
            for(DataSnapshot dss : dataSnapshot.getChildren())
            {
                String listID = dss.getKey();
                String listTitle = dss.getValue(String.class);
                lists.add(new Shopping_list(listID,uID, listTitle));

            }
            return lists;
        });
    }

    public static void getShoppingListMetaDataByID(String listID, String uID, Task<List<Shopping_list>> task)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("user_shopping_lists/"+uID);

        Query query = myRef.orderByChild("shopping_list_id").equalTo(listID).limitToFirst(1);


        firebaseDBGetRequestWithQuery(query,task,(dataSnapshot) -> {
            List<Shopping_list> lists = new ArrayList<>();

            for(DataSnapshot dss : dataSnapshot.getChildren())
                lists.add(dss.getValue(Shopping_list.class));

            return lists;
        });
    }

    public static void getListPermissions(String listID, Task<List<Permission>> task)
    {
        firebaseDBGetRequest("shopping_list_permissions/"+listID,task,(dataSnapshot) -> {
            List<Permission> permissions_data = new ArrayList<>();
            for(DataSnapshot dss : dataSnapshot.getChildren())
            {
                String permUserID = dss.getKey();
                Permission.PermissionType permType = Permission.PermissionType.valueOf(dss.child("permissionType").getValue(String.class));

                permissions_data.add(new Permission(permUserID,permType));
            }

            return permissions_data;
        });

    }

    public static void getUserNameByID(final String uID, Task<String[]> task)
    {
        firebaseDBGetRequest("users/"+uID+"/name",task,(dataSnapshot) -> {
            String name = dataSnapshot.getValue(String.class);
            String data[] = {uID, name};
            return data;
        });
    }

    public static void getUserIDByPhone(final String phone, Task<String[]> task)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        Query query = myRef.orderByChild("phoneNumber").equalTo(phone).limitToFirst(1);

        firebaseDBGetRequestWithQuery(query, task , (dataSnapshot) -> {

            String uID = null;
            String phoneNumber = phone;
            // get the first one with matching phoneNumber.
            for(DataSnapshot dss : dataSnapshot.getChildren())
            {
                uID = dss.getKey();
                break; // iterate only once.
            }

            return new String[] {phoneNumber, uID};
        });

    }

    public static void getUserListinSession(String uID, Task<List<UserSessionData>> task)
    {
        firebaseDBGetRequest("user_session_lists/"+uID,task,(dataSnapshot) -> {
            List<UserSessionData> lists = new ArrayList<>();
            for(DataSnapshot dss : dataSnapshot.getChildren())
                lists.add(dss.getValue(UserSessionData.class));

            return lists;
        });
    }

    public static void getProductList(Task task)
    {
        firebaseDBGetRequest("products",task,(dataSnapshot) -> {
            GenericTypeIndicator<List<Product>> genericTypeIndicator = new GenericTypeIndicator<List<Product>>() {};
            List<Product> products = dataSnapshot.getValue(genericTypeIndicator);

            return products;

        });
    }

    public static void getStoreMessages(String companybranchID, Task task)
    {
        firebaseDBGetRequest("store_message/"+companybranchID, task, (dataSnapshot) -> {
            List<Message> messages = new ArrayList<>();
            for(DataSnapshot dss : dataSnapshot.getChildren())
                messages.add(dss.getValue(Message.class));

            return messages;
        });
    }

    public static void getStoresList(Task task)
    {
        firebaseDBGetRequest("stores", task, (dataSnapshot) -> {
            GenericTypeIndicator<List<Branch>> genericTypeIndicator = new GenericTypeIndicator<List<Branch>>() {};
            List<Branch> lists = dataSnapshot.getValue(genericTypeIndicator);

            return lists;
        });

    }

    public static void getUserLists(String uID, Task task)
    {
        firebaseDBGetRequest("user_shopping_lists/"+uID,task,(dataSnapshot) -> {
            List<Shopping_list> lists = new ArrayList<>();
            for(DataSnapshot dss : dataSnapshot.getChildren())
                lists.add(dss.getValue(Shopping_list.class));
            return lists;
        });
    }

    public static void setUserLists(String uID,List<Shopping_list> shopping_lists){

        firebaseDBSetRequest("user_shopping_lists/"+uID,shopping_lists);
    }

    public static void pushUserList(String uID,Shopping_list shopping_lists){
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("user_shopping_lists/"+uID);

        String pushedKey = myRef.push().getKey();
        shopping_lists.setShopping_list_id(pushedKey);
        Map<String, Object> map = new HashMap<>();
        map.put(pushedKey, shopping_lists);
        myRef.updateChildren(map);

    }
    public static void getUserData(String uID, Task task)
    {
        firebaseDBGetRequest("users/"+uID, task, (dataSnapshot) -> {
            User user = dataSnapshot.getValue(User.class);
            return user;
        });
    }

    public static void removePermission(String listID, String uID)
    {
        // need to remove data at 2 locations:
        // [1] from shopping_list_permissions/listID/userID
        // [2] from user_shared_shopping_lists/userID/listID:title.

        // [1]
        firebaseDBSetRequest("shopping_list_permissions/"+listID+"/"+uID, null);
        // [2]
        firebaseDBSetRequest("user_shared_shopping_lists/"+uID, null);
    }

    public static void setNewPermission(String listID, String listTitle,Permission permission)
    {
        // need to set data at 2 locations:
        // [1] at shopping_list_permissions/listID/userID
        // [2] at user_shared_shopping_lists/userID/listID:title.

        String uID = permission.getUserID();

        // set [1].
        Map<String, Object> data = new HashMap<>();
        data.put("permissionType", permission.getPermissionType());
        firebaseDBSetRequest("shopping_list_permissions/"+listID+"/"+uID, data);

        // set [2]
        Map<String, Object> data2 = new HashMap<>();
        data2.put(listID, listTitle);
        firebaseDBSetRequest("user_shared_shopping_lists/"+uID, data2);
    }


    // Save list
    public static void setListProducts(String listID, List<SessionProduct> listItems)
    {
        firebaseDBSetRequest("shopping_list_items/"+listID, listItems);
    }

    // Save/post message
    public static void pushNewStoreMessage(String companybranchID, Message message)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("store_message/"+companybranchID);

        String pushedKey = myRef.push().getKey();

        Map<String, Object> map = new HashMap<>();
        map.put(pushedKey, message);
        myRef.updateChildren(map);
    }


    // Save/post message
    public static void pushNewSessionlist(String uID, String listID, String companybranchID)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("user_session_lists/"+uID);

        String pushedKey = myRef.push().getKey();

        // create the object to be saved.
        UserSessionData usd = new UserSessionData(listID,companybranchID);

        Map<String, Object> map = new HashMap<>();
        map.put(pushedKey, usd);
        myRef.updateChildren(map);
    }


    ////////////// Templates //////////////////

    private static void firebaseDBSetRequest(String refPath, Object value)
    {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference(refPath);

        myRef.setValue(value);
    }

    private static void firebaseDBGetRequestWithQuery(Query query, Task task, DelegateonDataChangeFunction func)
    {
        query.addListenerForSingleValueEvent(new ValueEventListenerTemplate2(func, task));
    }

    private static void firebaseDBGetRequest(String refPath, Task task, DelegateonDataChangeFunction func)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // read from database
        DatabaseReference myRef = database.getReference(refPath);

        firebaseDBGetRequestWithQuery(myRef, task, func);
    }


    // for lambda exp
    public interface Task<T>
    {
        public void execute(T data);
    }

}

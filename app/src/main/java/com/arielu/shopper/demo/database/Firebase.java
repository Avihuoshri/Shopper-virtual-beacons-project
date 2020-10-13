package com.arielu.shopper.demo.database;


import android.util.Log;

import com.arielu.shopper.demo.classes.Branch;
import com.arielu.shopper.demo.classes.Shopping_list;
import com.arielu.shopper.demo.models.Message;
import com.arielu.shopper.demo.models.Permission;
import com.arielu.shopper.demo.classes.Product;
import com.arielu.shopper.demo.models.StoreProductRef;
import com.arielu.shopper.demo.models.User;
import com.arielu.shopper.demo.utilities.DelegateonDataChangeFunction;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

@Deprecated
final public class Firebase {

    //// STATIC / GLOBAL variables
    public static User userData;
    ////

    public static Observable<List<Product>> getProductList()
    {
         Observable o = firebaseDBGetRequest("products",(dataSnapshot) -> {
            GenericTypeIndicator<List<Product>> genericTypeIndicator = new GenericTypeIndicator<List<Product>>() {};
            List<Product> products = dataSnapshot.getValue(genericTypeIndicator);

            return products;

        });

         return o;

    }

    public static Observable<List<Shopping_list>> getUserLists(String uID)
    {
        Observable o = firebaseDBGetRequest("user_shopping_lists/"+uID,(dataSnapshot) -> {
            GenericTypeIndicator<List<Shopping_list>> genericTypeIndicator = new GenericTypeIndicator<List<Shopping_list>>() {};
            List<Shopping_list> lists = dataSnapshot.getValue(genericTypeIndicator);

            return lists;
        });

        return o;
    }

    public static Observable<List<Message>> getStoreMessages(String companybranchID)
    {
        Observable o = firebaseDBGetRequest("store_message/"+companybranchID,(dataSnapshot) -> {
            GenericTypeIndicator<List<Message>> genericTypeIndicator = new GenericTypeIndicator<List<Message>>() {};
            List<Message> messages = dataSnapshot.getValue(genericTypeIndicator);

            return messages;
        });

        return o;
    }

    public static Observable<List<Branch>> getStoresList()
    {
        Observable o = firebaseDBGetRequest("stores",(dataSnapshot) -> {
            GenericTypeIndicator<List<Branch>> genericTypeIndicator = new GenericTypeIndicator<List<Branch>>() {};
            List<Branch> lists = dataSnapshot.getValue(genericTypeIndicator);

            return lists;
        });

        return o;
    }

    public static Observable<PublishSubject<Product>> getStoreProducts(String id)
    { //id : aaaa1-fAsf1//

        Observable obs = firebaseDBGetRequest("store_products/"+id,(dataSnapshot) -> {
            GenericTypeIndicator<List<StoreProductRef>> genericTypeIndicator = new GenericTypeIndicator<List<StoreProductRef>>() {};
            List<StoreProductRef> list = dataSnapshot.getValue(genericTypeIndicator);

            PublishSubject<Product> o = PublishSubject.create();
            for(StoreProductRef spf : list)
                getProduct(spf, o);

            return o;
        });

        return obs;
    }

    private static void getProduct(StoreProductRef spf,  PublishSubject<Product> o)
    {
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            // read from database
            DatabaseReference myRef = database.getReference("products");
            Query q = myRef.orderByChild("productCode").equalTo(spf.getProductCode());
            q.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Object prod = dataSnapshot.getValue();
                    Log.d("firebase_product",dataSnapshot.toString());
                    Map<String,String> prod_data = null;

                    if(prod instanceof Map)
                    {
                        Map<String,Map<String,String>> data = (Map<String,Map<String,String>>) prod;
                        prod_data = data.values().toArray(new HashMap[0])[0];

                    } else if(prod instanceof List){
                        List<Map<String,String>> data =  (List<Map<String,String>>) prod;
                        prod_data  = data.get(data.size()-1);
                    }

                    Product p = new Product();
                    if(prod_data!=null)
                    {
                        p.setCategoryName(prod_data.get("categoryName"));
                        p.setProductCode(prod_data.get("productCode"));
                        p.setProductImageUrl(prod_data.get("productImageUrl"));
                        p.setProductManufacturer(prod_data.get("productManufacturer"));
                        p.setProductName(prod_data.get("productName"));
                        p.setProductLocation(prod_data.get("productLocation"));
                    }

                    p.setProductPrice(spf.getPrice());
                    o.onNext(p);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            }

            );
    }

    public static void getStoreProductByCode(String productCode, String companybranchID,PublishSubject<StoreProductRef> o)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // read from database
        DatabaseReference myRef = database.getReference("store_products/"+companybranchID);
        Query q = myRef.orderByChild("productCode").equalTo(productCode);
        q.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        StoreProductRef store_prod = null;
                        Object raw_data = dataSnapshot.getValue();
                        Map<String,Object> parsed_data = null;

                        if(raw_data instanceof Map)
                        {
                            Map<String,Map<String,Object>> converted_data = (Map<String,Map<String,Object>>) raw_data;
                            parsed_data = converted_data.values().toArray(new HashMap[0])[0];

                        } else if(raw_data instanceof List){
                            List<Map<String,Object>> converted_data =  (List<Map<String,Object>>) raw_data;
                            parsed_data  = converted_data.get(converted_data.size()-1);
                        }


                        if(parsed_data!=null)
                        {
                            store_prod = new StoreProductRef();
                            store_prod.setProductCode((String) parsed_data.get("productCode"));
                            store_prod.setPrice((Double) parsed_data.get("price"));
                            store_prod.setProductLocation((String) parsed_data.get("productLocation"));
                        }

                        o.onNext(store_prod);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                }

        );
    }

    public static Observable<List<Product>> getListItems(String listID)
    {
        Observable o = firebaseDBGetRequest("shopping_list_items/"+listID,(dataSnapshot) -> {
            GenericTypeIndicator<List<Product>> genericTypeIndicator = new GenericTypeIndicator<List<Product>>() {};
            try{
                List<Product> items_data = dataSnapshot.getValue(genericTypeIndicator);
                return items_data;
            }
            catch (Exception e)
            {
                return new ArrayList<Product>();
            }



        });

        return o;

    }


    public static Observable<List<Permission>> getListPermissions(String listID)
    {
        Observable o = firebaseDBGetRequest("shopping_list_permissions/"+listID,(dataSnapshot) -> {
            GenericTypeIndicator<List<Permission>> genericTypeIndicator = new GenericTypeIndicator<List<Permission>>() {};
            List<Permission> permissions_data = dataSnapshot.getValue(genericTypeIndicator);

            return permissions_data;
        });

        return o;
    }

    public static Observable<Branch> getStoreByBothID(String companystoreID)
    {

        String[] ids = companystoreID.split("-");

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // read from database
        DatabaseReference myRef = database.getReference("stores");
        Query q = myRef.orderByChild("branch_id").equalTo(ids[1]);

        Observable<Branch> o = Observable.create(emitter -> {
            q.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Object prod = dataSnapshot.getValue();
                            Log.d("firebase_store", dataSnapshot.toString());
                            Map<String, String> prod_data = null;

                            Branch branch = null;

                            for (DataSnapshot dss : dataSnapshot.getChildren()) {
                                Branch tempBranch = dss.getValue(Branch.class);
                                if (tempBranch.getCompany_id().equals(ids[0])) {
                                    branch = tempBranch;
                                    break;
                                }
                            }

                            emitter.onNext(branch);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    }

            );
        });

        return o;
    }

    public static Observable<User> getUserData(String uID)
    {
        Observable o = firebaseDBGetRequest("users/"+uID,(dataSnapshot) -> {
            User user = dataSnapshot.getValue(User.class);
            userData = user;
            return user;
        });

        return o;
    }

    public static void setUserData(String uID, User user){
        firebaseDBSetRequest("users/"+uID, user);
    }



    // Save list
    public static void setListProducts(String listID, List<Product> listItems)
    {
        firebaseDBSetRequest("shopping_list_items/"+listID, listItems);
    }


    private static void firebaseDBSetRequest(String refPath, Object value)
    {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference(refPath);

        myRef.setValue(value);
    }

    private static Observable firebaseDBGetRequest(String refPath, DelegateonDataChangeFunction func)
    {
        Observable o = Observable.create(emitter -> {

            FirebaseDatabase database = FirebaseDatabase.getInstance();

            // read from database
            DatabaseReference myRef = database.getReference(refPath);

            myRef.addListenerForSingleValueEvent(new ValueEventListenerTemplate(emitter,func));
        });

        return o;
    }

/*AVIHU*/
    // Add new list to database
    //public static
}

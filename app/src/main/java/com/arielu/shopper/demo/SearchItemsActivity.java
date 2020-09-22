package com.arielu.shopper.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import io.reactivex.rxjava3.core.Observable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.arielu.shopper.demo.NavigationElements.Path;
import com.arielu.shopper.demo.NavigationElements.PathTracker;
import com.arielu.shopper.demo.NavigationElements.Point;
import com.arielu.shopper.demo.database.Firebase;
import com.arielu.shopper.demo.classes.Product;
import com.arielu.shopper.demo.database.Firebase2;
import com.arielu.shopper.demo.models.SessionProduct;
import com.arielu.shopper.demo.utilities.ObserverFirebaseTemplate;

import java.util.ArrayList;
import java.util.List;

public class SearchItemsActivity extends AppCompatActivity implements DialogAddProductQuantity.DialogListener {


    private List<Product> products;
    private List<Product> products_filtered;
    private ArrayAdapter<Product> adapter;
    private Product selected_item;
    private int productQuantity;

    // UI elements //
    private ListView lv_products_filtered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_items);

        products = new ArrayList<>(2);
        products_filtered = new ArrayList<>(30);

        LinkUI();
    }

    @Override
    protected void onStart() {
        super.onStart();

        /* DEPRECATED
        Observable<List<Product>> o = Firebase.getProductList();
        o.subscribe(new ObserverFirebaseTemplate<List<Product>>() {
            @Override
            public void onNext(List<Product> prods) {
                products = prods;
            }
        });
        */

        Firebase2.getProductList((prods)->{
            products = (List<Product>) prods;
            products_filtered.addAll(products);
            adapter.notifyDataSetChanged();

            // refresh UI by the query filter
            SearchView sv_search = findViewById(R.id.sv_search);
            CharSequence currSearchQuery = sv_search.getQuery();
            if(sv_search.getQuery().length() > 0)
                sv_search.setQuery(currSearchQuery,true);
        });
    }

    protected void LinkUI()
    {
        // listview products filtered
        lv_products_filtered = findViewById(R.id.lv_products_filtered);
        lv_products_filtered.setOnItemClickListener(this::onItemClick);

        adapter = new ArrayAdapter<Product>(SearchItemsActivity.this,android.R.layout.simple_list_item_1,products_filtered);
        lv_products_filtered.setAdapter(adapter);

        // searchbox
        SearchView sv_search = findViewById(R.id.sv_search);
        sv_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                products_filtered.clear();
                for(Product p : products)
                {
                    if(p.getProductName().contains(s))
                        products_filtered.add(p);
                }
                adapter.notifyDataSetChanged();

                return false;
            }
        });



    }

    public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
        // Save choice in a variable for later use.
        this.selected_item = (Product) adapter.getItemAtPosition(position);
        // open a dialog, asking the user for the quantity.
        DialogFragment dialogAddProductQuantity = new DialogAddProductQuantity();
        dialogAddProductQuantity.show(getSupportFragmentManager(),"Add Quantity");
    }

    public void btn_chooseitemClick(View view)
    {
        // convert "Product.class" into "SessionProduct.class" using copy-constructor.
        SessionProduct sessProd = new SessionProduct(this.selected_item);
        sessProd.setQuantity(this.productQuantity);

        // transfer the product object to the returning activity.
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",sessProd.toBundle()); // using parcelable instead of serializable.
        setResult(Activity.RESULT_OK,returnIntent);

        finish(); // reload this activity from scratch if opened again, do not preserve data.
    }

    public void addQuantity(int quantity)
    {
        this.productQuantity = quantity;

        if(quantity != 0 & this.selected_item != null)
        {
            TextView tv_chosen_item =  findViewById(R.id.tv_chosen_item);
            String itemText = this.selected_item.getProductName() + " | #" + quantity;
            tv_chosen_item.setText(itemText);

            // show chosen item textview
            tv_chosen_item.setVisibility(View.VISIBLE);
        }
    }
}

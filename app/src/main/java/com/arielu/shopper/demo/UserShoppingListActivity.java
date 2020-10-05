package com.arielu.shopper.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arielu.shopper.demo.NavigationAlgorithms.DiGraph;
import com.arielu.shopper.demo.NavigationAlgorithms.ShortestPath;
import com.arielu.shopper.demo.NavigationElements.Path;
import com.arielu.shopper.demo.NavigationElements.PathTracker;
import com.arielu.shopper.demo.NavigationElements.Point;
import com.arielu.shopper.demo.classes.Branch;
import com.arielu.shopper.demo.classes.Shopping_list;
import com.arielu.shopper.demo.database.Firebase;
import com.arielu.shopper.demo.database.Firebase2;
import com.arielu.shopper.demo.models.SessionProduct;
import com.arielu.shopper.demo.models.StoreProductRef;
import com.arielu.shopper.demo.pinnedsectionlistview.PinnedSectionAdapter;
import com.arielu.shopper.demo.pinnedsectionlistview.PinnedSectionListView;
import com.arielu.shopper.demo.utilities.ImageDownloader;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import io.reactivex.rxjava3.subjects.PublishSubject;


public class UserShoppingListActivity extends AppCompatActivity implements DialogAddProductQuantity.DialogListener {

    private TreeMap<String, ArrayList<SessionProduct>> list = new TreeMap<>();

    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private ProgressBar progressBar;
    private PinnedSectionAdapter pinnedSectionAdapter;
    private PinnedSectionListView pinnedSectionListView;

    private boolean isSelectOn;
    private int blue, white;
    private SessionProduct lastClicked;
    //private String listID;
    //private String listName;
    private Shopping_list listObj;

    private Branch selectedBranch;
    private PathTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_shopping_list);
        // need a policy to allow downloading / accessing images from the network
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //this.listID = getIntent().getStringExtra("listID");
        //this.listName = getIntent().getStringExtra("listName");
        this.listObj = (Shopping_list) getIntent().getSerializableExtra("list");

        //my edit
        progressBar = findViewById(R.id.spinner_loader);
        blue = ContextCompat.getColor(getApplicationContext(), R.color.blue);
        white = ContextCompat.getColor(getApplicationContext(), R.color.white);
        //toolbar
        toolbar = findViewById(R.id.user_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(listObj.getShopping_list_title());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //bottomNavigation
        //root check to remove navigation if keyboard up
        final View root = findViewById(R.id.activity_root);
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = root.getRootView().getHeight() - root.getHeight();
                if (heightDiff > dpToPx(200)) { // if more than 200 dp, it's probably a keyboard...
                    bottomNavigationView.setVisibility(View.GONE);
                } else {
                    if (bottomNavigationView.getVisibility() == View.GONE) {
                        bottomNavigationView.setAlpha(0f);
                        bottomNavigationView.setVisibility(View.VISIBLE);
                        bottomNavigationView.animate().alpha(1f).setDuration(500);
                    }
                }
            }
        });
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        //disable highlight the last selected item in navigation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bottomNavigationView.setItemIconTintList(getColorStateList(R.color.bottom_navigation_color_icon));
            bottomNavigationView.setItemTextColor(getColorStateList(R.color.bottom_navigation_color_icon));
        } else {
            bottomNavigationView.setItemIconTintList(getResources().getColorStateList(R.color.bottom_navigation_color_icon));
            bottomNavigationView.setItemTextColor(getResources().getColorStateList(R.color.bottom_navigation_color_icon));
        }
        bottomNavigationView.setItemTextAppearanceInactive(bottomNavigationView.getItemTextAppearanceActive());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.branch:
                        btn_searchBranchesClick();
                        return true;
                    case R.id.user_permission:
                        btn_permissionsClick();
                        return true;
                    case R.id.btn_drew:
                        btn_drawPathClick();
                        return true;
                    default:
                        return false;


                }
            }
        });
        //PinnedSectionListView
        pinnedSectionAdapter = new PinnedSectionAdapter(this, list);
        pinnedSectionListView = findViewById(R.id.user_list);
        pinnedSectionListView.setAdapter(pinnedSectionAdapter);
        pinnedSectionListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (pinnedSectionListView.getPackedPositionType(l) == PinnedSectionListView.PACKED_POSITION_TYPE_CHILD) {
                    int group = pinnedSectionListView.getPackedPositionGroup(l);
                    int child = pinnedSectionListView.getPackedPositionChild(l);
                    //TODO function set select mode for delete,new list,sum of selected items,etc...
                    if (!isSelectOn) {
                        selectMode(true);
                        view.setBackgroundColor(blue);
                        ((PinnedSectionAdapter) ((PinnedSectionListView) adapterView).getExpandableListAdapter()).select(group, child);
                        toolbar.setTitle(pinnedSectionAdapter.totalSelectedItems() + " Selected");
                    }
                }
                return true;
            }
        });
        pinnedSectionListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                if (isSelectOn) {
                    if (((PinnedSectionAdapter) expandableListView.getExpandableListAdapter()).select(i, i1))
                        view.setBackgroundColor(blue);
                    else
                        view.setBackgroundColor(white);
                    toolbar.setTitle(pinnedSectionAdapter.totalSelectedItems() + " Selected");
                    if (pinnedSectionAdapter.totalSelectedItems() == 0)
                        selectMode(false);
                } else {
                    SessionProduct product = (SessionProduct) expandableListView.getExpandableListAdapter().getChild(i, i1);
                    if (product.getIsBought()) {
                        product.setIsBought(false);
                        pinnedSectionAdapter.notifyDataSetChanged();
                    } else {
                        lastClicked = product;
                        DialogFragment dialogFragment = new DialogAddProductQuantity();
                        dialogFragment.show(getSupportFragmentManager(), "Change quantity");
                    }
                }
                return true;
            }
        });
        pinnedSectionListView.setPinnedSections(R.layout.list_group);
        //get data
        Firebase2.getListItems(this.listObj.getShopping_list_id(), (data) -> {
            if (data == null) return;
            progressBar.setVisibility(View.VISIBLE);

            List<SessionProduct> products = (List<SessionProduct>) data;
            ArrayList<SessionProduct> temp;
            for (SessionProduct p : products) {
                // get image
                p.setProductImage(ImageDownloader.getBitmapFromURL(p.getProductImageUrl()));

                if (list.get(p.getCategoryName()) == null) {
                    temp = new ArrayList<>();

                } else {
                    temp = list.get(p.getCategoryName());
                }

                temp.add(p);
                list.put(p.getCategoryName(), temp);

                // Log.d("product location", p.getProductLocation());
            }
            progressBar.setVisibility(View.GONE);
            pinnedSectionAdapter.notifyDataSetChanged();
            expandAll();

        });


    }

    private void btn_drawPathClick() {
        tracker = new PathTracker();
        //todo - sort list for fastest shouping route (by departments)
        int listSize = list.size();
        boolean firstProduct = true;
        Point lastProductPoint = null;
        for (Map.Entry<String, ArrayList<SessionProduct>> entry : list.entrySet()) {
            ArrayList<SessionProduct> subList = entry.getValue();
            Path path = new Path();
            for(SessionProduct product: subList) {
                Point p = new Point(product.getProductLocation());
                path.add(p);
            }
            tracker.list.add(path);
        }

        Intent intent = new Intent(UserShoppingListActivity.this, DrawMapActivity.class);
        intent.putExtra("tracker", tracker);

        startActivity(intent);
    }
//new

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isSelectOn)
            getMenuInflater().inflate(R.menu.products_select_mode_menu, menu);
        else {
            getMenuInflater().inflate(R.menu.user_list_menu, menu);
            MenuItem searchItem = menu.findItem(R.id.search_filter);
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setQueryHint("Search...");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchView.clearFocus();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    UserShoppingListActivity.this.pinnedSectionAdapter.getFilter().filter(newText);
                    expandAll();
                    return true;
                }
            });
            searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem menuItem) {
                    searchView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.search_view_fade_in));
                    setItemsVisibility(menu, searchItem, false);
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                    invalidateOptionsMenu();
                    return true;
                }
            });

        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                btn_searchitemsClick();
                return true;
            case R.id.save_list:
                btn_saveClick();
                return true;
            case R.id.delete:
                remove();
                return true;
            case R.id.done:
                pinnedSectionAdapter.done();
                selectMode(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void selectMode(boolean state) {
        isSelectOn = state;
        if (!state)
            toolbar.setTitle(listObj.getShopping_list_title());
        invalidateOptionsMenu();
    }

    private void expandAll() {
        for (int i = 0; i < list.size(); i++) {
            pinnedSectionListView.expandGroup(i);
        }
    }

    @Override
    public void onBackPressed() {
        if (isSelectOn)
            cancel();
        else
            super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void cancel() {
        pinnedSectionAdapter.cancel();
        selectMode(false);
    }

    public void remove() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete).setMessage(R.string.delete_message).setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                pinnedSectionAdapter.remove();
                selectMode(false);
            }
        }).setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create();
        builder.show();
    }

    public void myCart(View view) {
        Toast toast = Toast.makeText(getApplicationContext(), "worked", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void getProductsPrice(Branch branch) {
        // create a subject (observable/observer) that notifies when a product retrieved from Firebase.
        PublishSubject<StoreProductRef> storeProdRefSubject = PublishSubject.create();
        // now declare what to do when the subject notifies us.
        storeProdRefSubject.subscribe((storeProductRef) -> {
            List<SessionProduct> products = convertProductsMapToList(this.list);

            SessionProduct prod = null;
            for (SessionProduct p : products) {
                if (p.getProductCode().equals(storeProductRef.getProductCode())) {
                    prod = p;
                    break;
                }
            }

            prod.setProductPrice(storeProductRef.getPrice());
            pinnedSectionAdapter.notifyDataSetChanged();
        });

        List<SessionProduct> products = convertProductsMapToList(this.list);

        for (SessionProduct p : products)
            Firebase.getStoreProductByCode(p.getProductCode(), branch.getCompany_id() + "-" + branch.getBranch_id(), storeProdRefSubject);

    }


    private List<SessionProduct> convertProductsMapToList(Map<String, ArrayList<SessionProduct>> productsMap) {
        Collection<ArrayList<SessionProduct>> c = productsMap.values();
        ArrayList<SessionProduct> lst = new ArrayList<>();
        for (ArrayList<SessionProduct> l : c) {
            lst.addAll(l);
        }

        return lst;
    }


    public void btn_searchitemsClick() {
        Intent intent = new Intent(this, SearchItemsActivity.class);
        startActivityForResult(intent, 1);
    }

    public void btn_searchBranchesClick() {
        Intent intent = new Intent(this, BranchesActivity.class);
        startActivityForResult(intent, 2);
    }


    public void btn_saveClick() {
        List<SessionProduct> lst = convertProductsMapToList(list);
        Firebase2.setListProducts(listObj.getShopping_list_id(), lst);

        Toast.makeText(UserShoppingListActivity.this, "Saving your list...", Toast.LENGTH_SHORT);
    }


    public void btn_permissionsClick() {
        // TODO
        // intent -> change activity to ChangePermissionListActivity
        // putting the list id in putextra.
        // inside that activity, load the current permitted user from the DB
        // and make an option to change the list -> add new users / remove users.
        // adding new user by their (?)
        Intent intent = new Intent(this, ShoppingListPermissionsActivity.class);
        //intent.putExtra("listID", this.listID);
        //intent.putExtra("listName", this.listName);
        intent.putExtra("list", (Serializable) listObj);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    // parse result into a SessionProduct.class (serializable / parcelable).
                    SessionProduct sessProd = SessionProduct.fromBundle(data.getParcelableExtra("result"));
                    // download & set bitmap image to the product.
                    sessProd.setProductImage(ImageDownloader.getBitmapFromURL(sessProd.getProductImageUrl()));

                    // find correct group, if the group does not exists yet, create it.
                    ArrayList<SessionProduct> temp;
                    Boolean isCategoryExistInView = (list.get(sessProd.getCategoryName()) == null);
                    if (isCategoryExistInView) {
                        temp = new ArrayList<>();
                        list.put(sessProd.getCategoryName(), temp);
                    } else {
                        temp = list.get(sessProd.getCategoryName());
                    }

                    // add the product to the group, dont allow duplicates.
                    if (!temp.contains(sessProd))
                        temp.add(sessProd);

                    // notify adapter that changes were made to the dataset.
                    pinnedSectionAdapter.notifyDataSetChanged();
                    expandAll();

                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                }
                break;
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    selectedBranch = (Branch) data.getSerializableExtra("result");
                    if (selectedBranch == null) break;
                    getProductsPrice(selectedBranch);

                    Firebase2.pushNewSessionlist(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            this.listObj.getShopping_list_id(), (selectedBranch.getCompany_id() + "-" + selectedBranch.getBranch_id()));
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                }
                break;
        }
    }//onActivityResult

    @Override
    public void addQuantity(int quantity) {
        lastClicked.setQuantity(quantity);
        pinnedSectionAdapter.notifyDataSetChanged();
    }

    public float dpToPx(float valueInDp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i = 0; i < menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }
}



   
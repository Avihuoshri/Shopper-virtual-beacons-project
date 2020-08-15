package com.arielu.shopper.demo;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.arielu.shopper.demo.fragments.CollectionPagerAdapter;
import com.arielu.shopper.demo.fragments.UserListsFragment;
import com.arielu.shopper.demo.fragments.UserListsSharedFragment;
import com.google.android.material.tabs.TabLayout;

public class ChooseListActivity extends AppCompatActivity implements DialogAddList.DialogListener
{
    public Toolbar toolbar;
    CollectionPagerAdapter collectionPagerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    MenuItem searchItem;
    private ProgressBar progressBar;
    public boolean isSelectOn;
    public int blue,white;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_list);
        //ViewPager
        collectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(collectionPagerAdapter);
        tabLayout = viewPager.findViewById(R.id.lists_tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("Test", "onPageSelected: "+position);
                if (isSelectOn&&position!=0)
                    cancel();
                if (searchItem.isActionViewExpanded())
                    searchItem.collapseActionView();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        toolbar = findViewById(R.id.user_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("List's");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressBar = findViewById(R.id.spinner_loader);
        blue = ContextCompat.getColor(getApplicationContext(),R.color.blue);
        white = ContextCompat.getColor(getApplicationContext(), R.color.white);
    }
    public void createNewList(View v){
        DialogFragment newFragment = new DialogAddList();
        newFragment.show(getSupportFragmentManager(), "Add List");

    }

    //delegate between dialog and activity
    @Override
    public void addList(String listName) {
        ((UserListsFragment)currentFragmentDisplay()).addList(listName);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isSelectOn)
            getMenuInflater().inflate(R.menu.select_mode_menu,menu);
        else {
            getMenuInflater().inflate(R.menu.lists_menu, menu);
            searchItem = menu.findItem(R.id.search_filter);
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
                    switch (viewPager.getCurrentItem()){
                        case 0:
                            ((UserListsFragment)currentFragmentDisplay()).getFilter(newText);
                            break;
                        case 1:
                            ((UserListsSharedFragment)currentFragmentDisplay()).getFilter(newText);
                            break;
                        default:
                    }
                    return false;
                }
            });
            int tabHeight = tabLayout.getMeasuredHeight();
            searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem menuItem) {
                    searchView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.search_view_fade_in));
                    Animation animation =AnimationUtils.loadAnimation(getApplicationContext(),R.anim.collapse_tablayout);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            ValueAnimator anim = ValueAnimator.ofInt(tabLayout.getMeasuredHeight(),0);
                            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    int value = (int) valueAnimator.getAnimatedValue();
                                    ViewPager.LayoutParams layoutParams = (ViewPager.LayoutParams) tabLayout.getLayoutParams();
                                    layoutParams.height = value;
                                    tabLayout.setLayoutParams(layoutParams);
                                }
                            });
                            anim.setDuration(500);
                            anim.start();
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            tabLayout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    tabLayout.startAnimation(animation);
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                    Animation animation =AnimationUtils.loadAnimation(getApplicationContext(),R.anim.expand_tablayout);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            tabLayout.setVisibility(View.VISIBLE);
                            ValueAnimator anim = ValueAnimator.ofInt(tabLayout.getMeasuredHeight(),tabHeight);
                            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    int value = (int) valueAnimator.getAnimatedValue();
                                    ViewPager.LayoutParams layoutParams = (ViewPager.LayoutParams) tabLayout.getLayoutParams();
                                    layoutParams.height = value;
                                    tabLayout.setLayoutParams(layoutParams);
                                }
                            });
                            anim.setDuration(500);
                            anim.start();
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    tabLayout.startAnimation(animation);
                    return true;
                }
            });
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                remove();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
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
        return true;
    }
    private void cancel(){
        switch (viewPager.getCurrentItem()){
            case 0:
            case 1:
                ((UserListsFragment)collectionPagerAdapter.getItem(0)).cancel();
                break;
                default:
                    return;
        }
    }
    private void remove(){
        switch (viewPager.getCurrentItem()){
            case 0:
                ((UserListsFragment)currentFragmentDisplay()).remove();
                selectMode(false);
                break;
                default: return;
        }
    }
    public void selectMode(boolean state){
        isSelectOn=state;
        if (state) {
            ((UserListsFragment)collectionPagerAdapter.getItem(0)).FAB.setVisibility(View.GONE);
        }
        else {
            toolbar.setTitle("List's");
            ((UserListsFragment)collectionPagerAdapter.getItem(0)).FAB.setVisibility(View.VISIBLE);
        }
        invalidateOptionsMenu();
    }
    private Fragment currentFragmentDisplay(){
        return collectionPagerAdapter.getItem(viewPager.getCurrentItem());
    }
}

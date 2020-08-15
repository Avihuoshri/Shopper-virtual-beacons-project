package com.arielu.shopper.demo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.arielu.shopper.demo.classes.Branch;
import com.arielu.shopper.demo.database.Firebase;
import com.arielu.shopper.demo.services.LocationTracker;
import com.arielu.shopper.demo.utilities.ObserverFirebaseTemplate;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class BranchesActivity extends AppCompatActivity {

    ArrayAdapter adapter;
    List<Branch> branches = new ArrayList<>();
    List<Branch> branches_filtered = new ArrayList<>();
    int distance = 0;
    int step = 50; // meter
    Branch selectedBranch;

    // UI
    TextView tv_distance_value;

    // location services
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    LocationTracker locTracker = null;
    boolean permissionGranted = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branches);


        ////////////////////////////////////////////////////////////////
        /////////////// ListView of stores/branches list ///////////////
        ////////////////////////////////////////////////////////////////

        Firebase.getStoresList().subscribe(new ObserverFirebaseTemplate<List<Branch>>() {
            @Override
            public void onNext(List<Branch> o) {
                branches.addAll(o);
                filterBranchesByDistance(distance);
                adapter.notifyDataSetChanged();
            }
        });


        ListView lv_branches_list = findViewById(R.id.lv_branches_list);
        lv_branches_list.setClickable(true);
        lv_branches_list.setOnItemClickListener((parent, view, pos, id) -> {
            selectedBranch = branches_filtered.get(pos);
            returnSelectedBranch();
        });


        this.adapter = new ArrayAdapter<Branch>(BranchesActivity.this,android.R.layout.simple_list_item_1,branches_filtered);
        lv_branches_list.setAdapter(this.adapter);

        ///////////////////////////////////////////////////////////////
        /////////////// Distance SeekBar and its labels ///////////////
        ///////////////////////////////////////////////////////////////

        SeekBar sb_distance_limit = findViewById(R.id.sb_distance_limit);
        sb_distance_limit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distance = (progress * step);
                tv_distance_value.setText(Float.toString(distance/1000.0f)); //show in km
                filterBranchesByDistance(distance);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* ? */}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* ? */}
        });

        distance = sb_distance_limit.getProgress() * step;
        tv_distance_value = findViewById(R.id.tv_distance_value);
        tv_distance_value.setText(Float.toString(distance/1000.0f)); //show in km


        ///////////////////////////////////////////////////////////
        /////////////// ---- Location Services ---- ///////////////
        ///////////////////////////////////////////////////////////

        connectToLocationServices();

    }

    private void filterBranchesByDistance(int dist_filter)
    {
        if(locTracker == null) return;

        branches_filtered.clear();
        for (Branch b : branches) {
            if (locTracker.getLocation().distanceTo(b.LocationData()) <= dist_filter)
                branches_filtered.add(b);
        }
    }


    private void returnSelectedBranch()
    {
        returnBranch(this.selectedBranch);
    }

    private void returnBranch(Branch branch)
    {
        locTracker.stopUsingGPS();

        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",branch);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    private void connectToLocationServices()
    {
        // Step 1: check for permissions
        if(!checkLocationServicesAccessPermission()) {
            // step 1.1: if there is no permission, request the user to grant it.
            requestLocationServicesAccessPermission();
            // step 1.2: check if the user approved the request.
            if(!permissionGranted) // by the user
                // step 1.2.1: if the user didn't approve, don't init locTracker and let it be NULL.
                return;
        }

        // Step 2: initialize/connect the location service
        locTracker = new LocationTracker(this);
        // Step "final": can now access location via 'locTracker.getLocation()'
    }

    private boolean checkLocationServicesAccessPermission()
    {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M)
            return true;

        if (ContextCompat.checkSelfPermission(this, mPermission) != PackageManager.PERMISSION_GRANTED)
            return false;
        else return true;
    }

    private void requestLocationServicesAccessPermission()
    {
        try {
            ActivityCompat.requestPermissions(this, new String[]{mPermission},
                    REQUEST_CODE_PERMISSION);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Source code: android doc
     * https://developer.android.com/training/permissions/requesting
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    permissionGranted = true;
                    locTracker = new LocationTracker(this);

                    // refresh / update UI
                    SeekBar sb_distance_limit = findViewById(R.id.sb_distance_limit);
                    int progressValue = sb_distance_limit.getProgress();
                    sb_distance_limit.setProgress(1);
                    sb_distance_limit.setProgress(progressValue);

                } else {
                    // permission denied, boo!
                    permissionGranted = false;
                }
                return;
            }

        }
    }



}

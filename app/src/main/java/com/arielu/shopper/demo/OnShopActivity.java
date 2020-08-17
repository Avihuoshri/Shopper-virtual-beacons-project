package com.arielu.shopper.demo;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.arielu.shopper.demo.interfaces.StepListener;

import androidx.appcompat.app.AppCompatActivity;

import com.arielu.shopper.demo.senssors.stepDetector;
import com.arielu.shopper.demo.NavigationElements.PathTracker;
import com.arielu.shopper.demo.NavigationElements.Path;
import com.arielu.shopper.demo.NavigationElements.Point;

import java.util.ArrayList;
//import com.example.shopper.DataBaseHandlers.DBShoppingPathAppender;
//import com.example.shopper.DataBaseHandlers.DBStoreBranchRegisterer;

public class OnShopActivity extends AppCompatActivity implements SensorEventListener, StepListener {
    private TextView TvSteps;
    private Button BtnNext;
    private Button BtnTake;
    private Button BtnSave;
    private Button BtnDrewPath;
    private Button BtnShowRoute;
    private Button BtnShowProducts;
    private stepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private SensorManager sensorManagerComp;
    private Sensor accel;
    private Sensor compass;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps;
    private static final String FILE_NAME = "degrees.txt";
    EditText mEditText;
    EditText mEditTextProduct;
    private static final String CHOOSEN_BRANCH = "Ron's All Goods Store";

    PathTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_shop);

        initSenssors();

        initButtons();

        BtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numSteps = 0;
                sensorManager.registerListener(OnShopActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
                sensorManagerComp.registerListener(OnShopActivity.this, compass, SensorManager.SENSOR_DELAY_GAME);
            }
        });


        BtnTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorManager.unregisterListener(OnShopActivity.this);
                ArrayList<Float> degrees = simpleStepDetector.getDegrees();
                String destination = mEditTextProduct.getText().toString();
                tracker.saveRoute(degrees, destination);
            }
        });

        BtnShowRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText(tracker.toString() + System.lineSeparator() + System.lineSeparator());
            }
        });

        BtnShowProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText(tracker.ProductsToString() + System.lineSeparator() + System.lineSeparator());
            }
        });

        BtnDrewPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Path p = new Path();
                Point point1 = new Point(400, 1000);
                p.add(point1);
                Point point2 = new Point(253, 830);
                p.add(point2);

                tracker.list.head.setPath(p);

                Intent intent = new Intent(OnShopActivity.this, DrawMapActivity.class);
                intent.putExtra("tracker", tracker);
                startActivity(intent);
            }
        });

        // todo - fix DB
//        BtnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DBShoppingPathAppender pathAppender = new DBShoppingPathAppender();
//                pathAppender.addAllNodesOfShoppingListToDb(tracker);
//                DBStoreBranchRegisterer branchRegisterer = new DBStoreBranchRegisterer();
//                branchRegisterer.handleBranchStock(tracker);
//            }
//        });

    }


    private void initSenssors() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManagerComp = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        compass = sensorManagerComp.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        simpleStepDetector = new stepDetector();
        simpleStepDetector.registerListener(this);

        tracker = new PathTracker();
        //tracker.initList();

    }

    private void initButtons() {
        BtnSave = findViewById(R.id.button_save);
        TvSteps = findViewById(R.id.tv_steps);
        BtnNext = findViewById(R.id.btn_start);
        BtnTake = findViewById(R.id.btn_stop);
        BtnShowRoute = findViewById(R.id.button_show_route);
        BtnShowProducts = findViewById(R.id.button_Show_products);
        BtnDrewPath = findViewById(R.id.btn_drew);
        mEditText = findViewById(R.id.edit_text);
        mEditTextProduct = findViewById(R.id.edit_text_product);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            simpleStepDetector.setDegree(event.values[0]);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        TvSteps.setText(TEXT_NUM_STEPS + numSteps);
    }

}
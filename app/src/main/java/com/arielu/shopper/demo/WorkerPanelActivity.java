package com.arielu.shopper.demo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.arielu.shopper.demo.database.Firebase2;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

public class WorkerPanelActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_panel);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                signOutClick();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    private void init(){
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
    }
    public void signOutClick()
    {
        // Check the user is indeed logged in
        if(mAuth.getCurrentUser() != null)
        {
            // sign out.
            mAuth.signOut();

            // switch activity to login form.
            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
    public void goToMessages(View v){
        Intent intent = new Intent(this, MessageBoardActivity.class);
        startActivity(intent);
    }


}


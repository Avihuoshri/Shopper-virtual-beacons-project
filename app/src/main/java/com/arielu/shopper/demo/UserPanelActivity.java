package com.arielu.shopper.demo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.arielu.shopper.demo.classes.Shopping_list;
import com.arielu.shopper.demo.database.Firebase;
import com.arielu.shopper.demo.database.Firebase2;
import com.arielu.shopper.demo.models.SessionProduct;
import com.arielu.shopper.demo.models.User;
import com.arielu.shopper.demo.models.UserSessionData;
import com.arielu.shopper.demo.utilities.ObserverFirebaseTemplate;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.rxjava3.core.Observable;

public class UserPanelActivity extends AppCompatActivity {

    //// Firebase authentication ////
    private FirebaseAuth mAuth;


    EditText editTextName ;
    Spinner list ;
    Button addList ;

    private String sessionListID = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        //
        TextView txt_name = findViewById(R.id.txt_name);
        txt_name.setTextColor(Color.rgb(211,211,211));
        Observable<User> o = Firebase.getUserData(mAuth.getCurrentUser().getUid());
        o.subscribe(new ObserverFirebaseTemplate<User>() {
            @Override
            public void onNext(User user) {
                txt_name.setText( "Hello " + user.getName() /*+ " (" + user.getUsername() + ")"*/);
                txt_name.setTextColor(Color.BLACK);
            }
        });

        //
        Firebase2.getUserListinSession(mAuth.getCurrentUser().getUid(), (data) -> {
            Log.d("firebase_session_lists", data.toString());

            List<UserSessionData> sessLists = data;
            if(sessLists.size() > 0 )
            {
                Firebase2.getListItems(sessLists.get(sessLists.size()-1).getListID(), (data2) -> {
                    List<SessionProduct> sessProducts = (List<SessionProduct>) data2;
                    double sum = 0;
                    for(SessionProduct sp : sessProducts)
                        sum += sp.computeTotalPrice();

                    ((TextView)findViewById(R.id.tv_list_total_price)).setText(Double.toString(sum)+"₪");

                });

                // update also the button leading to the list itself in session.
                sessionListID = sessLists.get(sessLists.size()-1).getListID();
            }


        });
    }



    public void signOutClick(View view)
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


    public void onQrBtnClick(View view)
    {
        Intent intent = new Intent(this, QrCamera.class);
        startActivity(intent);
    }

    public void onListBtnClick(View view)
    {
        Intent intent = new Intent(this, ChooseListActivity.class);
        startActivity(intent);
    }

    public void btn_testClick(View view)
    {
        Intent intent = new Intent(this, UserMessageBoardActivity.class);
        startActivity(intent);
    }

    public void imgbtn_mycartClick(View view)
    {
        if(sessionListID == null) return;

        Firebase2.getShoppingListMetaDataByID(sessionListID, FirebaseAuth.getInstance().getCurrentUser().getUid(), (data) -> {
            if(data == null) return;

            Shopping_list list = data.get(data.size()-1);

            Intent intent = new Intent(this, UserShoppingListActivity.class);
            intent.putExtra("list",list);
            startActivity(intent);
        });


    }
}

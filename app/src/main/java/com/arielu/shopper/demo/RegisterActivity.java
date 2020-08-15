package com.arielu.shopper.demo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arielu.shopper.demo.database.Firebase;
import com.arielu.shopper.demo.models.User;
import com.arielu.shopper.demo.utilities.ObserverFirebaseTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import io.reactivex.rxjava3.core.Observable;

public class RegisterActivity extends AppCompatActivity {
    private Spinner spinner;
    private EditText email,password,confirmPassword,phoneNumber,name;
    private TextView emailText,passwordText,confirmPasswordText, phoneNumberText,nameText;
    private ProgressBar progressBar;
    private boolean isValidEmail,isValidPassword,isValidConfirmPassword,isValidPhoneNumber,isValidName;
    private FirebaseAuth mAuth;
    private User userData;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }
    public void init(){
        //Spinner
        spinner = findViewById(R.id.user_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.spinner_items,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //ProgressBar
        progressBar = findViewById(R.id.spinner_loader);
        //EditText
        email = findViewById(R.id.email_field);
        password = findViewById(R.id.password_field);
        confirmPassword = findViewById(R.id.confirm_password_field);
        phoneNumber = findViewById(R.id.phone_number_field);
        name = findViewById(R.id.name_field);
        //TextView
        emailText = findViewById(R.id.enter_email);
        passwordText = findViewById(R.id.enter_password);
        confirmPasswordText = findViewById(R.id.enter_confirm_password);
        phoneNumberText = findViewById(R.id.enter_phone_number);
        nameText = findViewById(R.id.enter_name);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //Add validation to EditText Input
        fieldsValidation();
    }
    private void fieldsValidation(){
        Drawable defaultInput = ContextCompat.getDrawable(getApplicationContext(),R.drawable.input_theme);
        Drawable wrongInput = ContextCompat.getDrawable(getApplicationContext(),R.drawable.wrong_input_theme);
        int pantone = ContextCompat.getColor(getApplicationContext(), R.color.pantone);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isValidName=!name.getText().toString().isEmpty();
                if (isValidName){
                    if (nameText.getCurrentTextColor()==pantone) {
                        nameText.setTextColor(Color.BLACK);
                        name.setBackground(defaultInput);
                    }
                }else{
                    nameText.setTextColor(pantone);
                    name.setBackground(wrongInput);
                }
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                isValidEmail = email.getText().toString().contains("@");
                if(isValidEmail) {
                    if (emailText.getCurrentTextColor()==pantone) {
                        emailText.setTextColor(Color.BLACK);
                        email.setBackground(defaultInput);
                    }
                }else{
                    emailText.setTextColor(pantone);
                    email.setBackground(wrongInput);
                }
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isValidPassword=Pattern.matches("^.{6,}$",password.getText());
                if (isValidPassword){
                    if (passwordText.getCurrentTextColor()==pantone) {
                        passwordText.setTextColor(Color.BLACK);
                        password.setBackground(defaultInput);
                    }
                }else{
                    passwordText.setTextColor(pantone);
                    password.setBackground(wrongInput);
                }
            }
        });
        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                isValidConfirmPassword=confirmPassword.getText().toString().equals(password.getText().toString());
                if (isValidConfirmPassword){
                    if (confirmPasswordText.getCurrentTextColor()==pantone) {
                        confirmPasswordText.setTextColor(Color.BLACK);
                        confirmPassword.setBackground(defaultInput);
                    }
                }else{
                    confirmPasswordText.setTextColor(pantone);
                    confirmPassword.setBackground(wrongInput);
                }
            }
        });
        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                isValidPhoneNumber = Pattern.matches("^\\d{10}$",phoneNumber.getText().toString());
                if (isValidPhoneNumber){
                    if (phoneNumberText.getCurrentTextColor()==pantone) {
                        phoneNumberText.setTextColor(Color.BLACK);
                        phoneNumber.setBackground(defaultInput);
                    }
                }else{
                    phoneNumberText.setTextColor(pantone);
                    phoneNumber.setBackground(wrongInput);
                }
            }
        });
    }
    private void updateUserUI(FirebaseUser currentUser) {
        // if not connected then "currentUser" is equal to null
        if(currentUser != null)
        { // signed in.
            // Switch to user's panel activity.
            Observable<User> o = Firebase.getUserData(currentUser.getUid());
            o.subscribe(new ObserverFirebaseTemplate<User>() {
                @Override
                public void onNext(User dbUser) {
                    Log.i("DATA", "onNext: "+dbUser);

                    Intent intent = null;
                    switch (dbUser.getUserType()) {
                        case Customer:
                            intent = new Intent(getApplicationContext(), UserPanelActivity.class);
                            break;
                        case Worker:
                            intent = new Intent(getApplicationContext(), WorkerPanelActivity.class);
                            break;
                    }
                    progressBar.setVisibility(View.GONE);
                    startActivity(intent);
                    finish();
                }
            });
        }
        else
        { // not signed in.
            // TODO
        }

    }

    private void createNewAccountFirebase(String email, String password,String name,String phoneNumber,int userType)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Firebase", "createUserWithEmail:success");
                            progressBar.setVisibility(View.VISIBLE);
                            user = mAuth.getCurrentUser();
                            userData = new User(name,phoneNumber,userType);
                            Firebase.setUserData(user.getUid(),userData);
                            updateUserUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Firebase", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUserUI(null);
                        }

                        // ...
                    }
                });
    }
    public void Register(View view){
        if (isValidEmail&&isValidName&&isValidPassword&&isValidConfirmPassword&&isValidPhoneNumber) {
            createNewAccountFirebase(this.email.getText().toString(),
                    this.password.getText().toString(), this.name.getText().toString()
                    , this.phoneNumber.getText().toString(), this.spinner.getSelectedItemPosition());
            Toast.makeText(getApplicationContext(), "you have been registered.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "can not register until all input fields are valid.", Toast.LENGTH_SHORT).show();
        }
    }
}

package com.example.sujit.utkal_hacks;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.sujit.utkal_hacks.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mRef, mRef1;


    TextInputLayout mEmailTextInput, mPasswordTextInput, mUsernameTextInput;

    EditText mEmailEditText, mPasswordEditText, mUsernameEditText;

    ProgressBar mProgressBar;
    Button signUpButton;

    String mEmail, mPassword, mUsername;

    Map userMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth = FirebaseAuth.getInstance();

        userMap = new HashMap<String,Object>();

        mEmailTextInput = findViewById(R.id.emailTextInputLayout);
        mPasswordTextInput = findViewById(R.id.passwordTextInputLayout);
        mUsernameTextInput = findViewById(R.id.usernameTextInputLayout);

        mEmailEditText = findViewById(R.id.emailEditText);
        mPasswordEditText = findViewById(R.id.passwordEditText);
        mUsernameEditText = findViewById(R.id.usernameEditText);


        mProgressBar = findViewById(R.id.progressBar);

        signUpButton = findViewById(R.id.signUpRegisterButton);



        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpButton.setClickable(false);


                mUsername = mUsernameEditText.getText().toString();
                mEmail = mEmailEditText.getText().toString();
                mPassword = mPasswordEditText.getText().toString();

                if (!TextUtils.isEmpty(mUsername) && !TextUtils.isEmpty(mEmail) && !TextUtils.isEmpty(mPassword)) {

                 register();


                } else {
                    if (TextUtils.isEmpty(mUsername))
                        mUsernameTextInput.setError("Username is required !");
                    else if (TextUtils.isEmpty(mEmail))
                        mEmailTextInput.setError("Email is required !");
                    else if (TextUtils.isEmpty(mPassword))
                        mPasswordTextInput.setError("Password is required !");
                    else {

                    }
                }


            }
        });

    }


    public void register() {

        mProgressBar.setVisibility(View.VISIBLE);

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);


        mAuth.createUserWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    String uid = currentUser.getUid();

                    mRef = FirebaseDatabase.getInstance().getReference().child("admin");
                   // mRef1 = FirebaseDatabase.getInstance().getReference().child("uid").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    mRef.keepSynced(true);

                    userMap = new HashMap<>();
                    userMap.put("name", mUsername);
                    userMap.put("email", mEmail);
                    userMap.put("password", mPassword);
                    userMap.put("uid",uid);

                    mRef.setValue(userMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                mProgressBar.setVisibility(View.INVISIBLE);

                                Intent intent;
                                intent = new Intent(RegisterActivity.this, HomeScreenActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(intent);
                                finish();

                            } else {
                                mProgressBar.setVisibility(View.INVISIBLE);
                                signUpButton.setClickable(true);
                            }

                        }
                    });

                } else {
                    signUpButton.setClickable(true);
                }


            }
        });
    }



}

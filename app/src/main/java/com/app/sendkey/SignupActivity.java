package com.app.sendkey;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnSignUp, btnSignUpUser, btnSignUpOwner;
    private boolean bool_user = true;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mDatabaseRef, userChild;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabaseRef = mFirebaseInstance.getReference("users");
//        mOwnerRef = mFirebaseInstance.getReference("owners");

        btnSignUpUser = (Button) findViewById(R.id.btn_signUp_user);
        btnSignUpOwner = (Button)findViewById(R.id.btn_signUp_owner);
        btnSignUp = (Button)findViewById(R.id.sign_up_button);
        btnSignIn = (Button)findViewById(R.id.sign_in_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnSignUpUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bool_user = true;
                //disable owner button
                btnSignUpOwner.setBackgroundColor(getResources().getColor(R.color.colorDefaultBackground));
                btnSignUpOwner.setTextColor(getResources().getColor(R.color.colorAccent));

                //enable user button
                btnSignUpUser.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                btnSignUpUser.setTextColor(getResources().getColor(R.color.colorBlack));

            }
        });

        btnSignUpOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bool_user = false;

                //disable user button
                btnSignUpUser.setBackgroundColor(getResources().getColor(R.color.colorDefaultBackground));
                btnSignUpUser.setTextColor(getResources().getColor(R.color.colorAccent));

                //enable owner button
                btnSignUpOwner.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                btnSignUpOwner.setTextColor(getResources().getColor(R.color.colorBlack));
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                methodSignUp();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    private void methodSignUp() {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //create user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = currentUser.getUid();

                            User user;
                            if(bool_user) {
                                user = new User(currentUser.getEmail(), "user", currentUser.getUid());
                            }
                            else {
                                user = new User(currentUser.getEmail(), "owner", currentUser.getUid());
                            }

                            userChild = mDatabaseRef.child(uid);
                            userChild.setValue(user);

                            if(user.user_type.equals("owner")) {
                                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Intent intent = new Intent(SignupActivity.this, UserActivity.class);
                                startActivity(intent);
                            }

                            finish();
                        }
                    }
                });

    }

}

package com.app.sendkey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    FirebaseUser user;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseRef, messageRef, singleMessageRef;
    private EditText mMessageEditText;
    private Button mSendButton;
    private TextView nameTextView, messageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final String user_selected = getIntent().getExtras().getString("user_selected");
        final String email_user_selected = getIntent().getExtras().getString("email_user_selected");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mFirebaseDatabase.getReference("messages");
        messageRef = mDatabaseRef.child("(" + user_selected + ")" + "to" + "(" + user.getUid() + ")");
        singleMessageRef = messageRef.child("text");

        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);
        //mMessageListView = (ListView)findViewById(R.id.messageListView);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        messageTextView = (TextView) findViewById(R.id.messageTextView);

        messageRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String message = dataSnapshot.getValue(String.class);
                nameTextView.setText(email_user_selected);
                messageTextView.setText(message);
                Log.v("ChatActivity", messageTextView.getText().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String message = dataSnapshot.getValue(String.class);
                nameTextView.setText(email_user_selected);
                messageTextView.setText(message);
                Log.v("ChatActivity", messageTextView.getText().toString());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

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
    FirebaseUser owner;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseRef, messageRef, singleMessageRef;
    private EditText mMessageEditText;
    private Button mSendButton;
//    private MessageAdapter mMessageAdapter;
//    private ListView mMessageListView;
    private TextView nameTextView, messageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        String user_selected = getIntent().getExtras().getString("user_selected");
        auth = FirebaseAuth.getInstance();
        owner = auth.getCurrentUser();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mFirebaseDatabase.getReference("messages");
        messageRef = mDatabaseRef.child("(" + owner.getUid() + ")" + "to" + "(" + user_selected + ")");
        singleMessageRef = messageRef.child("text");

        mMessageEditText = (EditText)findViewById(R.id.messageEditText);
        mSendButton = (Button)findViewById(R.id.sendButton);
        //mMessageListView = (ListView)findViewById(R.id.messageListView);
        nameTextView = (TextView)findViewById(R.id.nameTextView);
        messageTextView = (TextView)findViewById(R.id.messageTextView);

//        // Initialize message ListView and its adapter
//        List<MessageModel> messages = new ArrayList<>();
//        mMessageAdapter = new MessageAdapter(this, R.layout.item_message, messages);
//        mMessageListView.setAdapter(mMessageAdapter);

        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MessageModel message = new MessageModel(mMessageEditText.getText().toString(), owner.getEmail());
                String message = mMessageEditText.getText().toString();
                singleMessageRef.setValue(message);

                // Clear input box
                mMessageEditText.setText("");
            }


        });

        messageRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String message = dataSnapshot.getValue(String.class);
                nameTextView.setText(owner.getEmail());
                messageTextView.setText(message);
                Log.v("ChatActivity", messageTextView.getText().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String message = dataSnapshot.getValue(String.class);
                nameTextView.setText(owner.getEmail());
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

    //sign out method
    public void signOut() {
        auth.signOut();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                signOut();
                startActivity(new Intent(ChatActivity.this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

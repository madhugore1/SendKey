package com.app.sendkey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserReference;
    public ArrayList<User> users;
    public ListView usersListView;
    UserListAdapter userListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        users = new ArrayList<User>();
        usersListView = (ListView) findViewById(R.id.users_list);

        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String user_selected = users.get(position).uid;
                String email_user_selected = users.get(position).email;
                Intent chatIntent = new Intent(UserActivity.this, UserChatScreenActivity.class);
                chatIntent.putExtra("user_selected", user_selected);
                chatIntent.putExtra("email_user_selected", email_user_selected);
                startActivity(chatIntent);

            }
        });

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserReference = mFirebaseDatabase.getReference("users");

        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    //                tickets.add(ticket_details);
                    //Log.v("History ", "ticket_details : source : " + ticket_details.getSource());
                    if(user.user_type.equals("owner")) {
                        users.add(user);
                    }
                    //Log.v("MainActivity", user.email + " " + user.user_type);

                }

                userListAdapter = new UserListAdapter(UserActivity.this, R.layout.users_list_container, users);
                usersListView.setAdapter(userListAdapter);
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

//    @Override
//    public void onBackPressed()
//    {
//        super.onBackPressed();
//        finish();
//
//    }

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
                startActivity(new Intent(UserActivity.this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

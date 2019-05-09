package com.example.mohamed.capstone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity implements UserAdapter.OnClick{

    private static final String TAG ="UsersActivity" ;
    UserAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<User> users;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        users=new ArrayList<>();
        recyclerView=(RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
       // adapter=new UserAdapter(this,users,this);

        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        final FirebaseUser user=firebaseAuth.getCurrentUser();
        databaseReference=firebaseDatabase.getReference().child("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v(TAG,"dataCount"+dataSnapshot.getValue().toString());

                if(dataSnapshot .exists()){
                   for(DataSnapshot xData:dataSnapshot.getChildren()){
                        Log.v(TAG,"dataSnap"+xData.getValue().toString());
                        Log.v(TAG,"dataCount"+dataSnapshot.getChildrenCount());
                        String name=xData.child("name").getValue().toString();
                       Log.v(TAG,"stringValue"+name);
                        String image=xData.child("image").getValue().toString();
                        String status=xData.child("status").getValue().toString();
                        users=new ArrayList<>();
                        User user=new User(name,image,status);
                        users.add(user);
                       //Log.v(TAG,"userValue"+users.get(1));
                       Log.v(TAG,"userValue"+users.size());

                    }
                    adapter=new UserAdapter(UsersActivity.this,users,UsersActivity.this);
                    recyclerView.setAdapter(adapter);
                    Log.v(TAG,"userValue"+users.size());


                    Log.v(TAG,"userValue"+users.size());
                }

                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    @Override
    public void getPosition(int position) {

    }
}

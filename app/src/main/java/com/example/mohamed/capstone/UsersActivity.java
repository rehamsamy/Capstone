package com.example.mohamed.capstone;

import android.os.AsyncTask;
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
   public ArrayList<User> users;
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
        recyclerView.setHasFixedSize(true);
       // adapter=new UserAdapter(this,users,this);

        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        final FirebaseUser user=firebaseAuth.getCurrentUser();
        databaseReference=firebaseDatabase.getReference().child("users");

        new Task().execute();

    }



    @Override
    public void getPosition(int position) {

    }




    class Task extends AsyncTask<Void,Void,ArrayList<User>>{

        @Override
        protected ArrayList<User> doInBackground(Void... voids) {

           ArrayList<User> userArrayList= getData();
           Log.v(TAG,"sssssssssss"+userArrayList.size());
            return userArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<User> users) {
            super.onPostExecute(users);

            adapter=new UserAdapter(UsersActivity.this,users,UsersActivity.this);
                    recyclerView.setAdapter(adapter);
                    Log.v(TAG,"postValue"+users.size());

        }
    }


   ArrayList<User> getData(){

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

                        User user=new User(name,image,status);
                       UsersActivity.this.users.add(user);
                        //Log.v(TAG,"userValue"+users.get(1));
                        Log.v(TAG,"userValue"+users.size());

                    }
//                    adapter=new UserAdapter(UsersActivity.this,users,UsersActivity.this);
//                    recyclerView.setAdapter(adapter);
//                    Log.v(TAG,"userValue"+users.size());


                    Log.v(TAG,"userValue"+users.size());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       Log.v(TAG,"uuuuuuuuuu"+users.size());
       return UsersActivity.this.users;

    }
}

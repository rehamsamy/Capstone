package com.example.mohamed.capstone;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity  {

    FirebaseAuth firebaseAuth;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter adapter;



    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth=FirebaseAuth.getInstance();


        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();

       toolbar=(Toolbar) findViewById(R.id.main_page_toolbar);
       setSupportActionBar(toolbar);
       getSupportActionBar().setTitle("R2M CHAT");

       tabLayout=(TabLayout) findViewById(R.id.tab_layout);
       viewPager=(ViewPager) findViewById(R.id.view_pager);

       adapter=new PagerAdapter(getSupportFragmentManager());
       viewPager.setAdapter(adapter);
       tabLayout.setupWithViewPager(viewPager);













    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user==null){
            startActivity(new Intent(this,StartActivity.class));
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
         int id=item.getItemId();

         switch (id){
             case R.id.edit_setting:
                 startActivity(new Intent(this,SettingsActivity.class));
                 break;
             case R.id.all_users:
                 startActivity(new Intent(this,UsersActivity.class));
                 break;
             case R.id.logout:
                 FirebaseAuth.getInstance().signOut();
                 startActivity(new Intent(MainActivity.this,StartActivity.class));
                 finish();
                 break;
         }
        return true;
    }


    public void enter(View view) {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"choose"),1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      if(requestCode==1&&requestCode==RESULT_OK){

                      Uri uri=data.getData();
            Log.v("MainActivity","vvvvvvvv"+uri.toString());

            storageReference=storageReference.child("profile_image").child("image");

            storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Toast.makeText(MainActivity.this,"sucess",Toast.LENGTH_LONG).show();
                }
            });



      }
    }
}

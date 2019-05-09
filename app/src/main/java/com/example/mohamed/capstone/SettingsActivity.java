package com.example.mohamed.capstone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingActivity";
    private static final int PICK_IMAGE =1 ;
    Button statusChange,imageChange;
    TextView statusText,profileName;
    CircleImageView imageView;
    ProgressDialog progressDialog;


    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        statusChange=(Button) findViewById(R.id.status_change_button);
        imageChange=(Button) findViewById(R.id.image_change_button);
        statusText=(TextView) findViewById(R.id.status_text);
        profileName=(TextView) findViewById(R.id.profile_name);
        imageView=(CircleImageView) findViewById(R.id.profile_image);
        progressDialog=new ProgressDialog(this);

        firebaseDatabase=FirebaseDatabase.getInstance();

        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference().child("profile");

        firebaseAuth=FirebaseAuth.getInstance();

        FirebaseUser user=firebaseAuth.getCurrentUser();
        String uid=user.getUid();
        databaseReference=firebaseDatabase.getReference().child("users").child(uid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name=dataSnapshot.child("name").getValue().toString();
                String status=dataSnapshot.child("status").getValue().toString();
                String image=dataSnapshot.child("image").getValue().toString();


                statusText.setText(status);
                profileName.setText(name);
                Picasso.get().load(image).into(imageView);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        imageChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"choose image"),PICK_IMAGE);

            }
        });


        statusChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(SettingsActivity.this,StatusActivity.class);
                String newStatus=statusText.getText().toString();
                intent.putExtra("status",newStatus);
                startActivity(intent);
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v(TAG,"rrrrr"+data.getData().toString());

        if(requestCode==PICK_IMAGE&&resultCode==RESULT_OK){
            progressDialog.show();
            progressDialog.setTitle("choose image");
            progressDialog.setMessage("please wait until finishing");
            progressDialog.setCanceledOnTouchOutside(false);

            FirebaseUser user=firebaseAuth.getCurrentUser();
            String uid=user.getUid();

            final Uri image=data.getData();
            Log.v(TAG,"rrrrr"+image.toString());
           StorageReference path= storageReference.child(uid+".jpeg");
            path.putFile(image).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                           Toast.makeText(SettingsActivity.this,"sucess",Toast.LENGTH_LONG).show();

                           String url=task.getResult().getDownloadUrl().toString();
                           addImageToDatabase(url);

                    }
                    else{
                        Toast.makeText(SettingsActivity.this,"failed",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
            });



       }
       else {
            Toast.makeText(SettingsActivity.this,"failed",Toast.LENGTH_LONG).show();
        }
    }

    private void addImageToDatabase(final String image) {
        databaseReference.child("image").setValue(image).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SettingsActivity.this,"sucess",Toast.LENGTH_LONG).show();

                   // Picasso.get().load(image.get)
                }else{
                    Toast.makeText(SettingsActivity.this,"failed",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}


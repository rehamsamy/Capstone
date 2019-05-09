package com.example.mohamed.capstone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    Button statusButton;
    TextInputLayout statusText;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Toolbar toolbar;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        statusButton=(Button) findViewById(R.id.status_button);
        statusText=(TextInputLayout) findViewById(R.id.status_text);
        progressDialog=new ProgressDialog(this);
        toolbar=(Toolbar) findViewById(R.id.status_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Status Change");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();


        Intent intent=getIntent();
        String newStatus=intent.getStringExtra("status");
        statusText.getEditText().setText(newStatus);



        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newStatus=statusText.getEditText().getText().toString();

                progressDialog.setTitle("change status");
                progressDialog.setMessage("Please wail until finishing");
                progressDialog.show();
                changeStatus(newStatus);

            }
        });


    }

    private void changeStatus(String newStatus) {
        FirebaseUser user=firebaseAuth.getCurrentUser();
        String uid=user.getUid();
        databaseReference=firebaseDatabase.getReference().child("users").child(uid);

        databaseReference.child("status").setValue(newStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"change status sucess",Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(),"change status failed",Toast.LENGTH_LONG).show();


                }

            }
        });


    }
}

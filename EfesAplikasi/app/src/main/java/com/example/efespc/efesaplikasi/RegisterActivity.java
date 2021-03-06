package com.example.efespc.efesaplikasi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText mNameField;
    private EditText mEmailField;
    private EditText mPasswordField;
    private ProgressDialog progressDialog ;
    private Button mRegisterbtn;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mProgress = new ProgressDialog(this);

        mNameField = (EditText)findViewById(R.id.editText);
        mEmailField = (EditText) findViewById(R.id.editText2);
        mPasswordField = (EditText) findViewById(R.id.editText3);
        mRegisterbtn = (Button) findViewById(R.id.button);

        mRegisterbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view){
                startRegister();
            }
        });
    }

    private void startRegister() {
        final String name = mNameField.getText().toString().trim();
        String email = mEmailField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();

        if (TextUtils.isEmpty(name)){
            //email is empty
            Toast.makeText(this, "Masukkan Nama", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;


        }
        if (TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Masukkan Email", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;


        }
        if (TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this, "Masukkan Password", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }
        progressDialog.setMessage("Registrasi Akun");
        progressDialog.show();

        if (!TextUtils.isEmpty(name)&& ! TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference cureent_user_db = mDatabase.child(user_id);
                        cureent_user_db.child("name").setValue(name);
                        cureent_user_db.child("image").setValue("default");
                        mProgress.dismiss();

                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    }
                }
            });
        }
    }
}

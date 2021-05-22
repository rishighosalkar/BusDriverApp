package com.example.busdriverapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;

    private TextView textViewSignup, textViewResetPassword;
    private EditText editTextEmail, editTextPassword;
    private Button buttonSignin;

    SharedPreferences sharedPreferences;
    String username;

    private ProgressDialog progressDialog;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Intent intent = getIntent();
        if(sharedPreferences != null)
        {
            username = sharedPreferences.getString("username", null);
        }

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        textViewSignup = (TextView) findViewById(R.id.textViewSignup);
        textViewResetPassword = (TextView) findViewById(R.id.textViewResetPassword);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignin = (Button) findViewById(R.id.buttonSignin);

        progressDialog = new ProgressDialog(this);

        // Set Listeners
        textViewSignup.setOnClickListener(this);
        textViewResetPassword.setOnClickListener(this);
        buttonSignin.setOnClickListener(this);
    }

    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        // If email is empty, return
        if (TextUtils.isEmpty(email)){//change email to username if the code is changed
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        // If email is empty, return
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        DatabaseReference databaseReference  = FirebaseDatabase.getInstance().getReference("BusDriver");
        progressDialog.setMessage("Logging Please Wait...");
        progressDialog.show();

        //Query checkUser = databaseReference.orderByChild("username").equalTo(username);

        /*checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){


                    String passwordDB = snapshot.child(username).child(password).getValue(String.class);
                    if(passwordDB.equals(password)){
                        finish();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                    else{
                        editTextPassword.setError("Wrong password");
                        editTextPassword.requestFocus();
                    }
                }
                else{
                    editTextEmail.setError("Invalid Email");
                    editTextEmail.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        // Sign in with email and password
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            // If email is not verified, verify
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (!user.isEmailVerified()){
                                Toast.makeText(LoginActivity.this, "Please Verify email.",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                // start main activity
                                finish();
                                sharedPreferences.edit().putString("username", username).commit();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        }
                        else {
                            // Failed to log in
                            Toast.makeText(LoginActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onClick(View view){
        if (view == buttonSignin){
            userLogin();
        }
        else if (view == textViewSignup){
            finish();
            startActivity(new Intent(this, RegisterActivity.class ));
        }
        else if (view == textViewResetPassword){
            // Reset password through email
            firebaseAuth.getInstance().sendPasswordResetEmail("frodo1642@gmail.com.com")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Email Sent", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}
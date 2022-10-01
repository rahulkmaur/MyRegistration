package com.example.myregistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myregistration.models.Email;
import com.example.myregistration.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {


    TextView alreadyHaveAccount;
    EditText inputUsername, inputEmail, inputContact, inputPassword, inputConfirmPassword;
    Button btnRegister;

    private FirebaseAuth mAuth;

    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        alreadyHaveAccount=findViewById(R.id.alreadyHaveAccount);
        inputUsername=findViewById(R.id.inputUsername);
        inputEmail=findViewById(R.id.inputEmail);
        inputContact=findViewById(R.id.inputContact);
        inputPassword=findViewById(R.id.inputPass);
        inputConfirmPassword=findViewById(R.id.inputConformPassword);
        mAuth = FirebaseAuth.getInstance();

        btnRegister=findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=inputUsername.getText().toString().trim();
                String email=inputEmail.getText().toString().trim();
                String contact=inputContact.getText().toString().trim();
                String password=inputPassword.getText().toString().trim();
                String cpassword=inputConfirmPassword.getText().toString().trim();


                if (!TextUtils.isEmpty(name)){
                    if(!TextUtils.isEmpty(email)){


                        if(!TextUtils.isEmpty(contact)){

                            if(!TextUtils.isEmpty(password)){

                                if (password.matches(cpassword)){

                                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            if (task.isSuccessful()){
                                                database = FirebaseDatabase.getInstance();
                                                myRef = database.getReference("users").child(mAuth.getCurrentUser().getUid());

                                                User u = new User();
                                                u.setName(name);
                                                u.setEmail(email);
                                                u.setContact(contact);

                                                myRef = database.getReference("emails").child(mAuth.getCurrentUser().getUid());

                                                Email e = new Email();
                                                e.setName(name);
                                                e.setEmail(email);
                                                e.setContact(contact);


                                                myRef.setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {

                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(RegisterActivity.this, "Success"+task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(RegisterActivity.this,HomeActivity.class));

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(RegisterActivity.this, "Error"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                                    }
                                                });

                                            }else{
                                                Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterActivity.this, "Error : "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                }else{

                                    Toast.makeText(RegisterActivity.this, "Password Dosen't Matches", Toast.LENGTH_SHORT).show();

                                }
                            }else
                            {
                                Toast.makeText(RegisterActivity.this, "Password Required", Toast.LENGTH_SHORT).show();
                            }
                        }else
                        {

                            Toast.makeText(RegisterActivity.this, "Contact Required", Toast.LENGTH_SHORT).show();

                        }

                    }else
                    {

                        Toast.makeText(RegisterActivity.this, "Email Required", Toast.LENGTH_SHORT).show();

                    }

                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Username Required", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alreadyHaveAccount=findViewById(R.id.alreadyHaveAccount);
        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
}
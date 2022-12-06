package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.FRAGMENT.FragmentUserProfile;
import com.example.myapplication.MODEL.Admin;
import com.example.myapplication.MODEL.NhanVien;
import com.example.myapplication.MODEL.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

EditText ed_user, ed_pass;
Button btn_login, btn_dangky;
private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    TextView btn_forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        anhxaview();
        mAuth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String username = ed_user.getText().toString().trim();
                String passWord = ed_pass.getText().toString().trim();
                clickLogin(username, passWord);

            }
        });
        btn_dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterrActivity.class));
            }
        });
        btn_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LoginActivity.this, ForgotPassWordActivity.class));
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            finish();
           startActivity(new Intent(this, MainActivity.class));
        }
    }
    private void clickLogin(String email, String passWord) {
        mAuth.signInWithEmailAndPassword(email, passWord)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        finish();
                                        startActivity(intent);
                                        Toast.makeText(LoginActivity.this, "login success!", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Login faild", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    private void anhxaview(){
        btn_forgot=findViewById(R.id.forgot);
        progressDialog = new ProgressDialog(this);
        ed_user = findViewById(R.id.ed_username);
        ed_pass = findViewById(R.id.ed_password);
        btn_login = findViewById(R.id.btn_login);
        btn_dangky = findViewById(R.id.btn_dangky);
    }
//    private void forGotPassWord(String email) {
//        progressDialog.show();
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        auth.sendPasswordResetEmail(email)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        progressDialog.dismiss();
//                        if (task.isSuccessful()) {
//                            Toast.makeText(LoginActivity.this, "EMAIL sent", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                });
//    }




}
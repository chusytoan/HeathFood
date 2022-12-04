package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassWordActivity extends AppCompatActivity {
EditText ed_mail;
Button btn_send;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_word);
        anhXaView();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = ed_mail.getText().toString().trim();
                forGotPassWord(email);
            }
        });
    }

    private void anhXaView() {
        ed_mail = findViewById(R.id.ed_mail_forgot);
        btn_send = findViewById(R.id.btn_send);
        progressDialog = new ProgressDialog(this);
    }
    private void forGotPassWord(String email) {
        progressDialog.show();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPassWordActivity.this, "EMAIL sent", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

}
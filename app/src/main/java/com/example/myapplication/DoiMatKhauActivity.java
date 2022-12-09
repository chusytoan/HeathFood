package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DoiMatKhauActivity extends AppCompatActivity {
    EditText ed_password;
    Button btn_changepass;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_doi_mat_khau);
        anhxaView();
        btn_changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickChangePassword();
            }
        });
    }
    private void anhxaView() {
        ed_password = findViewById(R.id.ed_changepass);
        btn_changepass = findViewById(R.id.btn_changepass);
        progressDialog = new ProgressDialog(this);
    }
    private void onClickChangePassword() {
        progressDialog.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String newPassword = ed_password.getText().toString();

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(DoiMatKhauActivity.this, "Thay doi mat khau thanh cong", Toast.LENGTH_SHORT).show();
                        } else {
                            //show dialog goi ham reAuthenticate
                            Dialog dialog = new Dialog(DoiMatKhauActivity.this);
                            dialog.setContentView(R.layout.xacthucnguoidung);
                            Window window = dialog.getWindow();
                            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                            EditText ed_mail = dialog.findViewById(R.id.ed_mail);
                            EditText passWord = dialog.findViewById(R.id.ed_passwordxacthuc);
                            Button btn_ok = dialog.findViewById(R.id.btn_xacthuc);
                            dialog.show();
                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String m = ed_mail.getText().toString();
                                    String p = passWord.getText().toString();
                                    reAuthenticate(m, p);
                                    dialog.dismiss();
                                }
                            });


                        }
                    }
                });
    }
    private void reAuthenticate(String email, String password){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            onClickChangePassword();
                        }else {
                            Toast.makeText(DoiMatKhauActivity.this, "vui long xac nhan lai email va password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
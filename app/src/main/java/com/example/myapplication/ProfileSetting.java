package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.MODEL.KhachHang;
import com.example.myapplication.MODEL.Token;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifImageView;

public class ProfileSetting extends AppCompatActivity {

    DatabaseReference referencekhs;
    private View view;

    EditText ed_address, ed_phone;
    Button btn_update;
    ImageView avt_update;
    String TAG  ="KHDSD";

    String mVerificationId;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser userCurrent;
    FirebaseFirestore db;
    ProgressDialog progressDialog;



    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri img_uri;
    private StorageTask uploadtalk;
    private String muri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        anhXa();
        db= FirebaseFirestore.getInstance();
        userCurrent = mAuth.getCurrentUser();
        referencekhs = FirebaseDatabase.getInstance().getReference("KhachHangs");
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // progressDialog.show();
                String addressss = ed_address.getText().toString();
                String phonee = ed_phone.getText().toString();
                if(userCurrent==null){
                    progressDialog.dismiss();
                    startActivity(new Intent(ProfileSetting.this, LoginActivity.class));
                    return;
                }
                if(addressss.equals("")){
                    progressDialog.dismiss();
                    Toast.makeText(ProfileSetting.this, "khong duoc de trong dia chi", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(phonee.equals("")){
                    progressDialog.dismiss();
                    Toast.makeText(ProfileSetting.this, "khong dc de trong sdt", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!phonee.matches("^[+84]{1}+\\d{11,12}")){
                    progressDialog.dismiss();
                    ed_phone.setError("Vui lòng nhập đúng định dạng số điện thoại");
                    return;
                }
                FirebaseAuth mAuth = FirebaseAuth.getInstance();


                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber(phonee)       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(ProfileSetting.this)                 // Activity (for callback binding)
                                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(PhoneAuthCredential credential) {

                                        Dialog dialog = new Dialog(ProfileSetting.this);
                                        dialog.setContentView(R.layout.layout_update_profile);
                                        dialog.setTitle("Enter otp");
                                        Window window = dialog.getWindow();
                                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT );
                                        EditText ed_otp = dialog.findViewById(R.id.ed_otp);
                                        Button btn_check = dialog.findViewById(R.id.btn_check);
                                        dialog.show();
                                        btn_check.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                String Otp = ed_otp.getText().toString();
                                                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, Otp);
                                                userCurrent.updatePhoneNumber(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            updateProfile();
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(ProfileSetting.this, "cap nhat thong tin that bai", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                dialog.dismiss();
                                            }
                                        });

                                    }

                                    @Override
                                    public void onVerificationFailed(FirebaseException e) {
                                        Toast.makeText(ProfileSetting.this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        return;
                                        // Show a message and update the UI
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String verificationId,
                                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                        mVerificationId = verificationId;


                                    }
                                })          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);


            }
        });
        avt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImg();
            }
        });

    }  private void anhXa(){
        ed_address = findViewById(R.id.ed_adress);
        ed_phone = findViewById(R.id.ed_phone);
        btn_update = findViewById(R.id.btn_update);
        avt_update =findViewById(R.id.avt_update);

        progressDialog = new ProgressDialog(ProfileSetting.this);
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
    }

    private void updateProfile() {

        Map<String, Object> map = new HashMap<>();
        map.put("diachi", ed_address.getText().toString());
        map.put("sdt", ed_phone.getText().toString());
        if(muri==null){
            Log.d(TAG, "updateProfile: "+muri);
            map.put("imgURL", "default");
        }else{
            map.put("imgURL", muri);
        }

        referencekhs.child(userCurrent.getUid()).updateChildren(map);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference tokens = database.getReference("Tokens");
                        Token token_Model = new Token();
                        token_Model.setToken(token);
                        token_Model.setServerToken(false);
                        tokens.child(ed_phone.getText().toString()).setValue(token_Model);
                        Toast.makeText(ProfileSetting.this, "cap nhat thong tin thanh cong", Toast.LENGTH_SHORT).show();
                    }
                });



        return;

    }
    private void openImg() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = ProfileSetting.this.getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        final ProgressDialog dialog = new ProgressDialog(ProfileSetting.this);
        dialog.setMessage("Uploading");
        dialog.show();
        if (img_uri != null) {
            final StorageReference storage = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(img_uri));
            uploadtalk = storage.putFile(img_uri);



            uploadtalk.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return storage.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Uri dowloaduri = (Uri) task.getResult();
                        muri = dowloaduri.toString();
                        Log.d("CLMM", "clmm: " + muri);
                        Glide.with(ProfileSetting.this).load(muri).into(avt_update);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(ProfileSetting.this, "failed!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileSetting.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        } else {

            Toast.makeText(ProfileSetting.this, "no image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            img_uri = data.getData();
            if (uploadtalk != null && uploadtalk.isInProgress()) {
                Toast.makeText(ProfileSetting.this, "upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }

        }
    }
}
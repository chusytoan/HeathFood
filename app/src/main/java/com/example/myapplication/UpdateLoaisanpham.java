package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.ADAPTER.SpinnerLoaiSanPhamAdapter;
import com.example.myapplication.MODEL.Loaisanpham;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.List;

public class UpdateLoaisanpham extends AppCompatActivity {
    TextInputLayout ed_nameLoai;
    TextInputLayout tv_maLoai;
    Button btn_updateLoai;
    ImageView img_LoaisanphamUp,btn_upload;

    ProgressDialog progressDialog;

    //store
    StorageReference storageReference;
    private static final int IMAGE_REQUEST =1;
    private Uri img_uri;
    private StorageTask uploadtalk;
    private String Loaimuri;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_loai_san_pham);
        anhXaView();
        Intent intent = getIntent();
        db= FirebaseFirestore.getInstance();
        String maLoai = intent.getStringExtra("maLoai");
        String tenLoai = intent.getStringExtra("tenLoai");
        Loaimuri = intent.getStringExtra("hinhanhLoai");


        Glide.with(this).load(Loaimuri).into(img_LoaisanphamUp);
        ed_nameLoai.getEditText().setText(tenLoai);
        tv_maLoai.getEditText().setText(maLoai);




        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImg();
            }
        });

        btn_updateLoai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String nameLoaiSP_up = ed_nameLoai.getEditText().getText().toString();

                db.collection("LoaiSanPhams").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Loaisanpham lsp = document.toObject(Loaisanpham.class);
                                if(lsp.getMaLoai().equals(maLoai)){
                                    db.collection("LoaiSanPhams").document(document.getId())
                                            .update("name",nameLoaiSP_up,
                                                    "imgURL",Loaimuri)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progressDialog.dismiss();
                                                    if (task.isSuccessful()){

                                                        Toast.makeText(getBaseContext(), "Update Loại sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                                        onBackPressed();
                                                    }

                                                }
                                            });
                                }

                            }
                        }
                    }
                });


            }
        });


        }

    private void anhXaView() {
        progressDialog = new ProgressDialog(this);
        ed_nameLoai = findViewById(R.id.ed_nameLoai);
        tv_maLoai = findViewById(R.id.tv_maLoai);
        img_LoaisanphamUp=findViewById(R.id.img_LoaisanphamUP);
        btn_upload=findViewById(R.id.btn_uploadLoai);
        btn_updateLoai=findViewById(R.id.btn_updateLoai);
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
    }
    private void openImg() {
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getBaseContext().getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImage(){
        if(img_uri!=null){
            final StorageReference storage = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(img_uri)) ;
            uploadtalk = storage.putFile(img_uri);
            uploadtalk.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return storage.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Uri dowloaduri = (Uri) task.getResult();
                        Loaimuri = dowloaduri.toString();
                        Glide.with(UpdateLoaisanpham.this).load(Loaimuri).into(img_LoaisanphamUp);
                    }
                    else {
                        Toast.makeText(getBaseContext(), "failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {

            Toast.makeText(getBaseContext(), "no image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_REQUEST && resultCode==RESULT_OK && data != null && data.getData() != null){
            img_uri = data.getData();
            if(uploadtalk!=null && uploadtalk.isInProgress()){
                Toast.makeText(getBaseContext(), "upload in progress", Toast.LENGTH_SHORT).show();
            }else {
                uploadImage();
            }

        }
    }
}
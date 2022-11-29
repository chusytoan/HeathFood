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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.ADAPTER.SpinnerLoaiSanPhamAdapter;
import com.example.myapplication.MODEL.Loaisanpham;
import com.example.myapplication.MODEL.Sanpham;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThemSanPhamActivity extends AppCompatActivity {
    ImageView btn_upload,img_sp;

    TextInputLayout ed_ten,ed_gia, ed_masp, ed_time, ed_mo_ta;
    ProgressDialog progressDialog;
    Button btn_add;
    Spinner spinnerLoaisp;
    List<Loaisanpham> loaisanphams;
    SpinnerLoaiSanPhamAdapter spinnerLoaiSanPhamAdapter;
    private View view;
    //firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    //store
    StorageReference storageReference;
    private static final int IMAGE_REQUEST =1;
    private Uri img_uri;
    private StorageTask uploadtalk;
    private String muri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.them_san_pham);

        anhXaView();
        db.collection("LoaiSanPhams").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        Loaisanpham lsp = document.toObject(Loaisanpham.class);
                        loaisanphams.add(lsp);
                    }
                    spinnerLoaiSanPhamAdapter = new SpinnerLoaiSanPhamAdapter(ThemSanPhamActivity.this, loaisanphams);
                    spinnerLoaisp.setAdapter(spinnerLoaiSanPhamAdapter);
                }
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();

                String masp = ed_masp.getEditText().getText().toString();
                String name = ed_ten.getEditText().getText().toString();
                double price = Double.parseDouble(ed_gia.getEditText().getText().toString());
                String describe = ed_mo_ta.getEditText().getText().toString();
                int time_ship = Integer.parseInt(ed_time.getEditText().getText().toString());

                Loaisanpham lsp = (Loaisanpham) spinnerLoaisp.getSelectedItem();
                Map<String, Sanpham> map  = new HashMap<>();

                map.put(masp, new Sanpham(lsp.getMaLoai(),masp, name, price, time_ship, describe, 0,false, 0, muri,lsp.getName(),null,3));
                Loaisanpham lspnew = new Loaisanpham(lsp.getMaLoai(),lsp.getName(),lsp.getImgURL(), map);

                db.collection("LoaiSanPhams").document(lsp.getMaLoai()).set(lspnew, SetOptions.merge())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful()) {
                                    img_sp.setImageResource(R.drawable.img1);
                                    ed_ten.getEditText().setText("");
                                    ed_masp.getEditText().setText("");
                                    ed_gia.getEditText().setText("");
                                    ed_time.getEditText().setText("");
                                    ed_mo_ta.getEditText().setText("");
                                    Toast.makeText(ThemSanPhamActivity.this, "them san pham thanh cong", Toast.LENGTH_SHORT).show();

                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ThemSanPhamActivity.this, "them that bai", Toast.LENGTH_SHORT).show();
                            }
                        })     ;

            }
        });
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImg();
            }
        });
    }
    private void anhXaView() {
        btn_upload = findViewById(R.id.btn_upload);
        img_sp =findViewById(R.id.img_sp);
        ed_ten = findViewById(R.id.ed_name_sp);
        ed_gia = findViewById(R.id.ed_gia_sp);
        ed_masp = findViewById(R.id.ed_ma_sp);
        ed_time = findViewById(R.id.ed_time);
        ed_mo_ta =findViewById(R.id.ed_mota);
        btn_add = findViewById(R.id.btn_add_sp);

        spinnerLoaisp = findViewById(R.id.spinner_loai_sp);
        loaisanphams = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
    }
    private void openImg() {
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver =this.getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImage(){
        final ProgressDialog dialog = new ProgressDialog(ThemSanPhamActivity.this);
        dialog.setMessage("Uploading");
        dialog.show();
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
                        muri = dowloaduri.toString();
                        Glide.with(ThemSanPhamActivity.this).load(muri).into(img_sp);
                        dialog.dismiss();
                    }
                    else {
                        Toast.makeText(ThemSanPhamActivity.this, "failed!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ThemSanPhamActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }else {

            Toast.makeText(ThemSanPhamActivity.this, "no image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_REQUEST && resultCode==RESULT_OK && data != null && data.getData() != null){
            img_uri = data.getData();
            if(uploadtalk!=null && uploadtalk.isInProgress()){
                Toast.makeText(ThemSanPhamActivity.this, "upload in progress", Toast.LENGTH_SHORT).show();
            }else {
                uploadImage();
            }

        }
    }
}
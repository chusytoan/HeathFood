package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.List;

public class UpdateSanpham extends AppCompatActivity {
    TextInputLayout ed_nameUp,ed_giaUp,ed_motaUp,ed_timeshipUp;
    TextInputLayout tv_maspUp,tv_tenLoai;
    Button btn_update;
    ImageView  img_sanphamUp,btn_upload;
    List<Loaisanpham> loaisanphamList;


    ProgressDialog progressDialog;

    //store
    StorageReference storageReference;
    private static final int IMAGE_REQUEST =1;
    private Uri img_uri;
    private StorageTask uploadtalk;
    private String muri;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_updatesanpham);
        anhXaView();
        Intent intent = getIntent();
        db= FirebaseFirestore.getInstance();
        String maSP = intent.getStringExtra("maSPUP");
        String name = intent.getStringExtra("nameUP");
        double dgia = intent.getDoubleExtra("donGiaUP", 0);
        muri = intent.getStringExtra("hinhAnhUP");
        String moTa = intent.getStringExtra("moTaUP");
        int timeShip = intent.getIntExtra("timeUP", 0);
        String maLoai =  intent.getStringExtra("MaLoai");


        Glide.with(this).load(muri).into(img_sanphamUp);
        ed_nameUp.getEditText().setText(name);
        ed_motaUp.getEditText().setText(moTa);
        ed_giaUp.getEditText().setText(dgia + "");
        tv_maspUp.getEditText().setText(maSP);
        ed_timeshipUp.getEditText().setText(timeShip + "");
        tv_tenLoai.getEditText().setText(maLoai);


        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImg();
            }
        });


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String nameSP_up = ed_nameUp.getEditText().getText().toString();
                double priceSP_up = Double.parseDouble(ed_giaUp.getEditText().getText().toString());
                String describeSP_up = ed_motaUp.getEditText().getText().toString();
                int time_shipSP_up = Integer.parseInt(ed_timeshipUp.getEditText().getText().toString());

                db.collection("LoaiSanPhams").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Loaisanpham lsp = document.toObject(Loaisanpham.class);
                                if(lsp.getMaLoai().equals(maLoai)){
                                    db.collection("LoaiSanPhams").document(document.getId())
                                            .update("sanphams."+maSP+".name",nameSP_up,
                                                    "sanphams."+maSP+".price",priceSP_up,
                                                    "sanphams."+maSP+".time_ship",time_shipSP_up,
                                                    "sanphams."+maSP+".describe",describeSP_up,
                                                    "sanphams."+maSP+".imgURL",muri
                                                    )
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progressDialog.dismiss();
                                                    if (task.isSuccessful()){

                                                        Toast.makeText(getBaseContext(), "Update sản phẩm thành công", Toast.LENGTH_SHORT).show();
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
        ed_nameUp=findViewById(R.id.ed_name_spUP);
        tv_maspUp=findViewById(R.id.tv_ma_spUP);
        ed_giaUp=findViewById(R.id.ed_gia_spUP);
        ed_timeshipUp=findViewById(R.id.ed_timeUP);
        ed_motaUp=findViewById(R.id.ed_motaUP);
        btn_update=findViewById(R.id.btn_update_Sp);
        img_sanphamUp=findViewById(R.id.img_sanphamUP);
        btn_upload=findViewById(R.id.btn_upload);
        tv_tenLoai=findViewById(R.id.tv_tenLoai);
        loaisanphamList=new ArrayList<>();

        //spinner

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
                        muri = dowloaduri.toString();
                        Glide.with(getApplicationContext()).load(muri).into(img_sanphamUp);
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
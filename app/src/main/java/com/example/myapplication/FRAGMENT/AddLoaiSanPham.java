package com.example.myapplication.FRAGMENT;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.MODEL.Loaisanpham;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
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


public class AddLoaiSanPham extends Fragment {

TextInputLayout ed_ten, ed_ma;
Button btnadd, btn_xoa_trang;
ProgressDialog progressDialog;
    ImageView btn_upload,img_sp;

    //store
    StorageReference storageReference;
    private static final int IMAGE_REQUEST =1;
    private Uri img_uri;
    private StorageTask uploadtalk;
    private String muri;

    View view;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.them_loai_san_pham, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.chipNavigationBar.setVisibility(View.GONE);
        anhXaView();

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImg();
            }
        });
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String maLoai = ed_ma.getEditText().getText().toString();
                String tenLoai = ed_ten.getEditText().getText().toString();

                db.collection("LoaiSanPhams").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot doc : task.getResult()){
                                Loaisanpham lsp = doc.toObject(Loaisanpham.class);
                                if(lsp.getMaLoai().equals(maLoai)){
                                    Toast.makeText(getContext(), "Mã loại này đã tồn tại!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            Loaisanpham lsp = new Loaisanpham(maLoai, tenLoai, muri,null);

                            db.collection("LoaiSanPhams").document(maLoai).set(lsp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    if(task.isSuccessful()){
                                        img_sp.setImageResource(R.drawable.img1);
                                        ed_ten.getEditText().setText("");
                                        ed_ma.getEditText().setText("");

                                        Toast.makeText(getContext(), "Thêm loại sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });

            }
        });

        btn_xoa_trang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ed_ten.getEditText().setText("");
                ed_ma.getEditText().setText("");
            }
        });
        return view;
    }
    private void anhXaView(){
        btn_upload = view.findViewById(R.id.btn_upload);
        img_sp = view.findViewById(R.id.img_sp);
        progressDialog = new ProgressDialog(getContext());
        ed_ten = view.findViewById(R.id.ed_ten_loai);
        ed_ma = view.findViewById(R.id.ed_ma_loai);
        btnadd = view.findViewById(R.id.btn_add_loai);
        btn_xoa_trang = view.findViewById(R.id.btn_xoa_trang);
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
    }

    private void openImg() {
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImage(){
        final ProgressDialog dialog = new ProgressDialog(getContext());
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
                        Glide.with(getContext()).load(muri).into(img_sp);
                        dialog.dismiss();
                    }
                    else {
                        Toast.makeText(getContext(), "failed!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }else {

            Toast.makeText(getContext(), "no image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_REQUEST && resultCode==RESULT_OK && data != null && data.getData() != null){
            img_uri = data.getData();
            if(uploadtalk!=null && uploadtalk.isInProgress()){
                Toast.makeText(getContext(), "upload in progress", Toast.LENGTH_SHORT).show();
            }else {
                uploadImage();
            }

        }
    }
}
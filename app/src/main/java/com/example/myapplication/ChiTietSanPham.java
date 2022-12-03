package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.ADAPTER.CommentAdapter;

import com.example.myapplication.MODEL.Comment;
import com.example.myapplication.MODEL.GioHang;
import com.example.myapplication.MODEL.KhachHang;
import com.example.myapplication.MODEL.Loaisanpham;
import com.example.myapplication.MODEL.NhanVien;
import com.example.myapplication.MODEL.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import java.util.List;


public class ChiTietSanPham extends AppCompatActivity {
String TAG  = "ChiTietSanPham";
    RelativeLayout btn_yeuthich;
ImageView img_sanpham,img_tym_bay,imback;
TextView tv_ten_sp,tv_mota,tv_gia,tv_loaips,tv_luotban , tv_tongtien,tv_value,tv_time_ship,tv_total;
RecyclerView recyrcleDanhGia;
EditText ed_cmt;
Button btn_cmt;
Button btn_add_cart;


List<Comment> comments;
CommentAdapter commentAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_phamm2);

      anhXaView();
      imback.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
             onBackPressed();
          }
      });
        Intent intent = getIntent();

        FirebaseUser usercurent = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String maSP = intent.getStringExtra("maSP");
       String name =  intent.getStringExtra("name");
       double dgia =  intent.getDoubleExtra("donGia", 0);
       String urlIMG =  intent.getStringExtra("hinhAnh");
       String moTa =  intent.getStringExtra("moTa");
       int luotBan = intent.getIntExtra("LuotBan",0);
       int favorite = intent.getIntExtra("favorite",0);
       int timeShip =  intent.getIntExtra("time", 0);
       String tenLoai =  intent.getStringExtra("tenLoai");

        Glide.with(this).load(urlIMG).into(img_sanpham);
        tv_ten_sp.setText(name);
        tv_mota.setText(moTa);
        tv_time_ship.setText(timeShip +" - "+ (timeShip + 5) +" Min");
        tv_gia.setText(String.valueOf(dgia));
        tv_luotban.setText("Lượt bán: " + luotBan);
        tv_total.setText((count * Double.parseDouble(tv_gia.getText().toString()))+"");
        db.collection("Comments").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                comments.clear();
                for (QueryDocumentSnapshot doc : value) {

                    Comment cm = doc.toObject(Comment.class);
                    if(cm.getId_comment().equals(maSP)){
                        comments.add(cm);
                    }
                }
                Collections.sort(comments, new Comparator<Comment>() {
                    @Override
                    public int compare(Comment comment, Comment t1) {
                        return t1.getTime_comment().compareTo(comment.getTime_comment());
                    }
                });
                commentAdapter = new CommentAdapter(ChiTietSanPham.this, comments);
                recyrcleDanhGia.setAdapter(commentAdapter);
            }
        });
        btn_cmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //kiem tra nhap trong
                if(usercurent==null){
                    finish();
                    startActivity(new Intent(ChiTietSanPham.this, LoginActivity.class));
                    return;
                }
                String content = ed_cmt.getText().toString();
                if(content.equals("")){
                    Toast.makeText(ChiTietSanPham.this, "vui long nhap binh luan", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(usercurent.getEmail().matches("^nhanvien+\\w+\\@+\\w+\\.+\\w+")){
                    Comments(content, 2, maSP);
                }else {
                    Comments(content, 3, maSP);
                }

            }
        });



        btn_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usercurent==null){
                    finish();
                    startActivity(new Intent(ChiTietSanPham.this, LoginActivity.class));
                    return;
                }
                GioHang gh = new GioHang();
                if(Integer.parseInt(tv_value.getText().toString()) < 1){
                    Toast.makeText(ChiTietSanPham.this, "vui lòng chọn số lượng", Toast.LENGTH_SHORT).show();
                    return;
                }
                gh.setIdUser(usercurent.getUid());
                gh.setMaSP(maSP);
                gh.setTenSanPham(name);
                gh.setHinhAnh(urlIMG);
                gh.setSoLuong(Integer.parseInt(tv_value.getText().toString()));
                gh.setDonGia(dgia);

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                reference.child("GioHangs")
                        .child(maSP).setValue(gh, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(ChiTietSanPham.this, "da them vao gio hang", Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });


    }

    private void anhXaView(){
         img_sanpham = findViewById(R.id.img_sanpham);
         tv_ten_sp = findViewById(R.id.tv_ten_sp);
         tv_mota = findViewById(R.id.tv_mota);
         tv_gia = findViewById(R.id.tv_gia);
         tv_loaips = findViewById(R.id.tv_loaips);
        tv_time_ship = findViewById(R.id.tv_time_ship);
        tv_luotban  = findViewById(R.id.tv_luotban);
         recyrcleDanhGia = findViewById(R.id.recyrcleDanhGia);
         recyrcleDanhGia.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
         comments = new ArrayList<>();
         ed_cmt = findViewById(R.id.ed_comment);
         btn_cmt = findViewById(R.id.btn_comment);



        btn_add_cart = findViewById(R.id.btn_add_Cart);

        tv_total = findViewById(R.id.tv_total);
        tv_value = findViewById(R.id.tv_value);
        tv_tongtien = findViewById(R.id.tv_total);
        imback=findViewById(R.id.img_back);



    }

    private void Comments(String nd, int loaiUser, String maSP){
        FirebaseUser usercurent = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //kiem tra dang nhap
        if(usercurent==null){
            finish();
            startActivity(new Intent(ChiTietSanPham.this, LoginActivity.class));
        }else {
            if(loaiUser==2){
                String iduser  = usercurent.getUid();
                String content = ed_cmt.getText().toString();
                db.collection("Users").document("nhanvien")
                        .collection("nhanviens")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    return;
                                }
                                for(QueryDocumentSnapshot document : value){
                                    User usr = document.toObject(User.class);
                                    if(usr.getId().equals(iduser)){
                                        Comment cm = new Comment();
                                        cm.setId_comment(maSP);
                                        cm.setId_user(iduser);
                                        cm.setName_user(usr.getName());
                                        cm.setImg_user("default");
                                        cm.setContent(content);
                                        String timeCureent = "";
                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern( "yyyy/MM/dd HH:mm" );
                                            LocalDateTime now = LocalDateTime.now();
                                            timeCureent = dtf.format(now);
                                            cm.setTime_comment(timeCureent);
                                        }
                                        cm.setSoSaoDanhGia(5);
                                        db.collection("Comments").document(content).set(cm).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                ed_cmt.setText("");
                                            }
                                        });
                                    }
                                }
                            }
                        });
            }else if(loaiUser==3){
                DatabaseReference referencekhs = FirebaseDatabase.getInstance().getReference("KhachHangs");
                referencekhs.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            KhachHang kh = dataSnapshot.getValue(KhachHang.class);
                            if(kh.getId().equals(usercurent.getUid())){
                                Comment cmt = new Comment();
                                cmt.setId_comment(maSP);
                                cmt.setId_user(kh.getId());
                                cmt.setName_user(kh.getName());
                                cmt.setImg_user("default");
                                cmt.setContent(nd);
                                String timeCureent = "";
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern( "yyyy/MM/dd HH:mm" );
                                    LocalDateTime now = LocalDateTime.now();
                                    timeCureent = dtf.format(now);
                                    cmt.setTime_comment(timeCureent);
                                }
                                cmt.setSoSaoDanhGia(5);
                                db.collection("Comments").document(nd).set(cmt).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        ed_cmt.setText("");
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        }
    }
    int count = 1;
    public void onclickTang(View view) {
        count++;
        tv_value.setText(String.valueOf(count));
        tv_total.setText((count * Double.parseDouble(tv_gia.getText().toString()))+"");
    }

    public void onClickGiam(View view) {
        if(count<=1){
            count=1;
        }else{
            count--;
            tv_value.setText(String.valueOf(count));
            tv_total.setText((count * Double.parseDouble(tv_gia.getText().toString()))+"");
        }

    }

    public void showCart(View view) {
        startActivity(new Intent(ChiTietSanPham.this, ActivityGioHang.class));
    }
}
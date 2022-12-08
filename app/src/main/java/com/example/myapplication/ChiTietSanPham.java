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

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.ADAPTER.CommentAdapter;

import com.example.myapplication.MODEL.Comment;
import com.example.myapplication.MODEL.DanhGia;
import com.example.myapplication.MODEL.GioHang;
import com.example.myapplication.MODEL.KhachHang;
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

import java.util.HashMap;
import java.util.List;


public class ChiTietSanPham extends AppCompatActivity {
    String TAG = "ChiTietSanPham";
    RelativeLayout btn_yeuthich;
    ImageView img_sanpham;
    ImageView img_tym_bay;
    ImageView saoa;
    ImageView imback;
    TextView tv_ten_sp, tv_mota, tv_gia, tv_loaips, tv_luotban, tv_tongtien, tv_value, tv_time_ship, tv_total;
    RecyclerView recyrcleDanhGia;
    EditText ed_cmt;
    Button btn_cmt;
    Button btn_add_cart;
    RatingBar ratingBar;
    TextView tbsao;
    float rateValue;
    int rating;
    DatabaseReference reference;
    List<Comment> comments;
    CommentAdapter commentAdapter;
    Button GET;
    List<DanhGia> danhGias;

    int tb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_phamm2);

        anhXaView();
//        TbDanhGia();
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float a, boolean fromUser) {
                rating = (int) a;
                rateValue = ratingBar.getRating();

            }


        });

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
        String name = intent.getStringExtra("name");
        double dgia = intent.getDoubleExtra("donGia", 0);
        String urlIMG = intent.getStringExtra("hinhAnh");
        String moTa = intent.getStringExtra("moTa");
        int luotBan = intent.getIntExtra("LuotBan", 0);
        int favorite = intent.getIntExtra("favorite", 0);
        int timeShip = intent.getIntExtra("time", 0);
        String tenLoai = intent.getStringExtra("tenLoai");
        String maLoai = intent.getStringExtra("MaLoai");
        int saodanggia= intent.getIntExtra("star",0);

        Log.d(TAG, "onCreate: " + maLoai);
        Glide.with(this).load(urlIMG).into(img_sanpham);
        tv_ten_sp.setText(name);
        tv_mota.setText(moTa);
        tv_time_ship.setText(timeShip + " - " + (timeShip + 5) + " Min");
        tv_gia.setText(String.valueOf(dgia));
        tv_luotban.setText("Lượt bán: " + luotBan);
        tv_total.setText((count * Double.parseDouble(tv_gia.getText().toString())) + "");

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
                    if (cm.getId_comment().equals(maSP)) {
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
                if (usercurent == null) {
                    finish();
                    startActivity(new Intent(ChiTietSanPham.this, LoginActivity.class));
                    return;
                }
                String content = ed_cmt.getText().toString();
                if (content.equals("")) {
                    Toast.makeText(ChiTietSanPham.this, "vui long nhap binh luan", Toast.LENGTH_SHORT).show();
                    return;
                }else
                    Comments(content, 3, maSP);


            }
        });


        btn_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usercurent == null) {
                    finish();
                    startActivity(new Intent(ChiTietSanPham.this, LoginActivity.class));
                    return;
                }
                GioHang gh = new GioHang();
                if (Integer.parseInt(tv_value.getText().toString()) < 1) {
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


        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference("sanphams");
        GET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser==null){
                    Toast.makeText(ChiTietSanPham.this, "Bạn chưa Đằng Nhập", Toast.LENGTH_SHORT).show();
                    return;
                }
                String userId = usercurent.getUid();
                DatabaseReference danhGias = FirebaseDatabase.getInstance().getReference("DanhGias");
                DanhGia dg = new DanhGia();
                dg.setSosaoDanhgia(rating);
                danhGias.child(maSP).child(userId).setValue(dg);

                danhGias.child(maSP).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int saoTong = 0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            DanhGia dg = dataSnapshot.getValue(DanhGia.class);
                            if (dg == null)
                                return;
                            saoTong += dg.getSosaoDanhgia();
                            Log.d(TAG, "tong sao" + saoTong);
                        }
                        float tbsaodg;
                        Log.d(TAG, "so nguoi " + snapshot.getChildrenCount());
                        tbsaodg = saoTong / snapshot.getChildrenCount();

                        Log.d(TAG, "trung binh" + tbsaodg);

                        HashMap<String, Object> updatedabangia = new HashMap<>();
                        updatedabangia.put("starDanhGia", tbsaodg);
                        reference.child(maSP).updateChildren(updatedabangia);
                        tbsao.setText(saodanggia + "");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//                danhGias.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if(snapshot.child(maSP).hasChild(userId)) {
//
//                            DanhGia dgupdate = new DanhGia();
//                            dgupdate.setSosaoDanhgia(rating);
//                            danhGias.child(maSP).child(userId).setValue(dgupdate);
//                           danhGias.child(maSP).addValueEventListener(new ValueEventListener() {
//                               @Override
//                               public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                                   int saoTong = 0;
//                                   for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                                       DanhGia dg = dataSnapshot.getValue(DanhGia.class);
//                                       if(dg==null)
//                                           return;
//                                       saoTong += dg.getSosaoDanhgia();
//                                       Log.d(TAG, "tong sao"+saoTong);
//                                   }
//                                   float tbsaodg;
//                                   Log.d(TAG, "so nguoi "+snapshot.getChildrenCount());
//                                   tbsaodg=saoTong/snapshot.getChildrenCount();
//
//                                   Log.d(TAG, "trung binh"+tbsaodg);
//
//                                   HashMap<String,Object> updatedabangia=new HashMap<>();
//                                   updatedabangia.put("starDanhGia", tbsaodg);
//                                    reference.child(maSP).updateChildren(updatedabangia);
//                                   tbsao.setText(saodanggia+"");
//
//                               }
//
//                               @Override
//                               public void onCancelled(@NonNull DatabaseError error) {
//
//                               }
//                           });
//
//                        }else{
//                            DanhGia dg = new DanhGia();
//                            dg.setSosaoDanhgia(rating);
//                            danhGias.child(maSP).child(userId).setValue(dg);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });


            }
        });

        tbsao.setText(saodanggia+"");

    }

    private void anhXaView() {
        GET=findViewById(R.id.getsao);
        saoa=findViewById(R.id.saoo);
        tbsao = findViewById(R.id.tbsao);
        ratingBar = findViewById(R.id.raitingbar);
        img_sanpham = findViewById(R.id.img_sanpham);
        tv_ten_sp = findViewById(R.id.tv_ten_sp);
        tv_mota = findViewById(R.id.tv_mota);
        tv_gia = findViewById(R.id.tv_gia);
        tv_loaips = findViewById(R.id.tv_loaips);
        tv_time_ship = findViewById(R.id.tv_time_ship);
        tv_luotban = findViewById(R.id.tv_luotban);
        recyrcleDanhGia = findViewById(R.id.recyrcleDanhGia);
        recyrcleDanhGia.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        comments = new ArrayList<>();
        danhGias = new ArrayList<>();
        ed_cmt = findViewById(R.id.ed_comment);
        btn_cmt = findViewById(R.id.btn_comment);


        btn_add_cart = findViewById(R.id.btn_add_Cart);

        tv_total = findViewById(R.id.tv_total);
        tv_value = findViewById(R.id.tv_value);
        tv_tongtien = findViewById(R.id.tv_total);
        imback = findViewById(R.id.img_back);


    }


    private void Comments(String nd, int loaiUser, String maSP) {
        FirebaseUser usercurent = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //kiem tra dang nhap
        if (usercurent == null) {
            finish();
            startActivity(new Intent(ChiTietSanPham.this, LoginActivity.class));
        } else {
            if (loaiUser == 3) {
                DatabaseReference referencekhs = FirebaseDatabase.getInstance().getReference("KhachHangs");
                referencekhs.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            KhachHang kh = dataSnapshot.getValue(KhachHang.class);
                            if (kh.getId().equals(usercurent.getUid())) {
                                Comment cmt = new Comment();
                                cmt.setId_comment(maSP);
                                cmt.setId_user(kh.getId());
                                cmt.setName_user(kh.getName());
                                cmt.setImg_user("default");
                                cmt.setContent(nd);
                                String timeCureent = "";
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
                                    LocalDateTime now = LocalDateTime.now();
                                    timeCureent = dtf.format(now);
                                    cmt.setTime_comment(timeCureent);
                                }


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
        tv_total.setText((count * Double.parseDouble(tv_gia.getText().toString())) + "");
    }

    public void onClickGiam(View view) {
        if (count <= 1) {
            count = 1;
        } else {
            count--;
            tv_value.setText(String.valueOf(count));
            tv_total.setText((count * Double.parseDouble(tv_gia.getText().toString())) + "");
        }

    }

    public void showCart(View view) {
        startActivity(new Intent(ChiTietSanPham.this, ActivityGioHang.class));
    }
}

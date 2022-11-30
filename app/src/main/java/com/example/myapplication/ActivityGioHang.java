package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ADAPTER.GioHangAdapter;
import com.example.myapplication.ADAPTER.SpinnerAddressAdapter;
import com.example.myapplication.MODEL.DonHang;
import com.example.myapplication.MODEL.FCMSend;
import com.example.myapplication.MODEL.GioHang;
import com.example.myapplication.MODEL.KhachHang;

import com.example.myapplication.MODEL.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ActivityGioHang extends AppCompatActivity {



    String TAG  = "GIOHANG";
    private View view;
    TextView tv_tongTien, tv_phone;
    ImageView img_onBack;
    Button btn_mua;
    RecyclerView recyclerView;
    GioHangAdapter gioHangAdapter;
    List<GioHang> list;
    LinearLayout btn_start_buy,layout_buy;
    ProgressDialog progressDialog;
    //spinner kh
    Spinner spin_Adress;
    SpinnerAddressAdapter addressAdapter;
    List<KhachHang> khachHangs;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);

        anhXa();
        img_onBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        reference = FirebaseDatabase.getInstance().getReference("GioHangs");
        FirebaseUser userCurrent = FirebaseAuth.getInstance().getCurrentUser();
        if(userCurrent != null){
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    double tongTien = 0;
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        GioHang gh =dataSnapshot.getValue(GioHang.class);

                        if(gh.getIdUser().equals(userCurrent.getUid())){
                            list.add(gh);

                            tongTien += gh.getDonGia()*gh.getSoLuong();
                        }

                    }
                    tv_tongTien.setText(tongTien+"");
                    gioHangAdapter = new GioHangAdapter(ActivityGioHang.this, list);
                    recyclerView.setAdapter(gioHangAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }



        DatabaseReference referencekhs = FirebaseDatabase.getInstance().getReference("KhachHangs");
        referencekhs.child(userCurrent.getUid()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        KhachHang kh = snapshot.getValue(KhachHang.class);
                        khachHangs.add(kh);
                        addressAdapter = new SpinnerAddressAdapter(khachHangs);
                        spin_Adress.setAdapter(addressAdapter);
                        tv_phone.setText(kh.getSdt());
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        if(userCurrent!=null){
            referencekhs.child(userCurrent.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        Log.d(TAG, "onComplete: " + task.getResult().getValue());
                        KhachHang kh = task.getResult().getValue(KhachHang.class);
                        khachHangs.add(kh);
                        addressAdapter = new SpinnerAddressAdapter(khachHangs);
                        spin_Adress.setAdapter(addressAdapter);
                        tv_phone.setText(kh.getSdt());
                    }
                }
            });
        }


        btn_mua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                    DatabaseReference  referencedh =  FirebaseDatabase.getInstance().getReference("DonHangs");

                DonHang dh = new DonHang();
                String maDH = String.valueOf(System.currentTimeMillis());
                dh.setMaDonHang(maDH);
                dh.setTongTien(Double.parseDouble(tv_tongTien.getText().toString()));
                KhachHang kh = (KhachHang) spin_Adress.getSelectedItem();
                if(kh==null){
                    progressDialog.dismiss();
                    Toast.makeText(ActivityGioHang.this, "vui long them sdt va dia chi", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(kh.getDiachi().equals("")){
                    progressDialog.dismiss();
                    Toast.makeText(ActivityGioHang.this, "Vui long them dia chi", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(kh.getSdt().equals("")){
                    progressDialog.dismiss();
                    Toast.makeText(ActivityGioHang.this, "Vui long them sdt", Toast.LENGTH_SHORT).show();
                    return;
                }
                dh.setTrangThai("wait for confirmation");
                dh.setName(kh.getName());
                dh.setDiaChi(kh.getDiachi());
                dh.setSdt(tv_phone.getText().toString());
                dh.setSanphams(list);
                DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens").child("0777476404");
                tokens.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Token token = snapshot.getValue(Token.class);
                        FCMSend.pushNotification(ActivityGioHang.this, token.getToken(), "Thông báo đơn hàng mới", "Bạn vừa nhận 1 đơn hàng mới:"
                                +"\n" + kh.getName() +"\n"+ kh.getSdt() +"\n" + kh.getDiachi());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                referencedh.child(maDH).setValue(dh).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            Toast.makeText(ActivityGioHang.this, "Gui don hang thanh cong", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });

            }
        });




    }



    private void anhXa(){
        recyclerView = findViewById(R.id.rcv_gioHang);
        list = new ArrayList<>();
        tv_tongTien = findViewById(R.id.tv_tong_tien);
        btn_mua = findViewById(R.id.btn_dat_hang);
        spin_Adress = findViewById(R.id.spinner_diachi);
        img_onBack = findViewById(R.id.img_onBack);
        tv_phone = findViewById(R.id.tv_sdt);
        layout_buy = findViewById(R.id.layout_buy);
        btn_start_buy = findViewById(R.id.btn_buy);
        progressDialog = new ProgressDialog(this);
        khachHangs = new ArrayList<>();
    }

}
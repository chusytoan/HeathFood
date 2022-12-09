package com.example.myapplication.FRAGMENT;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ADAPTER.GioHangAdapter;
import com.example.myapplication.ADAPTER.SpinnerAddressAdapter;
import com.example.myapplication.ActivityGioHang;
import com.example.myapplication.LoginActivity;
import com.example.myapplication.MODEL.DonHang;
import com.example.myapplication.MODEL.FCMSend;
import com.example.myapplication.MODEL.GioHang;
import com.example.myapplication.MODEL.KhachHang;

import com.example.myapplication.MODEL.Token;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GioHangFragment extends Fragment {

String TAG  = "GIOHANG";
private View view;
TextView tv_tongTien, tv_phone;
ImageView img_onBack;
Button btn_mua;
RecyclerView recyclerView;
GioHangAdapter gioHangAdapter;
List<GioHang> list;

    ProgressDialog progressDialog;
//spinner kh
Spinner spin_Adress;
SpinnerAddressAdapter addressAdapter;
List<KhachHang> khachHangs;
    DatabaseReference reference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_gio_hang, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();

        mainActivity.navigationView.setVisibility(View.INVISIBLE);
        anhXa();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        reference = FirebaseDatabase.getInstance().getReference("GioHangs");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    double tongTien = 0;
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        GioHang gh =dataSnapshot.getValue(GioHang.class);

                        if(gh.getIdUser().equals(user.getUid())){
                            list.add(gh);

                            tongTien += gh.getDonGia()*gh.getSoLuong();
                        }

                    }
                    tv_tongTien.setText(tongTien+"");
                    gioHangAdapter = new GioHangAdapter(getContext(), list);
                    recyclerView.setAdapter(gioHangAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        DatabaseReference referencekhs = FirebaseDatabase.getInstance().getReference("KhachHangs");
        referencekhs.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                KhachHang kh = snapshot.getValue(KhachHang.class);
                if(kh==null)
                    return;
                if(khachHangs!=null)
                {
                    khachHangs.clear();
                }
                if(user == null){
                    return;
                }else{
                    if(kh.getId().equals(user.getUid())){

                        khachHangs.add(kh);
                        addressAdapter.notifyDataSetChanged();
                        tv_phone.setText(kh.getSdt());
                    }
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                KhachHang kh = snapshot.getValue(KhachHang.class);

                if(kh==null)
                    return;
                if(user==null)
                    return;
                else{
                    if(kh.getId().equals(user.getUid())){
                        for(int i = 0; i < khachHangs.size(); i ++){
                            if(kh.getId().equals(khachHangs.get(i).getId())){
                                khachHangs.set(i, kh);
                                break;
                            }
                        }
                        addressAdapter.notifyDataSetChanged();
                    }
                }



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

        if(user!=null){
            referencekhs.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        //Log.d(TAG, "onComplete: " + task.getResult().getValue());
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


                if(user==null){

                    startActivity(new Intent(getContext(), LoginActivity.class));
                    return;
                }
                progressDialog.show();
                DatabaseReference  referencedh =  FirebaseDatabase.getInstance().getReference("DonHangs");

                DonHang dh = new DonHang();
                String maDH = String.valueOf(System.currentTimeMillis());
                dh.setMaDonHang(maDH);
                dh.setTongTien(Double.parseDouble(tv_tongTien.getText().toString()));
                KhachHang kh = (KhachHang) spin_Adress.getSelectedItem();
                if(kh==null){
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "vui long them sdt va dia chi", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(kh.getDiachi().equals("")){
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Vui long them dia chi", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(kh.getSdt().equals("")){
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Vui long them sdt", Toast.LENGTH_SHORT).show();
                    return;
                }
                String timeCureent = "";

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    LocalDateTime now = LocalDateTime.now();
                    timeCureent = dtf.format(now);
                    dh.setNgayMuaHang(timeCureent);
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
                        FCMSend.pushNotification(getContext(), token.getToken(), "Thông báo đơn hàng mới", "Bạn vừa nhận 1 đơn hàng mới:"
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
                            Toast.makeText(getContext(), "Gui don hang thanh cong", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });

            }
        });
        return view;
    }
    private void anhXa(){
        recyclerView = view.findViewById(R.id.rcv_gioHang);
        list = new ArrayList<>();
        tv_tongTien = view.findViewById(R.id.tv_tong_tien);
        btn_mua = view.findViewById(R.id.btn_dat_hang);
       spin_Adress = view.findViewById(R.id.spinner_diachi);
        tv_phone = view.findViewById(R.id.tv_sdt);
    progressDialog = new ProgressDialog(getContext());
        khachHangs = new ArrayList<>();

        addressAdapter = new SpinnerAddressAdapter(khachHangs);
        spin_Adress.setAdapter(addressAdapter);
    }

}
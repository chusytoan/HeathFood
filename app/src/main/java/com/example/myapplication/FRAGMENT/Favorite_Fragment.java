package com.example.myapplication.FRAGMENT;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ADAPTER.ChatAdapter;
import com.example.myapplication.ADAPTER.GioHangAdapter;
import com.example.myapplication.ADAPTER.LoaiSanPhamAdapter;
import com.example.myapplication.ADAPTER.SanPhamAdapter;
import com.example.myapplication.ADAPTER.SanPhamFavoriteAdapter;
import com.example.myapplication.ADAPTER.SanPhamNgangAdapter;
import com.example.myapplication.ChatActivity;
import com.example.myapplication.ChiTietSanPham;
import com.example.myapplication.MODEL.ChatMessage;
import com.example.myapplication.MODEL.GioHang;
import com.example.myapplication.MODEL.Loaisanpham;
import com.example.myapplication.MODEL.Sanpham;
import com.example.myapplication.MODEL.SanphamFavorite;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Favorite_Fragment extends Fragment {
    private View view;
    RecyclerView rcv_Favorite;
    List<SanphamFavorite> listYT;
    List<Sanpham> list;
    DatabaseReference reference;
    SanPhamFavoriteAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.layoutfavorite_fragment,container,false);
        anhXa();

        reference = FirebaseDatabase.getInstance().getReference("SanPhamFavorite");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null){
           reference.addChildEventListener(new ChildEventListener() {
               @Override
               public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                   listYT.clear();
                   for (DataSnapshot dataSnapshot:snapshot.getChildren()) {

                       SanphamFavorite spYt=dataSnapshot.getValue(SanphamFavorite.class);
                       if (spYt.getIdUser().equals(user.getUid())) {
                           listYT.add(spYt);
                       }
                   }
                   onResume();
               }

               @Override
               public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                   onResume();
               }

               @Override
               public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                   onResume();
               }

               @Override
               public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });
        }


        return view;
    }
    public void anhXa(){
        listYT=new ArrayList<>();
        list=new ArrayList<>();
        rcv_Favorite=view.findViewById(R.id.rcv_favorite);
        rcv_Favorite.setLayoutManager(new GridLayoutManager(getContext(),2,RecyclerView.VERTICAL,false ));
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter = new SanPhamFavoriteAdapter(getContext(),listYT);
        rcv_Favorite.setAdapter(adapter);
    }
}

package com.example.myapplication.FRAGMENT;

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
import android.widget.ImageView;

import com.example.myapplication.ADAPTER.LoaiSanPhamAdapter;

import com.example.myapplication.ADAPTER.SanPhamNgangAdapter;
import com.example.myapplication.MODEL.Loaisanpham;
import com.example.myapplication.MODEL.Sanpham;
import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    ImageView img_boloc;
    TextInputEditText ed_search_main;
    private String TAG = "homefragment";



    RecyclerView recyclerView_sanpham;
   SanPhamNgangAdapter sanPhamNgangAdapter;
    List<Sanpham> sanPhamList;
    List<Sanpham> sanphamsTop10Favorites;


    //loaisanpham
    List<Loaisanpham> loaiSanPhams;
    LoaiSanPhamAdapter loaiSanPhamAdapter;
    RecyclerView recyclerView_loaisp;

    View view;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        anhXaView();
        readTop10SanPham();
        readDataLoaiSanPhamFromServer();
        return view;
    }

    private void anhXaView() {
        ed_search_main = view.findViewById(R.id.ed_search);
        img_boloc = view.findViewById(R.id.img_boloc);
        recyclerView_sanpham = view.findViewById(R.id.recyrcle_danhSachSp_horizontal);
        recyclerView_loaisp = view.findViewById(R.id.recyrcle_lsp);

        sanPhamList = new ArrayList<>();
        loaiSanPhams = new ArrayList<>();
       sanphamsTop10Favorites = new ArrayList<>();
        recyclerView_loaisp.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView_sanpham.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        sanPhamNgangAdapter = new SanPhamNgangAdapter(getContext(), sanphamsTop10Favorites);
        recyclerView_sanpham.setAdapter(sanPhamNgangAdapter);
    }

    DatabaseReference top10Tyms = FirebaseDatabase.getInstance().getReference("Top10Tym");
    public void readDataLoaiSanPhamFromServer(){
        db.collection("LoaiSanPhams")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override

                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        sanPhamList.clear();
                        loaiSanPhams.clear();
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        for (QueryDocumentSnapshot doc : value) {

                            Loaisanpham lsp = doc.toObject(Loaisanpham.class);

                            Map<String, Sanpham> map_sp = lsp.getSanphams();
                            if(map_sp!=null){
                                for(Sanpham sp : map_sp.values()){
                                    sanPhamList.add(sp);
                                }
                            }
                            loaiSanPhams.add(lsp);
                        }

                        Collections.sort(sanPhamList, new Comparator<Sanpham>() {
                            @Override
                            public int compare(Sanpham sanpham, Sanpham t1) {
                                return sanpham.getFavorite() > t1.getFavorite() ? -1 : 1;
                            }
                        });


                                top10Tyms.setValue(sanPhamList);

                         loaiSanPhamAdapter = new LoaiSanPhamAdapter(getContext(), loaiSanPhams);
                          loaiSanPhamAdapter.notifyDataSetChanged();
                          recyclerView_loaisp.setAdapter(loaiSanPhamAdapter);


                    }
                });
    }
    void readTop10SanPham(){
        top10Tyms.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Sanpham sp = snapshot.getValue(Sanpham.class);
                if(sanphamsTop10Favorites.size()<10){
                    sanphamsTop10Favorites.add(sp);
                    sanPhamNgangAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                Sanpham sp = snapshot.getValue(Sanpham.class);
//                if(sanphamsTop10Favorites.size()<10){
//                    sanphamsTop10Favorites.add(sp);
//                }
//                sanPhamNgangAdapter = new SanPhamNgangAdapter(getContext(), sanphamsTop10Favorites);
//                recyclerView_sanpham.setAdapter(sanPhamNgangAdapter);
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
    }

}
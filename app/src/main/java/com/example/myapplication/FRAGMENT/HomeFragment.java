package com.example.myapplication.FRAGMENT;

import android.os.Bundle;

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
import com.example.myapplication.ADAPTER.SanPhamAdapter;
import com.example.myapplication.ADAPTER.SanPhamNgangAdapter;
import com.example.myapplication.MODEL.Loaisanpham;
import com.example.myapplication.MODEL.Sanpham;
import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    ImageView img_boloc;
    TextInputEditText ed_search_main;
    private String TAG = "homefragment";



    RecyclerView recyclerView_sanpham;
   SanPhamNgangAdapter sanPhamNgangAdapter;
    List<Sanpham> sanPhamList;



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

        recyclerView_loaisp.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView_sanpham.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
    }
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


                         loaiSanPhamAdapter = new LoaiSanPhamAdapter(getContext(), loaiSanPhams);
                          loaiSanPhamAdapter.notifyDataSetChanged();
                          recyclerView_loaisp.setAdapter(loaiSanPhamAdapter);

                        sanPhamNgangAdapter = new SanPhamNgangAdapter(getContext(), sanPhamList);
                        sanPhamNgangAdapter.notifyDataSetChanged();
                        recyclerView_sanpham.setAdapter(sanPhamNgangAdapter);

                    }
                });
    }

}
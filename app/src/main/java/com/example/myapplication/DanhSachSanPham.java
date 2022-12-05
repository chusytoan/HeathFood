package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import com.example.myapplication.ADAPTER.LoaiSanPhamAdapter;
import com.example.myapplication.ADAPTER.SanPhamGridAdapter;
import com.example.myapplication.ADAPTER.SanPhamNgangAdapter;
import com.example.myapplication.MODEL.Loaisanpham;
import com.example.myapplication.MODEL.Sanpham;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DanhSachSanPham extends AppCompatActivity {
    String TAG = "DanhSachSanPham";
    FloatingActionButton btn_add;

GridView grid_dssp;
SanPhamGridAdapter sanPhamAdapter;
List<Sanpham> sanphams;
    SearchView searchView;


Intent intent;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_san_pham);
        anhXa();
        timkiem();

        intent = getIntent();
        readDataLoaiSanPhamFromServer();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DanhSachSanPham.this, ChatActivity.class));
            }
        });

    }
    private void anhXa(){

        searchView=findViewById(R.id.seachviewds);
        searchView.clearFocus();
        btn_add = findViewById(R.id.btn_add_sp);
        grid_dssp = findViewById(R.id.grid_sp);
        sanphams = new ArrayList<>();
    }
    public void readDataLoaiSanPhamFromServer(){
        db.collection("LoaiSanPhams")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        sanphams.clear();

                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        for (QueryDocumentSnapshot doc : value) {
                            Loaisanpham lsp = doc.toObject(Loaisanpham.class);
                            if(lsp.getName().equals(intent.getStringExtra("tenLoai"))){
                                Map<String, Sanpham> map_sp = lsp.getSanphams();
                                if(map_sp!=null){
                                    for(Sanpham sp : map_sp.values()){
                                        sanphams.add(sp);
                                    }
                                }
                            }
                        }
                        sanPhamAdapter = new SanPhamGridAdapter(DanhSachSanPham.this, sanphams);
                        grid_dssp.setAdapter(sanPhamAdapter);
                    }
                });
    }
    public void timkiem(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterliss(newText);
                return true;
            }

            private void filterliss(String Text) {
                List<Sanpham> fiteliss=new ArrayList<>();
                for (Sanpham sp: sanphams){
                    if (sp.getName().toLowerCase().contains(Text.toLowerCase())){
                        fiteliss.add(sp);
                    }}
                if (fiteliss.isEmpty()){

                }else{
                    sanPhamAdapter.setfilterliss(fiteliss);
                }
            }
        });
    }
}
package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.ADAPTER.SanPhamHomeAdapter;
import com.example.myapplication.MODEL.Sanpham;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DanhSachSanPham extends AppCompatActivity {
    String TAG = "DanhSachSanPham";
    FloatingActionButton btn_add;

    RecyclerView grid_dssp;
    SanPhamHomeAdapter sanPhamAdapter;
    List<Sanpham> sanphams;
    SearchView searchView;


    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_san_pham);
        anhXa();
        timkiem();

        intent = getIntent();
        readDataSanPhamFromDB();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DanhSachSanPham.this, ChatActivity.class));
            }
        });

    }

    private void anhXa() {

        searchView = findViewById(R.id.seachviewds);
        searchView.clearFocus();
        btn_add = findViewById(R.id.btn_add_sp);
        grid_dssp = findViewById(R.id.grid_sp);
        sanphams = new ArrayList<>();
        grid_dssp.setLayoutManager(new GridLayoutManager(getBaseContext(),2,RecyclerView.VERTICAL,false));
        sanPhamAdapter = new SanPhamHomeAdapter(this, sanphams);
        grid_dssp.setAdapter(sanPhamAdapter);
    }

    public void readDataSanPhamFromDB() {
        DatabaseReference SanPhams = FirebaseDatabase.getInstance().getReference("sanphams");
        SanPhams.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot == null)
                    return;
                Sanpham sp = snapshot.getValue(Sanpham.class);
                if (sp.getTen_loai().equals(intent.getStringExtra("tenLoai"))) {
                    if (sp != null) {
                        sanphams.add(sp);
                        sanPhamAdapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Sanpham sanpham = snapshot.getValue(Sanpham.class);
                if (sanpham == null)
                    return;
                for (int i = 0; i < sanphams.size(); i++) {
                    if (sanpham.getMasp().equals(sanphams.get(i).getMasp())) {
                        sanphams.set(i, sanpham);
                        break;
                    }
                }
                sanPhamAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Sanpham sanpham = snapshot.getValue(Sanpham.class);
                if (sanpham == null)
                    return;
                for (int i = 0; i < sanphams.size(); i++) {
                    if (sanpham.getMasp().equals(sanphams.get(i).getMasp())) {
                        sanphams.remove(sanphams.get(i));
                        break;
                    }
                }
                sanPhamAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void timkiem() {
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
                List<Sanpham> fiteliss = new ArrayList<>();
                for (Sanpham sp : sanphams) {
                    if (sp.getName().toLowerCase().contains(Text.toLowerCase())) {
                        fiteliss.add(sp);
                    }
                }
                if (fiteliss.isEmpty()) {

                } else {
                    sanPhamAdapter.setfilterliss(fiteliss);
                }
            }
        });
    }
}
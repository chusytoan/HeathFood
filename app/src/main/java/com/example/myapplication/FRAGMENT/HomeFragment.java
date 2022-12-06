package com.example.myapplication.FRAGMENT;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.ADAPTER.LoaiSanPhamAdapter;
import com.example.myapplication.ADAPTER.SanPhamAdapter;
import com.example.myapplication.ADAPTER.SanPhamNgangAdapter;
import com.example.myapplication.MODEL.Loaisanpham;
import com.example.myapplication.MODEL.Sanpham;
import com.example.myapplication.Photo;
import com.example.myapplication.PhotoAdapter;
import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;
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

import me.relex.circleindicator.CircleIndicator3;

public class HomeFragment extends Fragment {

    private String TAG = "homefragment";

    SearchView searchView;

    RecyclerView recyclerView_sanpham;
   SanPhamNgangAdapter sanPhamNgangAdapter;
    List<Sanpham> sanPhamList;

    private ViewPager2 mViewpager2;
    private CircleIndicator3 mCircleIndicator3;
    private List<Photo> mlistPhoto;
    private Handler handler = new Handler(Looper.getMainLooper());
    PhotoAdapter photoAdapter;

    //loaisanpham
    List<Loaisanpham> loaiSanPhams;
    LoaiSanPhamAdapter loaiSanPhamAdapter;
    RecyclerView recyclerView_loaisp;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Slider");


    View view;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        anhXaView();
        timkiem();
        Slider();

        readDataLoaiSanPhamFromServer();
        return view;
    }

    private void anhXaView() {
        searchView=view.findViewById(R.id.seachview);
        searchView.clearFocus();

        recyclerView_sanpham = view.findViewById(R.id.recyrcle_danhSachSp_horizontal);
        recyclerView_loaisp = view.findViewById(R.id.recyrcle_lsp);

        mlistPhoto = new ArrayList<>();
        sanPhamList = new ArrayList<>();
        loaiSanPhams = new ArrayList<>();

        recyclerView_loaisp.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        recyclerView_sanpham.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        mViewpager2 = view.findViewById(R.id.viewPager2);
        mCircleIndicator3 = view.findViewById(R.id.circle_indicator);

        mViewpager2.setOffscreenPageLimit(3);
        mViewpager2.setClipToPadding(false);
        mViewpager2.setClipToOutline(false);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(60));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        mViewpager2.setPageTransformer(compositePageTransformer);



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
                List<Loaisanpham> fiteliss=new ArrayList<>();
                for (Loaisanpham loaisan: loaiSanPhams){
                    if (loaisan.getName().toLowerCase().contains(Text.toLowerCase())){
                        fiteliss.add(loaisan);
                    }}
                if (fiteliss.isEmpty()){
                    Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                }else{
                    loaiSanPhamAdapter.setfilterliss(fiteliss);
                }
            }
        });
    }
    private void Slider() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Photo pt = dataSnapshot.getValue(Photo.class);
                    mlistPhoto.add(pt);
                    photoAdapter = new PhotoAdapter(mlistPhoto, getContext());

                    mViewpager2.setAdapter(photoAdapter);
                    mCircleIndicator3.setViewPager(mViewpager2);

                    mViewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                        @Override
                        public void onPageScrollStateChanged(int state) {
                            super.onPageScrollStateChanged(state);
                            handler.removeCallbacks(runnable);
                            handler.postDelayed(runnable, 3000);
                        }
                    });
                    //trl alt L
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int currentPossitiom = mViewpager2.getCurrentItem();
            if (currentPossitiom == mlistPhoto.size() - 1) {
                mViewpager2.setCurrentItem(0);
            } else {
                mViewpager2.setCurrentItem(currentPossitiom + 1);
            }
        }
    };


    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 3000);
    }

}
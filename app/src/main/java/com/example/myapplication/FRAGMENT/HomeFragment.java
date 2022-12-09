package com.example.myapplication.FRAGMENT;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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

import com.example.myapplication.ADAPTER.LoaiSanPhamAdapter;
import com.example.myapplication.ADAPTER.SanPhamHomeAdapter;
import com.example.myapplication.ADAPTER.SanPhamNgangAdapter;
import com.example.myapplication.MODEL.Loaisanpham;
import com.example.myapplication.MODEL.Sanpham;
import com.example.myapplication.Photo;
import com.example.myapplication.PhotoAdapter;
import com.example.myapplication.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class HomeFragment extends Fragment {

    private String TAG = "homefragment";



    RecyclerView recyclerView_sanpham,rcv_sanphams;
    SanPhamNgangAdapter sanPhamNgangAdapter;
    List<Sanpham> sanPhamList;
    SanPhamHomeAdapter sanPhamHomeAdapter;
    List<Sanpham> sanphamHome;
    List<Sanpham> spTop10homes;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        anhXaView();
//        timkiem();
        Slider();
        readDataSanPhamFromDB();
        readDataSanPhamHome();

        readDataLoaiSanPhamFromServer();
        return view;
    }

    private void anhXaView() {
        recyclerView_sanpham = view.findViewById(R.id.recyrcle_danhSachSp_horizontal);
        recyclerView_loaisp = view.findViewById(R.id.recyrcle_lsp);
        rcv_sanphams=view.findViewById(R.id.rcv_sanphams);

        mlistPhoto = new ArrayList<>();
        sanPhamList = new ArrayList<>();
        loaiSanPhams = new ArrayList<>();
        sanphamHome = new ArrayList<>();
        spTop10homes = new ArrayList<>();
        recyclerView_loaisp.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        loaiSanPhamAdapter = new LoaiSanPhamAdapter(getContext(), loaiSanPhams);
        recyclerView_loaisp.setAdapter(loaiSanPhamAdapter);

        recyclerView_sanpham.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        sanPhamNgangAdapter = new SanPhamNgangAdapter(getContext(), spTop10homes);
        recyclerView_sanpham.setAdapter(sanPhamNgangAdapter);

        //set adapter sanphamhome
        rcv_sanphams.setLayoutManager(new GridLayoutManager(getContext(),2,RecyclerView.VERTICAL,false));
        sanPhamHomeAdapter=new SanPhamHomeAdapter(getContext(),sanphamHome);
        rcv_sanphams.setAdapter(sanPhamHomeAdapter);


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

    public void readDataSanPhamFromDB() {
        DatabaseReference SanPhams = FirebaseDatabase.getInstance().getReference("sanphams");
        SanPhams.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot == null)
                    return;

                Sanpham sp = snapshot.getValue(Sanpham.class);
                //Log.d(TAG, "onChildAdded: " + sp.getName());
                if (sp != null) {
                    sanPhamList.add(sp);
                    sanPhamNgangAdapter.notifyDataSetChanged();
                }
                Collections.sort(sanPhamList, new Comparator<Sanpham>() {
                    @Override
                    public int compare(Sanpham sanpham, Sanpham t1) {
                        return sanpham.getFavorite() > t1.getFavorite() ?  -1 : 1;
                    }
                });
                spTop10homes.clear();
                for(int i = 0; i < sanPhamList.size(); i ++){
                    if(spTop10homes.size() < 10){
                        spTop10homes.add(sanPhamList.get(i));
                    }
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Sanpham sanpham = snapshot.getValue(Sanpham.class);
                if (sanpham == null)
                    return;
                for (int i = 0; i < sanPhamList.size(); i++) {
                    if (sanpham.getMasp().equals(sanPhamList.get(i).getMasp())) {
                        sanPhamList.set(i, sanpham);
                        break;
                    }
                }
                sanPhamNgangAdapter.notifyDataSetChanged();

                Collections.sort(sanPhamList, new Comparator<Sanpham>() {
                    @Override
                    public int compare(Sanpham sanpham, Sanpham t1) {
                        return sanpham.getFavorite() > t1.getFavorite() ?  -1 : 1;
                    }
                });
                spTop10homes.clear();
                for(int i = 0; i < sanPhamList.size(); i ++){
                    if(spTop10homes.size() < 10){
                        spTop10homes.add(sanPhamList.get(i));
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Sanpham sanpham = snapshot.getValue(Sanpham.class);
                if (sanpham == null)
                    return;
                for (int i = 0; i < sanPhamList.size(); i++) {
                    if (sanpham.getMasp().equals(sanPhamList.get(i).getMasp())) {
                        sanPhamList.remove(sanPhamList.get(i));
                        break;
                    }
                }
                sanPhamNgangAdapter.notifyDataSetChanged();

                Collections.sort(sanPhamList, new Comparator<Sanpham>() {
                    @Override
                    public int compare(Sanpham sanpham, Sanpham t1) {
                        return sanpham.getFavorite() > t1.getFavorite() ?  -1 : 1;
                    }
                });
                spTop10homes.clear();
                for(int i = 0; i < sanPhamList.size(); i ++){
                    if(spTop10homes.size() < 10){
                        spTop10homes.add(sanPhamList.get(i));
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void readDataSanPhamHome() {
        DatabaseReference SanPhams = FirebaseDatabase.getInstance().getReference("sanphams");
        SanPhams.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot == null)
                    return;

                Sanpham sp = snapshot.getValue(Sanpham.class);

                if (sp != null) {
                    sanphamHome.add(sp);
                    sanPhamHomeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Sanpham sanpham = snapshot.getValue(Sanpham.class);
                if (sanpham == null)
                    return;
                for (int i = 0; i < 20; i++) {
                    if (sanpham.getMasp().equals(sanphamHome.get(i).getMasp())) {
                        sanphamHome.set(i, sanpham);
                        break;
                    }
                }
                sanPhamNgangAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Sanpham sanpham = snapshot.getValue(Sanpham.class);
                if (sanpham == null)
                    return;
                for (int i = 0; i < 20; i++) {
                    if (sanpham.getMasp().equals(sanphamHome.get(i).getMasp())) {
                        sanphamHome.remove(sanphamHome.get(i));
                        break;
                    }
                }
                sanPhamNgangAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readDataLoaiSanPhamFromServer() {
        DatabaseReference LoaiSanPhams = FirebaseDatabase.getInstance().getReference("LoaiSanPhams");
        LoaiSanPhams.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot == null)
                    return;
                Loaisanpham lsp = snapshot.getValue(Loaisanpham.class);
                if (lsp != null) {
                    loaiSanPhams.add(lsp);
                    loaiSanPhamAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Loaisanpham loaisp = snapshot.getValue(Loaisanpham.class);
                if (loaisp == null)
                    return;
                for (int i = 0; i < loaiSanPhams.size(); i++) {
                    if (loaisp.getMaLoai().equals(loaiSanPhams.get(i).getMaLoai())) {
                        loaiSanPhams.set(i, loaisp);
                        break;
                    }
                }
                loaiSanPhamAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Loaisanpham loaisp = snapshot.getValue(Loaisanpham.class);
                if (loaisp == null)
                    return;
                for (int i = 0; i < loaiSanPhams.size(); i++) {
                    if (loaisp.getMaLoai().equals(loaiSanPhams.get(i).getMaLoai())) {
                        loaiSanPhams.remove(loaiSanPhams.get(i));
                        break;
                    }
                }
                loaiSanPhamAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
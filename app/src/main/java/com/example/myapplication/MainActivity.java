package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;



import com.bumptech.glide.Glide;
import com.example.myapplication.FRAGMENT.Favorite_Fragment;

import com.example.myapplication.FRAGMENT.FragmentUserProfile;
import com.example.myapplication.FRAGMENT.GioHangFragment;
import com.example.myapplication.FRAGMENT.HomeFragment;

import com.example.myapplication.MODEL.KhachHang;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {
    public  ChipNavigationBar chipNavigationBar;
    FragmentManager fragmentManager;
    CircleImageView avata_top;



    public DrawerLayout mdrawerLayout;
    public NavigationView navigationView;
    DatabaseReference referencekhs;
    FirebaseUser user;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hover();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            referencekhs = FirebaseDatabase.getInstance().getReference("KhachHangs");
            referencekhs.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        KhachHang kh = dataSnapshot.getValue(KhachHang.class);
                        if(kh==null){
                            return;
                        }
                        if(kh.getId().equals(user.getUid())){
                            if(kh.getImgURL().equals("default")){
                                avata_top.setImageResource(R.drawable.avatar_default);
                            }else{
                                Glide.with(MainActivity.this).load(kh.getImgURL()).into(avata_top);
                            }

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        } else {
            avata_top.setImageResource(R.drawable.logoo);
        }




        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.naHostFratment, new HomeFragment()).commit();
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i){
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.profile:
                        fragment = new FragmentUserProfile();
                        break;
                    case R.id.cart:
                        fragment = new GioHangFragment();
                        break;
                    case R.id.favorite:
                        fragment= new Favorite_Fragment();
                        break;



                }
                if(fragment!=null){
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.naHostFratment, fragment).commit();
                }
            }
        });

        findViewById(R.id.immenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setItemIconTintList(null);
        NavController navController = Navigation.findNavController(this, R.id.naHostFratment);
        NavigationUI.setupWithNavController(navigationView, navController);

        final TextView textTitle= findViewById(R.id.textTitle);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                textTitle.setText(navDestination.getLabel());
            }
        });
    }




    private void hover(){
        avata_top=findViewById(R.id.avata_top);
        chipNavigationBar = findViewById(R.id.chipNavigationbar);
        mdrawerLayout = findViewById(R.id.drawlayout);
        navigationView = findViewById(R.id.id_navigtion);

    }

}
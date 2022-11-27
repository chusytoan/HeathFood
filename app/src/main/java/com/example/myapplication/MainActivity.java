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


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.example.myapplication.FRAGMENT.FragmentProfile;
import com.example.myapplication.FRAGMENT.GioHangFragment;
import com.example.myapplication.FRAGMENT.HomeFragment;
import com.example.myapplication.MODEL.FCMSend;
import com.example.myapplication.MODEL.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {
    public  ChipNavigationBar chipNavigationBar;
    FragmentManager fragmentManager;



   public DrawerLayout mdrawerLayout;
   public NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hover();

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
                    case R.id.maps:
//                        fragment = new AddSanPhamFragment();
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        break;
                    case R.id.profile:
                        fragment = new FragmentProfile();
                        break;
                    case R.id.cart:
                        fragment = new GioHangFragment();
                        break;
                    case R.id.favorite:



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
        // navigation
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

        chipNavigationBar = findViewById(R.id.chipNavigationbar);
        mdrawerLayout = findViewById(R.id.drawlayout);
        navigationView = findViewById(R.id.id_navigtion);

    }

}
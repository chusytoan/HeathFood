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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.myapplication.FRAGMENT.FragmentUserProfile;
import com.example.myapplication.FRAGMENT.GioHangFragment;
import com.example.myapplication.FRAGMENT.HomeFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
//                        fragment=new HistoryFragmentMain();
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        break;
                    case R.id.profile:
                        fragment = new FragmentUserProfile();
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

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null){
            String name = currentUser.getDisplayName();
            String emial = currentUser.getEmail();

            Toast.makeText(this, name + "   "+emial, Toast.LENGTH_SHORT).show();
           // Log.d("MainActivity", name + "   "+emial);
        }

    }




    private void hover(){

        chipNavigationBar = findViewById(R.id.chipNavigationbar);
        mdrawerLayout = findViewById(R.id.drawlayout);
        navigationView = findViewById(R.id.id_navigtion);

    }

}
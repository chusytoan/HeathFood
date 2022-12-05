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
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.myapplication.FRAGMENT.Favorite_Fragment;
import com.example.myapplication.FRAGMENT.FragmentProfile;
import com.example.myapplication.FRAGMENT.FragmentUserProfile;
import com.example.myapplication.FRAGMENT.GioHangFragment;
import com.example.myapplication.FRAGMENT.HomeFragment;
import com.example.myapplication.MODEL.FCMSend;
import com.example.myapplication.MODEL.KhachHang;
import com.example.myapplication.MODEL.Token;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {
    public  ChipNavigationBar chipNavigationBar;
    FragmentManager fragmentManager;
    CircleImageView avata_top;



    public DrawerLayout mdrawerLayout;
    public NavigationView navigationView;
    DatabaseReference referencekhs;
    FirebaseUser user;
    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri img_uri;
    private StorageTask uploadtalk;
    private String muri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hover();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            finish();

      }else

        referencekhs = FirebaseDatabase.getInstance().getReference("KhachHangs");
        referencekhs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    KhachHang kh = dataSnapshot.getValue(KhachHang.class);
                    if(kh.getId().equals(user.getUid())){
                        Glide.with(MainActivity.this).load(kh.getImgURL()).into(avata_top);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        anhprofile();



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
                        fragment=new Favorite_Fragment();
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
        avata_top=findViewById(R.id.avata_top);
        chipNavigationBar = findViewById(R.id.chipNavigationbar);
        mdrawerLayout = findViewById(R.id.drawlayout);
        navigationView = findViewById(R.id.id_navigtion);
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
    }
    public void anhprofile(){
        avata_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImg();
            }
        });


    }

    private void openImg() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);

    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = this.getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading");
        dialog.show();
        if (img_uri != null) {
            final StorageReference storage = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(img_uri));
            uploadtalk = storage.putFile(img_uri);



            uploadtalk.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return storage.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Uri dowloaduri = (Uri) task.getResult();
                        muri = dowloaduri.toString();
                        // Log.d("CLMM", "clmm: " + muri);
                        Glide.with(MainActivity.this).load(muri).into(avata_top);
                        Map<String, Object> map = new HashMap<>();
                        map.put("imgURL", muri);
                        referencekhs.child(user.getUid()).updateChildren(map);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(MainActivity.this, "failed!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        } else {

            Toast.makeText(MainActivity.this, "no image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            img_uri = data.getData();
            if (uploadtalk != null && uploadtalk.isInProgress()) {
                Toast.makeText(MainActivity.this, "upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }

        }
    }
}
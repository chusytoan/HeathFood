package com.example.myapplication.FRAGMENT;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.MODEL.KhachHang;
import com.example.myapplication.MODEL.Token;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;


public class FragmentProfile extends Fragment {

    private View view;

EditText ed_address, ed_phone;
Button btn_update;
GifImageView avt_update;
String TAG  ="KHDSD";
List<KhachHang> list;
    private Uri muri;

    FirebaseFirestore db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_profile, container, false);
        anhXa();
        db= FirebaseFirestore.getInstance();
        FirebaseUser userCurrent = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference referencekhs = FirebaseDatabase.getInstance().getReference("KhachHangs");
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userCurrent==null){
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    return;
                }
                if(ed_address.getText().toString().equals("")){
                    Toast.makeText(getContext(), "khong duoc de trong dia chi", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(ed_phone.getText().toString().equals("")){
                    Toast.makeText(getContext(), "khong dc de trong sdt", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Object> map = new HashMap<>();
                map.put("diachi", ed_address.getText().toString());
                map.put("sdt", ed_phone.getText().toString());

                referencekhs.child(userCurrent.getUid()).updateChildren(map);

                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    return;
                                }
                                // Get new FCM registration token
                                String token = task.getResult();
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference tokens = database.getReference("Tokens");
                                Token token_Model = new Token();
                                token_Model.setToken(token);
                                token_Model.setServerToken(false);
                                tokens.child(ed_phone.getText().toString()).setValue(token_Model);

                            }
                        });
                Toast.makeText(getContext(), "cap nhat thong tin thanh cong", Toast.LENGTH_SHORT).show();
            }
        });
        avt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });

        return view;
    }
    private void anhXa(){
        ed_address = view.findViewById(R.id.ed_adress);
        ed_phone = view.findViewById(R.id.ed_phone);
        btn_update = view.findViewById(R.id.btn_update);
        avt_update = view.findViewById(R.id.avt_update);
    }

}
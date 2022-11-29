package com.example.myapplication.FRAGMENT;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.ProfileSetting;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class FragmentUserProfile extends Fragment {
    private View view;
    private LoginActivity loginActivity;
    TextView tv_gmail,tv_name;
    ImageView img_setting;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.layout_profile, container, false);
        anhxa();
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
//        String email= firebaseUser.getEmail();
//        String name=firebaseUser.getDisplayName();
//        tv_gmail.setText(email);
//        tv_name.setText(name);
        img_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ProfileSetting.class);
                startActivity(intent);



            }
        });



        return view;
    }
    public void anhxa(){
        img_setting=view.findViewById(R.id.setting);
        tv_gmail=view.findViewById(R.id.gmail_user);
        tv_name=view.findViewById(R.id.name_user);
    }


}

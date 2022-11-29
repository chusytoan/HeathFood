package com.example.myapplication.FRAGMENT;

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
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.ADAPTER.HistoryAdapterPager;
import com.example.myapplication.LoginActivity;
import com.example.myapplication.ProfileSetting;
import com.example.myapplication.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FragmentUserProfile extends Fragment {
    private View view;
    private LoginActivity loginActivity;
    TextView tv_gmail,tv_name;
    ImageView img_setting;
    private ViewPager2 m_pager;
    private TabLayout mTablayout;
    private HistoryAdapterPager historyAdapterPager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.layout_profile, container, false);
        anhxa();
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String email= firebaseUser.getEmail();
        String name=firebaseUser.getDisplayName();
        tv_gmail.setText(email);
        tv_name.setText(name);
        img_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ProfileSetting.class);
                startActivity(intent);

            }
        });
        tabLayout();



        return view;
    }
    public void anhxa(){
        img_setting=view.findViewById(R.id.setting);
        tv_gmail=view.findViewById(R.id.gmail_user);
        tv_name=view.findViewById(R.id.name_user);
        m_pager=view.findViewById(R.id.history_pager);
        mTablayout=view.findViewById(R.id.tab_history);
    }
    public void tabLayout(){
        historyAdapterPager =new HistoryAdapterPager(this);
        m_pager.setAdapter(historyAdapterPager);

        TabLayoutMediator mediator=new TabLayoutMediator(mTablayout, m_pager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position){
                            case 0:
                                tab.setIcon(R.drawable.history).setText("Chờ xác nhận");
                                break;
                            case 1:
                                tab.setIcon(R.drawable.express).setText("Đang giao");
                                break;
                            case 2:
                                tab.setIcon(R.drawable.history2).setText("Lịch sử mua");
                                break;
                        }

                    }
                });
        mediator.attach();
    }


}

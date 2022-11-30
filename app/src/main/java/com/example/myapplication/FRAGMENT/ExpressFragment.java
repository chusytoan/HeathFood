package com.example.myapplication.FRAGMENT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.ADAPTER.DonHangAdapter;
import com.example.myapplication.MODEL.DonHang;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ExpressFragment extends Fragment {
    ListView listView;
    DonHangAdapter adapter;
    List<DonHang> list;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.express_fragment,container,false);
        Init();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DonHangs");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DonHang dh = snapshot.getValue(DonHang.class);
                if(dh.getSdt().equals(firebaseUser.getPhoneNumber())){
                    if(dh.getTrangThai().equals("Delivering")){
                        list.add(dh);
                        adapter.notifyDataSetChanged();
                    }

                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DonHang donHang = snapshot.getValue(DonHang.class);
                if(list==null || list.isEmpty()|| donHang==null){
                    return;
                }

                for(int i = 0; i < list.size();i++){
                        if(donHang.getMaDonHang().equals(list.get(i).getMaDonHang())&&donHang.getTrangThai().equals("Delivering") ){
                            list.set(i, donHang);

                        }


                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
    private void Init(){
        listView = view.findViewById(R.id.lv_choxacnhan);
        list = new ArrayList<>();
        adapter = new DonHangAdapter(getContext(), list);

        listView.setAdapter(adapter);
    }
    private void ResetDataUpdate(DonHang donHang){
        adapter = new DonHangAdapter(getContext(), list);
        listView.setAdapter(adapter);
    }
}

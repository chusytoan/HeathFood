package com.example.myapplication.ADAPTER;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.FRAGMENT.ChoxacnhanFragment;
import com.example.myapplication.FRAGMENT.ExpressFragment;
import com.example.myapplication.FRAGMENT.HistoryFragment;

public class HistoryAdapterPager extends FragmentStateAdapter {
    int soLuong=3;

    public HistoryAdapterPager(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment=null;

        switch (position){
            case 0:
                fragment=new ChoxacnhanFragment();
                break;
            case 1:
                fragment=new ExpressFragment();
                break;
            case 2:
                fragment=new HistoryFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return soLuong;
    }
}

package com.example.myapplication.ADAPTER;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.MODEL.KhachHang;
import com.example.myapplication.R;

import java.util.List;

public class SpinnerAddressAdapter extends BaseAdapter {

    private List<KhachHang> list;

    public SpinnerAddressAdapter(List<KhachHang> list) {

        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    public class Viewholder{
        TextView tv_address;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Viewholder holder;
        if(view==null){
            holder = new Viewholder();
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_spinner_address,viewGroup,false);
            holder.tv_address = view.findViewById(R.id.tv_Address);
            view.setTag(holder);
        }else
            holder = (Viewholder) view.getTag();

        KhachHang kh = list.get(i);
        holder.tv_address.setText(kh.getDiachi());
        return view;
    }
}

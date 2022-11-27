package com.example.myapplication.ADAPTER;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.MODEL.Loaisanpham;
import com.example.myapplication.R;

import java.util.List;

public class SpinnerLoaiSanPhamAdapter extends BaseAdapter {
    private Context context;
    private List<Loaisanpham> list;

    public SpinnerLoaiSanPhamAdapter(Context context, List<Loaisanpham> list) {
        this.context = context;
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
        TextView tv_name,tv_ma;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Viewholder holder;
        if(view == null){
            holder = new Viewholder();
            view = LayoutInflater.from(context).inflate(R.layout.item_spinner,viewGroup,false);
            holder.tv_name = view.findViewById(R.id.tv_name_loaisp_spinner);
            holder.tv_ma = view.findViewById(R.id.tv_ma_loai_spinner);
            view.setTag(holder);
        }else {
            holder = (Viewholder) view.getTag();
        }

        Loaisanpham lsp = list.get(i);
        holder.tv_name.setText(lsp.getName());
        holder.tv_ma.setText(lsp.getMaLoai());
        return view;
    }
}

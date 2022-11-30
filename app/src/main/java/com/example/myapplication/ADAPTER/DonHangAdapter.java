package com.example.myapplication.ADAPTER;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.MODEL.DonHang;
import com.example.myapplication.R;

import java.util.List;

public class DonHangAdapter extends BaseAdapter {
    private Context context;
    private List<DonHang> list;

    public DonHangAdapter(Context context, List<DonHang> list) {
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
        TextView tv_ma, tv_sdt, tv_dc, tv_trangthai;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Viewholder itemView;
        if(view==null){
            itemView = new Viewholder();
            view = LayoutInflater.from(context).inflate(R.layout.item_don_hang_user, viewGroup, false);

            itemView.tv_trangthai = view.findViewById(R.id.tv_trangthai);
            itemView.tv_ma = view.findViewById(R.id.tv_ma_don);
            itemView.tv_sdt = view.findViewById(R.id.tv_sdt);
            itemView.tv_dc = view.findViewById(R.id.tv_dia_chi);

            view.setTag(itemView);
        }else
            itemView = (Viewholder) view.getTag();

        DonHang donHang = list.get(i);
        itemView.tv_trangthai.setText(donHang.getTrangThai());
        itemView.tv_dc.setText(donHang.getDiaChi());
        itemView.tv_sdt.setText(donHang.getSdt());
        itemView.tv_ma.setText(donHang.getMaDonHang());

        return view;
    }
}

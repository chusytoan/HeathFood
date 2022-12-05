package com.example.myapplication.ADAPTER;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.MODEL.DanhGia;
import com.example.myapplication.R;

import java.util.List;

public class DanhgiaAdapter extends RecyclerView.Adapter<DanhgiaAdapter.Viewholder> {
    private Context context;
    private List<DanhGia> list;

    public DanhgiaAdapter(Context context, List<DanhGia> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false);

        return  new Viewholder(view);
    }
    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        DanhGia dg = list.get(position);
        if(dg==null)
            return;
        holder.tv_sao.setText(dg.getSosaoDanhgia()+"");
    }

    @Override
    public int getItemCount() {
        if(list != null)
            return list.size();
        return 0;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView tv_sao;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tv_sao=itemView.findViewById(R.id.tv_sosao);

        }
    }
}

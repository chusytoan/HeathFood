package com.example.myapplication.ADAPTER;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.ChiTietSanPham;
import com.example.myapplication.MODEL.Sanpham;
import com.example.myapplication.R;

import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.Viewholder> {
    private Context context;
    private List<Sanpham> list;
    ImageView img_delete;

    public SanPhamAdapter(Context context, List<Sanpham> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sanpham,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Sanpham sp =list.get(position);
        if(sp == null)
            return;

        Glide.with(context).load(sp.getImgURL()).into(holder.img_sp);
        holder.tv_ten.setText(sp.getName());
       // holder.tv_mo_Ta.setText("mô tả:" +sp.getDescribe());



        holder.tv_gia.setText("Giá: " + sp.getPrice()+"$");
        holder.tv_ten_loai.setText(sp.getTen_loai());

        if(sp.getDescribe()!=null){
            if(sp.getDescribe().length()>50){
                holder.tv_mo_Ta.setText(sp.getDescribe().substring(0,50) + "...");
            }else {
                holder.tv_mo_Ta.setText(sp.getDescribe());
            }
        }
        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.img_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.itemsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChiTietSanPham.class);
                //String masp, String name, double price, int time_ship, String describe, int amount, boolean favorite, String imgURL,
                // Map<String, Comment> comments,String ten_loai,int starDanhGia
                intent.putExtra("maSP" , sp.getMasp());
                intent.putExtra("name", sp.getName());
                intent.putExtra("donGia", sp.getPrice());
                intent.putExtra("hinhAnh", sp.getImgURL());
                intent.putExtra("moTa", sp.getDescribe());
                intent.putExtra("star", sp.getStarDanhGia());
                intent.putExtra("favorite", sp.getFavorite());
                intent.putExtra("time", sp.getTime_ship());
                intent.putExtra("tenLoai", sp.getTen_loai());


                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(list!=null)
            return list.size();
        return 0;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView img_sp, img_start_four, img_start_five,img_Edit;
        TextView tv_ten,tv_mo_Ta, tv_gia, tv_ten_loai,tv_luotBan;
        CardView itemsp;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            img_sp = itemView.findViewById(R.id.img_sanpham);
            img_start_four = itemView.findViewById(R.id.img_start4);
            img_start_five = itemView.findViewById(R.id.img_start5);
//            img_Edit = itemView.findViewById(R.id.img_edit);
//            img_delete = itemView.findViewById(R.id.img_deleSP);
            tv_ten = itemView.findViewById(R.id.tv_ten_sp);
            tv_mo_Ta = itemView.findViewById(R.id.tv_mota);
            tv_gia = itemView.findViewById(R.id.tv_gia);
            tv_ten_loai = itemView.findViewById(R.id.tv_loaips);
            tv_luotBan = itemView.findViewById(R.id.tv_luotban);
            itemsp = itemView.findViewById(R.id.item_sp);

        }
    }

}

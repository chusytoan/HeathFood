package com.example.myapplication.ADAPTER;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.myapplication.ChiTietSanPham;
import com.example.myapplication.MODEL.Loaisanpham;
import com.example.myapplication.MODEL.Sanpham;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class SanPhamGridAdapter extends BaseAdapter {
    private Context context;
    private List<Sanpham> list;

    int tb;

    public SanPhamGridAdapter(Context context, List<Sanpham> list) {
        this.context = context;
        this.list = list;
    }

    public void setfilterliss(List<Sanpham> fiteliss) {
        this.list = fiteliss;
        notifyDataSetChanged();
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

    public class Viewholder {
        ImageView img_sp, img_start_four, img_start_five, img_Edit, img_delete, img_start_one, img_start_tow, img_start_three;
        TextView tv_ten, tv_mo_Ta, tv_gia, tv_ten_loai, tv_luotBan, statsao;
        CardView itemsp;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Viewholder holder;
        if (view == null) {
            holder = new Viewholder();
            view = LayoutInflater.from(context).inflate(R.layout.item_sanpham, viewGroup, false);

            holder.img_sp = view.findViewById(R.id.img_sanpham);
            holder.img_start_four = view.findViewById(R.id.img_start4);
            holder.img_start_five = view.findViewById(R.id.img_start5);
            holder.img_start_one = view.findViewById(R.id.img_start1);
            holder.img_start_tow = view.findViewById(R.id.img_start2);
            holder.img_start_three = view.findViewById(R.id.img_start3);

            holder.tv_ten = view.findViewById(R.id.tv_ten_sp);
            holder.tv_mo_Ta = view.findViewById(R.id.tv_mota);
            holder.tv_gia = view.findViewById(R.id.tv_gia);
            holder.tv_ten_loai = view.findViewById(R.id.tv_loaips);
            holder.tv_luotBan = view.findViewById(R.id.tv_luotban);
            holder.itemsp = view.findViewById(R.id.item_sp);
            view.setTag(holder);
        } else
            holder = (Viewholder) view.getTag();

        Sanpham sp = list.get(i);


        Glide.with(context).load(sp.getImgURL()).into(holder.img_sp);
        holder.tv_ten.setText(sp.getName());

        holder.tv_gia.setText("Giá: " + sp.getPrice() + "$");
        holder.tv_ten_loai.setText(sp.getTen_loai());


        if (sp.getDescribe() != null) {
            if (sp.getDescribe().length() > 50) {
                holder.tv_mo_Ta.setText(sp.getDescribe().substring(0, 50) + "...");
            } else {
                holder.tv_mo_Ta.setText(sp.getDescribe());
            }
        }

        DatabaseReference SanPhams = FirebaseDatabase.getInstance().getReference("sanphams");
        SanPhams.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot == null)
                    return;
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Sanpham spss = dataSnapshot.getValue(Sanpham.class);
                    if(spss==null)
                        return;

                    if (sp.getMasp() == null)
                        return;
                    if (sp.getMasp().equals(spss.getMasp())) {
                        switch (sp.getStarDanhGia()) {
                            case 1:
                                holder.img_start_one.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                holder.img_start_one.setVisibility(View.VISIBLE);
                                holder.img_start_tow.setVisibility(View.VISIBLE);
                                break;
                            case 3:
                                holder.img_start_one.setVisibility(View.VISIBLE);
                                holder.img_start_tow.setVisibility(View.VISIBLE);
                                holder.img_start_three.setVisibility(View.VISIBLE);
                                break;
                            case 4:
                                holder.img_start_one.setVisibility(View.VISIBLE);
                                holder.img_start_tow.setVisibility(View.VISIBLE);
                                holder.img_start_three.setVisibility(View.VISIBLE);
                                holder.img_start_four.setVisibility(View.VISIBLE);
                                break;
                            case 5:
                                holder.img_start_one.setVisibility(View.VISIBLE);
                                holder.img_start_tow.setVisibility(View.VISIBLE);
                                holder.img_start_three.setVisibility(View.VISIBLE);
                                holder.img_start_four.setVisibility(View.VISIBLE);
                                holder.img_start_five.setVisibility(View.VISIBLE);
                                break;
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        holder.itemsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChiTietSanPham.class);
                //String masp, String name, double price, int time_ship, String describe, int amount, boolean favorite, String imgURL,
                // Map<String, Comment> comments,String ten_loai,int starDanhGia
                intent.putExtra("maSP", sp.getMasp());
                intent.putExtra("name", sp.getName());
                intent.putExtra("donGia", sp.getPrice());
                intent.putExtra("hinhAnh", sp.getImgURL());
                intent.putExtra("moTa", sp.getDescribe());
                intent.putExtra("star", sp.getStarDanhGia());
                intent.putExtra("favorite", sp.getFavorite());
                intent.putExtra("time", sp.getTime_ship());
                intent.putExtra("MaLoai", sp.getMaLoai());
                context.startActivity(intent);
            }
        });
        return view;
    }


}

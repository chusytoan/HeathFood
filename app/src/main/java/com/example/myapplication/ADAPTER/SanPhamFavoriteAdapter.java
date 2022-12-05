package com.example.myapplication.ADAPTER;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.ChiTietSanPham;
import com.example.myapplication.FRAGMENT.Favorite_Fragment;
import com.example.myapplication.MODEL.Sanpham;
import com.example.myapplication.MODEL.SanphamFavorite;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class SanPhamFavoriteAdapter extends RecyclerView.Adapter<SanPhamFavoriteAdapter.ViewHolder> {
    private Context context;
    private List<SanphamFavorite> listYT;

    DatabaseReference likes;
    Favorite_Fragment favorite_fragment;

    public SanPhamFavoriteAdapter(Context context, List<SanphamFavorite> listYT) {
        this.context = context;
        this.listYT = listYT;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sp_favorite, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SanphamFavorite spYT = listYT.get(position);
        if (spYT == null)
            return;

        Glide.with(context).load(spYT.getHinhAnh()).into(holder.img_sp);
        holder.tv_name.setText(spYT.getTenSanPham());
        holder.tv_price.setText(spYT.getDonGia() + "$");
        holder.tv_minutes.setText(spYT.getTime_ship() + "minutes");
        holder.tv_ten_loai.setText(spYT.getTen_loai());
        DatabaseReference sanphamYT=FirebaseDatabase.getInstance().getReference("SanPhamFavorite");
        DatabaseReference likes=FirebaseDatabase.getInstance().getReference("tyms");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String idUser = user.getUid();
        String maSp = spYT.getMaSP();

        holder.ln_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChiTietSanPham.class);

                intent.putExtra("maSP" , spYT.getMaSP());
                intent.putExtra("name", spYT.getTenSanPham());
                intent.putExtra("donGia", spYT.getDonGia());
                intent.putExtra("hinhAnh", spYT.getHinhAnh());
                intent.putExtra("moTa", spYT.getMota());
                intent.putExtra("star", spYT.getStarDanhGia());
                intent.putExtra("favorite", spYT.getFavorite());
                intent.putExtra("time", spYT.getTime_ship());
                intent.putExtra("MaLoai", spYT.getMaLoai());
                context.startActivity(intent);



            }
        });
        //holder.tv_soLuotTym.setText(sp.getFavorite()+"");

    }

    @Override
    public int getItemCount() {
        if (listYT != null)
            return listYT.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_sp, img_favorite, imgFood_favorite;
        TextView tv_name, tv_price, tv_minutes, tv_ten_loai, tv_soLuotTym;
        LinearLayout ln_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_sp = itemView.findViewById(R.id.img_food);
            // img_favorite = itemView.findViewById(R.id.imgFood_favorite);
            tv_name = itemView.findViewById(R.id.tv_namOfFood);
            tv_price = itemView.findViewById(R.id.tv_price_food);
            tv_minutes = itemView.findViewById(R.id.tv_time_ship);
            tv_ten_loai = itemView.findViewById(R.id.tv_gerMan_food);
            tv_soLuotTym = itemView.findViewById(R.id.tv_luotTym);
            imgFood_favorite = itemView.findViewById(R.id.imgFood_favorite);
            ln_delete = itemView.findViewById(R.id.delete_fv);

        }



        }






}

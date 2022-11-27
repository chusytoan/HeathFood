package com.example.myapplication.ADAPTER;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.ChiTietSanPham;
import com.example.myapplication.FRAGMENT.HomeFragment;
import com.example.myapplication.LoginActivity;
import com.example.myapplication.MODEL.Loaisanpham;
import com.example.myapplication.MODEL.NhanVien;
import com.example.myapplication.MODEL.Sanpham;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.InputStream;
import java.util.List;


public class SanPhamNgangAdapter extends RecyclerView.Adapter<SanPhamNgangAdapter.ViewHolder> {
    private Context context;
    private List<Sanpham>list;

    public HomeFragment fragHome = new HomeFragment();
    public SanPhamNgangAdapter(Context context, List<Sanpham> list) {
        this.context = context;
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sp_horizoltal,parent,false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sanpham sp = list.get(position);
        if(sp==null)
            return;

        //new DownloadImageTask(holder.img_sp).execute(sp.getImgURL());
        Glide.with(context).load(sp.getImgURL()).into(holder.img_sp);
        holder.tv_name.setText(sp.getName());
        holder.tv_price.setText(sp.getPrice()+"$");
        holder.tv_minutes.setText(sp.getTime_ship()+"minutes");
        holder.tv_ten_loai.setText(sp.getTen_loai());
        holder.tv_soLuotTym.setText(sp.getFavorite()+"");
        FirebaseUser usercurent = FirebaseAuth.getInstance().getCurrentUser();
        holder.img_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        if(list!=null)
            return list.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_sp, img_favorite, tym_bay;
        TextView tv_name, tv_price, tv_minutes, tv_ten_loai,tv_soLuotTym;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_sp = itemView.findViewById(R.id.img_food);
            img_favorite = itemView.findViewById(R.id.imgFood_favorite);
            tv_name = itemView.findViewById(R.id.tv_namOfFood);
            tv_price = itemView.findViewById(R.id.tv_price_food);
            tv_minutes = itemView.findViewById(R.id.tv_time_ship);
            tv_ten_loai = itemView.findViewById(R.id.tv_gerMan_food);
            tv_soLuotTym = itemView.findViewById(R.id.tv_luotTym);
            tym_bay = itemView.findViewById(R.id.imgFood_favoritebay);

        }

    }


}

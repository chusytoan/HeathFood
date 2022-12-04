package com.example.myapplication.ADAPTER;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.myapplication.MODEL.GioHang;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.VIewholder>{
    private Context context;
    private List<GioHang> list;

    String TAG = "GIOHANGADAPTER";
    public GioHangAdapter(Context context, List<GioHang> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VIewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gio_hang,parent,false);

        return new VIewholder(view);
    }
    int count = 1;
    @Override
    public void onBindViewHolder(@NonNull VIewholder holder, int position) {
        GioHang gh = list.get(position);
        if(gh==null)
            return;

        Glide.with(context).load(gh.getHinhAnh()).into(holder.img_sp);
        holder.tvTenSp.setText(gh.getTenSanPham());
        holder.tvGia.setText("" + gh.getDonGia());
        holder.tvSoLuong.setText("" + gh.getSoLuong());
        count  = gh.getSoLuong();

        holder.item_gh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(context, "rac af", Toast.LENGTH_SHORT).show();
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_delete);
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button yes = dialog.findViewById(R.id.yes);
                dialog.show();
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GioHangs");
                        reference.child(gh.getMaSP()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "xoa thanh cong", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                    }
                });
            }
        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VIewholder extends RecyclerView.ViewHolder {
        ImageView img_sp;
        TextView  tvTenSp, tvGia, tvSoLuong;
        CardView item_gh;

        public VIewholder(@NonNull View itemView) {
            super(itemView);
            img_sp = itemView.findViewById(R.id.img_sp_gh);
            tvTenSp= itemView.findViewById(R.id.tv_tensp);
            tvGia = itemView.findViewById(R.id.tv_gia);
            tvSoLuong = itemView.findViewById(R.id.tv_soluong);
            item_gh = itemView.findViewById(R.id.item_gh);

        }
    }
}

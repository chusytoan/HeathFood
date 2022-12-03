package com.example.myapplication.ADAPTER;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.myapplication.ChiTietSanPham;
import com.example.myapplication.MODEL.Loaisanpham;
import com.example.myapplication.MODEL.Sanpham;
import com.example.myapplication.R;
import com.example.myapplication.UpdateSanpham;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SanPhamGridAdapter extends BaseAdapter {
    private Context context;
    private List<Sanpham> list;

    public SanPhamGridAdapter(Context context, List<Sanpham> list) {
        this.context = context;
        this.list = list;
    }
    public void setfilterliss(List<Sanpham> fiteliss) {
        this.list=fiteliss;
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

    public class Viewholder{
        ImageView img_sp, img_start_four, img_start_five,img_Edit, img_delete;
        TextView tv_ten,tv_mo_Ta, tv_gia, tv_ten_loai,tv_luotBan;
        CardView itemsp;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Viewholder holder;
        if(view == null){
            holder = new Viewholder();
            view = LayoutInflater.from(context).inflate(R.layout.item_sanpham, viewGroup, false);

            holder.img_sp = view.findViewById(R.id.img_sanpham);
            holder.img_start_four = view.findViewById(R.id.img_start4);
            holder.img_start_five = view.findViewById(R.id.img_start5);
//            holder.img_Edit = view.findViewById(R.id.img_edit);
//            holder.img_delete = view.findViewById(R.id.img_deleSP);
            holder.tv_ten = view.findViewById(R.id.tv_ten_sp);
            holder.tv_mo_Ta = view.findViewById(R.id.tv_mota);
            holder.tv_gia = view.findViewById(R.id.tv_gia);
            holder.tv_ten_loai =view.findViewById(R.id.tv_loaips);
            holder.tv_luotBan = view.findViewById(R.id.tv_luotban);
            holder.itemsp = view.findViewById(R.id.item_sp);
            view.setTag(holder);
        }else
            holder = (Viewholder) view.getTag();

        Sanpham sp = list.get(i);
        Glide.with(context).load(sp.getImgURL()).into(holder.img_sp);
        holder.tv_ten.setText(sp.getName());

        holder.tv_gia.setText("Giá: " + sp.getPrice()+"$");
        holder.tv_ten_loai.setText(sp.getTen_loai());
        if(sp.getDescribe()!=null){
            if(sp.getDescribe().length()>50){
                holder.tv_mo_Ta.setText(sp.getDescribe().substring(0,50) + "...");
            }else {
                holder.tv_mo_Ta.setText(sp.getDescribe());
            }
        }

//        holder.img_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                deleteSanpham(sp.getMasp(), sp.getMaLoai());
//
//            }
//        });
//        holder.img_Edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, UpdateSanpham.class);
//                //String masp, String name, double price, int time_ship, String describe, int amount, boolean favorite, String imgURL,
//                // Map<String, Comment> comments,String ten_loai,int starDanhGia
//                intent.putExtra("maSPUP" , sp.getMasp());
//                intent.putExtra("nameUP", sp.getName());
//                intent.putExtra("donGiaUP", sp.getPrice());
//                intent.putExtra("hinhAnhUP", sp.getImgURL());
//                intent.putExtra("moTaUP", sp.getDescribe());
//                intent.putExtra("timeUP", sp.getTime_ship());
//                intent.putExtra("MaLoai", sp.getMaLoai());
//
//
//
//                context.startActivity(intent);
//
//            }
//        });

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
                intent.putExtra("MaLoai", sp.getMaLoai());
                context.startActivity(intent);
            }
        });
        return view;
    }

//    public void deleteSanpham(String maspD,String maLoai) {
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("LoaiSanPhams").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                Dialog dialog = new Dialog(context);
//                dialog.setContentView(R.layout.dialog_delete);
//                Window window = dialog.getWindow();
//                if (window == null) return;
//                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                Button yes = dialog.findViewById(R.id.yes);
//                yes.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (task.isSuccessful()) {
//
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//
//                                Loaisanpham lsp =(Loaisanpham) document.toObject(Loaisanpham.class);
//                                DocumentReference docRef = db.collection("LoaiSanPhams").document(lsp.getMaLoai());
//                                docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                                    @Override
//                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                                        if (lsp.getMaLoai().equals(maLoai)) {
//                                            Map<String, Object> updates = new HashMap<>();
//                                            updates.put("sanphams."+maspD, FieldValue.delete());
//                                            docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    if(task.isSuccessful()) {
//                                                        Toast.makeText(context, "xoa thanh cong", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                }
//                                            }).addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//                                                    Toast.makeText(context, "xoá thất bại", Toast.LENGTH_SHORT).show();
//                                                }
//                                            });
//                                        }
//                                    }
//                                });
//
//                            }
//                        }
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
//
//            }
//        });
//    }

}

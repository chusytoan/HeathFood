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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.ChiTietSanPham;
import com.example.myapplication.FRAGMENT.HomeFragment;
import com.example.myapplication.LoginActivity;
import com.example.myapplication.MODEL.Loaisanpham;
import com.example.myapplication.MODEL.NhanVien;
import com.example.myapplication.MODEL.Sanpham;
import com.example.myapplication.MODEL.SanphamFavorite;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.InputStream;
import java.util.List;


public class SanPhamNgangAdapter extends RecyclerView.Adapter<SanPhamNgangAdapter.ViewHolder> {
    private Context context;
    private List<Sanpham> list;

    DatabaseReference likes;

    public SanPhamNgangAdapter(Context context, List<Sanpham> list) {
        this.context = context;
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sp_horizoltal, parent, false);

        return new ViewHolder(view);
    }

    boolean testclick = false;

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sanpham sp = list.get(position);
        if (sp == null)
            return;

        Glide.with(context).load(sp.getImgURL()).into(holder.img_sp);
        holder.tv_name.setText(sp.getName());
        holder.tv_price.setText(sp.getPrice() + "$");
        holder.tv_minutes.setText(sp.getTime_ship() + "minutes");
        holder.tv_ten_loai.setText(sp.getTen_loai());

        FirebaseUser usercurent = FirebaseAuth.getInstance().getCurrentUser();
        if (usercurent==null){

            return;

        }
        String idUser = usercurent.getUid();
        String maSp = sp.getMasp();
        String maLoai = sp.getMaLoai();
        String tenLoai=sp.getTen_loai();
        String tenSp=sp.getName();
        double donGia=sp.getPrice();
        int star=sp.getStarDanhGia();
        int tym=sp.getFavorite();
        int time_ship=sp.getTime_ship();
        String mota=sp.getDescribe();
        String imgAnh=sp.getImgURL();
        if(usercurent==null){
            holder.getLikeWhenUserSigOut(maSp);
            return;
        }
        holder.getLikeButtonStatus(maSp, idUser, maLoai);



        likes = FirebaseDatabase.getInstance().getReference("tyms");
        DatabaseReference  reference = FirebaseDatabase.getInstance().getReference("SanPhamFavorite");

        holder.imgFood_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usercurent==null){
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
                testclick = true;
                likes.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(testclick==true){
                            if(snapshot.child(maSp).hasChild(idUser)){
                                likes.child(maSp).child(idUser).removeValue();
                                reference.child(idUser).child(maSp).removeValue();
                                testclick = false;
                            }else {
                                likes.child(maSp).child(idUser).setValue(true);
                                holder.SanphamFavorite(idUser,maSp,maLoai,tenLoai,tenSp,imgAnh,donGia,tym,time_ship,star,mota);
                                testclick = false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_sp, imgFood_favorite;
        TextView tv_name, tv_price, tv_minutes, tv_ten_loai, tv_soLuotTym;

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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Sanpham sp= list.get(getAdapterPosition());
                    Intent intent = new Intent(context, ChiTietSanPham.class);
                    intent.putExtra("maSP" , sp.getMasp());
                    intent.putExtra("name", sp.getName());
                    intent.putExtra("donGia", sp.getPrice());
                    intent.putExtra("hinhAnh", sp.getImgURL());
                    intent.putExtra("moTa", sp.getDescribe());
                    intent.putExtra("star", sp.getStarDanhGia());
                    intent.putExtra("favorite", sp.getFavorite());
                    intent.putExtra("time", sp.getTime_ship());
                    intent.putExtra("tenLoai", sp.getMaLoai());


                    context.startActivity(intent);
                }
            });

        }
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        public void getLikeButtonStatus(String maSp, String idUser, String maLoai) {
            likes = FirebaseDatabase.getInstance().getReference("tyms");
            likes.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(maSp).hasChild(idUser)) {
                        int likeCount = (int) snapshot.child(maSp).getChildrenCount();
                        tv_soLuotTym.setText(likeCount + " favorites");
                        firestore.collection("LoaiSanPhams").document(maLoai).update("sanphams." + maSp +".favorite", likeCount);
                        imgFood_favorite.setImageResource(R.drawable.ic_favorite_24);
                    } else {
                        int likeCount = (int) snapshot.child(maSp).getChildrenCount();
                        tv_soLuotTym.setText(likeCount + " favorites");
                        firestore.collection("LoaiSanPhams").document(maLoai).update("sanphams." + maSp +".favorite", likeCount);
                        imgFood_favorite.setImageResource(R.drawable.ic_favorite_border_24);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        public void getLikeWhenUserSigOut(String maSP){
            likes = FirebaseDatabase.getInstance().getReference("tyms");
            likes.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int likeCount = (int) snapshot.child(maSP).getChildrenCount();
                    tv_soLuotTym.setText(likeCount + " favorites");
                    imgFood_favorite.setImageResource(R.drawable.ic_favorite_24);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        public  void SanphamFavorite(String idUser, String maSP, String maLoai,String tenLoai,
                                     String tenSP,String hinhAnh,double donGia, int favorite,int timeship,int star,String mota){
            SanphamFavorite spYT=new SanphamFavorite();
            spYT.setIdUser(idUser);
            spYT.setMaSP(maSP);
            spYT.setMaLoai(maLoai);
            spYT.setTen_loai(tenLoai);
            spYT.setTenSanPham(tenSP);
            spYT.setHinhAnh(hinhAnh);
            spYT.setDonGia(donGia);
            spYT.setFavorite(favorite);
            spYT.setTime_ship(timeship);
            spYT.setStarDanhGia(star);
            spYT.setMota(mota);

            DatabaseReference  reference = FirebaseDatabase.getInstance().getReference("SanPhamFavorite");
            reference.child(idUser).child(maSP).setValue(spYT, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    Toast.makeText(context, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                }
            });



        }
    }


}

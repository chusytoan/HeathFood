package com.example.myapplication.ADAPTER;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.DanhSachSanPham;
import com.example.myapplication.MODEL.Loaisanpham;
import com.example.myapplication.R;
import com.example.myapplication.UpdateLoaisanpham;
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

public class LoaiSanPhamAdapter extends RecyclerView.Adapter<LoaiSanPhamAdapter.ViewHolder>{
    private Context context;
    private List<Loaisanpham> list;

    public LoaiSanPhamAdapter(Context context, List<Loaisanpham> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_loai_sp,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Loaisanpham lsp = list.get(position);
        if(lsp==null)
            return;

        Glide.with(context).load(lsp.getImgURL()).into(holder.img_lsp);
        holder.tv_name.setText(lsp.getName());

//        holder.img_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, UpdateLoaisanpham.class);
//                intent.putExtra("maLoai",lsp.getMaLoai());
//                intent.putExtra("tenLoai", lsp.getName());
//                intent.putExtra("hinhanhLoai",lsp.getImgURL());
//                context.startActivity(intent);
//            }
//        });
//        holder.img_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
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
//                        FirebaseFirestore db = FirebaseFirestore.getInstance();
//                        db.collection("LoaiSanPhams").document(list.get(holder.getAdapterPosition()).getMaLoai()).delete();
//                        Toast.makeText(context, "Xoá Loại sản phẩm thành công", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
//            }
//
//        });

//        holder.img_lsp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, DanhSachSanPham.class);
//                intent.putExtra("tenLoai", lsp.getName());
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if(list!=null)
            return list.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        ImageView img_lsp, img_edit,img_delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_tenLoai);
            img_lsp =itemView.findViewById(R.id.img_lsp);
            //img_edit=itemView.findViewById(R.id.img_editLoai);
            //img_delete=itemView.findViewById(R.id.img_deleLoai);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DanhSachSanPham.class);
                intent.putExtra("tenLoai", list.get(getAdapterPosition()).getName());
                context.startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_delete);
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button yes = dialog.findViewById(R.id.yes);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("LoaiSanPhams").document(list.get(getAdapterPosition()).getMaLoai()).delete();
                        Toast.makeText(context, "Xoá Loại sản phẩm thành công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                dialog.show();
                    return false;
                }
            });
        }
    }

}

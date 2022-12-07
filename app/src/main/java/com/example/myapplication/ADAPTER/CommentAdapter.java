package com.example.myapplication.ADAPTER;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.ChiTietSanPham;
import com.example.myapplication.LoginActivity;
import com.example.myapplication.MODEL.Comment;
import com.example.myapplication.MODEL.DanhGia;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.Viewholder>{
    private Context context;
    private List<Comment> list;

    public CommentAdapter(Context context, List<Comment> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false);

        return new Viewholder(view);
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Comment cmt = list.get(position);
        if(cmt==null)
            return;

        if(cmt.getImg_user().equals("default")){
            Glide.with(context).load(R.drawable.avatar_default).into(holder.img_avt);
        }else
        Glide.with(context).load(cmt.getImg_user()).into(holder.img_avt);
        holder.tv_name.setText(cmt.getName_user());
        holder.tv_time.setText(cmt.getTime_comment());
        holder.tv_comment.setText(cmt.getContent());



    }

    @Override
    public int getItemCount() {
        if(list != null)
            return list.size();
        return 0;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        CircleImageView img_avt;
        TextView tv_name, tv_time, tv_comment;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            img_avt = itemView.findViewById(R.id.img_avt);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_timeCMT);
            tv_comment = itemView.findViewById(R.id.tv_comment);

        }
    }
}

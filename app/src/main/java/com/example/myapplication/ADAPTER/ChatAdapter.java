package com.example.myapplication.ADAPTER;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.MODEL.ChatMessage;
import com.example.myapplication.MODEL.Comment;
import com.example.myapplication.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Viewholder>{
    private Context context;
    private List<ChatMessage> list;

    public ChatAdapter(Context context, List<ChatMessage> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chatleft,parent,false);

        return new Viewholder(view);
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        ChatMessage chat = list.get(position);
        if(chat==null)
            return;

        if(chat.getImg_user().equals("default")){
            Glide.with(context).load(R.drawable.avatar_default).into(holder.img_profile);
        }else
        Glide.with(context).load(chat.getImg_user()).into(holder.img_profile);
        holder.tv_time.setText(chat.getTime_chat());
        holder.tv_chat.setText(chat.getMessager());

    }

    @Override
    public int getItemCount() {
        if(list != null)
            return list.size();
        return 0;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        CircleImageView img_profile;
        TextView tv_name, tv_time, tv_chat;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            img_profile = itemView.findViewById(R.id.img_profile);
//            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.textDate);
            tv_chat = itemView.findViewById(R.id.textMes);

        }
    }
}

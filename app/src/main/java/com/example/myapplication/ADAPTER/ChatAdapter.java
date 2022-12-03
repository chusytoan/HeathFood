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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Viewholder>{
    public static final int MSG_LEFT = 0;
    public static final int MSG_RIGHT = 1;
    FirebaseUser firebaseUser;
    private Context context;
    private List<ChatMessage> list;

    public ChatAdapter(Context context, List<ChatMessage> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chatitem_right, parent, false);
            return new Viewholder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new Viewholder(view);
        }
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        ChatMessage chatMessage = list.get(position);
        if (chatMessage == null)
            return;

        holder.show_msg.setText(chatMessage.getMsg());
        if(chatMessage.getImgUrl().equals("default")){
            holder.img_profile.setImageResource(R.drawable.avatar_default);
        }else {
            Glide.with(context).load(chatMessage.getImgUrl()).into(holder.img_profile);
        }
        holder.textDate.setText(chatMessage.getTimeChat());

    }

    @Override
    public int getItemCount() {
        if(list != null)
            return list.size();
        return 0;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView show_msg,textDate;
        CircleImageView img_profile;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            show_msg = itemView.findViewById(R.id.show_messgae);
            img_profile = itemView.findViewById(R.id.img_profile);
            textDate = itemView.findViewById(R.id.textDate);

        }
    }
    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //neu nguoi chat la ban than
        if (list.get(position).getNguoiGui().equals(firebaseUser.getUid())) {
            return MSG_RIGHT;//layout right
        } else {
            return MSG_LEFT;//layout left
        }
    }
}

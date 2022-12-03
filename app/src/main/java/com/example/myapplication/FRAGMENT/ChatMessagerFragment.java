package com.example.myapplication.FRAGMENT;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ADAPTER.ChatAdapter;
import com.example.myapplication.ADAPTER.CommentAdapter;
import com.example.myapplication.ADAPTER.GioHangAdapter;
import com.example.myapplication.ActivityGioHang;
import com.example.myapplication.ChiTietSanPham;
import com.example.myapplication.LoginActivity;
import com.example.myapplication.MODEL.Admin;
import com.example.myapplication.MODEL.ChatMessage;
import com.example.myapplication.MODEL.Comment;
import com.example.myapplication.MODEL.GioHang;
import com.example.myapplication.MODEL.KhachHang;
import com.example.myapplication.MODEL.User;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChatMessagerFragment extends Fragment {
    String TAG = "ChatMessagerFragment";
    View view;
    DatabaseReference reference;
    ImageView btn_send;
    EditText ed_message;

    ChatAdapter messageAdapter;
    List<ChatMessage> chats;
    RecyclerView recyclerView;

    FirebaseUser usercurent;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_chatapp,container,false);
        anhXa();

        usercurent = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Users").document("admin").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Admin admin = value.toObject(Admin.class);
                if(admin == null)
                    return;

                readMessages(usercurent.getUid(), "7Uh9NlGocOX0zPTnvPNzDtH4Wc63");
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = ed_message.getText().toString();
                if (!msg.equals("")) {
                    Chats(usercurent.getUid(), "7Uh9NlGocOX0zPTnvPNzDtH4Wc63", msg);
                } else {
                    Toast.makeText(getContext(), "vui long nhập nội dung tin nhắn", Toast.LENGTH_SHORT).show();
                    return;
                }
                ed_message.setText("");



                //kiem tra dang nhap


            }
        });
        return view;
    }

    private void readMessages(String uid, String s) {
        reference = FirebaseDatabase.getInstance().getReference("ChatMess");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chats.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    ChatMessage chat = dataSnapshot.getValue(ChatMessage.class);
                    //neu nguoi gui la toi hoac nguoi gui la doi phuong
                    if(chat.getNguoiNhan().equals(uid)  && chat.getNguoiGui().equals(s) || chat.getNguoiNhan().equals(s)
                            && chat.getNguoiGui().equals(uid)){
                        chats.add(chat);
                    }
                }
                messageAdapter = new ChatAdapter(getContext(), chats);
                recyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void anhXa(){
        btn_send = view.findViewById(R.id.img_send);
        ed_message = view.findViewById(R.id.text_inputchat);
        recyclerView = view.findViewById(R.id.rcv_chat);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        chats = new ArrayList<>();
    }
    private void Chats(String uid, String idUser, String msg) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        ChatMessage chat = new ChatMessage();
        chat.setNguoiGui(uid);
        chat.setNguoiNhan(idUser);
        chat.setMsg(msg);
        chat.setImgUrl("default");
        String timeCureent = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
            LocalDateTime now = LocalDateTime.now();
            timeCureent = dtf.format(now);
            chat.setTimeChat(timeCureent);
        }

        reference.child("ChatMess").push().setValue(chat);

    }

}

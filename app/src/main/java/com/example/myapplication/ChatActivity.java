package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.ADAPTER.ChatAdapter;
import com.example.myapplication.MODEL.Admin;
import com.example.myapplication.MODEL.ChatMessage;
import com.example.myapplication.MODEL.FCMSend;
import com.example.myapplication.MODEL.KhachHang;
import com.example.myapplication.MODEL.Token;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    String TAG = "ChatMessagerFragment";
    View view;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    ImageView btn_send, img_back;
    EditText ed_message;

    ChatAdapter messageAdapter;
    List<ChatMessage> chats;
    RecyclerView recyclerView;

    FirebaseUser usercurent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chatapp);

        anhXa();

        usercurent = FirebaseAuth.getInstance().getCurrentUser();
        if(usercurent == null){
            finish();
            startActivity(new Intent(ChatActivity.this, LoginActivity.class));
        }
            readMessages(usercurent.getUid(), "7Uh9NlGocOX0zPTnvPNzDtH4Wc63");

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = ed_message.getText().toString();
                if (!msg.equals("")) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("KhachHangs").child(usercurent.getUid());
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            KhachHang kh = snapshot.getValue(KhachHang.class);

                            Chats(usercurent.getUid(), "7Uh9NlGocOX0zPTnvPNzDtH4Wc63", msg, kh.getImgURL());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                    Toast.makeText(ChatActivity.this, "vui long nhập nội dung tin nhắn", Toast.LENGTH_SHORT).show();
                    return;
                }
                ed_message.setText("");

                //kiem tra dang nhap


            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public void anhXa(){
        btn_send = findViewById(R.id.img_send);
        ed_message = findViewById(R.id.text_inputchat);
        recyclerView = findViewById(R.id.rcv_chat);
        img_back = findViewById(R.id.img_back);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        chats = new ArrayList<>();
    }
    private void readMessages(String uid, String s) {
        reference = FirebaseDatabase.getInstance().getReference("ChatMess");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChatMessage chat = snapshot.getValue(ChatMessage.class);
                //neu nguoi gui la toi hoac nguoi gui la doi phuong
                if(chat.getNguoiNhan().equals(uid)  && chat.getNguoiGui().equals(s) || chat.getNguoiNhan().equals(s)
                        && chat.getNguoiGui().equals(uid)){
                    chats.add(chat);
                }
                messageAdapter = new ChatAdapter(ChatActivity.this, chats);
                messageAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(messageAdapter);


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void Chats(String uid, String idUser, String msg, String avtUrl) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        ChatMessage chat = new ChatMessage();
        chat.setNguoiGui(uid);
        chat.setNguoiNhan(idUser);
        chat.setMsg(msg);
        chat.setImgUrl(avtUrl);
        String timeCureent = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
            LocalDateTime now = LocalDateTime.now();
            timeCureent = dtf.format(now);
            chat.setTimeChat(timeCureent);
        }

        reference.child("ChatMess").push().setValue(chat);
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens").child("0777476404");
        tokens.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Token token = snapshot.getValue(Token.class);
                FCMSend.pushNotification(ChatActivity.this, token.getToken(), "Thông báo tin nhắn mới", "Bạn vừa nhận 1 tin nhắn từ:"
                        +"\n" + usercurent.getDisplayName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
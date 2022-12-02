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
    TextView tv_user;
    EditText edt_chat;
    ImageView img_send;
    List<ChatMessage> listchats ;
    ChatAdapter chatAdapter;
    RecyclerView rcv_chats;
    DatabaseReference reference;
    FirebaseUser usercurent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_chatapp,container,false);
        anhXa();
        usercurent = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("ChatMess");
        if(usercurent != null) {

            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    ChatMessage chat=snapshot.getValue(ChatMessage.class);
                    listchats.add(chat);
                    chatAdapter = new ChatAdapter(getContext(),listchats);
                    rcv_chats.setAdapter(chatAdapter);


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






        img_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usercurent == null) {
                    getActivity().finish();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    return;
                }
                String chatnd = edt_chat.getText().toString();
                if (chatnd.equals("")) {
                    Toast.makeText(getContext(), "vui long nhap noi dung chat", Toast.LENGTH_SHORT).show();
                    return;
                }
                Chats(chatnd, usercurent.getUid());


                //kiem tra dang nhap


            }
        });
        return view;
    }

    public void anhXa(){
        tv_user=view.findViewById(R.id.tv_user);
        edt_chat=view.findViewById(R.id.text_inputchat);
        img_send=view.findViewById(R.id.img_send);
        rcv_chats=view.findViewById(R.id.rcv_chat);
        rcv_chats.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        listchats = new ArrayList<>();
    }
    private void Chats(String nd, String user) {

        DatabaseReference referencekhs = FirebaseDatabase.getInstance().getReference("KhachHangs");
        referencekhs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    KhachHang kh = dataSnapshot.getValue(KhachHang.class);
                    if (kh.getId().equals(usercurent.getUid())) {
                        ChatMessage chat=new ChatMessage();
                        chat.setId_user(user);
                        chat.setName_user(kh.getName());
                        chat.setImg_user("default");
                        chat.setMessager(nd);
                        String timeCureent = "";
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
                            LocalDateTime now = LocalDateTime.now();
                            timeCureent = dtf.format(now);
                            chat.setTime_chat(timeCureent);
                        }

                        reference.push().setValue(chat, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(getContext(), "gửi tin nhắn thành công", Toast.LENGTH_SHORT).show();
                                edt_chat.setText("");
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}

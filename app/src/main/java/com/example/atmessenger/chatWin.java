package com.example.atmessenger;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Date;

public class chatWin extends AppCompatActivity {

    String reciverimg, reciverUid, reciverName, SenderUID;
    ImageView profile;
    TextView reciverNName;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    public static String senderImg;
    public static String reciverIImg;
    CardView sendbtn;
    EditText textmsg;

    String senderRoom, reciverRoom;
    RecyclerView messageAdpter;
    ArrayList<msgModelClass> messagesArrayList;
    MessagesAdapter mmessagesAdpter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_win);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        reciverName = getIntent().getStringExtra("nameeee");
        reciverimg = getIntent().getStringExtra("reciverImg");
        reciverUid = getIntent().getStringExtra("uid");

        messagesArrayList = new ArrayList<>();

        sendbtn = findViewById(R.id.sendbtnn);
        textmsg = findViewById(R.id.textmsg);
        reciverNName = findViewById(R.id.recivername);
        profile = findViewById(R.id.profileimgg);
        messageAdpter = findViewById(R.id.msgadpter);

        // Fallbacks for missing data
        reciverNName.setText(reciverName != null ? reciverName : "User");
        profile.setImageResource(R.drawable.man); // fallback image

        SenderUID = firebaseAuth.getUid();
        senderRoom = SenderUID + reciverUid;
        reciverRoom = reciverUid + SenderUID;

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdpter.setLayoutManager(linearLayoutManager);

        mmessagesAdpter = new MessagesAdapter(chatWin.this, messagesArrayList);
        messageAdpter.setAdapter(mmessagesAdpter);

        DatabaseReference reference = database.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatreference = database.getReference().child("chats").child(senderRoom).child("messages");

        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    msgModelClass messages = dataSnapshot.getValue(msgModelClass.class);
                    messagesArrayList.add(messages);
                }
                mmessagesAdpter.notifyDataSetChanged();

                if (messagesArrayList.isEmpty()) {
                    Toast.makeText(chatWin.this, "No messages yet. Start chatting!", Toast.LENGTH_SHORT).show();
                }

                messageAdpter.scrollToPosition(messagesArrayList.size() - 1);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(chatWin.this, "Failed to load messages. Try again.", Toast.LENGTH_SHORT).show();
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                senderImg = snapshot.child("profilepic").getValue() != null ?
                        snapshot.child("profilepic").getValue().toString() : "";
                reciverIImg = reciverimg != null ? reciverimg : "";
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(chatWin.this, "Failed to load user info.", Toast.LENGTH_SHORT).show();
            }
        });

        sendbtn.setOnClickListener(view -> {
            String message = textmsg.getText().toString().trim();
            if (message.isEmpty()) {
                Toast.makeText(chatWin.this, "Enter a message first", Toast.LENGTH_SHORT).show();
                return;
            }

            textmsg.setText("");
            Date date = new Date();
            msgModelClass messagess = new msgModelClass(message, SenderUID, date.getTime());

            database.getReference().child("chats").child(senderRoom).child("messages")
                    .push().setValue(messagess)
                    .addOnCompleteListener(task -> {
                        database.getReference().child("chats").child(reciverRoom).child("messages")
                                .push().setValue(messagess)
                                .addOnCompleteListener(task1 -> {
                                    messageAdpter.scrollToPosition(messagesArrayList.size() - 1);
                                });
                    });
        });
    }
}
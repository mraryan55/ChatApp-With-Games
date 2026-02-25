package com.example.atmessenger;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<msgModelClass> messagesList;

    final int ITEM_SEND = 1;
    final int ITEM_RECEIVE = 2;

    public MessagesAdapter(Context context, ArrayList<msgModelClass> messagesList) {
        this.context = context;
        this.messagesList = messagesList;
    }

    @Override
    public int getItemViewType(int position) {
        msgModelClass message = messagesList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message.getSenderid())) {
            return ITEM_SEND;
        } else {
            return ITEM_RECEIVE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciver_layout, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        msgModelClass message = messagesList.get(position);

        holder.itemView.setOnLongClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to delete this message?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        FirebaseDatabase.getInstance()
                                .getReference()
                                .child("chats")
                                .child(message.getSenderRoom())
                                .child("messages")
                                .child(message.getMessageId())
                                .removeValue()
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(context, "Message deleted", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
            return true;
        });

        if (holder instanceof SenderViewHolder) {
            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            viewHolder.msgText.setText(message.getMessage());
            viewHolder.imageView.setImageResource(R.drawable.man);
        } else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            viewHolder.msgText.setText(message.getMessage());
            viewHolder.imageView.setImageResource(R.drawable.man);
        }
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    // Sender ViewHolder
    static class SenderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView msgText;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profilerimgg);
            msgText = itemView.findViewById(R.id.msgsendertyp);
        }
    }

    // Receiver ViewHolder
    static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView msgText;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.pro);
            msgText = itemView.findViewById(R.id.recivertextset);
        }
    }
}
package com.example.atmessenger;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final ArrayList<Users> userList;
    private final MainActivity mainActivity;

    public UserAdapter(MainActivity mainActivity, ArrayList<Users> userList) {
        this.mainActivity = mainActivity;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users user = userList.get(position);

        holder.username.setText(user.getUserName() != null ? user.getUserName() : "Unknown");
        holder.userstatus.setText(user.getStatus() != null ? user.getStatus() : "No status");
        holder.userimg.setImageResource(R.drawable.man); // fallback image

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mainActivity, chatWin.class);
            intent.putExtra("nameeee", user.getUserName());
            intent.putExtra("reciverImg", user.getProfilepic());
            intent.putExtra("uid", user.getUserId());
            mainActivity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userimg;
        TextView username, userstatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userimg = itemView.findViewById(R.id.userimg);
            username = itemView.findViewById(R.id.username);
            userstatus = itemView.findViewById(R.id.userstatus);
        }
    }
}
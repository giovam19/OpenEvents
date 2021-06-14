package com.example.practica;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListChatAdapter extends RecyclerView.Adapter<ListChatAdapter.ChatViewHolder> {
    private JSONArray localDataset;
    private JSONObject userInfo;

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView userName;
        private TextView userMessage;
        private TextView messageTime;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = (TextView) itemView.findViewById(R.id.message_user);
            userMessage = (TextView) itemView.findViewById(R.id.message_text);
            messageTime = (TextView) itemView.findViewById(R.id.message_time);
        }

        public TextView getUserName() {
            return userName;
        }

        public TextView getUserMessage() {
            return userMessage;
        }

        public TextView getMessageTime() {
            return messageTime;
        }
    }

    public ListChatAdapter(JSONArray dataset, JSONObject user) {
        localDataset = dataset;
        userInfo = user;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(viewType, parent, false);

        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        try {
            String name = (String) userInfo.get("name");
            String message = (String) localDataset.getJSONObject(position).get("content");

            if (localDataset.getJSONObject(position).getString("user_id_send").equals(userInfo.getString("id")))
                holder.getUserName().setText(name);
            else
                holder.getUserName().setText(User.getInstance().getName());

            holder.getUserMessage().setText(message);
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }
    }

    @Override
    public int getItemViewType(int position) {
        try {
            if (localDataset.getJSONObject(position).getString("user_id_send").equals(userInfo.getString("id")))
                return (R.layout.item_message_other);
            else
                return (R.layout.item_message_user);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public int getItemCount() {
        if (localDataset != null) {
            return localDataset.length();
        } else {
            return 0;
        }
    }
}

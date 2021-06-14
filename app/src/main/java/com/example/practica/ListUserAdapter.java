package com.example.practica;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

public class ListUserAdapter extends RecyclerView.Adapter<ListUserAdapter.UserViewHolder> {
    private JSONArray localDataSet;

    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView userImage;
        private TextView userName;
        private JSONObject userObject;

        public UserViewHolder(View itemView) {
            super(itemView);
            this.itemView.setOnClickListener(this);
            userImage = (ImageView) itemView.findViewById(R.id.chatImage);
            userName = (TextView) itemView.findViewById(R.id.chatTitle);
        }

        public ImageView getUserImage() {
            return userImage;
        }

        public TextView getUserName() {
            return userName;
        }

        @Override
        public void onClick(View v) {
            try {
                Context context = v.getContext();
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("userID", userObject.getString("id"));
                intent.putExtra("userName", userObject.getString("name"));
                intent.putExtra("userLastName", userObject.getString("last_name"));
                intent.putExtra("userImage", userObject.getString("image"));
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void bind(JSONObject user) {
            userObject = user;
        }
    }

    public ListUserAdapter(JSONArray dataSet) {
        localDataSet = dataSet;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        try {
            String name = (String) localDataSet.getJSONObject(position).get("name");
            holder.getUserName().setText(name);

            holder.bind(localDataSet.getJSONObject(position));
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        if (localDataSet != null) {
            return localDataSet.length();
        } else {
            return 0;
        }
    }
}

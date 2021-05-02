package com.example.practica;

import android.content.Context;
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

public class ListEventAdapter extends RecyclerView.Adapter<ListEventAdapter.ViewHolder> {

    private JSONArray localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView month;
        private TextView day;
        private ImageView image;
        private TextView title;
        private JSONObject eventObject;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView.setOnClickListener(this);
            month = (TextView) itemView.findViewById(R.id.monthText);
            day = (TextView) itemView.findViewById(R.id.dayText);
            title = (TextView) itemView.findViewById(R.id.taskTitle);
            image = (ImageView) itemView.findViewById(R.id.taskImage);
        }

        public TextView getMonth() {
            return month;
        }

        public TextView getDay() {
            return day;
        }

        public ImageView getImage() {
            return image;
        }

        public TextView getTitle() {
            return title;
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();

            Toast toast;

            toast = Toast.makeText(v.getContext(), "Going to Registration Window", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 40);

            toast.show();

            //accion cuando presionas un evento
            //System.out.println("Going to Registration Window");
        }

        public void bind(JSONObject event) {
            eventObject = event;
        }
    }

    public ListEventAdapter(JSONArray dataSet) {
        localDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            String month = (String)localDataSet.getJSONObject(position).get("date");
            String day = month;//(String)localDataSet.getJSONObject(position).get("date");
            String title = (String)localDataSet.getJSONObject(position).get("name");

            holder.getMonth().setText(month);
            holder.getDay().setText(day);
            holder.getTitle().setText(title);

            holder.bind(localDataSet.getJSONObject(position));
        } catch (Exception e) {
            e.printStackTrace();
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

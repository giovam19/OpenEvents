package com.example.practica;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Date;

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
            Intent intent = new Intent(context, EventActivity.class);

            context.startActivity(intent);
        }

        public void bind(JSONObject event) {
            eventObject = event;
        }
    }

    public ListEventAdapter(JSONArray dataSet) {
        localDataSet = dataSet;
    }

    private String createMonth(String date) {
        String[] splitDate = date.split(":");
        String[] utilDate = splitDate[0].split("-");

        switch (utilDate[1]) {
            case "01":
                return "Jan";
            case  "02":
                return "Feb";
            case "03":
                return "Mar";
            case "04":
                return "Apr";
            case "05":
                return "May";
            case "06":
                return "Jun";
            case "07":
                return "Jul";
            case "08":
                return "Aug";
            case "09":
                return "Sep";
            case "10":
                return "Oct";
            case "11":
                return "Nov";
            case "12":
                return "Dec";
            default:
                return "";
        }
    }

    private String createDay(String date) {
        String[] splitDate = date.split(":");
        String[] utilDate = splitDate[0].split("-");
        String[] day = utilDate[2].split("T");

        return day[0];
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
            String month = createMonth((String) localDataSet.getJSONObject(position).get("date"));
            String day = createDay((String) localDataSet.getJSONObject(position).get("date"));
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

package com.example.practica;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

public class ListTimelineAdapter extends RecyclerView.Adapter<ListTimelineAdapter.TimelineHolder> {

    private JSONArray localDataSet;

    public static class TimelineHolder extends RecyclerView.ViewHolder {
        private TextView type;
        private TextView description;
        private TextView initMonth;
        private TextView initDay;
        private TextView endMonth;
        private TextView endDay;

        public TimelineHolder(View itemView) {
            super(itemView);
            type = (TextView) itemView.findViewById(R.id.typeTL);
            description = (TextView) itemView.findViewById(R.id.descriptionTimeline);
            initMonth = (TextView) itemView.findViewById(R.id.initMonthTL);
            initDay = (TextView) itemView.findViewById(R.id.initDayTL);
            endMonth = (TextView) itemView.findViewById(R.id.endMonthTL);
            endDay = (TextView) itemView.findViewById(R.id.endDayTL);
        }

        public TextView getType() {
            return type;
        }

        public TextView getDescription() {
            return description;
        }

        public TextView getInitMonth() {
            return initMonth;
        }

        public TextView getInitDay() {
            return initDay;
        }

        public TextView getEndMonth() {
            return endMonth;
        }

        public TextView getEndDay() {
            return endDay;
        }
    }

    public ListTimelineAdapter(JSONArray dataset) {
        localDataSet = dataset;
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

    @NonNull
    @Override
    public TimelineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_timeline, parent, false);

        return new TimelineHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineHolder holder, int position) {
        try {
            String dayTL = createDay((String)localDataSet.getJSONObject(position).get("eventStart_date"));
            String monthTL = createMonth((String)localDataSet.getJSONObject(position).get("eventStart_date"));
            holder.getType().setText((String)localDataSet.getJSONObject(position).get("type"));
            holder.getDescription().setText((String)localDataSet.getJSONObject(position).get("description"));
            holder.getInitDay().setText(dayTL);
            holder.getInitMonth().setText(monthTL);
            dayTL = createDay((String)localDataSet.getJSONObject(position).get("eventEnd_date"));
            monthTL = createMonth((String)localDataSet.getJSONObject(position).get("eventEnd_date"));
            holder.getEndDay().setText(dayTL);
            holder.getEndMonth().setText(monthTL);

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

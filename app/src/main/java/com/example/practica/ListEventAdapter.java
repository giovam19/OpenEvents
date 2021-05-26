package com.example.practica;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListEventAdapter extends RecyclerView.Adapter<ListEventAdapter.ViewHolder> {

    private JSONArray localDataSet;
    private FilterOptionListener optionListener;
    private int eventID;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        private TextView month;
        private TextView day;
        private ImageView image;
        private TextView title;
        private JSONObject eventObject;

        private FilterOptionListener optionListener;

        public ViewHolder(View itemView, FilterOptionListener listener) {
            super(itemView);
            this.itemView.setOnClickListener(this);
            month = (TextView) itemView.findViewById(R.id.monthText);
            day = (TextView) itemView.findViewById(R.id.dayText);
            title = (TextView) itemView.findViewById(R.id.taskTitle);
            image = (ImageView) itemView.findViewById(R.id.taskImage);
            optionListener = listener;
            itemView.setOnCreateContextMenuListener(this);
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
            selectAction(v);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        }

        public void bind(JSONObject event) {
            eventObject = event;
        }

        private void selectAction(View v) {
            Context context = v.getContext();

            if (context instanceof ListEvents) {
                Intent intent = new Intent(context, EventActivity.class);

                try {
                    intent.putExtra("eventTilte", eventObject.getString("name"));
                    intent.putExtra("eventDescription", eventObject.getString("description"));
                    intent.putExtra("eventUbi", eventObject.getString("location"));
                    intent.putExtra("eventMaxppl", eventObject.getString("n_participators"));
                    intent.putExtra("eventType", eventObject.getString("type"));
                    intent.putExtra("eventOwner", eventObject.getInt("owner_id"));
                    intent.putExtra("eventID", eventObject.getInt("id"));
                    intent.putExtra("startDate", eventObject.getString("eventStart_date"));
                    intent.putExtra("endDate", eventObject.getString("eventEnd_date"));
                }  catch (Exception e) {
                    Log.e("error", e.getMessage());
                }
                context.startActivity(intent);

            } else if (context instanceof CreatedEventsOption) {
                if (optionListener.getOptionSelected().equals(CreatedEventsOption.FUTURE)) {
                    Intent intent = new Intent(context, EditEvent.class);

                    try {
                        intent.putExtra("eventTilte", eventObject.getString("name"));
                        intent.putExtra("eventDescription", eventObject.getString("description"));
                        intent.putExtra("eventUbi", eventObject.getString("location"));
                        intent.putExtra("eventMaxppl", eventObject.getString("n_participators"));
                        intent.putExtra("eventType", eventObject.getString("type"));
                        intent.putExtra("eventOwner", eventObject.getInt("owner_id"));
                        intent.putExtra("eventID", eventObject.getInt("id"));
                        intent.putExtra("startDate", eventObject.getString("eventStart_date"));
                        intent.putExtra("endDate", eventObject.getString("eventEnd_date"));
                    }  catch (Exception e) {
                        Log.e("error", e.getMessage());
                    }
                    context.startActivity(intent);
                }
            }
        }
    }

    public ListEventAdapter(JSONArray dataSet, FilterOptionListener listener) {
        localDataSet = dataSet;
        optionListener = listener;
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

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);

        return new ViewHolder(view, optionListener);
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

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    try {
                        setEventID((Integer) localDataSet.getJSONObject(position).get("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });

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

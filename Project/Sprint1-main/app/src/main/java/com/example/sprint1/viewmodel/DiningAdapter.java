package com.example.sprint1.viewmodel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.sprint1.R;

import java.util.List;

// Adapts dining details to be shown on the Dining Screen UI
public class DiningAdapter extends RecyclerView.Adapter<DiningAdapter.ViewHolder> {
    private List<String> tripNames;
    private List<String> names;
    private List<String> locations;
    private List<String> websites;
    private List<String> dates;
    private List<String> times;

    public DiningAdapter(List<String> tripNames, List<String> names, List<String> locations,
                         List<String> websites, List<String> dates, List<String> times) {
        this.tripNames = tripNames;
        this.names = names;
        this.locations = locations;
        this.websites = websites;
        this.dates = dates;
        this.times = times;
    }

    @NonNull
    @Override
    public DiningAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflates layout and creates design of each row
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.dining_log_layout, parent, false);


        return new DiningAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiningAdapter.ViewHolder holder, int position) {
        // Assigns values to each rows
        holder.tvTrip.setText(tripNames.get(position));
        holder.tvName.setText(names.get(position));
        holder.tvLocation.setText(locations.get(position));
        holder.tvWebsite.setText(websites.get(position));
        holder.tvDate.setText(dates.get(position));
        holder.tvTime.setText(times.get(position));
    }

    @Override
    public int getItemCount() {
        // Recycler view wants to know how many items need to be displayed in total
        return locations.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTrip;
        private TextView tvName;
        private TextView tvLocation;
        private TextView tvWebsite;
        private TextView tvDate;
        private TextView tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTrip = itemView.findViewById(R.id.log_trip);
            tvName = itemView.findViewById(R.id.log_name);
            tvLocation = itemView.findViewById(R.id.log_location);
            tvWebsite = itemView.findViewById(R.id.log_website);
            tvDate = itemView.findViewById(R.id.log_date);
            tvTime = itemView.findViewById(R.id.log_time);
        }
    }
}
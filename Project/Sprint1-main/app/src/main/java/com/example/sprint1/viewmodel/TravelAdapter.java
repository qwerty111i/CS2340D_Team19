package com.example.sprint1.viewmodel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.sprint1.R;

import java.util.List;

// Adapts travel details to be shown on the Destination Screen UI
public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.ViewHolder> {
    private List<String> tripNames;
    private List<String> locations;
    private List<String> days;

    public TravelAdapter(List<String> tripNames, List<String> locations, List<String> days) {
        this.tripNames = tripNames;
        this.locations = locations;
        this.days = days;

    }

    @NonNull
    @Override
    public TravelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflates layout and creates design of each row
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.travel_log_layout, parent, false);


        return new TravelAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelAdapter.ViewHolder holder, int position) {
        // Assigns values to each rows
        holder.tvTrip.setText(tripNames.get(position));
        holder.tvDestination.setText(locations.get(position));
        holder.tvDays.setText(days.get(position));
    }

    @Override
    public int getItemCount() {
        // Recycler view wants to know how many items need to be displayed in total
        return locations.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTrip;
        private TextView tvDestination;
        private TextView tvDays;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTrip = itemView.findViewById(R.id.log_trip);
            tvDestination = itemView.findViewById(R.id.log_destination_text);
            tvDays = itemView.findViewById(R.id.log_days);
        }
    }
}
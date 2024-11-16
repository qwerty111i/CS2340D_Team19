package com.example.sprint1.viewmodel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.sprint1.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

// Adapts travel details to be shown on the Destination Screen UI
public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.ViewHolder> {
    private List<String> tripNames;
    private List<String> locations;
    private List<String> days;
    private List<String> startDates;
    private List<String> endDates;

    public TravelAdapter(List<String> tripNames, List<String> locations, List<String> days,
                         List<String> startDates, List<String> endDates) {
        this.tripNames = tripNames;
        this.locations = locations;
        this.days = days;
        this.startDates = startDates;
        this.endDates = endDates;
    }

    @NonNull
    @Override
    public TravelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflates layout and creates design of each row
        View view;

        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.layout_travel_log, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.layout_expired_travel_log, parent, false);
        }

        return new TravelAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelAdapter.ViewHolder holder, int position) {
        // Assigns values to each rows
        holder.tvTrip.setText(tripNames.get(position));
        holder.tvDestination.setText(locations.get(position));
        holder.tvDays.setText(days.get(position));
        holder.tvStartDate.setText(startDates.get(position));
        holder.tvEndDate.setText(endDates.get(position));
    }

    @Override
    public int getItemCount() {
        // Recycler view wants to know how many items need to be displayed in total
        return locations.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (!startDates.get(position).contains("-")) {
            startDates.set(position, startDates.get(position) + " -");
        }

        String reservationDate = endDates.get(position);
        if (isExpired(reservationDate)) {
            if (!tripNames.get(position).contains("(Expired)")) {
                tripNames.set(position, "(Expired) " + tripNames.get(position));
            }
            return 0;
        }
        return 1;
    }

    // Checks if the date is expired
    public boolean isExpired(String reservationDate) {
        // Converts the reservationDate into an actual Date
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("MM/dd/yy");
        try {
            Date checkDate = sdf.parse(reservationDate);
            Date currentDate = new Date();

            // Resetting the time
            checkDate = resetTime(checkDate);
            currentDate = resetTime(currentDate);

            return checkDate.before(currentDate);
        } catch (Exception e) {
            return false;
        }
    }

    // Resets the time in a date
    private Date resetTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTrip;
        private TextView tvDestination;
        private TextView tvDays;
        private TextView tvStartDate;
        private TextView tvEndDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTrip = itemView.findViewById(R.id.log_trip);
            tvDestination = itemView.findViewById(R.id.log_destination_text);
            tvDays = itemView.findViewById(R.id.log_days);
            tvStartDate = itemView.findViewById(R.id.log_start_date);
            tvEndDate = itemView.findViewById(R.id.log_end_date);
        }
    }
}
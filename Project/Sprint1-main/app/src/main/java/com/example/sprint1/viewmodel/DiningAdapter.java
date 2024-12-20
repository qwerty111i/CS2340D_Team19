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

// Adapts dining details to be shown on the Dining Screen UI
public class DiningAdapter extends RecyclerView.Adapter<DiningAdapter.ViewHolder> {
    private List<String> shared;
    private List<String> tripNames;
    private List<String> names;
    private List<String> locations;
    private List<String> websites;
    private List<String> dates;
    private List<String> times;

    public DiningAdapter(List<String> shared, List<String> tripNames, List<String> names, List<String> locations,
                         List<String> websites, List<String> dates, List<String> times) {
        this.shared = shared;
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
        View view;

        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.layout_shared_expired_dining_log, parent, false);
        } else if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.layout_shared_dining_log, parent, false);
        } else if (viewType == 2) {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.layout_expired_dining_log, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.layout_dining_log, parent, false);
        }

        return new DiningAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiningAdapter.ViewHolder holder, int position) {
        // Assigns values to each rows
        if (holder.tvShared != null) {
            holder.tvShared.setText(shared.get(position));
        }
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

    @Override
    public int getItemViewType(int position) {
        String reservationDate = dates.get(position);
        if (tripNames.get(position).contains("(Shared")) {
            int index = tripNames.get(position).indexOf("(Shared");
            String newShared = shared.get(position).substring(index);
            newShared = newShared.replace("(", "");
            newShared = newShared.replace(")", "");
            shared.set(position, newShared);

            String newTripNames = tripNames.get(position).substring(0, index);
            tripNames.set(position, newTripNames);

            // Expired Check
            if (isExpired(reservationDate)) {
                if (!tripNames.get(position).contains("(Expired)")) {
                    tripNames.set(position, "(Expired) " + tripNames.get(position));
                }
                // Shared + Expired
                return 0;
            } else {
                // Shared + Valid
                return 1;
            }
        } else if (shared.get(position).contains("Shared")) {
            // Expired Check
            if (isExpired(reservationDate)) {
                if (!tripNames.get(position).contains("(Expired)")) {
                    tripNames.set(position, "(Expired) " + tripNames.get(position));
                }
                // Shared + Expired
                return 0;
            } else {
                // Shared + Valid
                return 1;
            }
        } else {
            if (isExpired(reservationDate)) {
                if (!tripNames.get(position).contains("(Expired)")) {
                    tripNames.set(position, "(Expired) " + tripNames.get(position));
                }
                // Not Shared + Expired
                return 2;
            } else {
                // Not Shared + Valid
                return 3;
            }
        }
    }

    // Checks if the date is expired
    public boolean isExpired(String reservationDate) {
        // Converts the reservationDate into an actual Date
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
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
        private TextView tvShared;
        private TextView tvTrip;
        private TextView tvName;
        private TextView tvLocation;
        private TextView tvWebsite;
        private TextView tvDate;
        private TextView tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvShared = itemView.findViewById(R.id.shared);
            tvTrip = itemView.findViewById(R.id.log_trip);
            tvName = itemView.findViewById(R.id.log_name);
            tvLocation = itemView.findViewById(R.id.log_location);
            tvWebsite = itemView.findViewById(R.id.log_website);
            tvDate = itemView.findViewById(R.id.log_date);
            tvTime = itemView.findViewById(R.id.log_time);
        }
    }
}
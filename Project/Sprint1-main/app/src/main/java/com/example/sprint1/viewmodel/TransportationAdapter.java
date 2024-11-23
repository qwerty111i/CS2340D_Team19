package com.example.sprint1.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sprint1.R;
import com.example.sprint1.model.TransportationDetails;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransportationAdapter extends
        RecyclerView.Adapter<TransportationAdapter.TransportationViewHolder> {
    private Context context;
    private List<TransportationDetails> transportations;
    private List<String> shared;

    public TransportationAdapter(Context context, List<TransportationDetails> transportations,
                                List<String> shared) {
        this.context = context;
        this.transportations = transportations;
        this.shared = shared;
    }

    @NonNull
    @Override
    public TransportationAdapter.
            TransportationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.layout_shared_expired_transportation_log, parent, false);
        } else if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.layout_shared_transportation_log, parent, false);
        } else if (viewType == 2) {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.layout_expired_transportation_log, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.layout_transportation_log, parent, false);
        }

        return new TransportationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransportationAdapter.
            TransportationViewHolder holder, int position) {
        // Assign values to the views created in the row layout xml file
        // Based on the position of the recycler view
        if (holder.tvShared != null) {
            holder.tvShared.setText(shared.get(position));
        }
        holder.tvType.setText(transportations.get(position).getType());
        holder.tvStartLocation.setText("Start: " + transportations.get(position).getStartLocation());
        holder.tvEndLocation.setText("End: " + transportations.get(position).getEndLocation());
        holder.tvStartDate.setText(transportations.get(position).getStartDate());
        holder.tvStartTime.setText(transportations.get(position).getStartTime());
        holder.tvTripName.setText(transportations.get(position).getTripName());
    }

    @Override
    public int getItemCount() {
        // To know the number of items that should be displayed
        return transportations.size();
    }

    @Override
    public int getItemViewType(int position) {
        String date = transportations.get(position).getStartDate();
        if (transportations.get(position).getTripName().contains("(Shared")) {
            int index = transportations.get(position).getTripName().indexOf("(Shared");
            String newShared = shared.get(position).substring(index);
            newShared = newShared.replace("(", "");
            newShared = newShared.replace(")", "");
            shared.set(position, newShared);

            String newTripNames = transportations.get(position).getTripName().substring(0, index);
            transportations.get(position).setTripName(newTripNames);

            // Expired Check
            if (isExpired(date)) {
                if (!transportations.get(position).getTripName().contains("(Expired)")) {
                    transportations.get(position).setTripName("(Expired) " + transportations.get(position).getTripName());
                }
                // Shared + Expired
                return 0;
            } else {
                // Shared + Valid
                return 1;
            }
        } else if (shared.get(position).contains("Shared")) {
            // Expired Check
            if (isExpired(date)) {
                if (!transportations.get(position).getTripName().contains("(Expired)")) {
                    transportations.get(position).setTripName("(Expired) " + transportations.get(position).getTripName());
                }
                // Shared + Expired
                return 0;
            } else {
                // Shared + Valid
                return 1;
            }
        } else {
            if (isExpired(date)) {
                if (!transportations.get(position).getTripName().contains("(Expired)")) {
                    transportations.get(position).setTripName("(Expired) " + transportations.get(position).getTripName());
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
    public boolean isExpired(String date) {
        // Converts the reservationDate into an actual Date
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("MM/dd/yy");
        try {
            Date checkDate = sdf.parse(date);
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

    public static class TransportationViewHolder extends RecyclerView.ViewHolder {
        // Grabbing items from layout file and assigning them to variables
        private TextView tvShared;
        private TextView tvTripName;
        private TextView tvStartLocation;
        private TextView tvEndLocation;
        private TextView tvStartDate;
        private TextView tvStartTime;
        private TextView tvType;

        public TransportationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvShared = itemView.findViewById(R.id.shared);
            tvTripName = itemView.findViewById(R.id.log_trip);
            tvType = itemView.findViewById(R.id.log_type);
            tvStartLocation = itemView.findViewById(R.id.log_start_location);
            tvEndLocation = itemView.findViewById(R.id.log_end_location);
            tvStartDate = itemView.findViewById(R.id.log_date);
            tvStartTime = itemView.findViewById(R.id.log_time);
        }
    }
}

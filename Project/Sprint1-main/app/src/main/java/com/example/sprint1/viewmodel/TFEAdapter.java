package com.example.sprint1.viewmodel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import com.example.sprint1.R;
import com.example.sprint1.model.TFEUser;
import com.example.sprint1.model.TravelFormEntry;
import java.util.List;

// TravelAdapter.java
public class TFEAdapter extends RecyclerView.Adapter<TFEAdapter.ViewHolder> {
    private List<TFEUser> tfeUserList;

    public TFEAdapter(List<TFEUser> tfeUserList) {
        this.tfeUserList = tfeUserList;
    }

    public void updateData(List<TFEUser> newTfeUserList) {
        tfeUserList.clear();
        tfeUserList.addAll(newTfeUserList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TFEAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_community_log, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TFEAdapter.ViewHolder holder, int position) {
        TFEUser tfeUser = tfeUserList.get(position);
        // Bind data to the view holder
        holder.tvShared.setText("Shared by " + tfeUser.getUserId());
        TravelFormEntry tfe = tfeUser.getTFE();
        holder.tvTrip.setText("Trip to " + tfe.getDestination());
        holder.tvDestination.setText(tfe.getDestination());
        holder.tvStartDate.setText(tfe.getStartDate());
        holder.tvEndDate.setText(tfe.getEndDate());
        holder.tvAccommodations.setText("Accommodations: " + tfe.getAccommodation());
        holder.tvDining.setText("Dining: " + tfe.getDining());
        holder.tvRating.setText("Rating: " + tfe.getRating() + "/5");
        holder.rbRating.setRating(Float.parseFloat(tfe.getRating()));

        // Optionally calculate days and set tvDays
    }

    @Override
    public int getItemCount() {
        return tfeUserList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvShared;
        private TextView tvTrip;
        private TextView tvDestination;
        private TextView tvStartDate;
        private TextView tvEndDate;
        private TextView tvAccommodations;
        private TextView tvDining;
        private TextView tvRating;
        private RatingBar rbRating;
        // Initialize other views if necessary

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvShared = itemView.findViewById(R.id.shared);
            tvTrip = itemView.findViewById(R.id.log_trip);
            tvDestination = itemView.findViewById(R.id.log_destination_text);
            tvStartDate = itemView.findViewById(R.id.log_start_date);
            tvEndDate = itemView.findViewById(R.id.log_end_date);
            tvAccommodations = itemView.findViewById(R.id.travelAccommodations);
            tvDining = itemView.findViewById(R.id.travelDining);
            tvRating = itemView.findViewById(R.id.travelRating);
            rbRating = itemView.findViewById(R.id.ratingBar);
        }
    }
}

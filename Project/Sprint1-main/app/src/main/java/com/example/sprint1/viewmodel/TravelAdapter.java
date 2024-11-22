package com.example.sprint1.viewmodel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.sprint1.R;
import com.example.sprint1.model.TFEUser;
import com.example.sprint1.model.TravelFormEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

// TravelAdapter.java
public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.ViewHolder> {
    private List<TFEUser> tfeUserList;

    public TravelAdapter(List<TFEUser> tfeUserList) {
        this.tfeUserList = tfeUserList;
    }

    public void updateData(List<TFEUser> newTfeUserList) {
        tfeUserList.clear();
        tfeUserList.addAll(newTfeUserList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TravelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_shared_travel_log, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelAdapter.ViewHolder holder, int position) {
        TFEUser tfeUser = tfeUserList.get(position);
        // Bind data to the view holder
        holder.tvShared.setText("Shared by " + tfeUser.getUserId());
        TravelFormEntry tfe = tfeUser.getTFE();
        holder.tvTrip.setText("Trip to " + tfe.getDestination());
        holder.tvDestination.setText(tfe.getDestination());
        holder.tvStartDate.setText(tfe.getStartDate());
        holder.tvEndDate.setText(tfe.getEndDate());
        // Optionally calculate days and set tvDays
    }

    @Override
    public int getItemCount() {
        return tfeUserList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvShared;
        TextView tvTrip;
        TextView tvDestination;
        TextView tvStartDate;
        TextView tvEndDate;
        // Initialize other views if necessary

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvShared = itemView.findViewById(R.id.shared);
            tvTrip = itemView.findViewById(R.id.log_trip);
            tvDestination = itemView.findViewById(R.id.log_destination_text);
            tvStartDate = itemView.findViewById(R.id.log_start_date);
            tvEndDate = itemView.findViewById(R.id.log_end_date);
        }
    }
}

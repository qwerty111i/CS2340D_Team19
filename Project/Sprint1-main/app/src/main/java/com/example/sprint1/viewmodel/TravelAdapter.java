package com.example.sprint1.viewmodel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.sprint1.R;

import java.util.ArrayList;
import java.util.List;

//Adapts travel details to be shown on the Desination Screens UI
public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.ViewHolder> {
    //Context context;
    List<String> locations;
    List<String> days;

    public TravelAdapter(List<String> locations, List<String> days) {
        this.locations = locations;
        this.days = days;

    }

    @NonNull
    @Override
    public TravelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       //Inflates layout and creates design of each row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_item_layout, parent,false);


        return new TravelAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelAdapter.ViewHolder holder, int position) {
        //assigns values to each rows
        holder.tvDestination.setText(locations.get(position));
        holder.tvDays.setText(days.get(position));



    }

    @Override
    public int getItemCount() {
        //recycler view wants to know how many items need to be displayed in total
        return locations.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvDestination, tvDays;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDestination = itemView.findViewById(R.id.log_destination_text);
            tvDays = itemView.findViewById(R.id.log_days);
        }
    }


}
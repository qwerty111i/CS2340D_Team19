package com.example.sprint1.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprint1.R;
import com.example.sprint1.model.Accommodation;

import java.util.ArrayList;
import java.util.List;

public class AccommodationAdapter extends RecyclerView.Adapter<AccommodationAdapter.AccommodationViewHolder> {
    Context context;
    List<Accommodation> accommodations;

    public AccommodationAdapter(Context context, List<Accommodation> accommodations){
        this.context = context;
        this.accommodations = accommodations;
    }
    @NonNull
    @Override
    public AccommodationAdapter.AccommodationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflating layout and giving look to each row
        LayoutInflater inflator = LayoutInflater.from(context);
        View view = inflator.inflate(R.layout.accommodation_item_layout, parent, false);
        return new AccommodationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccommodationAdapter.AccommodationViewHolder holder, int position) {
    //assign values to the views created in the row layout xml file
        //based on the position of the recycler view
        holder.tvCheckIn.setText("Check In: " + accommodations.get(position).getCheckIn());
        holder.tvCheckOut.setText("Check Out: " + accommodations.get(position).getCheckOut());
        holder.tvLocation.setText(accommodations.get(position).getLocation());
        holder.tvNumRooms.setText(String.valueOf(accommodations.get(position).getNumRooms()) + " rooms");
       holder.tvTypeRoom.setText(" - " + String.valueOf(accommodations.get(position).getRoomType()));
        holder.tvHotel.setText(accommodations.get(position).getHotel());
        holder.tvWebsite.setText(accommodations.get(position).getWebsite());


    }

    @Override
    public int getItemCount() {
        //wants to know the number of items that should be displayed
        return accommodations.size();
    }

    public static class AccommodationViewHolder extends RecyclerView.ViewHolder{
        //grabbing items from layout file and assigning them to variables
        TextView tvCheckIn, tvCheckOut, tvLocation, tvNumRooms, tvTypeRoom, tvHotel, tvWebsite;

        public AccommodationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCheckIn = itemView.findViewById(R.id.check_in);
            tvCheckOut = itemView.findViewById(R.id.check_out);
            tvLocation = itemView.findViewById(R.id.location_text);
            tvNumRooms = itemView.findViewById(R.id.number_of_rooms_text);
            tvTypeRoom = itemView.findViewById(R.id.type_of_room_text);
            tvHotel = itemView.findViewById(R.id.hotel_text);
            tvWebsite = itemView.findViewById(R.id.website_text);

        }
    }
}

package com.example.sprint1.viewmodel;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprint1.R;
import com.example.sprint1.model.Accommodation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        Accommodation accommodation = accommodations.get(position);

        //Parse the check-in date
        String checkInDateString = accommodation.getCheckIn();
        String checkOutDateString = accommodation.getCheckOut();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
        try{
            Date checkInDate = dateFormat.parse(checkInDateString);
            Date checkOutDate = dateFormat.parse(checkOutDateString);
            Date currentDate = new Date();

            //Compare the dates
            if(checkInDate != null && checkInDate.before(currentDate)){
                //If check-in date is in the past, turn text red
                holder.tvCheckIn.setTextColor(Color.RED);
            } else{
                //set default color
                holder.tvCheckIn.setTextColor(Color.WHITE); //Replace w default white
            }

            if(checkOutDate != null && checkOutDate.before(currentDate)){
                //If check-in date is in the past, turn text red
                holder.tvCheckOut.setTextColor(Color.RED);
            } else{
                //set default color
                holder.tvCheckOut.setTextColor(Color.WHITE); //Replace w default white
            }
        }

        catch (ParseException e) {
            throw new RuntimeException(e);
        }


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

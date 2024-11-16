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
import com.example.sprint1.model.AccommodationDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AccommodationAdapter extends
        RecyclerView.Adapter<AccommodationAdapter.AccommodationViewHolder> {
    private Context context;
    private List<AccommodationDetails> accommodations;

    public AccommodationAdapter(Context context, List<AccommodationDetails> accommodations) {
        this.context = context;
        this.accommodations = accommodations;
    }
    @NonNull
    @Override
    public AccommodationAdapter.
            AccommodationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.layout_accommodation_log, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.layout_expired_accommodation_log, parent, false);
        }

        return new AccommodationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccommodationAdapter.AccommodationViewHolder holder,
                                 int position) {
        AccommodationDetails accommodation = accommodations.get(position);

        //Parse the check-in date
        String checkInDateString = accommodation.getCheckIn();
        String checkOutDateString = accommodation.getCheckOut();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
        try {
            Date checkInDate = dateFormat.parse(checkInDateString);
            Date checkOutDate = dateFormat.parse(checkOutDateString);
            Date currentDate = new Date();

            //Compare the dates
            if (checkInDate != null && checkInDate.before(currentDate)) {
                //If check-in date is in the past, turn text red
                holder.tvCheckIn.setTextColor(Color.RED);
            }

            if (checkOutDate != null && checkOutDate.before(currentDate)) {
                //If check-in date is in the past, turn text red
                holder.tvCheckOut.setTextColor(Color.RED);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        //assign values to the views created in the row layout xml file
        //based on the position of the recycler view
        holder.tvCheckIn.setText("Check In: " + accommodations.get(position).getCheckIn());
        holder.tvCheckOut.setText("Check Out: " + accommodations.get(position).getCheckOut());
        holder.tvName.setText(accommodations.get(position).getName());
        holder.tvLocation.setText(accommodations.get(position).getLocation());
        holder.tvNumRooms.setText(String.valueOf(accommodations.get(position).getNumRooms())
                + " rooms");
        holder.tvTypeRoom.setText(" - " + String.valueOf(accommodations.get(position).
                getRoomType()));
        holder.tvWebsite.setText(accommodations.get(position).getWebsite());
        holder.tvTripName.setText(accommodations.get(position).getTripName());
    }

    @Override
    public int getItemCount() {
        //wants to know the number of items that should be displayed
        return accommodations.size();
    }

    @Override
    public int getItemViewType(int position) {
        String checkOutDate = accommodations.get(position).getCheckOut();
        if (isExpired(checkOutDate)) {
            if (!accommodations.get(position).getTripName().contains("(Expired)")) {
                accommodations.get(position).setTripName("(Expired) " + accommodations.get(position).getTripName());
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

    public static class AccommodationViewHolder extends RecyclerView.ViewHolder {
        //grabbing items from layout file and assigning them to variables
        private TextView tvCheckIn;
        private TextView tvCheckOut;
        private TextView tvName;
        private TextView tvLocation;
        private TextView tvNumRooms;
        private TextView tvTypeRoom;
        private TextView tvWebsite;
        private TextView tvTripName;

        public AccommodationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCheckIn = itemView.findViewById(R.id.check_in);
            tvCheckOut = itemView.findViewById(R.id.check_out);
            tvName = itemView.findViewById(R.id.name_text);
            tvLocation = itemView.findViewById(R.id.location_text);
            tvNumRooms = itemView.findViewById(R.id.number_of_rooms_text);
            tvTypeRoom = itemView.findViewById(R.id.type_of_room_text);
            tvWebsite = itemView.findViewById(R.id.website_text);
            tvTripName = itemView.findViewById(R.id.log_trip);
        }
    }
}

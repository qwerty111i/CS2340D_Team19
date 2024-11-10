package com.example.sprint1.viewmodel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprint1.model.Trip;
import com.example.sprint1.model.UserModel;
import com.example.sprint1.model.VacationTime;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.example.sprint1.model.TravelDetails;

public class LogisticsViewModel extends ViewModel {
    private MutableLiveData<Integer> allottedTime;
    private MutableLiveData<Integer> plannedTime;

    private final MutableLiveData<List<String>> usersLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<String>> notesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<String>> invitedUsersLiveData = new MutableLiveData<>();
    private List<String> invitedUsersList;
    private final DatabaseReference databaseReference;
    private final MutableLiveData<List<VacationTime>> vacationTimesLiveData =
            new MutableLiveData<>();
    private List<String> notesList = new ArrayList<>();
    private final DatabaseReference notesRef;

    public LiveData<Integer> getAllottedTime() {
        return allottedTime;
    }
    public  LiveData<Integer> getPlannedTime() {
        return plannedTime;
    }

    public LogisticsViewModel() {
        allottedTime = new MutableLiveData<>(0);
        plannedTime = new MutableLiveData<>(0);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        notesRef = FirebaseDatabase.getInstance().
                getReference("users").child(userId).child("notes");
        invitedUsersList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        fetchUsers();
        fetchInvitedUsers();
        retrieveNotes();
        fetchVacationTimes();
    }

    // Saves the details in the database
    public void saveTrip(String tripText) {
        // Creates a new Trip object, storing all the data
        Trip newTrip = new Trip(tripText);

        // Uses the Singleton implemented Database to store information
        UserModel.getInstance().storeTrip(newTrip);
    }

    public void calculatePlannedTime(List<VacationTime> vacationTimes) {
        int totalPlannedTime = 0;

        for (VacationTime vacationTime : vacationTimes) {
            totalPlannedTime += vacationTime.getDuration();
        }

        plannedTime.setValue(totalPlannedTime);
    }

    public void calculateAllocatedTime(List<VacationTime> vacationTimes) {
        if (vacationTimes == null || vacationTimes.isEmpty()) {
            allottedTime.setValue(0);
            return;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        Date earliestStartDate = null;
        Date latestEndDate = null;

        for (VacationTime vacationTime : vacationTimes) {
            try {
                Date startDate = formatter.parse(vacationTime.getStartDate());
                Date endDate = formatter.parse(vacationTime.getEndDate());

                if (earliestStartDate == null || startDate.before(earliestStartDate)) {
                    earliestStartDate = startDate;
                }

                if (latestEndDate == null || endDate.after(latestEndDate)) {
                    latestEndDate = endDate;
                }
            } catch (ParseException e) {
                Log.e("LogisticsViewModel", "Date parsing error", e);
            }
        }

        if (earliestStartDate != null && latestEndDate != null) {
            long diffInMillis = latestEndDate.getTime() - earliestStartDate.getTime();
            int allocatedDays = (int) TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
            allottedTime.setValue(allocatedDays);
            Log.d("LogisticsViewModel", "Allocated time = " + allocatedDays);
        } else {
            allottedTime.setValue(0);
            Log.d("LogisticsViewModel", "No valid date range found for allocation");
        }
    }

    private void fetchVacationTimes() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference vacationTimesRef = FirebaseDatabase.getInstance().
                getReference("users").child(userId).child("vacations");

        vacationTimesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<VacationTime> vacationTimes = new ArrayList<>();
                for (DataSnapshot vacationSnapshot : snapshot.getChildren()) {
                    VacationTime vacationTime = vacationSnapshot.getValue(VacationTime.class);
                    if (vacationTime != null) {
                        vacationTimes.add(vacationTime);
                        Log.d("LogisticsViewModel", "Retrieved VacationTime: "
                                + vacationTime.getDuration() + ", Start: "
                                + vacationTime.getStartDate() + ", End: "
                                + vacationTime.getEndDate());
                    } else {
                        Log.d("LogisticsViewModel",
                                "VacationTime is null for snapshot: " + vacationSnapshot);
                    }
                }
                vacationTimesLiveData.setValue(vacationTimes);
                calculateAllocatedTime(vacationTimes);
                calculatePlannedTime(vacationTimes);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("Firebase", "Error fetching vacation times", error.toException());
            }
        });
    }

    public void addNote(String tripId, String noteText) {
        if (noteText == null || noteText.trim().isEmpty()) {
            Log.d("UserModel", "Attempted to add an empty note, ignoring.");
            return;
        }

        // Get the reference for the selected trip's notes
        DatabaseReference tripNotesRef = FirebaseDatabase.getInstance()
                .getReference("Trips")  // Assuming trips are stored under "trips" node
                .child(tripId)           // Access the specific trip by ID
                .child("notes");         // Access or create the "notes" section within the trip

        // Add the note under the "notes" section with a unique ID
        String noteId = tripNotesRef.push().getKey();
        if (noteId != null) {
            tripNotesRef.child(noteId).setValue(noteText)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("UserModel", "Note added under trip: " + tripId);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("UserModel", "Failed to add note under trip: " + tripId, e);
                    });
        }
    }

    public void saveNoteForTrip(String currentTripName, String note ) {
        if (note == null || note.trim().isEmpty()) {
            Log.d("LogisticsViewModel", "Attempted to add an empty note, ignoring.");
            return;
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (userId != null) {
            // Reference to the Trips node under the current user
            DatabaseReference tripsRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("Trips");

            // Retrieve all trips for the current user
            tripsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot tripsSnapshot) {
                    if (!tripsSnapshot.exists()) {
                        Log.e("saveNoteForTrip", "No trips found for the user");
                        return;
                    }

                    // Iterate through each trip to find the matching trip by name
                    for (DataSnapshot tripSnapshot : tripsSnapshot.getChildren()) {
                        String tripId = tripSnapshot.getKey();
                        String tripName = tripSnapshot.child("tripName").getValue(String.class);

                        // If the trip name matches, store the note under this trip
                        if (currentTripName.equals(tripName)) {
                            // Reference to the Notes node under the selected trip
                            DatabaseReference notesRef = tripsRef.child(tripId).child("Notes");

                            // Push the note under the selected trip's Notes node
                            String noteId = notesRef.push().getKey();
                            if (noteId != null) {
                                notesRef.child(noteId).setValue(note)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("saveNoteForTrip", "Note saved under trip: " + tripId);
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.d("saveNoteForTrip", "Failed to save note under trip: " + tripId);
                                        });
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("saveNoteForTrip", "Error retrieving trips for userId: " + userId);
                }
            });
        } else {
            Log.e("LogisticsViewModel", "UserId is not set, cannot save note.");
        }
    }



    public void removeNote(String note) {
        notesList.remove(note);
        notesLiveData.setValue(notesList);
        removeNoteFromFirebase(note);

        if (note.contains("-->")) {
            // Extract the inviters email (assumes the note ends with " --> inviterEmail")
            String[] parts = note.split("-->");
            if (parts.length == 2) {
                String inviterEmail = parts[1].trim();

                // Now remove the note from the inviter's Firebase node
                removeNoteFromInviter(inviterEmail, parts[0].trim());
            }
        }
    }

    private void removeNoteFromInviter(String inviterEmail, String originalNote) {
        // Locate the inviter based on their email in the database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.orderByChild("email").
                equalTo(inviterEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot inviterSnapshot : snapshot.getChildren()) {
                                // Get the inviter's user ID
                                String inviterId = inviterSnapshot.getKey();
                                if (inviterId != null) {
                                    DatabaseReference inviterNotesRef =
                                            usersRef.child(inviterId).child("notes");
                                    // Call the separate method to remove the note
                                    removeInviterNote(inviterNotesRef, originalNote);
                                }
                            }
                        } else {
                            Log.e("Firebase", "Inviter not found based on email: " + inviterEmail);
                        }
                        }

                        @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Error finding inviter", error.toException());
                    }
                });
    }

    private void removeInviterNote(DatabaseReference inviterNotesRef, String originalNote) {
        inviterNotesRef.orderByValue().equalTo(originalNote).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot noteSnapshot : snapshot.getChildren()) {
                            noteSnapshot.getRef().removeValue();
                        }
                    }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("Firebase", "Error removing from inviter", error.toException());
                        }
                    });
    }



    public void retrieveNotes() {
        notesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                notesList.clear(); // Clear existing notes
                for (DataSnapshot noteSnapshot : snapshot.getChildren()) {
                    String note = noteSnapshot.getValue(String.class);
                    if (note != null) {
                        notesList.add(note);
                    }
                }
                notesLiveData.setValue(notesList); // Update LiveData
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle possible errors
                Log.e("Firebase", "Error retrieving notes", error.toException());
            }
        });
    }

    public void saveNoteToFirebase(String note) {
        notesRef.push().setValue(note); // Push new note to Firebase
    }

    public void removeNoteFromFirebase(String note) {
        notesRef.orderByValue().equalTo(note).addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot noteSnapshot : snapshot.getChildren()) {
                        noteSnapshot.getRef().removeValue();
                        // Remove note from Firebase
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e("Firebase", "Error removing note", error.toException());
                }
            });
    }

    public void fetchUsers() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> userList = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String email = userSnapshot.child("email").getValue(String.class);
                    if (email != null) {
                        userList.add(email);
                    }
                }
                usersLiveData.setValue(userList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Error in fetching users", databaseError.toException());
            }
        });
    }

    public void fetchInvitedUsers() {
        String inviterId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child(inviterId).child("invitedUsers").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        invitedUsersList.clear();
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String invitedUserEmail = userSnapshot.getKey().
                                    replace(",", ".");
                            invitedUsersList.add(invitedUserEmail);
                        }
                        invitedUsersLiveData.setValue(invitedUsersList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("LogisticsViewModel",
                                "Error retrieving invited users: "
                                        + databaseError.getMessage());
                    }
                });
    }

    private void shareNotesWithInvitedUser(String inviterId, String inviterEmail,
                                           String invitedUserId, DatabaseReference userReference, String selectedTrip) {
        userReference.child(inviterId).child("notes")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot notesSnapshot) {
                        for (DataSnapshot noteSnapshot : notesSnapshot.getChildren()) {
                            String noteContent = noteSnapshot.getValue(String.class);
                            String marker = "-->";
                            if (noteContent != null && !noteContent.contains(marker)) {
                                noteContent = noteContent + " -->  " + inviterEmail;
                                userReference.child(invitedUserId).child("notes").
                                        push().setValue(noteContent);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("Firebase", "Error retrieving notes");
                    }
                });
    }

    private void shareTravelDetailsWithInvitedUser(String inviterId, String inviterEmail, String invitedUserId, DatabaseReference userReference, String selectedTrip) {
        // Reference to the Trips node under the current inviter
        userReference.child(inviterId).child("Trips")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot tripsSnapshot) {
                        if (!tripsSnapshot.exists()) {
                            Log.e("shareTravelDetails", "No trips found for the user");
                            return;
                        }
                        // Iterate through each trip to find the matching trip name
                        for (DataSnapshot tripSnapshot : tripsSnapshot.getChildren()) {
                            Trip trip = tripSnapshot.getValue(Trip.class);

                            // Check if the tripName matches the selectedTrip
                            if (trip != null && selectedTrip.equals(trip.getTripName())) {
                                // Append "(Shared by ...)" to the trip name
                                String sharedTripName = trip.getTripName() + " (Shared by " + inviterEmail + ")";

                                // Create a new Trip object with the updated trip name
                                Trip sharedTrip = new Trip(sharedTripName);

                                // Add the new Trip under the invited user's "Trips" node
                                String newTripId = userReference.child(invitedUserId).child("Trips").push().getKey();
                                if (newTripId != null) {
                                    userReference.child(invitedUserId).child("Trips").child(newTripId).setValue(sharedTrip)
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    Log.d("Firebase", "Shared trip created successfully.");
                                                } else {
                                                    Log.d("Firebase", "Failed to create shared trip.");
                                                }
                                            });

                                    // Now copy the TravelDetails into the new shared trip for the invited user
                                    DataSnapshot travelDetailsSnapshot = tripSnapshot.child("Travel Details");
                                    for (DataSnapshot travelIdSnapshot : travelDetailsSnapshot.getChildren()) {
                                        TravelDetails travelDetails = travelIdSnapshot.getValue(TravelDetails.class);
                                        if (travelDetails != null) {
                                            String sharedTripName2 = trip.getTripName() + " (Shared by " + inviterEmail + ")";
                                            travelDetails.setTripName(sharedTripName2);
                                            // Share travel details under the new shared trip
                                            userReference.child(invitedUserId).child("Trips").child(newTripId)
                                                    .child("Travel Details").child(travelIdSnapshot.getKey()).setValue(travelDetails)
                                                    .addOnCompleteListener(task -> {
                                                        if (task.isSuccessful()) {
                                                            Log.d("Firebase", "Travel details shared successfully.");
                                                        } else {
                                                            Log.d("Firebase", "Failed to share travel details.");
                                                        }
                                                    });
                                        }
                                    }
                                }

                                // Share notes with the invited user under the selected trip
                                DataSnapshot notesSnapshot = tripSnapshot.child("Notes");
                                for (DataSnapshot noteSnapshot : notesSnapshot.getChildren()) {
                                    String noteContent = noteSnapshot.getValue(String.class);
                                    String marker = "-->";
                                    if (noteContent != null && !noteContent.contains(marker)) {
                                        noteContent = noteContent + " -->  " + inviterEmail;

                                        // Share note content under the new shared trip for the invited user
                                        userReference.child(invitedUserId).child("Trips").child(newTripId)
                                                .child("Notes").child(noteSnapshot.getKey()).setValue(noteContent)
                                                .addOnCompleteListener(task -> {
                                                    if (task.isSuccessful()) {
                                                        Log.d("Firebase", "Note shared successfully.");
                                                    } else {
                                                        Log.d("Firebase", "Failed to share note.");
                                                    }
                                                });
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("shareTravelDetails", "Failed to read trips", error.toException());
                    }
                });
    }


    private void fetchAndShareUserData(String inviterId, String invitedUserId,
            DatabaseReference userReference, String selectedUser) {
        userReference.child(inviterId).
                child("email").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot emailSnapshot) {
                        String inviterEmail = emailSnapshot.getValue(String.class);
                        if (inviterEmail != null && !inviterEmail.isEmpty()) {
                            // Share notes
                            //shareNotesWithInvitedUser(inviterId, inviterEmail,
                                    //invitedUserId, userReference, selectedUser);

                            // Share travel details
                            shareTravelDetailsWithInvitedUser(inviterId, inviterEmail,
                                    invitedUserId, userReference, selectedUser);
                        } else {
                            Log.d("Firebase", "Inviter email not found");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("Firebase", "Error retrieving inviter email");
                    }
                });
    }

    public void inviteUsers(List<String> selectedUsers, Context context, String selectedUser) {
        String inviterId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users");

        for (String invitedUserEmail : selectedUsers) {
            boolean alreadyInvited = invitedUsersList.contains(invitedUserEmail);
            if (!alreadyInvited) {
                invitedUsersList.add(invitedUserEmail);
                invitedUsersLiveData.setValue(invitedUsersList);

                userReference.orderByChild("email").
                        equalTo(invitedUserEmail).
                        addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                        String invitedUserId = userSnapshot.getKey();
                                        if (invitedUserId != null) {
                                            // Add to invited users
                                            userReference.child(inviterId).child("invitedUsers")
                                                    .child(invitedUserEmail.
                                                            replace(".", ",")).setValue(true);

                                            // Fetch and share data
                                            fetchAndShareUserData(inviterId,
                                                    invitedUserId, userReference, selectedUser);
                                        }
                                    }
                                    Toast.makeText(context, "Users invited!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.d("LogisticsViewModel",
                                        "Error finding invited user: " + error.getMessage());
                            }
                        });
            }
        }
    }

    public void fetchNotesForTrip(String currentTripName) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (userId == null) {
            Log.e("LogisticsViewModel", "UserId is not set, cannot fetch notes.");
            return;
        }

        // Reference to the Trips node under the current user
        DatabaseReference tripsRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("Trips");

        // Retrieve all trips for the current user
        tripsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot tripsSnapshot) {
                if (!tripsSnapshot.exists()) {
                    Log.e("fetchNotesForTrip", "No trips found for the user");
                    return;
                }

                // Iterate through each trip to find the matching trip by name
                for (DataSnapshot tripSnapshot : tripsSnapshot.getChildren()) {
                    String tripId = tripSnapshot.getKey();
                    String tripName = tripSnapshot.child("tripName").getValue(String.class);

                    // If the trip name matches, retrieve the notes under this trip
                    if (currentTripName.equals(tripName)) {
                        DatabaseReference notesRef = tripsRef.child(tripId).child("Notes");

                        // Fetch notes under the selected trip's Notes node
                        notesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot notesSnapshot) {
                                ArrayList<String> notes = new ArrayList<>();
                                for (DataSnapshot noteSnapshot : notesSnapshot.getChildren()) {
                                    String note = noteSnapshot.getValue(String.class);
                                    if (note != null) {
                                        notes.add(note);
                                    }
                                }

                                // Update LiveData with the list of notes
                                notesLiveData.setValue(notes);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e("fetchNotesForTrip", "Error fetching notes: " + databaseError.getMessage());
                            }
                        });
                        break; // Stop after finding the matching trip
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("fetchNotesForTrip", "Error retrieving trips for userId: " + userId);
            }
        });
    }





    public LiveData<List<String>> getUsersLiveData() {
        return usersLiveData;
    }
    public LiveData<List<String>> getNotesLiveData() {
        return notesLiveData;
    }

    public LiveData<List<String>> getInvitedUsersLiveData() {
        return invitedUsersLiveData;
    }
}
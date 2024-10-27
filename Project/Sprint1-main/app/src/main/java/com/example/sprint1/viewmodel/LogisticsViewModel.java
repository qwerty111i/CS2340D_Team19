package com.example.sprint1.viewmodel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprint1.model.VacationTime;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

import com.example.sprint1.model.TravelDetails;

import java.util.concurrent.TimeUnit;



public class LogisticsViewModel extends ViewModel {
    private MutableLiveData<Integer> allottedTime;
    private MutableLiveData<Integer> plannedTime;

    private final MutableLiveData<List<String>> usersLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<String>> notesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<String>> invitedUsersLiveData = new MutableLiveData<>();
    private List<String> invitedUsersList = new ArrayList<>();
    private final DatabaseReference databaseReference;


    private final MutableLiveData<List<String>> notesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<VacationTime>> vacationTimesLiveData = new MutableLiveData<>();


    private List<String> notesList = new ArrayList<>();
    //private List<String> invitedUsersList;
    private final DatabaseReference notesRef;

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

    public LiveData<Integer> getAllottedTime() {
        return allottedTime;
    }
    public LiveData<Integer> getPlannedTime() {
        return plannedTime;
    }

    public LogisticsViewModel() {
        allottedTime = new MutableLiveData<>(0);
        plannedTime = new MutableLiveData<>(0);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        notesRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("notes");
        invitedUsersList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        fetchUsers();
        fetchInvitedUsers();
        retrieveNotes();


        fetchVacationTimes();
    }

    private void fetchVacationTimes() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference vacationTimesRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("vacations");

        vacationTimesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<VacationTime> vacationTimes = new ArrayList<>();
                for (DataSnapshot vacationSnapshot : snapshot.getChildren()) {
                    VacationTime vacationTime = vacationSnapshot.getValue(VacationTime.class);
                    if (vacationTime != null) {
                        vacationTimes.add(vacationTime);
                        Log.d("LogisticsViewModel", "Retrieved VacationTime: " + vacationTime.getDuration() + ", Start: " + vacationTime.getStartDate() + ", End: " + vacationTime.getEndDate());
                    } else {
                        Log.d("LogisticsViewModel", "VacationTime is null for snapshot: " + vacationSnapshot);
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
    public void addNote(String note) {
        if (note == null || note.trim().isEmpty()) {
            Log.d("LogisticsViewModel", "Attempted to add an empty note, ignoring.");
            return;
        }

        notesList.add(note);
        notesLiveData.setValue(notesList);
        saveNoteToFirebase(note);
    }


    public void removeNote(String note) {
        notesList.remove(note);
        notesLiveData.setValue(notesList);
        removeNoteFromFirebase(note);

        if (note.contains("-->")) {
            // Extract the inviter's email (assumes the note ends with " --> inviterEmail")
            String[] parts = note.split("-->");
            if (parts.length == 2) {
                String inviterEmail = parts[1].trim();

                // Now remove the note from the inviter's Firebase node
                RemoveNoteFromInviter(inviterEmail, parts[0].trim());
            }
        }

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

    private void RemoveNoteFromInviter(String inviterEmail, String originalNote) {
        // Locate the inviter based on their email in the database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.orderByChild("email").equalTo(inviterEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot inviterSnapshot : snapshot.getChildren()) {
                        // Get the inviter's user ID
                        String inviterId = inviterSnapshot.getKey();
                        if (inviterId != null) {

                            DatabaseReference inviterNotesRef = usersRef.child(inviterId).child("notes");

                            // Call the inviter's ViewModel method to remove the note
                            inviterNotesRef.orderByValue().equalTo(originalNote).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    for (DataSnapshot noteSnapshot : snapshot.getChildren()) {
                                        noteSnapshot.getRef().removeValue();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e("Firebase", "Error removing note from inviter", error.toException());
                                }
                            });
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

    public void saveNoteToFirebase(String note) {
        notesRef.push().setValue(note); // Push new note to Firebase
    }

    public void removeNoteFromFirebase(String note) {
        notesRef.orderByValue().equalTo(note).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot noteSnapshot : snapshot.getChildren()) {
                    noteSnapshot.getRef().removeValue(); // Remove note from Firebase
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

    public void fetchInvitedUsers() {
        String inviterId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child(inviterId).child("invitedUsers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                invitedUsersList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String invitedUserEmail = userSnapshot.getKey().replace(",", ".");
                    invitedUsersList.add(invitedUserEmail);
                }
                invitedUsersLiveData.setValue(invitedUsersList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("LogisticsViewModel", "Error retrieving invited users: " + databaseError.getMessage());
            }
        });
    }

    public void inviteUsers(List<String> selectedUsers, Context context) {
        String inviterId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users");

        for (String invitedUserEmail : selectedUsers) {
            boolean alreadyInvited = invitedUsersList.contains(invitedUserEmail);
            if (!alreadyInvited) {
                invitedUsersList.add(invitedUserEmail);
                invitedUsersLiveData.setValue(invitedUsersList);
                userReference.orderByChild("email").equalTo(invitedUserEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                String invitedUserId = userSnapshot.getKey();
                                userReference.child(inviterId).child("invitedUsers")
                                        .child(invitedUserEmail.replace(".", ",")).setValue(true);

                                // Share notes with the invited user
                                userReference.child(inviterId).child("email").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot emailSnapshot) {
                                        String inviterEmail = emailSnapshot.getValue(String.class); // Fetch the inviter's email

                                        if (inviterEmail != null && !inviterEmail.isEmpty()) {
                                            userReference.child(inviterId).child("notes")
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot notesSnapshot) {
                                                            for (DataSnapshot noteSnapshot : notesSnapshot.getChildren()) {
                                                                // Retrieve the note content
                                                                String noteContent = noteSnapshot.getValue(String.class);
                                                                String marker = "-->";
                                                                if (noteContent != null && !noteContent.contains(marker)) {
                                                                    // Add the inviter's email to the note content
                                                                    noteContent = noteContent + " -->  " + inviterEmail;

                                                                    // Copy each note to the invited user's "notes" node
                                                                    userReference.child(invitedUserId).child("notes")
                                                                            .push().setValue(noteContent);
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                            Log.d("Firebase", "Error retrieving notes");
                                                        }
                                                    });
                                        } else {
                                            Log.d("Firebase", "Inviter email not found");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.d("Firebase", "Error retrieving inviter email");
                                    }
                                });

                                // Share travel details with the invited user
                                userReference.child(inviterId).child("email").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot emailSnapshot) {
                                        String inviterEmail = emailSnapshot.getValue(String.class);
                                        if (inviterEmail != null && !inviterEmail.isEmpty()) {
                                            userReference.child(inviterId).child("travelDetails")
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot travelDetailsSnapshot) {
                                                            for (DataSnapshot travelDetailSnapshot : travelDetailsSnapshot.getChildren()) {
                                                                TravelDetails value = travelDetailSnapshot.getValue(TravelDetails.class);
                                                                String marker = "-->";
                                                                if (value != null && !value.getLocation().contains(marker)) {
                                                                    // Add the inviter's email to the location
                                                                    value.setLocation(value.getLocation() + " --> " + inviterEmail);

                                                                    // Save the modified travel detail to the invited user's "travelDetails" node
                                                                    userReference.child(invitedUserId).child("travelDetails")
                                                                            .push().setValue(value);
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                            Log.d("Firebase", "Error retrieving travel details");
                                                        }
                                                    });
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
                        }


                        Toast.makeText(context, "Users invited!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("LogisticsViewModel", "Error finding invited user: " + error.getMessage());
                    }
                });
            }
        }
    }
}

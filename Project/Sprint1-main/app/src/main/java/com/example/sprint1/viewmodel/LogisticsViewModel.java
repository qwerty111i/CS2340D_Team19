package com.example.sprint1.viewmodel;

import android.util.Log;

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
import java.util.concurrent.TimeUnit;


public class LogisticsViewModel extends ViewModel {
    private MutableLiveData<Integer> allottedTime;
    private MutableLiveData<Integer> plannedTime;

    private final MutableLiveData<List<String>> usersLiveData = new MutableLiveData<>();
    private final DatabaseReference databaseReference;

    private final MutableLiveData<List<String>> notesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<VacationTime>> vacationTimesLiveData = new MutableLiveData<>();

    private List<String> notesList = new ArrayList<>();
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
                e.printStackTrace();
            }
        }

        if (earliestStartDate != null && latestEndDate != null) {
            long diffInMillis = latestEndDate.getTime() - earliestStartDate.getTime();
            int allocatedDays = (int) TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
            allottedTime.setValue(allocatedDays);
        } else {
            allottedTime.setValue(0);
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
        retrieveNotes();

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        fetchUsers();


        fetchVacationTimes();
    }

    private void fetchVacationTimes() {
        DatabaseReference vacationTimesRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("vacationTimes");

        vacationTimesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<VacationTime> vacationTimes = new ArrayList<>();
                for (DataSnapshot vacationSnapshot : snapshot.getChildren()) {
                    VacationTime vacationTime = vacationSnapshot.getValue(VacationTime.class);
                    if (vacationTime != null) {
                        vacationTimes.add(vacationTime);
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
        notesList.add(note);
        notesLiveData.setValue(notesList);
        saveNoteToFirebase(note); // This saves the note to Firebase
    }


    public void removeNote(String note) {
        notesList.remove(note);
        notesLiveData.setValue(notesList);
        removeNoteFromFirebase(note); // Remove the note from Firebase
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
                // Handle possible errors.
                Log.e("Firebase", "Error retrieving notes", error.toException());
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

}

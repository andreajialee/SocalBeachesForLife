package com.example.socalbeachesforlife.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socalbeachesforlife.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TripInfo extends AppCompatActivity {

    private TextView beach, start, end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_info);


        beach = (TextView) findViewById(R.id.beachName);
        start = (TextView) findViewById(R.id.startTime);
        end = (TextView) findViewById(R.id.endTime);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            private static final String TAG = "TripInfo";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("trip").exists()) {
                    System.out.println("Trip exists");
                    DatabaseReference tripInfo = ref.child("trip");
                    tripInfo.addValueEventListener(new ValueEventListener() {
                        private static final String TAG = "TripInfo";

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String beachName = (String) snapshot.child("beachName").getValue();
                            String startTime = (String) snapshot.child("endTime").getValue();
                            String endTime = (String) snapshot.child("startTime").getValue();

                            beach.setText("Beach Name: " + beachName);
                            start.setText("Start Time : " + startTime);
                            end.setText("End Name: " + endTime);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d(TAG, error.getMessage());
                        }
                    });
                } else  {
                    Toast.makeText(TripInfo.this, "There is no saved trip.", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, error.getMessage());
            }
        });
    }
}
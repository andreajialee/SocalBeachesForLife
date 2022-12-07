package com.example.socalbeachesforlife.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socalbeachesforlife.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TripInfo extends AppCompatActivity implements View.OnClickListener{

    private TextView beach, start, end, duration, hotel, hotelBeach, hotelInfo;
    private Button beachButton, hotelButton;
    private String beachUrl, hotelUrl;
    private static final String TAG = "TripInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_info);

        beach = (TextView) findViewById(R.id.beachName);
        start = (TextView) findViewById(R.id.startTime);
        end = (TextView) findViewById(R.id.endTime);
        duration = (TextView) findViewById(R.id.duration);
        beachButton = (Button) findViewById(R.id.beachButton);

        hotelBeach = (TextView) findViewById(R.id.hotelBeach);
        hotel = (TextView) findViewById(R.id.hotelName);
        hotelInfo = (TextView) findViewById(R.id.hotelInfo);
        hotelButton = (Button) findViewById(R.id.hotelButton);

        beachButton.setOnClickListener(this);
        hotelButton.setOnClickListener(this);

        getBeachInfo();
        getHotelInfo();
    }

    protected void getHotelInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            private static final String TAG = "TripInfo";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("hotel").exists()) {
                    DatabaseReference tripInfo = ref.child("hotel");
                    tripInfo.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String hotelName = (String) snapshot.child("hotelName").getValue();
                            String hotelData = (String) snapshot.child("hotelInfo").getValue();
                            String beachName = (String) snapshot.child("beachName").getValue();
                            hotelUrl = (String) snapshot.child("url").getValue();

                            hotelBeach.setText(beachName);
                            hotel.setText(hotelName);
                            hotelInfo.setText(hotelData);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d(TAG, error.getMessage());
                        }
                    });
                } else  {
                    Toast.makeText(TripInfo.this, "There is no saved hotel.", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, error.getMessage());
            }
        });
    }

    protected void getBeachInfo() {
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

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String beachName = (String) snapshot.child("beachName").getValue();
                            String startTime = (String) snapshot.child("startTime").getValue();
                            String endTime = (String) snapshot.child("endTime").getValue();
                            long time = (Long) snapshot.child("duration").getValue();
                            beachUrl = (String) snapshot.child("url").getValue();

                            beach.setText(beachName);
                            start.setText(startTime);
                            end.setText(endTime);
                            duration.setText(time + " minutes");
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

    @Override
    public void onClick(View view) {
        String uri = "";
        switch (view.getId()) {
            case R.id.beachButton:
                uri = beachUrl;
                break;
            case R.id.hotelButton:r:
                uri = hotelUrl;
                break;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(TripInfo.this,"Failed to launch map.", Toast.LENGTH_SHORT).show();
        }
    }
}
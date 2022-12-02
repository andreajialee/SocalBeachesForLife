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

public class Profile extends AppCompatActivity implements View.OnClickListener{
    private TextView add_reviews, manage_reviews, logout, saved_routes, find_beach;
    private static final String TAG = "Profile.java ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        saved_routes = (Button) findViewById(R.id.saved_route);
        saved_routes.setOnClickListener(this);

        add_reviews = (Button) findViewById(R.id.add_review);
        add_reviews.setOnClickListener(this);

        manage_reviews = (Button) findViewById(R.id.manage_review);
        manage_reviews.setOnClickListener(this);

        find_beach = (Button) findViewById(R.id.find_beaches);
        find_beach.setOnClickListener(this);

        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.saved_route:
                System.out.println("Saved Route");
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("trip").exists()) {
                            System.out.println("Trip exists");
                            DatabaseReference tripInfo = ref.child("trip");
                            tripInfo.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String url = (String) snapshot.child("url").getValue();
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    intent.setPackage("com.google.android.apps.maps");
                                    try {
                                        startActivity(intent);
                                    } catch (ActivityNotFoundException e) {
                                        Toast.makeText(Profile.this,"Google Maps Intent NULL", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.d(TAG, error.getMessage());
                                }
                            });
                        } else  {
                            Toast.makeText(Profile.this, "There is no saved trip.", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, error.getMessage());
                    }
                });
                break;
            case R.id.add_review:
                startActivity(new Intent(this, AddReview.class));
                break;
            case R.id.manage_review:
                startActivity(new Intent(this, ManageReview.class));
                break;
            case R.id.find_beaches:
                startActivity(new Intent(this, MapsActivity.class));
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(Profile.this, "Sign Out Successful!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}

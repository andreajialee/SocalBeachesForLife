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
    private TextView name, email, manage_reviews, logout, saved_routes, find_beach;
    private static final String TAG = "Profile.java ";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = (TextView) findViewById(R.id.names);
        email = (TextView) findViewById(R.id.emails);
        email.setText(mAuth.getCurrentUser().getEmail());

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference ref = rootNode.getReference("Users").child(mAuth.getCurrentUser().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText((String) snapshot.child("fullName").getValue() );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        saved_routes = (Button) findViewById(R.id.saved_route);
        saved_routes.setOnClickListener(this);

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
                startActivity(new Intent(this, TripInfo.class));
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

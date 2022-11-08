package com.example.socalbeachesforlife.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socalbeachesforlife.R;
import com.google.firebase.auth.FirebaseAuth;

public class Profile extends AppCompatActivity implements View.OnClickListener{
    private TextView add_reviews, manage_reviews, logout, saved_routes;

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

        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.saved_route:
                break;
            case R.id.add_review:
                startActivity(new Intent(this, AddReview.class));
                break;
            case R.id.manage_review:
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(Profile.this, "Sign Out Successful!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}

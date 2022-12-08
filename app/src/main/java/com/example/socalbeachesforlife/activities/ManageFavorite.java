package com.example.socalbeachesforlife.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socalbeachesforlife.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

public class ManageFavorite extends AppCompatActivity implements View.OnClickListener{

    Button mUnfavorite, mRoute, mReviews;
    private String beachName, uri;
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_favorite);

        mUnfavorite = (Button) findViewById(R.id.unfavorite_button);
        mUnfavorite.setOnClickListener(this);
        mReviews = (Button) findViewById(R.id.reviews_button);
        mReviews.setOnClickListener(this);
        mRoute = (Button) findViewById(R.id.route_button);
        mRoute.setOnClickListener(this);

        Intent curIntent = getIntent();
        beachName = curIntent.getStringExtra("bname");
        uri = curIntent.getStringExtra("uri");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.unfavorite_button:
                unfavoriteReview();
                break;
            case R.id.reviews_button:
                Intent i = new Intent(ManageFavorite.this, ReviewMenu.class);
                i.putExtra("bname", beachName);
                startActivity(i);
                break;
            case R.id.route_button:
                System.out.println(uri);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(ManageFavorite.this,"Failed to launch map.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void unfavoriteReview(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference ref = rootNode.getReference("Users").child(mAuth.getCurrentUser().getUid());
        DatabaseReference favorites = ref.child("favorites");
        Bundle extras = getIntent().getExtras();

        favorites.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    String bname = (String) postSnapshot.child("beachName").getValue();
                    if(extras != null) {
                        if (bname.equals(beachName)) {
                            postSnapshot.getRef().removeValue();
                            mStorageRef.child(postSnapshot.getKey()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void v) {
                                    Toast.makeText( ManageFavorite.this, "Unfavorited Beach!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            startActivity(new Intent(ManageFavorite.this, Profile.class));
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getMessage());
            }
        });
    }
}

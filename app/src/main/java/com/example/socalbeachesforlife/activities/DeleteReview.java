package com.example.socalbeachesforlife.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class DeleteReview extends AppCompatActivity implements View.OnClickListener{
    private List<String> listReview = new ArrayList<>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    TextView banner, beachName, anon, comments;
    RatingBar bar;
    Button delete;
    ImageView imag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_review);

        banner = (TextView) findViewById(R.id.banner);
        beachName = (TextView) findViewById(R.id.beachName);
        anon = (TextView) findViewById(R.id.anon);
        comments = (TextView) findViewById(R.id.comments);
        bar = (RatingBar) findViewById(R.id.simpleRatingBar);
        delete = (Button) findViewById(R.id.delete);
        imag = (ImageView) findViewById(R.id.add_image);

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference ref = rootNode.getReference("Users").child(mAuth.getCurrentUser().getUid());

        DatabaseReference reviews = ref.child("review");
        Bundle extras = getIntent().getExtras();
        reviews.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    String bname = (String) postSnapshot.child("beachName").getValue();
                    if(extras != null) {
                        String name = extras.getString("bname");

                        if (bname.equals(name)) {
                            beachName.setText((String) postSnapshot.child("beachName").getValue());
                            if ((Boolean) postSnapshot.child("anon").getValue() == true) {
                                anon.setText("Anonymous: Yes");
                            } else {
                                anon.setText("Anonymous: No");
                            }
                            comments.setText("Comments: " + (String) postSnapshot.child("comment").getValue());
                            Long test = (Long) postSnapshot.child("starCount").getValue();
                            Float num = test.floatValue();
                            bar.setRating((Float) num);


                            mStorageRef.child(postSnapshot.getKey()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Picasso.with(DeleteReview.this).load(uri).into(imag);
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getMessage());
            }
        });

        delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.delete:
                deleteReview();
                break;
        }
    }


    public void deleteReview(){
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference ref = rootNode.getReference("Users").child(mAuth.getCurrentUser().getUid());

        DatabaseReference reviews = ref.child("review");

        Bundle extras = getIntent().getExtras();
        reviews.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    String bname = (String) postSnapshot.child("beachName").getValue();
                    if(extras != null) {
                        String name = extras.getString("bname");

                        if (bname.equals(name)) {
                           postSnapshot.getRef().removeValue();
                            Toast.makeText(DeleteReview.this, "Deleted review!", Toast.LENGTH_SHORT).show();
                            mStorageRef.child(postSnapshot.getKey()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void v) {
                                    Toast.makeText(DeleteReview.this, "Deleted image!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            startActivity(new Intent(DeleteReview.this, Profile.class));
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

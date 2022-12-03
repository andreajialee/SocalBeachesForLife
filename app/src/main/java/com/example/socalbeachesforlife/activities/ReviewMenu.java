package com.example.socalbeachesforlife.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socalbeachesforlife.R;
import com.example.socalbeachesforlife.getters.ReviewViewArrayAdapter;
import com.example.socalbeachesforlife.models.ReviewView;
import com.example.socalbeachesforlife.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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


public class ReviewMenu extends AppCompatActivity {
    private ArrayList<ReviewView> reviews = new ArrayList<>();
    private ListView list;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private Uri mUri;
    private ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_menu);
        list = (ListView) findViewById(R.id.beach_menu);
        image = (ImageView) findViewById(R.id.images);
        String uid = mAuth.getCurrentUser().getUid();
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference ref = rootNode.getReference("Users");
        Bundle extras = getIntent().getExtras();
        ReviewViewArrayAdapter adapt = new ReviewViewArrayAdapter(ReviewMenu.this, reviews);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    if(snap.hasChild("review")){
                        DatabaseReference r = snap.getRef().child("review");
                        r.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot snaps : snapshot.getChildren()){
                                    String bname = (String) snaps.child("beachName").getValue();
                                    if(extras != null) {
                                        String name = extras.getString("bname");

                                        if (bname.equals(name)) {
                                            String user;
                                            if((Boolean) snaps.child("anon").getValue() == false){
                                                user = (String) snap.child("fullName").getValue();
                                            }
                                            else {
                                                user = "Anonymous";
                                            }
                                            String comment = (String) snaps.child("comment").getValue();
                                            Long test = (Long) snaps.child("starCount").getValue();
                                            Float rat = test.floatValue();

                                            mStorageRef.child(snaps.getKey()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    ReviewView v = new ReviewView(uri, rat, user, comment);
                                                    reviews.add(v);
                                                    list.setAdapter(adapt);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    ReviewView v = new ReviewView(mUri, rat, user, comment);
                                                    reviews.add(v);
                                                    list.setAdapter(adapt);
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

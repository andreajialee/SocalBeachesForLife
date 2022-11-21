package com.example.socalbeachesforlife.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socalbeachesforlife.R;
import com.example.socalbeachesforlife.models.Review;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AddReview extends AppCompatActivity implements View.OnClickListener {
    private TextView banner;
    private RatingBar ratingBar;
    private EditText comments, beach_names;
    private ImageView add_images;
    private ProgressBar progressBar;
    private CheckBox anon;
    private Button upload_review, image_button;
    private Uri imageURI;
    private FirebaseStorage storage;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        banner = (TextView) findViewById(R.id.banner);
        beach_names = (EditText) findViewById(R.id.beachName);
        ratingBar = (RatingBar) findViewById(R.id.simpleRatingBar);
        comments = (EditText) findViewById(R.id.comments);
        anon = (CheckBox) findViewById(R.id.anon);
        add_images = (ImageView) findViewById(R.id.add_image);
        image_button = (Button) findViewById(R.id.image_button);
        upload_review = (Button) findViewById(R.id.upload_review);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        image_button.setOnClickListener(this);
        upload_review.setOnClickListener(this);
        anon.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageURI = data.getData();
            add_images.setImageURI(imageURI);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upload_review:
                addReview();
                break;
            case R.id.image_button:
                Intent gallery = new Intent();
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, 2);
                break;
        }
    }

    private void addReview() {
        String name = beach_names.getText().toString().trim();
        Float rating = ratingBar.getRating();
        String comment = comments.getText().toString().trim();
        Boolean isAnon = anon.isChecked();

        if (name.isEmpty()) {
            beach_names.setError("Please provide name of beach");
            beach_names.requestFocus();
            return;
        }

        if (comment.isEmpty()) {
            comment = "";
        }

        if (rating == 0) {
            Toast.makeText(AddReview.this, "Please provide rating", Toast.LENGTH_LONG).show();
            ratingBar.requestFocus();
            return;
        }

        String id = UUID.randomUUID().toString();

        if (imageURI != null) {
            storage = FirebaseStorage.getInstance();
            StorageReference ref = storage.getReference().child(id);
            ref.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddReview.this, "Upload image successful!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(AddReview.this, "Upload image failed", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        Review review = new Review(rating, name, comment, isAnon);

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Users");
        DatabaseReference reference1 = reference.child(mAuth.getCurrentUser().getUid());

        reference1.child("review").child(id).setValue(review).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    // Redirect to user profile
                    Toast.makeText(AddReview.this, "Uploaded review!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.VISIBLE);
                    startActivity (new Intent(AddReview.this, Profile.class));
                }
                else {
                    Toast.makeText(AddReview.this, "Failed to upload review. Please try again", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}
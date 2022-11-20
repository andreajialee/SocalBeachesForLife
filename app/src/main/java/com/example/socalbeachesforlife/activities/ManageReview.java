package com.example.socalbeachesforlife.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.socalbeachesforlife.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class ManageReview extends AppCompatActivity{
    private List<String> listReview = new ArrayList<>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_reviews);
        ListView list = (ListView) findViewById(R.id.list_view);
        ArrayAdapter<String> r = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listReview);

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference ref = rootNode.getReference("Users").child(mAuth.getCurrentUser().getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("review").exists()){
                    Toast.makeText(ManageReview.this, "Reviews available!", Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(ManageReview.this, "No reviews", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ManageReview.this, Profile.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference reviews = ref.child("review");
        reviews.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    String bname = (String) postSnapshot.child("beachName").getValue();

                    listReview.add(bname);
                    list.setAdapter(r);

                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {

                            String item = (String) list.getItemAtPosition(position);
                            Intent i = new Intent(ManageReview.this, DeleteReview.class);
                            i.putExtra("bname", item);
                            startActivity(i);
                        }

                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getMessage());
            }
        });
    }
}

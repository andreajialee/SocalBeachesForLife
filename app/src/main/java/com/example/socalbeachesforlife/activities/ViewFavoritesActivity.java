package com.example.socalbeachesforlife.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.socalbeachesforlife.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class ViewFavoritesActivity extends AppCompatActivity{
    private List<String> listBeaches = new ArrayList<>();
    private List<String> listUrls = new ArrayList<>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_beaches);
        ListView list = (ListView) findViewById(R.id.list_view);
        ArrayAdapter<String> r = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listBeaches);

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference ref = rootNode.getReference("Users").child(mAuth.getCurrentUser().getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("favorites").exists()){
                    Toast.makeText(ViewFavoritesActivity.this, "Favorites available!", Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(ViewFavoritesActivity.this, "No favorite beaches.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ViewFavoritesActivity.this, Profile.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference faves = ref.child("favorites");
        faves.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    String bName = (String) postSnapshot.child("beachName").getValue();
                    String url = (String) postSnapshot.child("url").getValue();
                    listBeaches.add(bName);
                    listUrls.add(url);
                    list.setAdapter(r);

                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            Intent i = new Intent(ViewFavoritesActivity.this, ReviewMenu.class);
                            i.putExtra("bname", listBeaches.get(position));
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

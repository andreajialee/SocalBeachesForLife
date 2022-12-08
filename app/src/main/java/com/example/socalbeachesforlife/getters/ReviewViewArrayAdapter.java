package com.example.socalbeachesforlife.getters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RatingBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.socalbeachesforlife.R;
import com.example.socalbeachesforlife.activities.DeleteReview;
import com.example.socalbeachesforlife.models.ReviewView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReviewViewArrayAdapter extends ArrayAdapter<ReviewView> {
    private Context context;

    public ReviewViewArrayAdapter(@NonNull Context context, ArrayList<ReviewView> arrayList) {
        super(context, R.layout.custom_list_view, arrayList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(context).inflate(R.layout.custom_list_view, parent, false);
        }

        ReviewView currentNumberPosition = getItem(position);

        ImageView imag = currentItemView.findViewById(R.id.images);

        if(currentNumberPosition.getNumbersImageId() != null){
            System.out.println("here");
            System.out.println(currentNumberPosition.getNumbersImageId().toString());
            Picasso.with(context).load(currentNumberPosition.getNumbersImageId()).fit().into(imag);
        }
        else {
            imag.setVisibility(View.GONE);
        }



        RatingBar bar = currentItemView.findViewById(R.id.rating);
        bar.setRating(currentNumberPosition.getRatings());

        TextView name = currentItemView.findViewById(R.id.usernames);
        name.setText(currentNumberPosition.getUsername());

        TextView desc = currentItemView.findViewById(R.id.description);
        desc.setText(currentNumberPosition.getDescription());

        return currentItemView;
    }
}

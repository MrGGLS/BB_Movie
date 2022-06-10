package com.ggls.myapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ggls.myapp.R;
import com.ggls.myapp.entity.ReviewEntity;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private List<ReviewEntity> reviewEntityList;

    public void setReviewEntityList(List<ReviewEntity> reviewEntityList) {
        this.reviewEntityList = reviewEntityList;
    }

    public ReviewListAdapter(Context mContext, List<ReviewEntity> reviewEntityList) {
        this.mContext = mContext;
        this.reviewEntityList = reviewEntityList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.review_item_layout,parent,false);
        ReviewListAdapter.ViewHolader viewHolader = new ReviewListAdapter.ViewHolader(v);
        return viewHolader;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ReviewListAdapter.ViewHolader vh = (ReviewListAdapter.ViewHolader) holder;
        ReviewEntity reviewEntity = reviewEntityList.get(position);
        vh.username.setText(reviewEntity.getUsername());
        DecimalFormat format = new DecimalFormat("0.0");
        String rate = format.format(reviewEntity.getRating()*10);
        vh.rating.setText(rate+"/10");
        vh.ratingBar.setRating(Float.parseFloat(format.format(reviewEntity.getRating()*5)));
        vh.review.setText(reviewEntity.getReview());
    }

    @Override
    public int getItemCount() {
        return reviewEntityList.size();
    }

    static class ViewHolader extends RecyclerView.ViewHolder{
        private TextView username;
        private TextView rating;
        private RatingBar ratingBar;
        private TextView review;
        public ViewHolader(@NonNull View v) {
            super(v);
            username = v.findViewById(R.id.review_username);
            rating = v.findViewById(R.id.review_movie_rating_text);
            ratingBar = v.findViewById(R.id.review_movie_rating_bar);
            review = v.findViewById(R.id.user_review_content);
        }
    }
}

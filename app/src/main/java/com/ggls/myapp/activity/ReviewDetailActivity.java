package com.ggls.myapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.ggls.myapp.R;
import com.ggls.myapp.fragment.MovieListFragment;
import com.ggls.myapp.fragment.ReviewListFragment;

public class ReviewDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        String movieid = bundle.getString("movieid");
        ReviewListFragment.setMovieid(movieid);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.review_list_fragment_id, ReviewListFragment.newInstance())
                .commit();
    }
}
package com.ggls.myapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ggls.myapp.R;
import com.ggls.myapp.api.APIConfig;
import com.ggls.myapp.entity.MovieEntity;
import com.ggls.myapp.entity.ReviewEntity;
import com.ggls.myapp.fragment.MovieListFragment;
import com.ggls.myapp.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieDetailActivity extends AppCompatActivity {
    private RatingBar ratingBar;
    private Button reviewButton;
    private Button ratingButton;
    private MovieEntity movieEntity;
    private boolean isChangedRating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        movieEntity = (MovieEntity) bundle.getSerializable(MovieListFragment.MOVIE_KEY);
        ratingBar = findViewById(R.id.rating_bar);
        EditText review = findViewById(R.id.movie_detail_review_text);
        reviewButton = findViewById(R.id.review_button);
        ratingButton = findViewById(R.id.rating_button);
        TextView name = findViewById(R.id.movie_detail_name);
        TextView summary = findViewById(R.id.summary_detail_content);
        TextView genre = findViewById(R.id.genre_detail_content);
        TextView rate = findViewById(R.id.movie_detail_rate_text);
        TextView year = findViewById(R.id.movie_detail_year_text);
        TextView runtime = findViewById(R.id.movie_detail_runtime_text);
        ImageView cover = findViewById(R.id.movie_detail_img);
        name.setText(movieEntity.getName());
        summary.setText(movieEntity.getSummary());
        genre.setText(movieEntity.getGenre());
        DecimalFormat format = new DecimalFormat("0.0");
        String rateStr = format.format(movieEntity.getRate() * 10);
        rate.setText(rateStr);
        year.setText(String.valueOf(movieEntity.getYear()));
        runtime.setText(String.valueOf(movieEntity.getRuntime()));
        Picasso.get().load(movieEntity.getImgUrl()).into(cover);
        ratingBar.setRating(2.5f);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    isChangedRating = true;
                }
            }
        });
        reviewButton.setOnClickListener((_v) -> {
            Intent reviewIntent = new Intent(MovieDetailActivity.this, ReviewDetailActivity.class);
            reviewIntent.putExtra("movieid", movieEntity.getId());
            startActivity(reviewIntent);
        });
        ratingButton.setOnClickListener((_v) -> {
            if(review.getText().toString().length()<1){
                Toast.makeText(this, "You should write a review first!", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Thanks for your rating!", Toast.LENGTH_SHORT).show();
            //TODO finish submit user rating and review logics
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OkHttpClient client = new OkHttpClient();
                        String url = APIConfig.USER_OPER_URL + APIConfig.USER_RATING +
                                "?username=" + AppUtils.USERNAME + "&movieid=" +
                                movieEntity.getId() + "&rating=" + (isChangedRating ? ratingBar.getRating()*2/10.0 : 0) + "&review=" + review.getText().toString();
                        Log.e("review_url", url);
                        isChangedRating = false;
                        Request request = new Request.Builder().url(url).build();
                        Response response = client.newCall(request).execute();
                        String resp = response.body().string();//获取到的json数据
                        Log.d("resp", resp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            finish();
        });
    }
}
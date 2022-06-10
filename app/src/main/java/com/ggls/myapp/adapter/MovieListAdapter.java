package com.ggls.myapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.ggls.myapp.MainActivity;
import com.ggls.myapp.entity.MovieEntity;
import com.ggls.myapp.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<MovieEntity> movieEntityList;
    protected OnItemClickListener listener;

    public MovieListAdapter(Context mContext, List<MovieEntity> movieEntityList, OnItemClickListener listener) {
        this.mContext = mContext;
        this.movieEntityList = movieEntityList;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onClick(int pos);
        void onLongClick(int pos);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.movie_item_layout,parent,false);
        ViewHolader viewHolader = new ViewHolader(v);
        return viewHolader;
    }

    public void setMovieEntityList(List<MovieEntity> movieEntityList) {
        this.movieEntityList = movieEntityList;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolader vh = (ViewHolader) holder;
        MovieEntity movieEntity = movieEntityList.get(position);
        holder.itemView.setOnClickListener(v -> {
            listener.onClick(position);
        });
        holder.itemView.setOnLongClickListener(v -> {
            listener.onLongClick(position);
            return false;
        });
        vh.mName.setText(movieEntity.getName());
        vh.mSummary.setText(movieEntity.getSummary());
        vh.mGenre.setText(movieEntity.getGenre());
        DecimalFormat format = new DecimalFormat("0.0");
        String rate = format.format(movieEntity.getRate() * 10);
        vh.mRate.setText(rate);
        vh.mYear.setText(String.valueOf(movieEntity.getYear()));
        vh.mRuntime.setText(String.valueOf(movieEntity.getRuntime())+" min");
        Picasso.get().load(movieEntity.getImgUrl()).into(vh.coverImg);
    }

    @Override
    public int getItemCount() {
        return movieEntityList.size();
    }

    static class ViewHolader extends RecyclerView.ViewHolder{
        private TextView mName;
        private TextView mSummary;
        private TextView mGenre;
        private TextView mRate;
        private TextView mYear;
        private TextView mRuntime;
        private ImageView coverImg;
        public ViewHolader(@NonNull View v) {
            super(v);
            mName = v.findViewById(R.id.movie_name);
            mSummary = v.findViewById(R.id.summary_content);
            mGenre = v.findViewById(R.id.genre_content);
            mRate = v.findViewById(R.id.movie_rate_text);
            mYear = v.findViewById(R.id.movie_year_text);
            mRuntime = v.findViewById(R.id.movie_runtime_text);
            coverImg = v.findViewById(R.id.movie_img_id);
        }
    }

}

package com.ggls.myapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.ggls.myapp.R;
import com.ggls.myapp.activity.MovieDetailActivity;
import com.ggls.myapp.adapter.MovieListAdapter;
import com.ggls.myapp.adapter.ReviewListAdapter;
import com.ggls.myapp.api.APIConfig;
import com.ggls.myapp.entity.MovieEntity;
import com.ggls.myapp.entity.ReviewEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.header.BezierRadarHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewListFragment extends Fragment {
    private int pageNum = 1;
    private int pageSize = APIConfig.PAGE_SIZE;
    private List<ReviewEntity> reviewList = new ArrayList<>();
    private RecyclerView rv;
    private RefreshLayout refreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private ReviewListAdapter adapter;
    private static String movieid = "666";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    adapter.setReviewEntityList(reviewList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    public ReviewListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ReviewListFragment newInstance() {
        ReviewListFragment fragment = new ReviewListFragment();
        return fragment;
    }

    public static void setMovieid(String movieid) {
        ReviewListFragment.movieid = movieid;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reviewList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_review_list, container, false);
        rv = v.findViewById(R.id.review_list_recycler);
        adapter = new ReviewListAdapter(getActivity(), reviewList);
        rv.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);
        refreshLayout = (RefreshLayout) v.findViewById(R.id.review_reflesh_layout);
        refreshLayout.setRefreshHeader(new BezierRadarHeader(getContext()));
        refreshLayout.setRefreshFooter(new BallPulseFooter(getContext()));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                pageNum = 1;
                reviewList = new ArrayList<>();
                getReviewList();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                pageNum++;
                getReviewList();
            }
        });
        Log.e("hehe", "fuck"+movieid);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getReviewList();
    }

    private void getReviewList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    String url = APIConfig.USER_OPER_URL + APIConfig.REVIEW_LIST + "?movieid="+movieid+"&pageNum=" + pageNum + "&pageSize=" + pageSize;
                    Log.e("review_url",url);
                    Request request = new Request.Builder().url(url).build();
                    Response response = client.newCall(request).execute();
                    String resp = response.body().string();//获取到的json数据
                    Gson gson = new Gson();
                    Log.e("resp",resp);
                    List<ReviewEntity> newData = gson.fromJson(resp, new TypeToken<List<ReviewEntity>>() {
                    }.getType());
                    for (ReviewEntity newDatum : newData) {
                        reviewList.add(newDatum);
                    }
//                    Log.e("ok", newData.get(0).toString());
                    mHandler.sendEmptyMessage(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
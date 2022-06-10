package com.ggls.myapp.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ggls.myapp.R;
import com.ggls.myapp.activity.HomeActivity;
import com.ggls.myapp.activity.MovieDetailActivity;
import com.ggls.myapp.adapter.MovieListAdapter;
import com.ggls.myapp.api.APIConfig;
import com.ggls.myapp.entity.MovieEntity;
import com.ggls.myapp.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.header.BezierRadarHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieListFragment extends Fragment {
    private int pageNum = 1;
    private int pageSize = APIConfig.PAGE_SIZE;
    private List<MovieEntity> movieList = new ArrayList<>();
    private RecyclerView rv;
    private RefreshLayout refreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private MovieListAdapter adapter;
    private static String movieName = "";
    public static final String MOVIE_KEY = "MOVIE";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    adapter.setMovieEntityList(movieList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    public MovieListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MovieListFragment newInstance() {
        MovieListFragment fragment = new MovieListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie_list, container, false);
        rv = v.findViewById(R.id.movie_list_recycler);
        EditText search = getActivity().findViewById(R.id.search_text);
        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    movieName = search.getText().toString();
                    pageNum = 1;
                    movieList = new ArrayList<>();
                    getMovieList(pageSize);
                    return true;
                } else {
                    return false;
                }
            }
        });
        adapter = new MovieListAdapter(getActivity(), movieList, new MovieListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
//                MovieDetailFragment movieDetailFragment = MovieDetailFragment.newInstance();
//                movieDetailFragment.setMovieEntity(movieList.get(pos));
//                getFragmentManager()
//                        .beginTransaction()
//                        .addToBackStack(MovieListFragment.class.getSimpleName())
//                        .replace(R.id.movie_list_fragment_id,movieDetailFragment)
//                        .commit();
                Bundle bundle = new Bundle();
                bundle.putSerializable(MOVIE_KEY, movieList.get(pos));
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(int pos) {
                Toast.makeText(getActivity(), "你以为长按有用吗？", Toast.LENGTH_SHORT).show();
            }
        });
        rv.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);
        refreshLayout = (RefreshLayout) v.findViewById(R.id.reflesh_layout);
        refreshLayout.setRefreshHeader(new BezierRadarHeader(getContext()));
        refreshLayout.setRefreshFooter(new BallPulseFooter(getContext()));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                pageNum = 1;
                movieList = new ArrayList<>();
                getMovieList(pageSize);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                pageNum++;
                getMovieList(pageSize);
            }
        });
        Log.e("hehe", "fuck");
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMovieList(pageSize);
    }

    private void getMovieList(Integer pageSize) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    String url = APIConfig.MOVIE_BASE_URL + APIConfig.MOVIE_LIST + "?pageNum=" + pageNum + "&pageSize=" + pageSize + "&movieName=" + movieName;
//                    Log.e("url",url);
                    Request request = new Request.Builder().url(url).build();
                    Response response = client.newCall(request).execute();
                    String resp = response.body().string();//获取到的json数据
                    Gson gson = new Gson();
                    Log.e("fuck", resp);
                    List<MovieEntity> newData = gson.fromJson(resp, new TypeToken<List<MovieEntity>>() {}.getType());
                    for (MovieEntity newDatum : newData) {
                        movieList.add(newDatum);
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
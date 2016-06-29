package kung.movies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import kung.movies.model.Movie;
import kung.movies.model.Page;

/**
 * HomeView
 */
public class MainActivity extends AppCompatActivity implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private BaseQuickAdapter mAdapter;
    private ArrayList<Movie> list = new ArrayList<>();

    private Context context;
    private int pageIndex = 1;
    private int pageSize = 20;


    private int sort_type;
    private final int SORT_BY_POPULAR = 1;
    private final int SORT_BY_RATED = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        onRefresh();
    }

    private void initUI() {
        context = this;
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new BaseQuickAdapter<Movie>(this, R.layout.list_item_movie, list) {
            @Override
            protected void convert(BaseViewHolder holder, Movie movie) {
                String url = Constant.image_baseUrl + movie.getPoster_path();
                Glide.with(context).load(url).crossFade().placeholder(R.drawable.ic_movie).into((ImageView) holder.getView(R.id.iv_poster));
            }
        };
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(this);
        mAdapter.openLoadMore(pageSize, true);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context, DetailsActivity.class);
                Movie item = (Movie) mAdapter.getItem(position);
                intent.putExtra("movId", item.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_popular) {
            sort_type = SORT_BY_POPULAR;
            onRefresh();
            return true;
        } else if (id == R.id.action_rated) {
            sort_type = SORT_BY_RATED;
            onRefresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        getMovies(sort_type, pageIndex);
    }

    @Override
    public void onLoadMoreRequested() {
        pageIndex++;
        getMovies(sort_type, pageIndex);
    }

    private void getMovies(int sortType, int page) {
        RequestParams params = new RequestParams();
        params.add("api_key", Constant.api_key);
        params.add("page", page + "");
        params.add("language", "zh");
        AsyncHttpClient client = new AsyncHttpClient();
        String url = (sortType == SORT_BY_RATED) ? Constant.api_rated : Constant.api_popular;
        client.get(context, url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Page page = JSON.parseObject(responseString, Page.class);
                if (pageIndex == 1) {
                    list.clear();
                    list.addAll(page.getResults());
                    mAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    list.addAll(page.getResults());
                    if (mAdapter.getData().size() >= page.getTotal_results()) {
                        mAdapter.notifyDataChangedAfterLoadMore(false);
                        TextView tvAll = new TextView(context);
                        tvAll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        tvAll.setText("加载完毕");
                        tvAll.setPadding(5, 15, 5, 15);
                        tvAll.setGravity(Gravity.CENTER);
                        mAdapter.addFooterView(tvAll);
                    } else {
                        mAdapter.notifyDataChangedAfterLoadMore(true);
                    }
                }
            }
        });
    }
}

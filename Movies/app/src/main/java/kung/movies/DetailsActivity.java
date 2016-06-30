package kung.movies;

import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.w3c.dom.Text;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import kung.movies.model.Movie;
import kung.movies.model.MovieDetail;
import kung.movies.model.Page;
import kung.movies.model.Result;
import kung.movies.model.Review;
import kung.movies.model.Trailer;
import kung.movies.view.ListItemDecoration;

public class DetailsActivity extends AppCompatActivity {

    private MovieDetail movie;
    private long movId;
    private TextView tvTitle;
    private ImageView ivPoster;
    private TextView tvYear;
    private TextView tvRuntime;
    private RatingBar rb;
    private TextView tvRate;
    private TextView tvOverview;
    private Button btnFavor;

    private RecyclerView rvReviews;
    private RecyclerView rvTrailers;

    private BaseQuickAdapter mReviewAdapter;
    private BaseQuickAdapter mTrailerAdapter;
    private ArrayList<Trailer> trailers = new ArrayList<>();
    private ArrayList<Review> reviews = new ArrayList<>();

    private Context context;
    private TextView tvNoVideos;
    private TextView tvNoReviews;
    private LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(
            LinearLayoutCompat.LayoutParams.MATCH_PARENT,
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        context = this;
        if (savedInstanceState != null) {
            movie = savedInstanceState.getParcelable("mov");
            trailers = savedInstanceState.getParcelableArrayList("trailers");
            reviews = savedInstanceState.getParcelableArrayList("reviews");
        } else {
            movId = getIntent().getLongExtra("movId", 0);
            getDetail(movId);
            getTrailers(movId);
            getReviews(movId);
        }
        initUI();
    }

    private void initUI() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        tvNoVideos = new TextView(context);
        tvNoReviews = new TextView(context);
        tvNoVideos.setLayoutParams(layoutParams);
        tvNoReviews.setLayoutParams(layoutParams);
        tvNoVideos.setGravity(Gravity.CENTER_HORIZONTAL);
        tvNoReviews.setGravity(Gravity.CENTER_HORIZONTAL);
        tvNoVideos.setText("NO  Videos:(");
        tvNoReviews.setText("NO  Reviews :(");
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivPoster = (ImageView) findViewById(R.id.iv_poster);
        tvYear = (TextView) findViewById(R.id.tv_mov_year);
        tvRuntime = (TextView) findViewById(R.id.tv_runtime);
        rb = (RatingBar) findViewById(R.id.rb);
        tvRate = (TextView) findViewById(R.id.tv_rate);
        btnFavor = (Button) findViewById(R.id.btn_play);
        tvOverview = (TextView) findViewById(R.id.tv_overview);
        rvReviews = (RecyclerView) findViewById(R.id.rv_reviews);
        rvTrailers = (RecyclerView) findViewById(R.id.rv_trailers);
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvTrailers.setLayoutManager(new LinearLayoutManager(this));
        rvTrailers.addItemDecoration(new ListItemDecoration(context, LinearLayoutManager.VERTICAL));
        rvReviews.addItemDecoration(new ListItemDecoration(context, LinearLayoutManager.VERTICAL));
        mReviewAdapter = new BaseQuickAdapter<Review>(this, R.layout.list_item_review, reviews) {
            @Override
            protected void convert(BaseViewHolder holder, Review review) {
                holder.setText(R.id.tv_author, review.getAuthor() + ":")
                        .setText(R.id.tv_content, review.getContent());
            }
        };
        rvReviews.setAdapter(mReviewAdapter);
        mReviewAdapter.setEmptyView(tvNoReviews);
        mTrailerAdapter = new BaseQuickAdapter<Trailer>(this, R.layout.list_item_trailer, trailers) {
            @Override
            protected void convert(BaseViewHolder holder, Trailer trailer) {
                holder.setText(R.id.tv_name, trailer.getName());
            }
        };
        mTrailerAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context, VideoActivity.class);
                Trailer item = (Trailer) mTrailerAdapter.getItem(position);
                intent.putExtra("id", item.getKey());
                context.startActivity(intent);
            }
        });
        mTrailerAdapter.setEmptyView(tvNoVideos);
        rvTrailers.setAdapter(mTrailerAdapter);
        btnFavor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnFavor.isSelected())
                    btnFavor.setSelected(false);
                else
                    btnFavor.setSelected(true);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("mov", movie);
        outState.putParcelableArrayList("trailers", trailers);
        outState.putParcelableArrayList("reviews", reviews);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void bind(MovieDetail mov) {
        tvTitle.setText(mov.getTitle());
        tvYear.setText(mov.getRelease_date().substring(0, 4));
        tvRuntime.setText(getString(R.string.runtime, mov.getRuntime()));
        rb.setRating(mov.getVote_average());
        tvRate.setText(getString(R.string.move_rated, mov.getVote_average()));
        tvOverview.setText(mov.getOverview());
        if (mov.getHomepage().isEmpty()) {
            btnFavor.setSelected(false);
        } else {
            btnFavor.setSelected(true);
        }
        Glide.with(this).load(Constant.image_baseUrl + mov.getPoster_path()).crossFade().placeholder(R.drawable.ic_movie).into(ivPoster);
    }

    /**
     * get movie detail by id
     *
     * @param movId
     */
    private void getDetail(long movId) {
        RequestParams params = new RequestParams();
        params.add("api_key", Constant.api_key);
//        params.add("language", "zh");
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(this, Constant.api_baseUrl + movId, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                movie = JSON.parseObject(responseString, MovieDetail.class);
                bind(movie);
            }
        });

    }

    /**
     * get videos by id
     *
     * @param movId
     */
    private void getTrailers(long movId) {
        RequestParams params = new RequestParams();
        params.add("api_key", Constant.api_key);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(this, Constant.api_baseUrl + movId + "/videos", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Result result = JSON.parseObject(responseString, Result.class);
                trailers.clear();
                trailers.addAll(JSON.parseArray(result.getResults(), Trailer.class));
                mTrailerAdapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * get reviews of movie
     *
     * @param movId
     */
    private void getReviews(long movId) {
        RequestParams params = new RequestParams();
        params.add("api_key", Constant.api_key);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(this, Constant.api_baseUrl + movId + "/reviews", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Result result = JSON.parseObject(responseString, Result.class);
                reviews.clear();
                reviews.addAll(JSON.parseArray(result.getResults(), Review.class));
                mReviewAdapter.notifyDataSetChanged();
            }
        });

    }
}

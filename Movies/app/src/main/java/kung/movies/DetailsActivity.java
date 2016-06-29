package kung.movies;

import android.content.Intent;
import android.media.Rating;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;
import kung.movies.model.Movie;
import kung.movies.model.MovieDetail;
import kung.movies.model.Page;

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
    private Button btnPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if (savedInstanceState != null && savedInstanceState.getParcelable("mov") != null) {
            movie = savedInstanceState.getParcelable("mov");
        } else {
            movId = getIntent().getLongExtra("movId", 0);
            getDetail(movId);
        }
        initUI();
    }

    private void initUI() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivPoster = (ImageView) findViewById(R.id.iv_poster);
        tvYear = (TextView) findViewById(R.id.tv_mov_year);
        tvRuntime = (TextView) findViewById(R.id.tv_runtime);
        rb = (RatingBar) findViewById(R.id.rb);
        tvRate = (TextView) findViewById(R.id.tv_rate);
        btnPlay = (Button) findViewById(R.id.btn_play);
        tvOverview = (TextView) findViewById(R.id.tv_overview);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, VideoActivity.class);
                intent.putExtra("movTitle", movie.getTitle());
                intent.putExtra("videoUrl", movie.getHomepage());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("mov", movie);
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
            btnPlay.setEnabled(false);
        } else {
            btnPlay.setEnabled(true);
        }
        Glide.with(this).load(Constant.image_baseUrl + mov.getPoster_path()).crossFade().placeholder(R.drawable.ic_movie).into(ivPoster);
    }

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
}

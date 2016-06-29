package kung.movies;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;
import kung.movies.model.MovieDetail;
import kung.movies.model.Page;

public class DetailsActivity extends AppCompatActivity {

    private long movId;
    private TextView tvTitle;
    private ImageView ivPoster;
    private TextView tvYear;
    private TextView tvRuntime;
    private TextView tvOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initUI();
        movId = getIntent().getLongExtra("movId", 0);
        getDetail(movId);
    }

    private void initUI() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivPoster = (ImageView) findViewById(R.id.iv_poster);
        tvYear = (TextView) findViewById(R.id.tv_mov_year);
        tvRuntime = (TextView) findViewById(R.id.tv_runtime);
        tvOverview = (TextView) findViewById(R.id.tv_overview);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void bind(MovieDetail mov) {
        tvTitle.setText(mov.getTitle());
        tvYear.setText(mov.getRelease_date().substring(0, 3));
        tvRuntime.setText(mov.getRuntime() + "min");
        tvOverview.setText(mov.getOverview());
        Glide.with(this).load(Constant.image_baseUrl + mov.getPoster_path()).crossFade().placeholder(R.drawable.ic_movie).into(ivPoster);
    }

    private void getDetail(long movId) {
        RequestParams params = new RequestParams();
        params.add("api_key", Constant.api_key);
        params.add("language", "zh");
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(this, Constant.api_baseUrl + movId, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                MovieDetail movie = JSON.parseObject(responseString, MovieDetail.class);
                bind(movie);
            }
        });

    }
}

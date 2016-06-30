package kung.movies;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class VideoActivity extends AppCompatActivity {

    private WebView webView;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initUI();
    }

    private void initUI() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        id = getIntent().getStringExtra("id");

        webView = (WebView) findViewById(R.id.wv_video);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        String html = getHTML(id);
        webView.loadData(html, "text/html", "utf-8");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public String getHTML(String id) {
        return "<html><body><iframe class=\"youtube-player\" style=\"border: 0; width: 100%; height: 100%; padding:0px; margin:0px\" src=\"https://www.youtube.com/embed/"
                + id
                + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
    }
}

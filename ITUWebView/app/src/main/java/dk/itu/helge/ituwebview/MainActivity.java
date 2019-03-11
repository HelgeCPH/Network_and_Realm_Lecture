package dk.itu.helge.ituwebview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String url = getIntent().getDataString();
        if (null != url) {
            WebView webView = findViewById(R.id.web_view);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(url);
        }
    }
}

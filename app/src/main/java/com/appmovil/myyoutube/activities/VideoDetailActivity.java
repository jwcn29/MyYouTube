package com.appmovil.myyoutube.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.appmovil.myyoutube.R;

public class VideoDetailActivity extends AppCompatActivity {

    private WebView videoWebView;
    private ProgressBar loadingProgressBar;
    private Toolbar toolbar;

    @SuppressLint("SetJavaScriptEnabled") // Permite JavaScript en el WebView
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);

        toolbar = findViewById(R.id.detail_toolbar);
        videoWebView = findViewById(R.id.video_webview);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 🔙 Botón de regreso
            getSupportActionBar().setTitle(""); // Opcional: sin texto en Toolbar
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed()); // 🔙

        // Recibe datos enviados
        String videoId = getIntent().getStringExtra("VIDEO_ID");
        String videoTitle = getIntent().getStringExtra("title");

        // Configura el WebView
        WebSettings webSettings = videoWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        videoWebView.setWebChromeClient(new WebChromeClient());
        videoWebView.setWebViewClient(new WebViewClient() {
            // Muestra el progress bar mientras carga
            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                loadingProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                loadingProgressBar.setVisibility(View.GONE);
            }
        });

        // Carga el video
        if (videoId != null) {
            String embedUrl = "https://www.youtube.com/embed/" + videoId + "?autoplay=1&vq=small";
            String html = "<html><body style='margin:0; padding:0;'>"
                    + "<iframe width='100%' height='100%' src='" + embedUrl + "' frameborder='0' allow='autoplay; encrypted-media' allowfullscreen></iframe>"
                    + "</body></html>";

            videoWebView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        }
    }

    @Override
    protected void onDestroy() {
        if (videoWebView != null) {
            videoWebView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (videoWebView.canGoBack()) {
            videoWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}

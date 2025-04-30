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

import com.appmovil.myyoutube.R;

public class VideoPlayerActivity extends AppCompatActivity {

    public static final String EXTRA_VIDEO_ID = "video_id";

    private WebView webView;
    private ProgressBar loadingProgressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        webView = findViewById(R.id.webView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);

        String videoId = getIntent().getStringExtra(EXTRA_VIDEO_ID);

        if (videoId != null && !videoId.isEmpty()) {
            String videoUrl = "https://www.youtube.com/embed/" + videoId + "?autoplay=1&vq=hd720"; // 🔥 Mejor calidad y autoplay

            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true); // YouTube necesita JavaScript
            webSettings.setDomStorageEnabled(true);
            webSettings.setMediaPlaybackRequiresUserGesture(false); // 🔥 Empezar a reproducir sin tocar

            webView.setWebChromeClient(new WebChromeClient());
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    loadingProgressBar.setVisibility(View.GONE); // 🔥 Ocultar ProgressBar cuando carga
                    webView.setVisibility(View.VISIBLE);
                }
            });

            webView.loadUrl(videoUrl);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack(); // Si el usuario navegó dentro del WebView
        } else {
            super.onBackPressed(); // Cierra normalmente
        }
    }
}

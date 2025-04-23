package com.appmovil.myyoutube.activities;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.appmovil.myyoutube.R;

public class VideoDetailActivity extends AppCompatActivity {

    WebView videoWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);

        videoWebView = findViewById(R.id.video_webview);

        // Recibir la URL desde el intent
        String videoUrl = getIntent().getStringExtra("videoUrl");

        // Configurar WebView para mostrar el video embebido
        WebSettings webSettings = videoWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        videoWebView.setWebViewClient(new WebViewClient());

        // Cargar el video
        videoWebView.loadUrl(videoUrl);
    }
}

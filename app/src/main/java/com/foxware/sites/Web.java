package com.foxware.sites;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class Web extends AppCompatActivity {

    WebView web_view;
    String web_uri;
    ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        web_view = (WebView) findViewById(R.id.web_view);
        progress = (ProgressBar) findViewById(R.id.progress);
        web_uri = getIntent().getStringExtra("web_uri");

        web_view.getSettings().setJavaScriptEnabled(true);

        final Activity activity = this;
        web_view.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                activity.setProgress(newProgress);
                progress.setProgress(newProgress);
                if(newProgress == 100){
                    progress.setVisibility(View.GONE);
                }
            }


        });

        web_view.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Extras.makeText(Web.this, "Oh no "+description);
            }
        });

        web_view.loadUrl(web_uri);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

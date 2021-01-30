package com.example.mobiletermproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HurcodeWebView extends AppCompatActivity {

    private String sellerURL;
    private WebView webView;
    private Button backButton;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_web_view);
        Bundle extras = getIntent().getExtras();
        if(extras != null)
            sellerURL = extras.getString("sellerURL");
        else
        {
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "Seller's page does not exist",Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }

        //SET A TOOLBAR
        setContentView(R.layout.activity_product_web_view);
        Toolbar toolbar = findViewById(R.id.webToolbar);
        setSupportActionBar(toolbar);

        //BACK BUTTON AND TITLE
        backButton = (Button)findViewById(R.id.webBackButton);
        title = (TextView) findViewById(R.id.webTextView);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(sellerURL.contains("hurkanugur"))
        {
            backButton.setText("ABOUT");
            title.setText("    D e v e l o p e r");
        }
        else
        {
            backButton.setText("SELLERS");
            title.setText("     D e t a i l s");
        }

        //WEBVIEW SETTINGS
        webView = (WebView)findViewById(R.id.webView);
        HukoWebViewClient webViewClient = new HukoWebViewClient(this);
        webView.setWebViewClient(webViewClient);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(sellerURL);
    }

    private class HukoWebViewClient extends WebViewClient {
        private Activity activity = null;
        public HukoWebViewClient(Activity activity) { this.activity = activity; }
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            return false;
        }
    }
}
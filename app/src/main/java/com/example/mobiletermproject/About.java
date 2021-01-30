package com.example.mobiletermproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class About extends AppCompatActivity {
    private TextView linkedInText, githubText;
    private Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.aboutToolbar);
        setSupportActionBar(toolbar);

        backButton = (Button)findViewById(R.id.aboutBackButton);
        linkedInText = (TextView)findViewById(R.id.linkedIn);
        githubText = (TextView)findViewById(R.id.github);

        linkedInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkNetworkConnection() == true) {
                    Intent intent = new Intent(getApplicationContext(), HurcodeWebView.class);
                    intent.putExtra("sellerURL", "https://www.linkedin.com/in/hurkanugur");
                    startActivity(intent);
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "There is no internet connection",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        githubText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkNetworkConnection() == true) {
                    Intent intent = new Intent(getApplicationContext(), HurcodeWebView.class);
                    intent.putExtra("sellerURL", "https://github.com/hurkanugur");
                    startActivity(intent);
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "There is no internet connection",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNetworkConnection();
                finish();
            }
        });
   }
    private boolean checkNetworkConnection()
    {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && (networkInfo.isConnected() == true))
        {
            MainActivity.connectionTextView.setText("O n l i n e");
            return true;
        }
        else
        {
            MainActivity.connectionTextView.setText("O f f l i n e");
            return false;
        }
    }
}
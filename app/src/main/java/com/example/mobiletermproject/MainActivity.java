package com.example.mobiletermproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private Button scannerButton, productListButton, aboutButton;
    public static TextView connectionTextView;
    public static String barcodeText = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectionTextView = (TextView)findViewById(R.id.connectionTextView);
        scannerButton = (Button)findViewById(R.id.scannerButton);
        productListButton = (Button)findViewById(R.id.productListButton);
        aboutButton = (Button)findViewById(R.id.aboutButton);
        scannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setPrompt("Use Volume Up Key To Turn On The Flash");
                //set the beep sound
                intentIntegrator.setBeepEnabled(true);
                //locked orientation
                intentIntegrator.setOrientationLocked(true);
                //set capture activity
                intentIntegrator.setCaptureActivity(BarcodeScanner.class);
                //initiate scan
                intentIntegrator.initiateScan();
            }
        });
        productListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(barcodeText.length() != 0)
                    startActivity(new Intent(MainActivity.this, Products.class));
                else
                {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Please scan a code firstly",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, About.class));
            }
        });

        //CHECK INTERNET CONNECTION
        checkNetworkConnection();
    }
    private void checkNetworkConnection()
    {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && (networkInfo.isConnected() == true))
            connectionTextView.setText("O n l i n e");
        else
            connectionTextView.setText("O f f l i n e");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        checkNetworkConnection();
        //Initiate intent result
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        //Check if it is null or not
        if(intentResult.getContents() != null)
        {
            barcodeText = intentResult.getContents();
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "Code: [" + barcodeText + "]",Toast.LENGTH_SHORT).show();
                }
            });
            startActivity(new Intent(MainActivity.this,Products.class));
        }
        else
        {
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "Please come back later :(",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
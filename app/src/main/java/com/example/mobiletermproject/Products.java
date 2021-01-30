package com.example.mobiletermproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.util.ArrayList;

public class Products extends AppCompatActivity implements InformationAdapter.ProductOnClickListener
{
    private SharedPreferences sharedPreferences;
    public static final String sharedPreferencesName = "hurcodeReaderPreferences";
    private String barcodeText;
    private RecyclerView recyclerView;
    private InformationAdapter informationAdapter;
    private ArrayList<Information> informationArrayList = new ArrayList<>();
    private ProgressBar progressBar;
    private Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //SET A TOOLBAR
        setContentView(R.layout.activity_products);
        Toolbar toolbar = findViewById(R.id.productsToolbar);
        setSupportActionBar(toolbar);

        //BACK BUTTON
        backButton = (Button)findViewById(R.id.productsBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNetworkConnection();
                finish();
            }
        });

        //GET THE BARCODE VALUE
        barcodeText = MainActivity.barcodeText;

        //INITIALIZATION
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        informationAdapter = new InformationAdapter(informationArrayList,getApplicationContext(), this);
        recyclerView.setAdapter(informationAdapter);

        //SHARED PREFERENCES IS BEING USED
        sharedPreferences = getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
        if(checkNetworkConnection() == true)
            new GoogleShoppingInfo().execute();
        else
            offlineShoppingInfo();
    }
    @Override
    public void productOnClick(int position) {
        if(checkNetworkConnection() == true)
        {
            Intent intent = new Intent(getApplicationContext(), HurcodeWebView.class);
            intent.putExtra("sellerURL",informationArrayList.get(position).getSellerURL());
            startActivity(intent);
        }
        else
        {
            runOnUiThread(new Runnable() {
                public void run() {
                    checkNetworkConnection();
                    Toast.makeText(getApplicationContext(), "There is no internet connection",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private boolean checkNetworkConnection()
    {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && (networkInfo.isConnected() == true)) {
            MainActivity.connectionTextView.setText("O n l i n e");
            return true;
        }else {
            MainActivity.connectionTextView.setText("O f f l i n e");
            return false;
        }
    }
    private void offlineShoppingInfo()
    {
        if(sharedPreferences.contains(barcodeText + "title0"))
        {
            for(int i = 0; sharedPreferences.contains(barcodeText + "title" + i); i++)
            {
                String title = sharedPreferences.getString(barcodeText + "title" + i,"");
                String imgURL =  Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" + R.drawable.shopping).toString();
                String price = sharedPreferences.getString(barcodeText + "price" + i,"");
                String seller = sharedPreferences.getString(barcodeText + "seller" + i,"");
                String sellerURL = sharedPreferences.getString(barcodeText + "sellerURL" + i,"");
                informationArrayList.add(new Information(imgURL, title, price, seller, sellerURL));
            }
            runOnUiThread(new Runnable() {
                public void run() {
                    checkNetworkConnection();
                    Toast.makeText(getApplicationContext(), "The product is found in Offline History",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            runOnUiThread(new Runnable() {
                public void run() {
                    checkNetworkConnection();
                    Toast.makeText(getApplicationContext(), "The code is not found in Offline History",Toast.LENGTH_LONG).show();
                    finish(); //Products.super.onBackPressed(); //GO BACK COMMAND
                }
            });
        }

    }
    private class GoogleShoppingInfo extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            progressBar.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
            informationAdapter.notifyDataSetChanged();
        }

        @SuppressLint("ShowToast")
        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                //THE RESULTS THAT WILL BE FOUND WILL BE SAVED IN SHARED PREFERENCES !
                SharedPreferences.Editor editor = sharedPreferences.edit();
                int results = 0;
                //SEARCH ON GOOGLE
                String googleURL = "https://www.google.com/search?q=" + barcodeText + "&sa=X&hl=en&tbm=shop&tbs=p_ord:p,vw:l";
                Document document = Jsoup.connect(googleURL).get();
                Elements data = document.select("div.sh-dlr__list-result");
                for(int i = 0; i < data.size(); i++)
                {
                    String title = data.select("h3.xsRiS").eq(i).text();
                    String imgURL = "https://images.idgesg.net/images/article/2018/08/google_logo_black-100769125-large.jpg"; /*data.select("img.TL92Hc").eq(i).attr("src");*/
                    String price = data.select("span.QIrs8").eq(i).text();
                    price = price.substring(0, price.length() - 1); //Deletes the . at the end of the price
                    String seller = data.select("a.shntl.hy2WroIfzrX__merchant-name").eq(i).text();
                    String sellerURL = "https://www.google.com" + data.select("a.shntl.hy2WroIfzrX__merchant-name").eq(i).attr("href");

                    //SAVES THE DATA IN ARRAY LIST
                    informationArrayList.add(new Information(imgURL, title, price, seller, sellerURL));

                    //SAVES THE DATA IN SHARED PREFERENCES FOR OFFLINE SEARCHING
                    editor.putString(barcodeText + "title" + results, title);
                    editor.putString(barcodeText + "price" + results, price);
                    editor.putString(barcodeText + "seller" + results, seller);
                    editor.putString(barcodeText + "sellerURL" + results, sellerURL);
                    results++;
                    editor.commit();
                }
                //SEARCH ON EBAY
                googleURL = "https://www.ebay.com/sch/i.html?_from=R40&_nkw=" + barcodeText + "&_sacat=0&LH_TitleDesc=0&rt=nc";
                document = Jsoup.connect(googleURL).get();
                data = document.select("li.s-item.s-item--watch-at-corner");
                for(int i = 0; i < data.size(); i++) {
                    String title = data.select("h3.s-item__title").eq(i).text();
                    String imgURL = data.select("img.s-item__image-img").eq(i).attr("src");
                    String price = data.select("span.s-item__price").eq(i).text();
                    String seller = data.select("span.s-item__location.s-item__itemLocation").eq(i).text();
                    seller = "Ebay.com/" + seller.substring(5, seller.length());
                    String sellerURL = data.select("a.s-item__link").eq(i).attr("href");

                    //SAVES THE DATA IN ARRAY LIST
                    informationArrayList.add(new Information(imgURL, title, price, seller, sellerURL));

                    //SAVES THE DATA IN SHARED PREFERENCES FOR OFFLINE SEARCHING
                    editor.putString(barcodeText + "title" + results, title);
                    editor.putString(barcodeText + "price" + results, price);
                    editor.putString(barcodeText + "seller" + results, seller);
                    editor.putString(barcodeText + "sellerURL" + results, sellerURL);
                    results++;
                    editor.commit();
                }
                if(results == 0) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            checkNetworkConnection();
                            Toast.makeText(getApplicationContext(), "Sorry but I couldn't find anything :(",Toast.LENGTH_SHORT).show();
                            finish(); //Products.super.onBackPressed(); //GO BACK COMMAND
                        }
                    });
                }

            }catch (Exception e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        checkNetworkConnection();
                        Toast.makeText(getApplicationContext(), "Ops...Something went wrong :(",Toast.LENGTH_SHORT).show();
                        finish(); //Products.super.onBackPressed(); //GO BACK COMMAND
                    }
                });
            }
            return null;
        }
    }
}
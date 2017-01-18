package com.ssrij.warnindia;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;
import com.wang.avi.AVLoadingIndicatorView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    AVLoadingIndicatorView loadingIndicatorView;
    TextView loadingText;
    ArrayList<EarthquakeObject> earthquakeObjectArrayList;
    ArrayList<TsunamiObject> tsunamiObjectArrayList;
    ArrayList<String> cycloneObjectArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loadingText = (TextView) findViewById(R.id.textViewLoading);
        loadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.avloadingIndicatorView);

        performLoadOrThrowError();
    }

    public void getLatestEarthquakeData(Context context) {
        Tasks.executeInBackground(context, new BackgroundWork<ArrayList<EarthquakeObject>>() {
            @Override
            public ArrayList<EarthquakeObject> doInBackground() throws Exception {
                ArrayList<EarthquakeObject> earthquakeObjectList = new ArrayList<>();
                Document doc = Jsoup.connect(getString(R.string.isgn_earthquake_data_url)).timeout(15000).get();
                Element quakeDataTable = doc.select("table#homeEventTable").get(0);
                Elements quakeRows = quakeDataTable.select("tr");
                for (int i = 1; i < quakeRows.size(); i++) {
                    Element row = quakeRows.get(i);
                    Elements cols = row.select("td");
                    for (int j = 0; j < cols.size(); j = j + 6) {
                        earthquakeObjectList.add(new EarthquakeObject(cols.get(j).text(),
                                cols.get(j + 1).text(), cols.get(j + 2).text().substring(0, cols.get(j + 2).text().length() - 2),
                                cols.get(j + 3).text().substring(0, cols.get(j + 3).text().length() - 2), cols.get(j + 4).text(),
                                cols.get(j + 5).text()));
                    }
                }
                return earthquakeObjectList;
            }
        }, new Completion<ArrayList<EarthquakeObject>>() {
            @Override
            public void onSuccess(Context context, ArrayList<EarthquakeObject> result) {
                earthquakeObjectArrayList = result;
                getLatestTsunamiData(context);
            }

            @Override
            public void onError(Context context, Exception e) {
                loadingIndicatorView.setVisibility(View.INVISIBLE);
                loadingText.setVisibility(View.INVISIBLE);
                Snackbar snackbar = Snackbar
                        .make(getWindow().getDecorView().findViewById(android.R.id.content), "Failed to connect to server", Snackbar.LENGTH_INDEFINITE)
                        .setAction("TRY AGAIN", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                loadingIndicatorView.setVisibility(View.VISIBLE);
                                loadingText.setVisibility(View.VISIBLE);
                                performLoadOrThrowError();
                            }
                        });

                snackbar.show();
                Log.e("Error", e.getMessage());
            }
        });
    }

    public void getLatestTsunamiData(Context context) {
        Tasks.executeInBackground(context, new BackgroundWork<ArrayList<TsunamiObject>>() {
            @Override
            public ArrayList<TsunamiObject> doInBackground() throws Exception {
                ArrayList<TsunamiObject> tsunamiObjectsList = new ArrayList<>();
                Document doc = Jsoup.connect(getString(R.string.incois_tsunami_data_url)).timeout(15000).get();
                Element tsunamiDataTable = doc.select("table#table").get(0);
                Elements tsunamiRows = tsunamiDataTable.select("tr");
                Element row = tsunamiRows.get(1);
                Elements cols = row.select("td");
                tsunamiObjectsList.add(new TsunamiObject(cols.get(0).text(), cols.get(1).text(),
                        cols.get(2).text(),cols.get(3).text(),cols.get(4).text(),cols.get(5).text(),
                        cols.get(6).text(), cols.get(7).select("td").select("a").attr("href")));
                return tsunamiObjectsList;
            }
        }, new Completion<ArrayList<TsunamiObject>>() {
            @Override
            public void onSuccess(Context context, ArrayList<TsunamiObject> result) {
                tsunamiObjectArrayList = result;
                getLatestCycloneData(context);
            }

            @Override
            public void onError(Context context, Exception e) {
                loadingIndicatorView.setVisibility(View.INVISIBLE);
                loadingText.setVisibility(View.INVISIBLE);
                Snackbar snackbar = Snackbar
                        .make(getWindow().getDecorView().findViewById(android.R.id.content), "Failed to connect to server", Snackbar.LENGTH_INDEFINITE)
                        .setAction("TRY AGAIN", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                loadingIndicatorView.setVisibility(View.VISIBLE);
                                loadingText.setVisibility(View.VISIBLE);
                                performLoadOrThrowError();
                            }
                        });

                snackbar.show();
                Log.e("Error", e.getMessage());
            }
        });
    }

    public void getLatestCycloneData(Context context) {
        Tasks.executeInBackground(context, new BackgroundWork<ArrayList<String>>() {
            @Override
            public ArrayList<String> doInBackground() throws Exception {
                ArrayList<String> cycloneObjectsList = new ArrayList<>();
                Document doc = Jsoup.connect(getString(R.string.rsmc_cyclone_data_url)).timeout(15000).get();
                Element cycloneDataTable = doc.select("ul#ticker01").get(0);
                String tickerText = cycloneDataTable.select("li").first().select("span").first().text();
                cycloneDataTable = doc.select("div.mapArea.mapAreaHeight").get(0);
                String imageLink = cycloneDataTable.select("img").attr("src");
                cycloneObjectsList.add(tickerText);
                cycloneObjectsList.add(imageLink);
                return cycloneObjectsList;
            }
        }, new Completion<ArrayList<String>>() {
            @Override
            public void onSuccess(Context context, ArrayList<String> result) {
                cycloneObjectArrayList = result;
                getDetailedLatestCycloneData(context);
            }

            @Override
            public void onError(Context context, Exception e) {
                loadingIndicatorView.setVisibility(View.INVISIBLE);
                loadingText.setVisibility(View.INVISIBLE);
                Snackbar snackbar = Snackbar
                        .make(getWindow().getDecorView().findViewById(android.R.id.content), "Failed to connect to server", Snackbar.LENGTH_INDEFINITE)
                        .setAction("TRY AGAIN", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                loadingIndicatorView.setVisibility(View.VISIBLE);
                                loadingText.setVisibility(View.VISIBLE);
                                performLoadOrThrowError();
                            }
                        });

                snackbar.show();
                Log.e("Error", e.getMessage());
            }
        });
    }

    public void getDetailedLatestCycloneData(Context context) {
        Tasks.executeInBackground(context, new BackgroundWork<ArrayList<CycloneObject>>() {
            @Override
            public ArrayList<CycloneObject> doInBackground() throws Exception {
                ArrayList<CycloneObject> cycloneObjectsList = new ArrayList<>();
                Document doc = Jsoup.connect(getString(R.string.incois_storm_surge_data_url)).timeout(15000).get();
                Element cycloneDataTable = doc.select("table").get(0);
                Elements cycloneRows = cycloneDataTable.select("tr");
                for (int i = 1; i < cycloneRows.size(); i++) {
                    Element row = cycloneRows.get(i);
                    Elements cols = row.select("td");
                    for (int j = 0; j < cols.size(); j = j + 3) {
                        cycloneObjectsList.add(new CycloneObject(cols.get(j).text(),
                                cols.get(j + 1).text(), cols.get(j + 2).select("a").attr("href")));
                    }
                }
                return cycloneObjectsList;
            }
        }, new Completion<ArrayList<CycloneObject>>() {
            @Override
            public void onSuccess(Context context, ArrayList<CycloneObject> result) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putParcelableArrayListExtra("quakeDataList", earthquakeObjectArrayList);
                intent.putParcelableArrayListExtra("tsunamiDataList", tsunamiObjectArrayList);
                intent.putParcelableArrayListExtra("detailedCycloneDataList", result);
                intent.putStringArrayListExtra("cycloneDataList", cycloneObjectArrayList);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Context context, Exception e) {
                loadingIndicatorView.setVisibility(View.INVISIBLE);
                loadingText.setVisibility(View.INVISIBLE);
                Snackbar snackbar = Snackbar
                        .make(getWindow().getDecorView().findViewById(android.R.id.content), "Failed to connect to server", Snackbar.LENGTH_INDEFINITE)
                        .setAction("TRY AGAIN", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                loadingIndicatorView.setVisibility(View.VISIBLE);
                                loadingText.setVisibility(View.VISIBLE);
                                performLoadOrThrowError();
                            }
                        });

                snackbar.show();
                Log.e("Error", e.getMessage());
            }
        });
    }

    public void performLoadOrThrowError() {
        if (isNetworkAvailable()) {
            loadingIndicatorView.setVisibility(View.VISIBLE);
            loadingText.setVisibility(View.VISIBLE);
            getLatestEarthquakeData(this);
        } else {
            loadingIndicatorView.setVisibility(View.INVISIBLE);
            loadingText.setVisibility(View.INVISIBLE);
            Snackbar snackbar = Snackbar
                    .make(getWindow().getDecorView().findViewById(android.R.id.content), "No internet connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("TRY AGAIN", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            performLoadOrThrowError();
                        }
                    });

            snackbar.show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

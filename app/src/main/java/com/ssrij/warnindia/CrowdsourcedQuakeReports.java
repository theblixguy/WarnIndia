package com.ssrij.warnindia;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidmapsextensions.ClusteringSettings;
import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.MarkerOptions;
import com.androidmapsextensions.SupportMapFragment;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.truizlop.fabreveallayout.FABRevealLayout;
import com.truizlop.fabreveallayout.OnRevealChangeListener;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesWithFallbackProvider;

public class CrowdsourcedQuakeReports extends AppCompatActivity {

    com.androidmapsextensions.GoogleMap map;
    Location curLoc;
    Boolean didGetLocation = false;
    PrettyTime p;
    ProgressBar progressBar;
    Firebase ref;
    long old_time = 0;
    long cur_time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_crowdsourced_quake_reports);

        FABRevealLayout fabRevealLayout = (FABRevealLayout) findViewById(R.id.fab_reveal_layout);
        configureFABReveal(fabRevealLayout);
        progressBar = (ProgressBar) findViewById(R.id.progress_spinner);
        progressBar.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCrowdsourced);
        toolbar.setTitle("Did you feel it?");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        p = new PrettyTime();
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_earthquake_crowdsourced_new)).getExtendedMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setClustering(new ClusteringSettings().addMarkersDynamically(true));
        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                if (SmartLocation.with(CrowdsourcedQuakeReports.this).location().state().locationServicesEnabled()) {
                    if (SmartLocation.with(CrowdsourcedQuakeReports.this).location().state().isAnyProviderAvailable()) {
                        curLoc = SmartLocation.with(CrowdsourcedQuakeReports.this).location(new LocationGooglePlayServicesWithFallbackProvider(CrowdsourcedQuakeReports.this)).getLastLocation();

                        if (curLoc != null) {
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(curLoc.getLatitude(), curLoc.getLongitude()), 5);
                            map.animateCamera(cameraUpdate);
                        }

                        SmartLocation.with(CrowdsourcedQuakeReports.this).location(new LocationGooglePlayServicesWithFallbackProvider(CrowdsourcedQuakeReports.this))
                                .oneFix()
                                .start(new OnLocationUpdatedListener() {
                                    @Override
                                    public void onLocationUpdated(Location location) {
                                        curLoc = location;
                                        didGetLocation = true;
                                        SmartLocation.with(getApplicationContext()).location().stop();
                                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                                                new LatLng(location.getLatitude(), location.getLongitude()), 5);
                                        map.animateCamera(cameraUpdate);

                                        ref = new Firebase("http://path_to_firebase_instance");
                                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                for (DataSnapshot child : dataSnapshot.getChildren()) {

                                                    if (child.getChildrenCount() == 1) {
                                                        Snackbar snackbar = Snackbar
                                                                .make(getWindow().getDecorView().findViewById(android.R.id.content), "No quake reports today!", Snackbar.LENGTH_LONG);
                                                        snackbar.show();
                                                        progressBar.setVisibility(View.GONE);
                                                    } else {
                                                        for (DataSnapshot child1 : child.getChildren()) {
                                                            map.addMarker(new MarkerOptions()
                                                                    .position(new LatLng(Double.parseDouble(child1.child("latitude").getValue().toString()),
                                                                            Double.parseDouble(child1.child("longitude").getValue().toString())))
                                                                    .title("Quake felt")
                                                                    .snippet(p.format(new Date(Long.parseLong(child1.child("timestamp").getValue().toString()))))
                                                            );
                                                        }
                                                        progressBar.setVisibility(View.GONE);
                                                    }
                                                }

                                            }

                                            @Override
                                            public void onCancelled(FirebaseError firebaseError) {

                                            }
                                        });

                                    }
                                });
                    } else {
                        Snackbar snackbar = Snackbar
                                .make(getWindow().getDecorView().findViewById(android.R.id.content), "No location providers available at the moment", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar
                            .make(getWindow().getDecorView().findViewById(android.R.id.content), "Location turned off", Snackbar.LENGTH_INDEFINITE)
                            .setAction("OPEN SETTINGS", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent viewIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(viewIntent);
                                }
                            });
                    snackbar.show();
                }
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void configureFABReveal(FABRevealLayout fabRevealLayout) {
        fabRevealLayout.setOnRevealChangeListener(new OnRevealChangeListener() {
            @Override
            public void onMainViewAppeared(FABRevealLayout fabRevealLayout, View mainView) {}

            @Override
            public void onSecondaryViewAppeared(final FABRevealLayout fabRevealLayout, View secondaryView) {
                cur_time = System.currentTimeMillis();
                old_time = PreferenceManager.getDefaultSharedPreferences(CrowdsourcedQuakeReports.this)
                        .getLong("repTimestamp", 0);
                if (old_time == 0) {
                    submitUserReport();
                    prepareBackTransition(fabRevealLayout);
                } else if ((( cur_time - old_time)/1000) > 3600) {
                    submitUserReport();
                    prepareBackTransition(fabRevealLayout);
                } else {
                    ((TextView)secondaryView.findViewById(R.id.report_text)).setText("You have already submitted a report!");
                    prepareBackTransition(fabRevealLayout);
                }
            }
        });
    }

    private void prepareBackTransition(final FABRevealLayout fabRevealLayout) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fabRevealLayout.revealMainView();
            }
        }, 2000);
    }

    private void submitUserReport() {
        CrowdsourcedQuakeReportObject repObj = new CrowdsourcedQuakeReportObject();
        repObj.setDeviceID(Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID));
        repObj.setLatitude(Double.toString(curLoc.getLatitude()));
        repObj.setLongitude(Double.toString(curLoc.getLongitude()));
        repObj.setTimestamp(Long.toString(System.currentTimeMillis()/1000));
        String strUUID = java.util.UUID.randomUUID().toString();
        ref.child("crowdSourcedData").child(strUUID).setValue(repObj);
        PreferenceManager.getDefaultSharedPreferences(this).edit().putLong("repTimestamp", System.currentTimeMillis()).apply();
    }
}

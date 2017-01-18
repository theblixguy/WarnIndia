package com.ssrij.warnindia;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.OnMapReadyCallback;
import com.github.fabtransitionactivity.SheetLayout;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class EarthquakeMapFragment extends Fragment {

    com.androidmapsextensions.MapView mapView;
    com.androidmapsextensions.GoogleMap map;
    ArrayList<EarthquakeObject> earthquakeObjectArrayList;
    Boolean latestQuakeObject = false;
    SheetLayout mSheetLayout;

    public static final EarthquakeMapFragment newInstance(ArrayList<EarthquakeObject> earthquakeObjects) {
        EarthquakeMapFragment fragment = new EarthquakeMapFragment();
        Bundle bundle = new Bundle(1);
        bundle.putParcelableArrayList("quakeListData", earthquakeObjects);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        earthquakeObjectArrayList = getArguments().getParcelableArrayList("quakeListData");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_earthquake_map, container, false);
        mSheetLayout = (SheetLayout) v.getRootView().findViewById(R.id.bottom_sheet);
        mapView = (com.androidmapsextensions.MapView) v.findViewById(R.id.map_earthquake);
        mapView.onCreate(savedInstanceState);
        mapView.getExtendedMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                try {
                    googleMap.setMyLocationEnabled(true);
                } catch (SecurityException s) {

                }

                MapsInitializer.initialize(EarthquakeMapFragment.this.getActivity());

                for (int i = 0; i < earthquakeObjectArrayList.size(); i++) {

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                    try {
                        calendar.setTime(sdf.parse(earthquakeObjectArrayList.get(i).getOriginTime()));
                    } catch (ParseException e) {

                    }
                    sdf.setTimeZone(TimeZone.getDefault());

                    map.addMarker(new com.androidmapsextensions.MarkerOptions()
                            .position(createLatLongFromStringVals(earthquakeObjectArrayList.get(i).getLatitude(),
                                    earthquakeObjectArrayList.get(i).getLongitude()))
                            .draggable(false)
                            .icon((!latestQuakeObject) ? BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED) : BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                            .snippet("Origin Time (UTC): " + earthquakeObjectArrayList.get(i).getOriginTime() + "\n"
                                    + "Origin Time (IST): " + sdf.format(calendar.getTime()) + "\n"
                                    + "Magnitude: " + earthquakeObjectArrayList.get(i).getMagnitude() + "\n" +
                                    "Latitude: " + earthquakeObjectArrayList.get(i).getLatitude() + "\n" +
                                    "Longitude: " + earthquakeObjectArrayList.get(i).getLongitude() + "\n" +
                                    "Depth (Km): " + earthquakeObjectArrayList.get(i).getDepth())
                            .data((!latestQuakeObject) ? "latest" : 0)
                            .title(earthquakeObjectArrayList.get(i).getRegionName()));
                    latestQuakeObject = true;

                }

                map.setInfoWindowAdapter(new com.androidmapsextensions.GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoContents(com.androidmapsextensions.Marker marker) {
                        Context context = getContext();

                        LinearLayout info = new LinearLayout(context);
                        info.setOrientation(LinearLayout.VERTICAL);

                        TextView title = new TextView(context);
                        title.setTextColor(Color.BLACK);
                        title.setGravity(Gravity.CENTER);
                        title.setTypeface(null, Typeface.BOLD);
                        title.setText(marker.getTitle());

                        TextView snippet = new TextView(context);
                        snippet.setTextColor(Color.GRAY);
                        snippet.setText(marker.getSnippet());

                        info.addView(title);
                        info.addView(snippet);

                        return info;
                    }

                    @Override
                    public View getInfoWindow(com.androidmapsextensions.Marker marker) {
                        return null;
                    }
                });

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        createLatLongFromStringVals(
                                earthquakeObjectArrayList.get(0).getLatitude(),
                                earthquakeObjectArrayList.get(0).getLongitude()), 5);
                map.animateCamera(cameraUpdate, new GoogleMap.CancelableCallback() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onFinish() {
                        for (int i = 0; i < map.getMarkers().size(); i++) {
                            if (map.getMarkers().get(i).getData().equals("latest")) {
                                map.getMarkers().get(i).showInfoWindow();
                            }
                        }
                    }
                });
            }
        });

        return v;
    }

    public LatLng createLatLongFromStringVals(String lat, String lng) {
        return new LatLng(Float.valueOf(lat), Float.valueOf(lng));
    }


    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}

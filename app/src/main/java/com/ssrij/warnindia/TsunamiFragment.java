package com.ssrij.warnindia;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.OnActionClickListener;
import com.dexafree.materialList.card.action.TextViewAction;
import com.dexafree.materialList.view.MaterialListView;

import java.util.ArrayList;

public class TsunamiFragment extends Fragment {

    ArrayList<TsunamiObject> tsunamiObjectArrayList;

    public static final TsunamiFragment newInstance(ArrayList<TsunamiObject> tsunamiObjects) {
        TsunamiFragment fragment = new TsunamiFragment();
        Bundle bundle = new Bundle(1);
        bundle.putParcelableArrayList("tsunamiListData", tsunamiObjects);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tsunamiObjectArrayList = getArguments().getParcelableArrayList("tsunamiListData");
        CustomTabs.with(getContext()).warm();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tsunami, container, false);
        MaterialListView mListView = (MaterialListView) v.findViewById(R.id.material_listview);

        final CardProvider provider = new Card.Builder(getContext())
                .setTag("BASIC_BUTTONS_CARD")
                .withProvider(new CardProvider())
                .setSubtitle("Preliminary information")
                .setSubtitleColor(Color.RED)
                .setLayout(R.layout.material_basic_buttons_card)
                .setTitle(tsunamiObjectArrayList.get(0).getRegionName())
                .setDescription("Origin Time (UTC): " + tsunamiObjectArrayList.get(0).getOriginTime() +
                        "\n" + "Quake Magnitude: " + tsunamiObjectArrayList.get(0).getMagnitude() +
                "\n" + "Latitude: " + tsunamiObjectArrayList.get(0).getLatitude() + "\n" +
                "Longitude: " + tsunamiObjectArrayList.get(0).getLongitude() + "\n" +
                "Depth (Kilometers): " + tsunamiObjectArrayList.get(0).getDepth() + "\n" +
                "Warning type (Automatic/Manual): " + tsunamiObjectArrayList.get(0).getAutomaticManual())
                .addAction(R.id.left_text_button, new TextViewAction(getContext())
                        .setText("View on map")
                        .setTextResourceColor(R.color.black_button)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                Toast.makeText(getContext(), "TODO", Toast.LENGTH_SHORT).show();
                            }
                        }))
                .addAction(R.id.right_text_button, new TextViewAction(getContext())
                        .setText("More info")
                        .setTextResourceColor(R.color.accent_material_dark)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                String url = getString(R.string.incois_home_url);
                                url = url + tsunamiObjectArrayList.get(0).getBulletinLink();

                                CustomTabs.Style style = new CustomTabs.Style(getContext());
                                style.setCloseButton(R.drawable.ic_arrow_back);
                                style.setStartAnimation(R.anim.slide_in_right, R.anim.slide_out_left);
                                style.setExitAnimation(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                style.setToolbarColor(R.color.colorPrimary);
                                style.setShowTitle(true);

                                CustomTabs
                                        .with(getContext())
                                        .setStyle(style)
                                        .openUrl(url, getActivity());

                            }
                        }));
        mListView.getAdapter().add(provider.endConfig().build());
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

}

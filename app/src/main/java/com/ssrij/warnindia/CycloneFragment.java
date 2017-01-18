package com.ssrij.warnindia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.OnActionClickListener;
import com.dexafree.materialList.card.action.TextViewAction;
import com.dexafree.materialList.view.MaterialListView;

import java.util.ArrayList;

public class CycloneFragment extends Fragment {

    ArrayList<String> cycloneObjectArrayList;
    ArrayList<CycloneObject> detailedCycloneObjectArrayList;

    public static final CycloneFragment newInstance(ArrayList<String> cycloneDataList,
                                                    ArrayList<CycloneObject> detailedCycloneDataList) {
        CycloneFragment fragment = new CycloneFragment();
        Bundle bundle = new Bundle(2);
        bundle.putParcelableArrayList("detailedCycloneDataList", detailedCycloneDataList);
        bundle.putStringArrayList("cycloneListData", cycloneDataList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cycloneObjectArrayList = getArguments().getStringArrayList("cycloneListData");
        detailedCycloneObjectArrayList = getArguments().getParcelableArrayList("detailedCycloneDataList");
        CustomTabs.with(getContext()).warm();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cyclone, container, false);
        MaterialListView mListView = (MaterialListView) v.findViewById(R.id.material_listview1);

        final String imageUrl = getString(R.string.rsmc_cyclone_main_url) + cycloneObjectArrayList.get(1);

        final CardProvider provider = new Card.Builder(getContext())
                .withProvider(new CardProvider())
                .setLayout(R.layout.material_image_with_buttons_card)
                .setDescription(cycloneObjectArrayList.get(0))
                .setDrawable(imageUrl)
                .addAction(R.id.left_text_button, new TextViewAction(getContext())
                        .setText("View larger image")
                        .setTextResourceColor(R.color.black_button)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                Intent intent = new Intent(getContext(), ImageViewer.class);
                                intent.putExtra("imageUrl", imageUrl);
                                startActivity(intent);
                            }
                        }))
                .addAction(R.id.right_text_button, new TextViewAction(getContext())
                        .setText("More info")
                        .setTextResourceColor(R.color.accent_material_dark)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {

                                CustomTabs.Style style = new CustomTabs.Style(getContext());
                                style.setShowTitle(false);
                                style.setCloseButton(R.drawable.ic_arrow_back);
                                style.setStartAnimation(R.anim.slide_in_right, R.anim.slide_out_left);
                                style.setExitAnimation(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                style.setToolbarColor(R.color.colorPrimary);

                                CustomTabs
                                        .with(getContext())
                                        .setStyle(style)
                                        .openUrl(getString(R.string.rsmc_bulletin_url_conv), getActivity());

                            }
                        }));

        mListView.getAdapter().add(provider.endConfig().build());

        Card card = new Card.Builder(getContext())
                .withProvider(new DividerCardProvider())
                .setText("Previous cyclones")
                .endConfig()
                .build();

        mListView.getAdapter().add(card);

        for (int i = 0; i < detailedCycloneObjectArrayList.size(); i++) {
            final CardProvider provider1 = new Card.Builder(getContext())
                    .setTag(detailedCycloneObjectArrayList.get(i).getBulletinLink())
                    .withProvider(new CardProvider())
                    .setLayout(R.layout.material_basic_buttons_card)
                    .setTitle(detailedCycloneObjectArrayList.get(i).getCycloneName())
                    .setDescription("Wind gust speed (Kmph): " + detailedCycloneObjectArrayList.get(i).getWindSpeed())
                    .addAction(R.id.left_text_button, new TextViewAction(getContext())
                            .setText("View more info")
                            .setTextResourceColor(R.color.black_button)
                            .setListener(new OnActionClickListener() {
                                @Override
                                public void onActionClicked(View view, Card card) {
                                    CustomTabs.Style style = new CustomTabs.Style(getContext());
                                    style.setShowTitle(true);
                                    style.setCloseButton(R.drawable.ic_arrow_back);
                                    style.setStartAnimation(R.anim.slide_in_right, R.anim.slide_out_left);
                                    style.setExitAnimation(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                    style.setToolbarColor(R.color.colorPrimary);

                                    if (card.getTag().toString().startsWith("http")) {
                                        CustomTabs
                                                .with(getContext())
                                                .setStyle(style)
                                                .openUrl(card.getTag().toString(), getActivity());
                                    } else {
                                        String finalUrl = getString(R.string.incois_home_url) + card.getTag().toString();
                                        CustomTabs
                                                .with(getContext())
                                                .setStyle(style)
                                                .openUrl(finalUrl, getActivity());
                                    }

                                }
                            }));
            mListView.getAdapter().add(provider1.endConfig().build());
        }
        mListView.scrollToPosition(0);
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

package com.ssrij.warnindia;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;

public class DividerCardProvider extends CardProvider<DividerCardProvider> {

    String text;

    public int getLayout() {
        return R.layout.divider_card;
    }

    public DividerCardProvider setText(String text){
        this.text = text;
        notifyDataSetChanged();
        return this;
    }

    @Override
    public void render(@NonNull View view, @NonNull Card card) {
        super.render(view, card);
        TextView textView = (TextView)view.findViewById(R.id.my_text);
        textView.setText(text);
    }
}

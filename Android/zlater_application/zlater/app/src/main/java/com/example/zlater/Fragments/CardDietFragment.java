package com.example.zlater.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zlater.Model.Diet;
import com.example.zlater.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardDietFragment extends Fragment {
    private ImageView iv_diet;
    private TextView tv_title, tv_description;
    private Diet diet;
    public CardDietFragment() {
        // Required empty public constructor
    }

    public CardDietFragment(Diet diet) {
        this.diet = diet;
    }

    private void initView(View view) {
        iv_diet = view.findViewById(R.id.iv_item_card_diet);
        tv_title = view.findViewById(R.id.tv_item_card_diet_title);
        tv_description = view.findViewById(R.id.tv_item_card_diet_description);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_diet, container, false);
        initView(view);
        if(diet != null) {
            Glide.with(getContext()).load(diet.getImage_url()).centerCrop().into(iv_diet);
            tv_title.setText(diet.getTitle());
            tv_description.setText(diet.getDescription());
        }
        return view;
    }

}

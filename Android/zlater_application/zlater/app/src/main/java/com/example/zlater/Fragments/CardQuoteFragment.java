package com.example.zlater.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zlater.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardQuoteFragment extends Fragment {
    private String imageUrl, title, author;
    private ImageView iv_img;
    private TextView tv_title, tv_author;

    private void initView(View view) {
        iv_img = view.findViewById(R.id.iv_image_quote);
        tv_title = view.findViewById(R.id.tv_title_quote);
        tv_author = view.findViewById(R.id.tv_author_quote);
    }

    public CardQuoteFragment(String imageUrl, String title, String author) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.author = author;
    }

    public CardQuoteFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_quote, container, false);
        initView(view);

        //TODO: iv_image to show quote's image
        tv_title.setText(title);
        tv_author.setText(author);
        return view;
    }

}

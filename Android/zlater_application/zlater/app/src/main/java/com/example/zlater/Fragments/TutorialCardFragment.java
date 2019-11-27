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
public class TutorialCardFragment extends Fragment {
    private TextView tv_title, tv_description;
    private ImageView iv_image;
    private String title, description, imageUrl;

    public TutorialCardFragment() {
        // Required empty public constructor
    }

    public TutorialCardFragment(String title, String description) {
        this.title = title;
        this.description = description;
//        this.imageUrl = imageUrl; //Todo: Loading image later
    }

    private void initView(View view) {
        tv_title = view.findViewById(R.id.tv_title_tutorial);
        tv_description = view.findViewById(R.id.tv_description_tutorial);
        iv_image = view.findViewById(R.id.iv_image_tutorial);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial_card, container, false);
        initView(view);
        tv_title.setText(this.title);
        tv_description.setText(this.description);
        return view;
    }

}

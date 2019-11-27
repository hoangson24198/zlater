package com.example.zlater.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.zlater.Adapter.BodyPartsExercisesAdapter;
import com.example.zlater.Model.Responses.BodypartResponse;
import com.example.zlater.R;
import com.example.zlater.Service.remote.BodypartsAPI;
import com.example.zlater.Service.remote.RetrofitClient;
import com.google.android.material.appbar.AppBarLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExerciseFragment extends Fragment {
    private RecyclerView rv_bodyparts_exercises;
    private BodypartsAPI bodypartsAPI;
    private AppBarLayout appBarLayout;
    private ImageView iv_bg_ex_fragment;

    public ExerciseFragment() {
        // Required empty public constructor
    }

    private void initView(View view) {
        rv_bodyparts_exercises = view.findViewById(R.id.rv_bodyparts_exercises);
        appBarLayout = view.findViewById(R.id.app_bar_ex);
        iv_bg_ex_fragment = view.findViewById(R.id.iv_bg_ex_fragment);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        initView(view);
        Retrofit retrofit = RetrofitClient.getInstance();
        bodypartsAPI = retrofit.create(BodypartsAPI.class);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(final AppBarLayout appBarLayout, final int verticalOffset) {
                float offsetAlpha = (appBarLayout.getY() / appBarLayout.getTotalScrollRange());
                iv_bg_ex_fragment.setAlpha( 1 - (offsetAlpha * -1));
            }
        });
        getAllBodyparts();
        return view;
    }

    private void getAllBodyparts() {
        Call<BodypartResponse> bodypartResponseCall = bodypartsAPI.getAllBodyParts();
        bodypartResponseCall.enqueue(new Callback<BodypartResponse>() {
            @Override
            public void onResponse(Call<BodypartResponse> call, Response<BodypartResponse> response) {
                if(response.isSuccessful()) {
                    BodypartResponse bodypartResponse = response.body();
                    if(bodypartResponse.getStatus() == 0) {
                        rv_bodyparts_exercises.setHasFixedSize(true);
                        rv_bodyparts_exercises.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                        BodyPartsExercisesAdapter bodyPartsExercisesAdapter = new BodyPartsExercisesAdapter(bodypartResponse.getResponse(), getActivity());
                        rv_bodyparts_exercises.setAdapter(bodyPartsExercisesAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<BodypartResponse> call, Throwable t) {

            }
        });
    }

}

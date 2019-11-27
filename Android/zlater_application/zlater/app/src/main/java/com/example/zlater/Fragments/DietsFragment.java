package com.example.zlater.Fragments;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zlater.Adapter.DietFragmentPagerAdapter;
import com.example.zlater.Interface.ItemClickListener;
import com.example.zlater.Model.Responses.DietsResponse;
import com.example.zlater.R;
import com.example.zlater.Service.remote.DietsAPI;
import com.example.zlater.Service.remote.RetrofitClient;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.rd.PageIndicatorView;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DietsFragment extends Fragment implements ItemClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private View bottomSheetIngredientView;
    private BottomSheetLayout sheet_dish;
    private DietsAPI dietsAPI;
    private DiscreteScrollView rv_diets;
    private PageIndicatorView pageIndicatorView;

    private OnFragmentInteractionListener mListener;

    private void initView(View view) {
        sheet_dish = view.findViewById(R.id.bottom_sheet_dish_details);
        bottomSheetIngredientView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_dish_details, sheet_dish, false);
        rv_diets = view.findViewById(R.id.rv_diets_fragment);
        pageIndicatorView = view.findViewById(R.id.pageIndicatorView);
        // specify total count of indicators
    }

    public DietsFragment() {
    }


    public static DietsFragment newInstance(String param1, String param2) {
        DietsFragment fragment = new DietsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diets, container, false);
        initView(view);
        Retrofit retrofit = RetrofitClient.getInstance();
        dietsAPI = retrofit.create(DietsAPI.class);
        getDietData();
        rv_diets.addScrollListener(new DiscreteScrollView.ScrollListener<RecyclerView.ViewHolder>() {
            @Override
            public void onScroll(float v, int i, int i1, @Nullable RecyclerView.ViewHolder viewHolder, @Nullable RecyclerView.ViewHolder t1) {
                pageIndicatorView.setSelection(i1);
            }
        });
        return view;
    }

    private void getDietData() {
        Call<DietsResponse> dietsResponseCall = dietsAPI.getAllDiets();
        dietsResponseCall.enqueue(new Callback<DietsResponse>() {
            @Override
            public void onResponse(Call<DietsResponse> call, Response<DietsResponse> response) {
                if (response.isSuccessful()) {
                    DietsResponse dietsResponse = response.body();
                    if (dietsResponse.getStatus() == 0) {
                        pageIndicatorView.setCount(dietsResponse.getResponse().size());
                        DietFragmentPagerAdapter dietFragmentPagerAdapter = new DietFragmentPagerAdapter(dietsResponse.getResponse(), getContext());
                        rv_diets.setOffscreenItems(20);
                        rv_diets.setSlideOnFlingThreshold(1000);
                        rv_diets.setOverScrollEnabled(true);
                        rv_diets.setItemTransformer(new ScaleTransformer.Builder()
                                .setMaxScale(1.05f)
                                .setMinScale(0.8f)
                                .build());
                        rv_diets.setAdapter(dietFragmentPagerAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<DietsResponse> call, Throwable t) {

            }
        });
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClickItem(int id) {

    }

    @Override
    public void onClick(View view, int posittion) {

    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

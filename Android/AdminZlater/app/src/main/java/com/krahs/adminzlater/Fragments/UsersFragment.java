package com.krahs.adminzlater.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.krahs.adminzlater.Adapter.UserAdapter;
import com.krahs.adminzlater.Model.Ingredients;
import com.krahs.adminzlater.Model.User;
import com.krahs.adminzlater.R;
import com.krahs.adminzlater.Services.AdminZlaterServices;
import com.krahs.adminzlater.Services.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UsersFragment extends Fragment implements View.OnClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ImageView reloadUser;
    private RecyclerView viewUser;
    private Animation animation;
    private AdminZlaterServices adminZlaterServices;
    private OnFragmentInteractionListener mListener;
    private UserAdapter userAdapter;

    public UsersFragment() {
        // Required empty public constructor
    }

    public static UsersFragment newInstance(String param1, String param2) {
        UsersFragment fragment = new UsersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void connectView(View view){
        reloadUser=view.findViewById(R.id.reloadUser);
        reloadUser.setOnClickListener(this);
        viewUser=view.findViewById(R.id.viewUser);
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
        View view=inflater.inflate(R.layout.fragment_users, container, false);
        connectView(view);
        Retrofit retrofit = RetrofitClient.getInstance();
        adminZlaterServices = retrofit.create(AdminZlaterServices.class);
        handleGetAllUser();
        return view;
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.reloadUser:
                Log.e("HS::","Reload user");
                handleGetAllUser();
                break;
        }
    }

    public void  handleGetAllUser() {
        animation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.rotate);
        reloadUser.startAnimation(animation);
        adminZlaterServices.getAllUsers().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.e("HSUser::", response.body());
                    JSONArray array = null;
                    try {
                        JSONObject obj = new JSONObject(response.body());
                        array = obj.getJSONArray("Response");
                    } catch (Throwable t) {
                        Log.e("HS::", "Error!!!");
                    }
                    Gson gson = new Gson();
                    String jsonOutput = array.toString();
                    Type listType = new TypeToken<List<User>>() {
                    }.getType();
                    List<User> users = gson.fromJson(jsonOutput, listType);
                    setData(users);
                    Log.e("HSUser::", /*exercisesList.get(0).getId() +*/":: List user ::" + array);

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), "Please check your connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setData(List<User> users){
        viewUser.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        viewUser.setLayoutManager(mLayoutManager);
        userAdapter = new UserAdapter(users, getContext(), UsersFragment.this);
        viewUser.setAdapter(userAdapter);
        reloadUser.clearAnimation();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

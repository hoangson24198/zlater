package com.example.zlater.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.zlater.Activity.Login.DishesDetails;
import com.example.zlater.Adapter.DishesTodayAdapter;
import com.example.zlater.Interface.ItemClickListener;
import com.example.zlater.Model.Dishes;
import com.example.zlater.Model.Ingredient;
import com.example.zlater.Model.Responses.DishesResponse;
import com.example.zlater.R;
import com.example.zlater.Service.remote.DishesAPI;
import com.example.zlater.Service.remote.RetrofitClient;
import com.example.zlater.Utils.CheckInternetConnection;
import com.flipboard.bottomsheet.BottomSheetLayout;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DishesTodayActivity extends AppCompatActivity implements ItemClickListener {
    private Toolbar toolbar;
    private RecyclerView rv_dishes_today;
    private DishesAPI dishesAPI;
    private ArrayList<Dishes> dishes;
    private BottomSheetLayout bottom_dishes_today;

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        rv_dishes_today = findViewById(R.id.rv_dishes_today);
        bottom_dishes_today = findViewById(R.id.bottom_dishes_today);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes_today);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        initView();
        new CheckInternetConnection(this).checkConnection();

        Retrofit retrofit = RetrofitClient.getInstance();
        dishesAPI = retrofit.create(DishesAPI.class);
        Intent i = getIntent();
        int mealId = i.getIntExtra("mealId", 0);
        String title = i.getStringExtra("title");
        toolbar.setTitle("Thực đơn cho buổi "+title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if(mealId > 0) {
            setDishesData(mealId);
        }

    }

    private void setDishesData(int mealId) {
        Call<DishesResponse> dishesResponseCall = dishesAPI.getDishesByMeal(mealId); //TODO: LOAD DYNAMIC ID
        dishesResponseCall.enqueue(new Callback<DishesResponse>() {
            @Override
            public void onResponse(Call<DishesResponse> call, Response<DishesResponse> response) {
                if(response.isSuccessful()) {
                    DishesResponse dishesResponse = response.body();
                    if(dishesResponse.getStatus() == 0) {
                        dishes = dishesResponse.getResponse();
                        DishesTodayAdapter dishesAdapter = new DishesTodayAdapter(dishesResponse.getResponse(), DishesTodayActivity.this, DishesTodayActivity.this);
                        rv_dishes_today.setHasFixedSize(true);
                        rv_dishes_today.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                        rv_dishes_today.setAdapter(dishesAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<DishesResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClickItem(int id) {
        Dishes dish = dishes.get(id);
        ArrayList<Ingredient> ingredients = dish.getIngredients();
        DishesDetails dishesDetails = new DishesDetails(this, dish, ingredients, bottom_dishes_today);
        bottom_dishes_today.showWithSheetView(dishesDetails.getView());
    }

    @Override
    public void onClick(View view, int posittion) {

    }
}

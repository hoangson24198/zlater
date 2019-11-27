package com.example.zlater.Activity.Login;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zlater.Model.Dishes;
import com.example.zlater.Model.Ingredient;
import com.example.zlater.R;

import java.util.ArrayList;

public class DishesDetails {
    private ImageView iv_dishes;
    private TextView tv_carbs, tv_fat, tv_protein, tv_calories, tv_description, tv_title;
    private RecyclerView rv_ingredients;
    private Dishes dishes;
    private ArrayList<Ingredient> ingredients;
    private Context context;
    private ViewGroup viewGroup;

    public DishesDetails() {

    }

    public DishesDetails(Context context, Dishes dishes, ArrayList<Ingredient> ingredients, ViewGroup viewGroup) {
        this.context = context;
        this.dishes = dishes;
        this.ingredients = ingredients;
        this.viewGroup = viewGroup;
    }
    

    private void initView(View view) {
        iv_dishes = view.findViewById(R.id.iv_dish_details);
        tv_carbs = view.findViewById(R.id.tv_carbs_details);
        tv_fat = view.findViewById(R.id.tv_fat_details);
        tv_protein = view.findViewById(R.id.tv_protein_details);
        tv_calories = view.findViewById(R.id.tv_calories_details);
        rv_ingredients = view.findViewById(R.id.rv_ingredients_dish_details);
        tv_description = view.findViewById(R.id.tv_description_dishes_details);
        tv_title = view.findViewById(R.id.tv_title_dish_details);
    }

    private void setDishesData() {
        Log.d("err:", dishes.getTitle());
        tv_title.setText(dishes.getTitle());
        tv_description.setText(dishes.getDescription());
        tv_calories.setText(dishes.getCalories()+"");
        tv_carbs.setText(dishes.getCarb()+"");
        tv_fat.setText(dishes.getFat()+"");
        tv_protein.setText(dishes.getProtein()+"");
        Glide.with(context).load(dishes.getImage_url()).centerCrop().into(iv_dishes);
        setIngredientAdapter();
    }

    private void setIngredientAdapter() {
        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(ingredients, context);
        rv_ingredients.setHasFixedSize(true);
        rv_ingredients.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        rv_ingredients.setAdapter(ingredientsAdapter);
    }

    public View getView() {
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_dish_details, viewGroup, false);
        initView(view);
        setDishesData();
        return view;
    }

}

class IngredientsAdapter extends RecyclerView.Adapter<IngredientViewHolder>  {
    private ArrayList<Ingredient> ingredients;
    private Context context;

    public IngredientsAdapter(ArrayList<Ingredient> ingredients, Context context) {
        this.ingredients = ingredients;
        this.context = context;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_item_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.tv_title.setText(ingredient.getTitle());
        Glide.with(context).load(ingredient.getImage_url()).centerCrop().into(holder.iv_ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}

class IngredientViewHolder extends RecyclerView.ViewHolder {
    protected ImageView iv_ingredient;
    protected TextView tv_title;
    public IngredientViewHolder(@NonNull View itemView) {
        super(itemView);
        iv_ingredient = itemView.findViewById(R.id.iv_item_ingredient);
        tv_title = itemView.findViewById(R.id.tv_item_title_ingredient);
    }
}

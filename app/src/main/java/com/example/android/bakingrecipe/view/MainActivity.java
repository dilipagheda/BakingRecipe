package com.example.android.bakingrecipe.view;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RemoteViews;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.adapter.ItemClickListener;
import com.example.android.bakingrecipe.adapter.RecipeAdapter;
import com.example.android.bakingrecipe.databinding.ActivityMainBinding;
import com.example.android.bakingrecipe.model.BakingViewModel;
import com.example.android.bakingrecipe.model.Ingredient;
import com.example.android.bakingrecipe.model.Recipe;
import com.example.android.bakingrecipe.service.StatusCodes;
import com.example.android.bakingrecipe.widget.IngradientsAppWidget;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class MainActivity extends AppCompatActivity implements ItemClickListener {

    public static final String WIDGET_BOOLEAN_KEY = "w";
    private static String RECIPE_WIDGET="recipe_widget";

    private RecyclerView mRecipeListRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    //private RecyclerView.LayoutManager mLayoutManager;
    //private GridLayoutManager mLayoutManager;
    private BakingViewModel viewModel;
    private boolean doesWidgetContainValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mRecipeListRecyclerView = binding.recipeRecyclerView;
        mRecipeListRecyclerView.setHasFixedSize(true);

        //get view model
        viewModel = ViewModelProviders.of(this).get(BakingViewModel.class);

        // use a linear layout manager
        //mLayoutManager = new GridLayoutManager(this,1);
        //mRecipeListRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mRecipeAdapter = new RecipeAdapter(this);
        mRecipeAdapter.setClickListener(this);
        mRecipeListRecyclerView.setAdapter(mRecipeAdapter);

        //get the flag from sharedPreferences if the widget was updated with recipe last time
        SharedPreferences prefs = getSharedPreferences(RECIPE_WIDGET, MODE_PRIVATE);
        doesWidgetContainValue = prefs.getBoolean(WIDGET_BOOLEAN_KEY, false);

        //setup observer for list of recipes
        viewModel.getLiveDataObject().observe(this, new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Recipe> recipes) {
                if(recipes.size()>0){
                    mRecipeAdapter.setData(recipes);
                    mRecipeAdapter.notifyDataSetChanged();

                    //update the widget to default to first recipe only first time
                    if(!doesWidgetContainValue){
                        updateWidget(mRecipeAdapter.getRecipe(0).getName(),mRecipeAdapter.getRecipe(0).getIngredients());
                    }

                }
            }
        });
        //make a network call
        viewModel.fetchRecipes().observe(this, new Observer<StatusCodes>() {
            @Override
            public void onChanged(@Nullable StatusCodes statusCodes) {
                //handle status code here
            }
        });

    }

    @Override
    public void onClick(View view, int position) {
        //first update the widget
        updateWidget(mRecipeAdapter.getRecipe(position).getName(),mRecipeAdapter.getRecipe(position).getIngredients());
        Intent i = new Intent(this, RecipeDetailsActivity.class);
        i.putExtra("Receipe",mRecipeAdapter.getRecipe(position));

        startActivity(i);
    }

    public void updateWidget(String recipeName, ArrayList<Ingredient> ingredients){
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.ingradients_app_widget);
        String strIngredients="";
        for(Ingredient i:ingredients){
            strIngredients+=i.getIngredient()+" "+String.valueOf(i.getQuantity())+" "+i.getMeasure()+"\n";
        }
        views.setTextViewText(R.id.appwidget_ingredients, strIngredients);
        views.setTextViewText(R.id.appwidget_recipe_name,recipeName);
        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(), IngradientsAppWidget.class));
        if(ids.length>0){
            AppWidgetManager.getInstance(getApplication()).updateAppWidget(ids,views);
        }
        //set the flag from sharedPreferences if the widget was never updated before
        SharedPreferences.Editor prefsEditor = getSharedPreferences(RECIPE_WIDGET, MODE_PRIVATE).edit();
        if(!doesWidgetContainValue){
            prefsEditor.putBoolean(WIDGET_BOOLEAN_KEY,true);
            prefsEditor.putString("RecipeName",recipeName);
            prefsEditor.putString("Ingredients",strIngredients);
            prefsEditor.commit();
        }
    }
}

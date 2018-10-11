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
import android.widget.Toast;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.adapter.ItemClickListener;
import com.example.android.bakingrecipe.adapter.RecipeAdapter;
import com.example.android.bakingrecipe.databinding.ActivityMainBinding;
import com.example.android.bakingrecipe.model.BakingViewModel;
import com.example.android.bakingrecipe.model.Ingredient;
import com.example.android.bakingrecipe.model.Recipe;
import com.example.android.bakingrecipe.service.StatusCodes;
import com.example.android.bakingrecipe.widget.IngradientsAppWidget;
import com.example.android.bakingrecipe.widget.PersistWidgetData;
import com.example.android.bakingrecipe.widget.WidgetData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class MainActivity extends AppCompatActivity implements ItemClickListener {

    private RecyclerView mRecipeListRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private BakingViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mRecipeListRecyclerView = binding.recipeRecyclerView;
        mRecipeListRecyclerView.setHasFixedSize(true);

        //get view model
        viewModel = ViewModelProviders.of(this).get(BakingViewModel.class);

        // specify an adapter (see also next example)
        mRecipeAdapter = new RecipeAdapter(this);
        mRecipeAdapter.setClickListener(this);
        mRecipeListRecyclerView.setAdapter(mRecipeAdapter);



        //setup observer for list of recipes
        viewModel.getLiveDataObject().observe(this, new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Recipe> recipes) {
                if(recipes.size()>0){
                    mRecipeAdapter.setData(recipes);
                    mRecipeAdapter.notifyDataSetChanged();

                    //update the widget to default to first recipe only first time
                    if(PersistWidgetData.readFromSharedPreferences(getApplicationContext())==null){
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
                if(statusCodes==StatusCodes.NETWORK_FAILURE){
                    Toast.makeText(getApplicationContext(),"Network error!",Toast.LENGTH_SHORT).show();
                }
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

    public void updateWidget(String recipeName, ArrayList<Ingredient> ingredients) {
        WidgetData widgetData = new WidgetData(recipeName,ingredients);
        PersistWidgetData.writeToSharedPreferences(this,widgetData);

        //trigger the update using broadcast message so that widget gets latest data
        Intent intent = new Intent(this, IngradientsAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetI‌​ds(new ComponentName(getApplication(), IngradientsAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }
}

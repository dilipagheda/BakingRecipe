package com.example.android.bakingrecipe.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.adapter.ItemClickListener;
import com.example.android.bakingrecipe.adapter.RecipeAdapter;
import com.example.android.bakingrecipe.databinding.ActivityMainBinding;
import com.example.android.bakingrecipe.model.BakingViewModel;
import com.example.android.bakingrecipe.model.Recipe;
import com.example.android.bakingrecipe.service.StatusCodes;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements ItemClickListener {

    private RecyclerView mRecipeListRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    //private RecyclerView.LayoutManager mLayoutManager;
    //private GridLayoutManager mLayoutManager;
    private BakingViewModel viewModel;

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

        //setup observer for list of recipes
        viewModel.getLiveDataObject().observe(this, new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Recipe> recipes) {
                mRecipeAdapter.setData(recipes);
                mRecipeAdapter.notifyDataSetChanged();
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
        Intent i = new Intent(this, RecipeDetailsActivity.class);
        i.putExtra("Receipe",mRecipeAdapter.getRecipe(position));
        startActivity(i);
    }
}

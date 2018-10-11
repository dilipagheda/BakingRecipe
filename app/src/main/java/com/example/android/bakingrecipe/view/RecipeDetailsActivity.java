package com.example.android.bakingrecipe.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.adapter.StepFragmentClickListener;
import com.example.android.bakingrecipe.databinding.ActivityRecipeDetailsBinding;
import com.example.android.bakingrecipe.model.Recipe;
import com.example.android.bakingrecipe.model.RecipeViewModel;

public class RecipeDetailsActivity extends AppCompatActivity implements StepFragmentClickListener{

    private Recipe recipe;
    private FrameLayout stepDetailsContainer;
    private FrameLayout recipeDetailsContainer;
    private RecipeViewModel recipeViewModel;
    private RecipeDetailsFragment recipeDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        Intent intent = getIntent();
        recipe = intent.getParcelableExtra("Receipe");
        recipeViewModel.setRecipe(recipe);


        //fragment
        ActivityRecipeDetailsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_details);

        stepDetailsContainer = binding.stepDetailsContainer;
        recipeDetailsContainer = binding.recipeDetailsContainer;

        if(getResources().getBoolean(R.bool.tabletPortrait) || getResources().getBoolean(R.bool.tabletLandscape)){
           insertStepDetailsFragment(0,false);
        }

        // Add the fragment to the 'fragment_container' FrameLayout
        if(getSupportFragmentManager().findFragmentById(recipeDetailsContainer.getId())==null){
            recipeDetailsFragment = RecipeDetailsFragment.newInstance(recipe);

            getSupportFragmentManager().beginTransaction()
                    .add(recipeDetailsContainer.getId(), recipeDetailsFragment).commit();
        }



    }

    private void insertStepDetailsFragment(int position,boolean isReplace) {
        StepDetailsFragment stepDetailsFragment;

        if(!isReplace){
            if(getSupportFragmentManager().findFragmentById(stepDetailsContainer.getId())==null){
                stepDetailsFragment = StepDetailsFragment.newInstance(recipe.getSteps(),position);

                getSupportFragmentManager().beginTransaction()
                        .add(stepDetailsContainer.getId(), stepDetailsFragment).commit();
            }

        }else{
            stepDetailsFragment = StepDetailsFragment.newInstance(recipe.getSteps(),position);
            getSupportFragmentManager().beginTransaction()
                    .replace(stepDetailsContainer.getId(), stepDetailsFragment).commit();
        }

    }

    @Override
    public void onClick(int position) {
        insertStepDetailsFragment(position,true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

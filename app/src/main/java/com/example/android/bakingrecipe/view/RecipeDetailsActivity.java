package com.example.android.bakingrecipe.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.ReceiverCallNotAllowedException;
import android.databinding.DataBindingUtil;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.adapter.RecipeDetailsFragmentAdaptor;
import com.example.android.bakingrecipe.adapter.StepFragmentClickListener;
import com.example.android.bakingrecipe.databinding.ActivityRecipeDetailsBinding;
import com.example.android.bakingrecipe.databinding.StepDetailsFragmentBinding;
import com.example.android.bakingrecipe.model.BakingViewModel;
import com.example.android.bakingrecipe.model.Recipe;
import com.example.android.bakingrecipe.model.RecipeViewModel;
import com.example.android.bakingrecipe.model.Step;

public class RecipeDetailsActivity extends AppCompatActivity implements StepFragmentClickListener{

    private boolean isDualMode;
    private Recipe recipe;
    private FrameLayout stepDetailsContainer;
    private FrameLayout recipeDetailsContainer;
    private RecipeViewModel recipeViewModel;
    private static String ARG_RECIPE="recipe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        isDualMode=false;
        Intent intent = getIntent();
        recipe = intent.getParcelableExtra("Receipe");
        recipeViewModel.setRecipe(recipe);


        //fragment
        ActivityRecipeDetailsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_details);

        stepDetailsContainer = binding.stepDetailsContainer;
        recipeDetailsContainer = binding.recipeDetailsContainer;

       if(stepDetailsContainer!=null && stepDetailsContainer.getVisibility()== View.VISIBLE){
           isDualMode = true;
           insertStepDetailsFragment(0,false);
        }

        RecipeDetailsFragment recipeDetailsFragment = RecipeDetailsFragment.newInstance(recipe,isDualMode);
        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(recipeDetailsContainer.getId(), recipeDetailsFragment).commit();


    }

    private void insertStepDetailsFragment(int position,boolean isReplace) {
        StepDetailsFragment stepDetailsFragment = StepDetailsFragment.newInstance(recipe.getSteps(),position,isDualMode);

        if(!isReplace){
            getSupportFragmentManager().beginTransaction()
                    .add(stepDetailsContainer.getId(), stepDetailsFragment).commit();
        }else{
            getSupportFragmentManager().beginTransaction()
                    .replace(stepDetailsContainer.getId(), stepDetailsFragment).commit();
        }

    }

    @Override
    public void onClick(int position) {
        //Toast.makeText(this,"pos:"+position,Toast.LENGTH_SHORT).show();
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

//    public Recipe getRecipe() {
//        return recipeViewModel.getRecipe();
//    }



}

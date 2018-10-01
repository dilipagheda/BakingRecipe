package com.example.android.bakingrecipe.view;

import android.content.Intent;
import android.content.ReceiverCallNotAllowedException;
import android.databinding.DataBindingUtil;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.adapter.RecipeDetailsFragmentAdaptor;
import com.example.android.bakingrecipe.databinding.ActivityRecipeDetailsBinding;
import com.example.android.bakingrecipe.databinding.StepDetailsFragmentBinding;
import com.example.android.bakingrecipe.model.Recipe;

public class RecipeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        Intent intent = getIntent();
        Recipe recipe = intent.getParcelableExtra("Receipe");

        //fragment
        ActivityRecipeDetailsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_details);

        FrameLayout stepDetailsContainer = binding.stepDetailsContainer;
        FrameLayout recipeDetailsContainer = binding.recipeDetailsContainer;

        RecipeDetailsFragment recipeDetailsFragment = RecipeDetailsFragment.newInstance(recipe);
        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(recipeDetailsContainer.getId(), recipeDetailsFragment).commit();

        if(stepDetailsContainer!=null && stepDetailsContainer.getVisibility()== View.VISIBLE){

            StepDetailsFragment stepDetailsFragment = StepDetailsFragment.newInstance(recipe.getSteps().get(0),true);

            getSupportFragmentManager().beginTransaction()
                    .add(stepDetailsContainer.getId(), stepDetailsFragment).commit();

        }

    }
}

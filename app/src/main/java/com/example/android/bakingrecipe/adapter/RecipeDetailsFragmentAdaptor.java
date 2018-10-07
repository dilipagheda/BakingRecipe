package com.example.android.bakingrecipe.adapter;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.bakingrecipe.model.Recipe;
import com.example.android.bakingrecipe.view.IngredientFragment;
import com.example.android.bakingrecipe.view.StepFragment;


public class RecipeDetailsFragmentAdaptor extends FragmentPagerAdapter {

    private String title[];
    private Recipe recipe;
    private boolean isDualMode;

    public void setDualMode(boolean dualMode) {
        isDualMode = dualMode;
    }

    public RecipeDetailsFragmentAdaptor(FragmentManager fm, String[] title) {
        super(fm);
        this.title = title;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;
        switch(position){
            case 0:
                f = IngredientFragment.newInstance(recipe);
                
                break;
            case 1:
                f = StepFragment.newInstance(recipe,isDualMode);
                break;

        }
        return f;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}

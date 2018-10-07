package com.example.android.bakingrecipe.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.bakingrecipe.repository.BakingRepository;
import com.example.android.bakingrecipe.service.StatusCodes;

import java.util.ArrayList;

public class RecipeViewModel extends AndroidViewModel {
    public static final String TAG = "RecipeViewModel";

    Application application;
    private Recipe mRecipe;

    public RecipeViewModel(@NonNull Application application) {
        super(application);

    }

    public Recipe getRecipe() {
        return mRecipe;
    }

    public void setRecipe(Recipe mRecipe) {
        this.mRecipe = mRecipe;
    }
}

package com.example.android.bakingrecipe.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.graphics.Movie;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.bakingrecipe.repository.BakingRepository;
import com.example.android.bakingrecipe.service.StatusCodes;

import java.util.ArrayList;
import java.util.List;

public class BakingViewModel extends AndroidViewModel {
    public static final String TAG = "BakingViewModel";

    BakingRepository bakingRepository;
    Application application;

    public BakingViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        bakingRepository = new BakingRepository();
        Log.d(TAG, "Constructor");
    }

    public LiveData<ArrayList<Recipe>> getLiveDataObject(){
        return bakingRepository.getLiveDataObject();
    }

    public LiveData<StatusCodes> fetchRecipes(){
        return bakingRepository.fetchRecipes();
    }

}

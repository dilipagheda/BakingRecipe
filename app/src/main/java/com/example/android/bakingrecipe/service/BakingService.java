package com.example.android.bakingrecipe.service;

import com.example.android.bakingrecipe.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BakingService {

    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<ArrayList<Recipe>> getBakingRecipes();


}


package com.example.android.bakingrecipe.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.android.bakingrecipe.model.Recipe;
import com.example.android.bakingrecipe.service.BakingService;
import com.example.android.bakingrecipe.service.RetrofitInstance;
import com.example.android.bakingrecipe.service.StatusCodes;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BakingRepository {
    private MutableLiveData<ArrayList<Recipe>> recipeArrayList;
    private MutableLiveData<StatusCodes> statusCodes;

    public static final String TAG = "BakingRepository";

    public BakingRepository(){
        recipeArrayList = new MutableLiveData<ArrayList<Recipe>>();
        statusCodes = new MutableLiveData<>();
    }

    //Below methods are for retrieving data from the network
    public MutableLiveData<StatusCodes> fetchRecipes(){

        BakingService bakingService = RetrofitInstance.getService();
        Call<ArrayList<Recipe>> callBackend;

        callBackend = bakingService.getBakingRecipes();

        Log.d(TAG,callBackend.request().url().toString());


        callBackend.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                Log.d(TAG, "Response:" + response.message());
                if(response.isSuccessful() && response.body().size()>0){
                    recipeArrayList.setValue(response.body());
                    statusCodes.setValue(StatusCodes.SUCCESS);
                }else if(response.isSuccessful() && response.body().size()==0){
                    statusCodes.setValue(StatusCodes.EMPTY);
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.d(TAG,"Network error:"+t.getMessage() );
                statusCodes.setValue(StatusCodes.NETWORK_FAILURE);
            }});

        return statusCodes;
    }

    public LiveData<ArrayList<Recipe>> getLiveDataObject(){
        return recipeArrayList;
    }

}

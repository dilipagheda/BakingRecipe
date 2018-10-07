package com.example.android.bakingrecipe.view;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingrecipe.adapter.IngredientAdapter;
import com.example.android.bakingrecipe.databinding.IngredientListBinding;
import com.example.android.bakingrecipe.model.Recipe;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IngredientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IngredientFragment extends Fragment{
    private RecyclerView mIngredientListRecyclerView;
    private IngredientAdapter mIngredientAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final String ARG_RECIPE = "recipe";

    private Recipe mRecipe;

    public IngredientFragment() {
        // Required empty public constructor
    }

    public static IngredientFragment newInstance(Recipe r) {
        IngredientFragment fragment = new IngredientFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RECIPE, r);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipe = getArguments().getParcelable(ARG_RECIPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        IngredientListBinding ingredientListBinding = IngredientListBinding.inflate(inflater,container,false);
        mIngredientListRecyclerView = ingredientListBinding.ingredientRecyclerView;
        mIngredientListRecyclerView.setHasFixedSize(true);

        mIngredientAdapter = new IngredientAdapter(mRecipe.getIngredients());
        mIngredientListRecyclerView.setAdapter(mIngredientAdapter);

        return ingredientListBinding.getRoot();
    }


}

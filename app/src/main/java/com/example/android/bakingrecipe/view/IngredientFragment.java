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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IngredientFragment.
     */
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

//        if(mRecipe==null){
//            //hmm. seems like fragment is being restored and our mRecipe is wiped out.
//            //Let's get it back from parent activity
//            //(I tried to use viewModel and savedInstanceState but they didn't work reliably for a fragment:-()
//            RecipeDetailsActivity recipeDetailsActivity = (RecipeDetailsActivity)getActivity();
//            mRecipe = recipeDetailsActivity.getRecipe();
//        }
        mIngredientAdapter = new IngredientAdapter(mRecipe.getIngredients());
        mIngredientListRecyclerView.setAdapter(mIngredientAdapter);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(container.getContext());
        mIngredientListRecyclerView.setLayoutManager(mLayoutManager);

        return ingredientListBinding.getRoot();
    }


}

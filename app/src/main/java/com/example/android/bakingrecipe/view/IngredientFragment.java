package com.example.android.bakingrecipe.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingrecipe.adapter.IngredientAdapter;
import com.example.android.bakingrecipe.adapter.ItemClickListener;
import com.example.android.bakingrecipe.adapter.RecipeAdapter;
import com.example.android.bakingrecipe.databinding.IngredientListBinding;
import com.example.android.bakingrecipe.model.Ingredient;
import com.example.android.bakingrecipe.model.Recipe;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IngredientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IngredientFragment extends Fragment{


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static Recipe recipe;
    private RecyclerView mIngredientListRecyclerView;
    private IngredientAdapter mIngredientAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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
    // TODO: Rename and change types and number of parameters
    public static IngredientFragment newInstance(Recipe r) {
        IngredientFragment fragment = new IngredientFragment();
        recipe=r;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        IngredientListBinding ingredientListBinding = IngredientListBinding.inflate(inflater,container,false);
        mIngredientListRecyclerView = ingredientListBinding.ingredientRecyclerView;
        mIngredientListRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(container.getContext());
        mIngredientListRecyclerView.setLayoutManager(mLayoutManager);



        // specify an adapter (see also next example)
        mIngredientAdapter = new IngredientAdapter(recipe.getIngredients());
        mIngredientListRecyclerView.setAdapter(mIngredientAdapter);

        return ingredientListBinding.getRoot();
    }

}

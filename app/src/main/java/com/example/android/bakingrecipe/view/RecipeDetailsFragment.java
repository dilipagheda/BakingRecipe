package com.example.android.bakingrecipe.view;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.adapter.RecipeDetailsFragmentAdaptor;
import com.example.android.bakingrecipe.databinding.RecipeDetailsFragmentBinding;
import com.example.android.bakingrecipe.databinding.StepListBinding;
import com.example.android.bakingrecipe.model.Recipe;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetailsFragment extends Fragment {
    private static final String ARG_RECIPE = "recipe";
    private Recipe mRecipe;
    private TabLayout tabLayout;
    private RecipeDetailsFragmentBinding recipeDetailsFragmentBinding;
    private RecipeDetailsFragmentAdaptor adapter;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    public static RecipeDetailsFragment newInstance(Recipe recipe) {
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RECIPE, recipe);
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
        recipeDetailsFragmentBinding = RecipeDetailsFragmentBinding.inflate(inflater,container,false);

        ViewPager viewPager = recipeDetailsFragmentBinding.viewPager;
        FragmentManager fm = getActivity().getSupportFragmentManager();

        adapter = new RecipeDetailsFragmentAdaptor(fm,new String[]{getString(R.string.ingredients_tab),
                getString(R.string.steps_tab)});
        adapter.setRecipe(mRecipe);
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        tabLayout = recipeDetailsFragmentBinding.slidingTabs;
        tabLayout.setupWithViewPager(viewPager);
        return recipeDetailsFragmentBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

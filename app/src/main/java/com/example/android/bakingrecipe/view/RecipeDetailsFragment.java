package com.example.android.bakingrecipe.view;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_RECIPE = "recipe";
    private static final String ARG_DUAL = "dual";
    private boolean isDualMode;
    private Recipe mRecipe;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeDetailsFragment newInstance(Recipe recipe, boolean isDualMode) {
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RECIPE, recipe);
        args.putBoolean(ARG_DUAL,isDualMode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipe = getArguments().getParcelable(ARG_RECIPE);
            isDualMode =  getArguments().getBoolean(ARG_DUAL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecipeDetailsFragmentBinding recipeDetailsFragmentBinding = RecipeDetailsFragmentBinding.inflate(inflater,container,false);

        ViewPager viewPager = recipeDetailsFragmentBinding.viewPager;
        FragmentManager fm = getActivity().getSupportFragmentManager();

        RecipeDetailsFragmentAdaptor adapter = new RecipeDetailsFragmentAdaptor(fm,new String[]{"Ingredients",
                "Steps"});
        adapter.setRecipe(mRecipe);
        adapter.setDualMode(isDualMode);
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = recipeDetailsFragmentBinding.slidingTabs;
        tabLayout.setupWithViewPager(viewPager);

        return recipeDetailsFragmentBinding.getRoot();
    }

}

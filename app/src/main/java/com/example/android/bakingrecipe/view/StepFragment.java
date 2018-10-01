package com.example.android.bakingrecipe.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.adapter.IngredientAdapter;
import com.example.android.bakingrecipe.adapter.ItemClickListener;
import com.example.android.bakingrecipe.adapter.StepAdapter;
import com.example.android.bakingrecipe.databinding.StepListBinding;
import com.example.android.bakingrecipe.model.Ingredient;
import com.example.android.bakingrecipe.model.Recipe;
import com.example.android.bakingrecipe.model.Step;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StepFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepFragment extends Fragment implements ItemClickListener{

    private RecyclerView mStepListRecyclerView;
    private StepAdapter mStepAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static Recipe recipe;

    public StepFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StepFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StepFragment newInstance(Recipe recipe) {
        StepFragment fragment = new StepFragment();
        StepFragment.recipe = recipe;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        StepListBinding stepListBinding = StepListBinding.inflate(inflater,container,false);

        mStepListRecyclerView = stepListBinding.stepRecyclerView;
        mStepListRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(container.getContext());
        mStepListRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mStepAdapter = new StepAdapter(recipe.getSteps());
        mStepAdapter.setClickListener(this);
        mStepListRecyclerView.setAdapter(mStepAdapter);

        return stepListBinding.getRoot();
    }

    @Override
    public void onClick(View view, int position) {
        Intent i = new Intent(this.getContext(),StepDetailsActivity.class);
        i.putExtra("Step",recipe.getSteps().get(position));
        startActivity(i);
    }
}

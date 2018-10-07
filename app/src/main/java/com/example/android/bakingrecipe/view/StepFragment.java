package com.example.android.bakingrecipe.view;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.adapter.IngredientAdapter;
import com.example.android.bakingrecipe.adapter.ItemClickListener;
import com.example.android.bakingrecipe.adapter.StepAdapter;
import com.example.android.bakingrecipe.adapter.StepFragmentClickListener;
import com.example.android.bakingrecipe.databinding.StepListBinding;
import com.example.android.bakingrecipe.model.Ingredient;
import com.example.android.bakingrecipe.model.Recipe;
import com.example.android.bakingrecipe.model.Step;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StepFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepFragment extends Fragment implements ItemClickListener{

    private RecyclerView mStepListRecyclerView;
    private StepAdapter mStepAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Recipe recipe;
    private static final String ARG_RECIPE = "recipe";
    private static final String ARG_DUAL = "dual";
    private StepFragmentClickListener stepFragmentClickListener;
    private boolean isDualMode;
    private int currentPosition;

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
    public static StepFragment newInstance(Recipe recipe,boolean isDualMode) {
        StepFragment fragment = new StepFragment();
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
            recipe = getArguments().getParcelable(ARG_RECIPE);
            isDualMode = getArguments().getBoolean(ARG_DUAL);

        }
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
        mStepAdapter = new StepAdapter(recipe.getSteps(),getContext());
        mStepAdapter.setClickListener(this);
        mStepListRecyclerView.setAdapter(mStepAdapter);

        return stepListBinding.getRoot();
    }

    @Override
    public void onClick(View view, int position) {
        if(!isDualMode){
            Intent i = new Intent(this.getContext(),StepDetailsActivity.class);
            i.putExtra("currentPosition",position);
            i.putParcelableArrayListExtra("Steps",recipe.getSteps());
            startActivityForResult(i,1);
        }else{
            //call parent activity to replace step details fragment
            stepFragmentClickListener.onClick(position);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                currentPosition=data.getIntExtra("currentPosition",0);
                mStepAdapter.setCurrentPosition(currentPosition);
                mStepAdapter.notifyDataSetChanged();
                //Toast.makeText(getActivity(),"pos:"+currentPosition,Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            stepFragmentClickListener = (StepFragmentClickListener) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }
}

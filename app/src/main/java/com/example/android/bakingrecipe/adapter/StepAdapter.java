package com.example.android.bakingrecipe.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.databinding.StepListItemBinding;
import com.example.android.bakingrecipe.model.Step;

import java.util.ArrayList;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder>{
    private ArrayList<Step> mDataset;
    private ItemClickListener clickListener;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public  class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        private StepListItemBinding binding;

        public StepViewHolder(StepListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.stepDescription.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public StepAdapter(ArrayList<Step> steps) {
        mDataset = steps;

    }


    // Create new views (invoked by the layout manager)
    @Override
    public StepAdapter.StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        StepListItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.step_list_item, parent, false);
        StepAdapter.StepViewHolder vh = new StepAdapter.StepViewHolder(binding);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(StepAdapter.StepViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.binding.stepDescription.setText(mDataset.get(position).getShortDescription());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

}

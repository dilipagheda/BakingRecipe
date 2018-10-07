package com.example.android.bakingrecipe.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.databinding.IngredientListItemBinding;
import com.example.android.bakingrecipe.model.Ingredient;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>{
    private ArrayList<Ingredient> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public  class IngredientViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private IngredientListItemBinding binding;
        public IngredientViewHolder(IngredientListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public IngredientAdapter(ArrayList<Ingredient> mDataset) {
        this.mDataset = mDataset;

    }


    // Create new views (invoked by the layout manager)
    @Override
    public IngredientAdapter.IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        IngredientListItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.ingredient_list_item, parent, false);
        IngredientAdapter.IngredientViewHolder vh = new IngredientAdapter.IngredientViewHolder(binding);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(IngredientAdapter.IngredientViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.binding.ingredient.setText(mDataset.get(position).getIngredient());
        holder.binding.measureQuantity.setText(getQtyMeasure(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private String getQtyMeasure(int p){
        String q = String.valueOf(mDataset.get(p).getQuantity());
        String m = mDataset.get(p).getMeasure();
        return q+" "+m;
    }

}



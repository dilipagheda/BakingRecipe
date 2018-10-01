package com.example.android.bakingrecipe.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.databinding.RecipeListItemBinding;
import com.example.android.bakingrecipe.model.Recipe;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private ArrayList<Recipe> mDataset;
    private ItemClickListener clickListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        private RecipeListItemBinding binding;
        public RecipeViewHolder(RecipeListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.recipeName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());

        }
    }

    public RecipeAdapter() {
        mDataset = new ArrayList<>();
    }

    public void setData(ArrayList<Recipe> mDataset){
        this.mDataset = mDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        RecipeListItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.recipe_list_item, parent, false);
        RecipeViewHolder vh = new RecipeViewHolder(binding);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.binding.recipeName.setText(mDataset.get(position).getName());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public Recipe getRecipe(int p) {
        return mDataset.get(p);
    }
}


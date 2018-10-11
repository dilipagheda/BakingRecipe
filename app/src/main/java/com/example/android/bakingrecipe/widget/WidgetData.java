package com.example.android.bakingrecipe.widget;

import com.example.android.bakingrecipe.model.Ingredient;

import java.util.ArrayList;

public class WidgetData {
    private String recipeName;
    private ArrayList<Ingredient> ingredients;

    public WidgetData(String recipeName, ArrayList<Ingredient> ingredients) {
        this.recipeName = recipeName;
        this.ingredients = ingredients;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }
}

package com.example.android.bakingrecipe.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recipe implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ingredients")
    @Expose
    private ArrayList<Ingredient> ingredients = null;
    @SerializedName("steps")
    @Expose
    private ArrayList<Step> steps = null;
    @SerializedName("servings")
    @Expose
    private Integer servings;
    @SerializedName("image")
    @Expose
    private String image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Recipe(){
        ingredients=new ArrayList<>();
        steps=new ArrayList<>();
    }

    public Recipe(Parcel in) {
        this();
        id=in.readInt();
        name=in.readString();
        in.readTypedList(ingredients,Ingredient.CREATOR);
        in.readTypedList(steps,Step.CREATOR);
        servings=in.readInt();
        image=in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeTypedList(ingredients);
        parcel.writeTypedList(steps);
        parcel.writeInt(servings);
        parcel.writeString(image);
    }

    public static final Parcelable.Creator<Recipe> CREATOR
            = new Parcelable.Creator<Recipe>(){

        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}

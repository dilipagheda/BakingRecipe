package com.example.android.bakingrecipe.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.android.bakingrecipe.model.Ingredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class PersistWidgetData {
    private static final String KEY="key";
    private static final String WIDGET_DATA="widget_data";

    public static void writeToSharedPreferences(Context context, WidgetData widgetData){
        //save to shared preferences
        SharedPreferences shref;
        SharedPreferences.Editor editor;
        shref = context.getSharedPreferences(WIDGET_DATA, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = gson.toJson(widgetData);

        editor = shref.edit();
        editor.putString(KEY, json);
        editor.commit();

    }

    public static WidgetData readFromSharedPreferences(Context context){
        SharedPreferences shref;
        shref = context.getSharedPreferences(WIDGET_DATA, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String response=shref.getString(KEY , "");
        if(response.isEmpty()){
            return null;
        }
        WidgetData widgetData = gson.fromJson(response,
                new TypeToken<WidgetData>(){}.getType());

        return widgetData;
    }
}

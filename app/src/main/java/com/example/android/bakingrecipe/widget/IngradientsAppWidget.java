package com.example.android.bakingrecipe.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.example.android.bakingrecipe.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 */
public class IngradientsAppWidget extends AppWidgetProvider {

    public static final String WIDGET_BOOLEAN_KEY = "w";
    private static final String RECIPE_WIDGET="recipe_widget";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        SharedPreferences prefs = context.getSharedPreferences(RECIPE_WIDGET, MODE_PRIVATE);
        boolean doesWidgetContainValue = prefs.getBoolean(WIDGET_BOOLEAN_KEY, false);
        String recipeName="";
        String ingredients="";

        if(doesWidgetContainValue){
            recipeName = prefs.getString("RecipeName","");
            ingredients = prefs.getString("Ingredients","");
        }

        //  Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingradients_app_widget);
        // set initial values
        views.setTextViewText(R.id.appwidget_ingredients, ingredients);
        views.setTextViewText(R.id.appwidget_recipe_name,recipeName);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


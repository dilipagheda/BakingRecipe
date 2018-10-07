package com.example.android.bakingrecipe;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ScrollToAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingrecipe.adapter.IngredientAdapter;
import com.example.android.bakingrecipe.adapter.StepAdapter;
import com.example.android.bakingrecipe.view.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Text;

import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtras;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class IntegrationInstrumentedTest {
    private MainActivity mActivity;
    private boolean mIsScreenSw600dp;

    private String[] ingredientsOfNutellaPie=new String[]{
            "Graham Cracker crumbs",
            "unsalted butter, melted",
            "granulated sugar",
            "salt",
            "vanilla",
            "Nutella or other chocolate-hazelnut spread",
            "Mascapone Cheese(room temperature)",
            "heavy cream(cold)",
            "cream cheese(softened)"
    };

    private String[] stepsOfNutellaPie=new String[]{
            "Recipe Introduction",
            "Starting prep",
            "Prep the cookie crust.",
            "Press the crust into baking form.",
            "Start filling prep",
            "Finish filling prep",
            "Finishing Steps"
    };

    //I have removed unicodes and some parts of text for simplicity
    //validating initial part of the text still good enough to check that right text is displayed.
    private String[] detailedStepsOfNutellaPie=new String[]{
            "Recipe Introduction",
            "1. Preheat the oven to",
            "2. Whisk the graham cracker crumbs, 50 grams (1/4 cup) of sugar, and 1/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed.",
            "3. Press the cookie crumb mixture into the prepared pie pan and bake for 12 minutes. Let crust cool to room temperature.",
            "4. Beat together the nutella, mascarpone, 1 teaspoon of salt, and 1 tablespoon of vanilla on medium speed in a stand mixer or high speed with a hand mixer until fluffy.",
            "5. Beat the cream cheese and 50 grams (1/4 cup) of sugar on medium speed in a stand mixer or high speed with a hand mixer for 3 minutes. Decrease the speed to medium-low and gradually add in the cold cream. Add in 2 teaspoons of vanilla and beat until stiff peaks form.",
            "6. Pour the filling into the prepared crust and smooth the top. Spread the whipped cream over the filling. Refrigerate the pie for at least 2 hours."
    };

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class,true,true);

    @Before
    public void setUp(){
        mActivity = mActivityRule.getActivity();
        mIsScreenSw600dp = isScreenSw600dp();
    }

    @After
    public void tearDown() {
        mActivity.finish();
    }

    @Test
    public void testRecipeDetailsViewsDisplayedCorrectly(){
        //Click on the first recipe from mainActivity's recyclerview
        onView(ViewMatchers.withId(R.id.recipeRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));

        //Verify that recipe details container is present for both phone and tablet
        onView(withId(R.id.recipe_details_container))
                .check(matches(isDisplayed()));

        //if it is a tablet then verify that step details is also present
        if(mIsScreenSw600dp){

            onView(withId(R.id.step_details_container))
                    .check(matches(isDisplayed()));
        }
    }

    @Test
    public void testCorrectIngredientsDisplayedForRecipe(){
        //Click on the first recipe (Nutella Pie) from mainActivity's recyclerview
        onView(ViewMatchers.withId(R.id.recipeRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));

        //Verify that ingredients match up for that recipe by scrolling to that element using a custom matcher
        // If an ingradient is not there, then test will fail indicating that can't find the view

        for(String s:ingredientsOfNutellaPie){
            onView(withId(R.id.ingredientRecyclerView)).perform(
                    RecyclerViewActions.scrollToHolder(
                            withIngredientHolderTextView(s)
                    )
            );
        }
    }

    @Test
    public void testCorrectStepsDisplayedForRecipe(){
        //Click on the first recipe (Nutella Pie) from mainActivity's recyclerview
        onView(ViewMatchers.withId(R.id.recipeRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));

        //Click on a Step tab on Tab Layout
        onView(withText("Steps")).perform(click());

        //Verify that steps match up for that recipe by scrolling to that element using a custom matcher
        // If a step is not there, then test will fail indicating that can't find the view

        for(String s:stepsOfNutellaPie){
            onView(withId(R.id.stepRecyclerView)).perform(
                    RecyclerViewActions.scrollToHolder(
                            withStepHolderTextView(s)
                    )
            );

            onView(allOf(withId(R.id.stepDescription),withText(s)))
                    .check(matches(isDisplayed()));

        }
    }

    @Test
    public void testClickingOnStepLoadsCorrectStepDetails() {
        //Click on the first recipe (Nutella Pie) from mainActivity's recyclerview
        onView(ViewMatchers.withId(R.id.recipeRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));

        //Click on a Step tab on Tab Layout
        onView(withText("Steps")).perform(click());

        //Click on each of the item of steps recycle view and verify the detailed step description
        //this logic works for both phone and tablet
        for(int i=0;i<stepsOfNutellaPie.length;i++){

            onView(withId(R.id.stepRecyclerView)).perform(
                    RecyclerViewActions.actionOnItemAtPosition(i,click()));

            onView(withId(R.id.description))
                    .check(matches(withStepDetailedDescription(detailedStepsOfNutellaPie[i])));

            if(!mIsScreenSw600dp){
                //go back to previous screen
                Espresso.pressBack();
            }
        }

    }


    private boolean isScreenSw600dp() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float widthDp = displayMetrics.widthPixels / displayMetrics.density;
        float heightDp = displayMetrics.heightPixels / displayMetrics.density;
        float screenSw = Math.min(widthDp, heightDp);
        return screenSw >= 600;
    }

    public static BoundedMatcher<View,TextView> withStepDetailedDescription(final String text) {

       return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("No match for step detailed description found with text: " + text);

            }

            @Override
            protected boolean matchesSafely(TextView item) {
                String s = item.getText().toString();
                if (s.contains(text))
                    return true;
                return false;
            }
        };
    }
    public static Matcher<RecyclerView.ViewHolder> withIngredientHolderTextView(final String text) {
        return new BoundedMatcher<RecyclerView.ViewHolder, IngredientAdapter.IngredientViewHolder>(IngredientAdapter.IngredientViewHolder.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("No Ingredient ViewHolder found with text: " + text);
            }

            @Override
            protected boolean matchesSafely(IngredientAdapter.IngredientViewHolder item) {
                TextView textView = (TextView) item.itemView.findViewById(R.id.ingredient);
                if (textView == null) {
                    return false;
                }
                return textView.getText().toString().equalsIgnoreCase(text);
            }
        };
    }

    public static Matcher<RecyclerView.ViewHolder> withStepHolderTextView(final String text) {
        return new BoundedMatcher<RecyclerView.ViewHolder, StepAdapter.StepViewHolder>(StepAdapter.StepViewHolder.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("No Step ViewHolder found with text: " + text);
            }

            @Override
            protected boolean matchesSafely(StepAdapter.StepViewHolder item) {
                TextView textView = (TextView) item.itemView.findViewById(R.id.stepDescription);
                if (textView == null) {
                    return false;
                }
                return textView.getText().toString().equalsIgnoreCase(text);
            }
        };
    }
}

package com.example.android.bakingrecipe.view;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.databinding.StepDetailsFragmentBinding;
import com.example.android.bakingrecipe.model.Step;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;


public class StepDetailsActivity extends AppCompatActivity {

    ArrayList<Step> steps;
    int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        currentPosition=i.getIntExtra("currentPosition",0);
        steps = i.getParcelableArrayListExtra("Steps");

        StepDetailsFragmentBinding binding = DataBindingUtil.setContentView(this, R.layout.step_details_fragment);

        //create a new instance of a fragment only when savedInstanceState is not null
        //This is to cater for config change (i.e., when the screen rotates)
        //To Udacity: Thanks for pointing this out!!
        if(savedInstanceState==null){
            StepDetailsFragment stepDetailsFragment = StepDetailsFragment.newInstance(steps,currentPosition);

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, stepDetailsFragment).commit();
        }

    }

    @Override
    protected void onUserLeaveHint() {
        if (Util.SDK_INT > 23) {
            this.enterPictureInPictureMode();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra("currentPosition", currentPosition);
                setResult(Activity.RESULT_OK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("currentPosition", currentPosition);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }
}



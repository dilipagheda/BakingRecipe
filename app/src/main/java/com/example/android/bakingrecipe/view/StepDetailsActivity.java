package com.example.android.bakingrecipe.view;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.databinding.ActivityMainBinding;
import com.example.android.bakingrecipe.databinding.StepDetailsBinding;
import com.example.android.bakingrecipe.databinding.StepDetailsFragmentBinding;
import com.example.android.bakingrecipe.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import static com.google.android.exoplayer2.ui.AspectRatioFrameLayout.*;


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

        //fragment
        StepDetailsFragmentBinding binding = DataBindingUtil.setContentView(this, R.layout.step_details_fragment);

        StepDetailsFragment stepDetailsFragment = StepDetailsFragment.newInstance(steps,currentPosition,false);

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, stepDetailsFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
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



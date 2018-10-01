package com.example.android.bakingrecipe.view;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
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

import static com.google.android.exoplayer2.ui.AspectRatioFrameLayout.*;


public class StepDetailsActivity extends AppCompatActivity {

    Step step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        step = i.getParcelableExtra("Step");

        //fragment
        StepDetailsFragmentBinding binding = DataBindingUtil.setContentView(this, R.layout.step_details_fragment);

        StepDetailsFragment stepDetailsFragment = StepDetailsFragment.newInstance(step,false);

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, stepDetailsFragment).commit();
    }
}



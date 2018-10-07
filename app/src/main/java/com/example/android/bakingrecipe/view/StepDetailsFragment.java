package com.example.android.bakingrecipe.view;


import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.databinding.StepDetailsBinding;
import com.example.android.bakingrecipe.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StepDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepDetailsFragment extends Fragment  {
    SimpleExoPlayerView playerView;
    SimpleExoPlayer player;
    int orientation;
    boolean isFullScreen=false;
    ArrayList<Step> steps;

    private static final String ARG_STEPS = "steps";
    private static final String ARG_DUAL = "dual";
    private static final String ARG_CURRENT_POSITION = "currentposition";
    private StepDetailsBinding binding;
    private boolean isDualMode;
    private ImageView imageView;
    private static String TAG="StepDetailsFragment";
    private int currentPosition;
    public StepDetailsFragment() {
        // Required empty public constructor
    }

    public static StepDetailsFragment newInstance(ArrayList<Step> steps, int currentPosition, boolean isDualMode) {
        StepDetailsFragment fragment = new StepDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CURRENT_POSITION,currentPosition);
        args.putParcelableArrayList(ARG_STEPS, steps);
        args.putBoolean(ARG_DUAL,isDualMode);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            steps = getArguments().getParcelableArrayList(ARG_STEPS);
            currentPosition = getArguments().getInt(ARG_CURRENT_POSITION);
            isDualMode = getArguments().getBoolean(ARG_DUAL);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = StepDetailsBinding.inflate(inflater,container,false);

        playerView = (SimpleExoPlayerView) binding.videoView;
        imageView = binding.imageView;

        orientation = getResources().getConfiguration().orientation;
        playerView.setResizeMode(RESIZE_MODE_ZOOM);

        if (orientation == Configuration.ORIENTATION_LANDSCAPE && !isDualMode) {
            // In landscape

            View decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Hide the nav bar and status bar
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
            isFullScreen = true;
        } else {
            // In portrait
            binding.description.setText(steps.get(currentPosition).getDescription());

            if(!isDualMode){
                //set-up click listeners on back/forward buttons
                binding.nextStep.setOnClickListener(new NavigationClickListener(true));
                binding.previousStep.setOnClickListener(new NavigationClickListener(false));
            }

        }
        return binding.getRoot();
    }


    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        Step step = steps.get(currentPosition);
        String videoURL = step.getVideoURL();
        String thumbnailURL = step.getThumbnailURL();
        if(videoURL.isEmpty()){
            playerView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            if(thumbnailURL.isEmpty()){
                imageView.setImageResource(R.drawable.ic_error_black_24dp);

            }else{
                Picasso.with(getContext())
                        .load(thumbnailURL)
                        .into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                imageView.setImageResource(R.drawable.ic_error_black_24dp);
                            }
                        });
            }

        }else{
            playerView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);

            Uri uri = Uri.parse(videoURL);
            DefaultDataSourceFactory mediaDataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "BakingRecipe"));

            MediaSource mediaSource = new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                    .createMediaSource(uri);

            player.prepare(mediaSource, true, false);
            player.setPlayWhenReady(true);
            player.seekTo(player.getCurrentWindowIndex(), player.getCurrentPosition());

            playerView.setPlayer(player);
            }

    }


    private void releasePlayer() {

        if (player != null) {
            player.release();
            player = null;
            Log.d(TAG,"releasePlayer");

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
            Log.d(TAG,"onStart >23");

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
            Log.d(TAG,"onStart <=23");

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
            Log.d(TAG,"onPause <=23");

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
            Log.d(TAG,"onStop >23");

        }
    }

    private class NavigationClickListener implements View.OnClickListener {
        private boolean isForward;

        public NavigationClickListener(boolean isForward) {
            this.isForward = isForward;
        }

        @Override
        public void onClick(View view) {
            StepDetailsActivity stepDetailsActivity = (StepDetailsActivity)getActivity();

            if(isForward){
                currentPosition++;
                if(currentPosition >= steps.size()){
                    currentPosition = steps.size()-1;
                    binding.nextStep.setEnabled(false);
                }else{
                    binding.previousStep.setEnabled(true);
                    stepDetailsActivity.setCurrentPosition(currentPosition);
                    releasePlayer();
                    initializePlayer();
                    binding.description.setText(steps.get(currentPosition).getDescription());
                }

            }else{
                currentPosition--;
                if(currentPosition<0){
                    currentPosition=0;
                    binding.previousStep.setEnabled(false);
                }else{
                    binding.nextStep.setEnabled(true);
                    stepDetailsActivity.setCurrentPosition(currentPosition);
                    releasePlayer();
                    initializePlayer();
                    binding.description.setText(steps.get(currentPosition).getDescription());
                }


            }

        }
    }
}




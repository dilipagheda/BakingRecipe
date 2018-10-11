package com.example.android.bakingrecipe.view;


import android.app.PictureInPictureParams;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Rational;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.databinding.StepDetailsBinding;
import com.example.android.bakingrecipe.model.Step;
import com.github.rubensousa.previewseekbar.PreviewLoader;
import com.github.rubensousa.previewseekbar.exoplayer.PreviewTimeBar;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import static com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StepDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepDetailsFragment extends Fragment implements PreviewLoader {
    public static final String CURRENT_PLAYBACK_POSITION = "CURRENT_PLAYBACK_POSITION";
    public static final String GET_PLAY_WHEN_READY = "GET_PLAY_WHEN_READY";
    private static final String ARG_STEPS = "steps";
    private static final String ARG_CURRENT_POSITION = "currentposition";
    private static final String TAG = "StepDetailsFragment";

    private SimpleExoPlayerView playerView;
    private SimpleExoPlayer player;
    private int orientation;
    private ArrayList<Step> steps;
    private StepDetailsBinding binding;
    private ImageView imageView;
    private int currentPosition;//this is for current step position
    private String videoURL;

    //Below are for saving play back state on config change
    private long currentPlaybackPosition;
    private boolean playWhenReady;
    //Below are for preview seek bar
    private PreviewTimeBar previewTimeBar;
    private ImageView thumbnailImageView;
    private StepDetailsActivity stepDetailsActivity;

    public StepDetailsFragment() {
        // Required empty public constructor
    }

    public static StepDetailsFragment newInstance(ArrayList<Step> steps, int currentPosition) {
        StepDetailsFragment fragment = new StepDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CURRENT_POSITION, currentPosition);
        args.putParcelableArrayList(ARG_STEPS, steps);
        args.putLong(CURRENT_PLAYBACK_POSITION, 0);
        args.putBoolean(GET_PLAY_WHEN_READY, true);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            steps = getArguments().getParcelableArrayList(ARG_STEPS);
            currentPosition = getArguments().getInt(ARG_CURRENT_POSITION);
            currentPlaybackPosition = getArguments().getLong(CURRENT_PLAYBACK_POSITION);
            playWhenReady = getArguments().getBoolean(GET_PLAY_WHEN_READY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = StepDetailsBinding.inflate(inflater, container, false);

        playerView = (SimpleExoPlayerView) binding.videoView;
        imageView = binding.imageView;

        orientation = getResources().getConfiguration().orientation;
        playerView.setResizeMode(RESIZE_MODE_ZOOM);
        try {
            stepDetailsActivity = (StepDetailsActivity) getActivity();
        }catch(ClassCastException e){
            stepDetailsActivity = null;
            e.printStackTrace();
            Log.d(TAG,e.getMessage());
        }
        //retrieve the state
        if (savedInstanceState != null) {
            playWhenReady = savedInstanceState.getBoolean(GET_PLAY_WHEN_READY);
            currentPlaybackPosition = savedInstanceState.getLong(CURRENT_PLAYBACK_POSITION);
            currentPosition = savedInstanceState.getInt(ARG_CURRENT_POSITION);
            if(stepDetailsActivity!=null)
                stepDetailsActivity.setCurrentPosition(currentPosition);
        }

        if (getContext().getResources().getBoolean(R.bool.phoneLandscape)) {
            // If phone and orientation is landscape
            fullScreenMode(true);
        } else {
            binding.description.setText(steps.get(currentPosition).getDescription());

            //do this only if device is a phone -portrait mode and not the tablet
            if (getResources().getBoolean(R.bool.phonePortrait)) {
                //set-up click listeners on back/forward buttons
                binding.nextStep.setOnClickListener(new NavigationClickListener(true));
                binding.previousStep.setOnClickListener(new NavigationClickListener(false));
            }

        }

        previewTimeBar = playerView.findViewById(R.id.exo_progress);
        thumbnailImageView = playerView.findViewById(R.id.thumbnailImageView);
        previewTimeBar.setPreviewLoader(this);

        return binding.getRoot();
    }

    private void fullScreenMode(boolean enable) {
        View decorView = getActivity().getWindow().getDecorView();

        if (enable) {
            decorView.setSystemUiVisibility(
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Hide the nav bar and status bar
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            decorView.setSystemUiVisibility(
                    View.VISIBLE);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d(TAG, "LANDSCAPE:");
            ShowHideOtherElements(View.GONE);
            fullScreenMode(true);


        } else {
            Log.d(TAG, "PORTRAIT:");
            ShowHideOtherElements(View.VISIBLE);
            fullScreenMode(false);


        }
    }

    private void initializePlayer(long currentPlaybackPosition, boolean playWhenReady) {

        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        Step step = steps.get(currentPosition);
        videoURL = step.getVideoURL();
        String thumbnailURL = step.getThumbnailURL();
        if (videoURL.isEmpty()) {
            playerView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            if (thumbnailURL.isEmpty()) {
                imageView.setImageResource(R.drawable.ic_error_black_24dp);

            } else {
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

        } else {
            playerView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);

            Uri uri = Uri.parse(videoURL);
            DefaultDataSourceFactory mediaDataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "BakingRecipe"));

            MediaSource mediaSource = new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                    .createMediaSource(uri);

            player.prepare(mediaSource, true, false);
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(player.getCurrentWindowIndex(), currentPlaybackPosition);
            player.addListener(new ExoPlayerEventListener());
            playerView.setPlayer(player);

        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(CURRENT_PLAYBACK_POSITION, currentPlaybackPosition);
        outState.putBoolean(GET_PLAY_WHEN_READY, playWhenReady);
        outState.putInt(ARG_CURRENT_POSITION,currentPosition);
        super.onSaveInstanceState(outState);
    }

    private void releasePlayer() {

        if (player != null) {
            player.release();
            player = null;
            Log.d(TAG, "releasePlayer");

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer(currentPlaybackPosition, playWhenReady);
            Log.d(TAG, "onStart >23");

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer(currentPlaybackPosition, playWhenReady);
            Log.d(TAG, "onStart <=23");

        }
    }

    @Override
    public void onPause() {
        super.onPause();

        //retrieve the state here for current playback position and play when ready flag
        //Thanks to Udacity for pointing this out!
        currentPlaybackPosition = player.getCurrentPosition();
        playWhenReady = player.getPlayWhenReady();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
            Log.d(TAG, "onPause <=23");

        } else {
            //Putting a check for PIP mode.
            //Although it is not necessary as we are not releasing a player here for SDK >23
            //However, it's good to follow documentation as a best practice
            if (getActivity().isInPictureInPictureMode()) {
                return;
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
            Log.d(TAG, "onStop >23");

        }
    }

    @Override
    public void loadPreview(long currentPosition, long max) {
        player.setPlayWhenReady(false);


        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoURL, new HashMap<String, String>());
        Bitmap image = retriever.getFrameAtTime(100);

        //Using Glide here as Picasso doesn't support loading Bitmap
        Glide.with(getActivity())
                .load(image)
                .into(thumbnailImageView);

    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        if (isInPictureInPictureMode) {

            ShowHideOtherElements(View.GONE);


            if (Util.SDK_INT > 25) {
                Rational rational = new Rational(16,
                        9);

                PictureInPictureParams mParams =
                        new PictureInPictureParams.Builder()
                                .setAspectRatio(rational)
                                .build();

                getActivity().setPictureInPictureParams(mParams);

            }
            //hide nav and status bar
            View decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            // Hide the nav bar and status bar
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN


            );

        } else {
            ShowHideOtherElements(View.VISIBLE);

            //hide nav and status bar
            View decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);


        }
    }

    private void ShowHideOtherElements(int gone) {
        if (binding.previousStep != null) {
            binding.previousStep.setVisibility(gone);
        }
        if (binding.nextStep != null) {
            binding.nextStep.setVisibility(gone);
        }
        if (binding.stepDetailsOther != null) {
            binding.stepDetailsOther.setVisibility(gone);
        }

        if (binding.description != null) {
            binding.description.setVisibility(gone);
        }
    }

    private class ExoPlayerEventListener implements Player.EventListener {

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState) {
                case Player.STATE_BUFFERING:
                    break;
                case Player.STATE_ENDED:
                    player.seekTo(0);
                    player.setPlayWhenReady(false);
                    break;
                case Player.STATE_IDLE:
                    break;
                case Player.STATE_READY:
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        @Override
        public void onPositionDiscontinuity(int reason) {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }

        @Override
        public void onSeekProcessed() {

        }
    }

    private class NavigationClickListener implements View.OnClickListener {
        private boolean isForward;

        public NavigationClickListener(boolean isForward) {
            this.isForward = isForward;
        }

        @Override
        public void onClick(View view) {

            if (isForward) {
                currentPosition++;
                if (currentPosition >= steps.size()) {
                    currentPosition = steps.size() - 1;
                    binding.nextStep.setVisibility(View.GONE);
                } else {
                    binding.previousStep.setVisibility(View.VISIBLE);
                    if(stepDetailsActivity!=null)
                        stepDetailsActivity.setCurrentPosition(currentPosition);
                    releasePlayer();
                    initializePlayer(currentPlaybackPosition, playWhenReady);
                    binding.description.setText(steps.get(currentPosition).getDescription());
                }

            } else {
                currentPosition--;
                if (currentPosition < 0) {
                    currentPosition = 0;
                    binding.previousStep.setVisibility(View.GONE);
                } else {
                    binding.nextStep.setVisibility(View.VISIBLE);
                    if(stepDetailsActivity!=null)
                        stepDetailsActivity.setCurrentPosition(currentPosition);
                    releasePlayer();
                    initializePlayer(currentPlaybackPosition, playWhenReady);
                    binding.description.setText(steps.get(currentPosition).getDescription());
                }


            }

        }
    }
}




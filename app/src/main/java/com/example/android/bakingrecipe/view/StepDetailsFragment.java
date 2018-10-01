package com.example.android.bakingrecipe.view;


import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import static com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StepDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepDetailsFragment extends Fragment implements GestureDetector.OnGestureListener  {
    SimpleExoPlayerView playerView;
    SimpleExoPlayer player;
    int orientation;
    boolean isFullScreen=false;
    Step step;

    private static final String ARG_STEP = "step";
    private static final String ARG_DUAL = "dual";
    private boolean isDualMode;
    private ImageView imageView;

    public StepDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StepDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StepDetailsFragment newInstance(Step step,boolean isDualMode) {
        StepDetailsFragment fragment = new StepDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_STEP, step);
        args.putBoolean(ARG_DUAL,isDualMode);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            step = getArguments().getParcelable(ARG_STEP);
            isDualMode = getArguments().getBoolean(ARG_DUAL);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        StepDetailsBinding binding = StepDetailsBinding.inflate(inflater,container,false);

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
            binding.description.setText(step.getDescription());
        }
        return binding.getRoot();
    }


    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());

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
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }


    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT || !isDualMode) {
            return false;
        }
        View decorView = getActivity().getWindow().getDecorView();
        if (isFullScreen) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            isFullScreen = false;
        } else {
            isFullScreen = true;
            decorView.setSystemUiVisibility(
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Hide the nav bar and status bar
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }

        return true;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}




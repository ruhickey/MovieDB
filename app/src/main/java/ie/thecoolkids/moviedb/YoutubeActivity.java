package ie.thecoolkids.moviedb;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{
    private YouTubePlayer player;
    private boolean fullscreen;
    private int millis;
    private String videoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        final RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.youtube_activity);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.youtubePlayerView);
        playerView.initialize(getString(R.string.YOUTUBE_API_KEY), this);

        if(savedInstanceState != null) {
            millis = savedInstanceState.getInt("videoTime");
        }

        final Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("passedID")) {
            videoID = extras.getString("passedID");
        } else {
            finish();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        player = youTubePlayer;
        youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
        youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
        youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
            @Override
            public void onFullscreen(boolean b) {
                fullscreen = b;
            }
        });

        if (videoID != null && !wasRestored) {
            youTubePlayer.loadVideo(videoID);
        }

        if (wasRestored) {
            youTubePlayer.seekToMillis(millis);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(getApplicationContext(), "Unable To Load Video", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(player != null){
            outState.putInt("videoTime", player.getCurrentTimeMillis());
        }
    }

    @Override
    public void onBackPressed(){
        boolean finish = true;
        try{
            if(player != null){
                if(fullscreen){
                    finish = false;
                    player.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                        @Override
                        public void onFullscreen(boolean b) {
                            if(!b){
                                finish();
                            }
                        }
                    });
                    player.setFullscreen(false);
                }
                player.pause();
            }
        } catch (final IllegalStateException e) {
            e.printStackTrace();
        }

        if(finish){
            super.onBackPressed();
        }
    }
}

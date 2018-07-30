package ir.codetower.moshaver.Activities;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import ir.codetower.moshaver.R;

public class VideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private Timer timer;
    private TextView videoCurrentDurationTextView;
    private SeekBar seekBar;

    private RelativeLayout.LayoutParams portraitLayoutParams;
    private RelativeLayout.LayoutParams landscapeLayoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        setupLayoutParams();
        if(!getIntent().hasExtra("url")){
            finish();
        }
        else {
            setupVideo(getIntent().getExtras().getString("url"));
            if (Build.VERSION.SDK_INT < 16) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else {
                View decorView = getWindow().getDecorView();
                // Hide Status Bar.
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }
        }
    }

    private void setupVideo(String url) {
        videoView=(VideoView)findViewById(R.id.video_view);
        videoView.setVideoURI(Uri.parse("http://gognuus.ir/service/"+url));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                setupVideoControlersViews();
            }
        });
    }

    private void setupVideoControlersViews() {
        final AppCompatImageView playButton=findViewById(R.id.button_play);
        playButton.setVisibility(View.VISIBLE);
        ProgressBar progress=findViewById(R.id.progress);
        progress.setVisibility(View.GONE);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoView.isPlaying()){
                    videoView.pause();
                    playButton.setImageResource(R.drawable.ic_play);
                }else {
                    videoView.start();
                    playButton.setImageResource(R.drawable.ic_pause);
                }
            }
        });


        TextView videoDurationTextView=(TextView)findViewById(R.id.text_video_duration);
        videoDurationTextView.setText(formatDuration(videoView.getDuration()));

        videoCurrentDurationTextView=(TextView)findViewById(R.id.text_video_current_duration);
        videoCurrentDurationTextView.setText(formatDuration(0));

        seekBar=findViewById(R.id.seek_bar);
        seekBar.setMax(videoView.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    videoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        videoCurrentDurationTextView.setText(formatDuration(videoView.getCurrentPosition()));
                        seekBar.setProgress(videoView.getCurrentPosition());
                        seekBar.setSecondaryProgress((videoView.getBufferPercentage()*videoView.getDuration())/100);
                    }
                });
            }
        },0,1000);
    }

    private String formatDuration(long duration) {
        int seconds = (int) (duration / 1000);
        int minutes = seconds / 60;
        seconds %= 60;
        return String.format(Locale.ENGLISH, "%02d", minutes) + ":" + String.format(Locale.ENGLISH, "%02d", seconds);
    }

    @Override
    protected void onDestroy() {
        if (timer!=null){
            timer.purge();
            timer.cancel();

        }
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


    }

    private void setupLayoutParams(){

        landscapeLayoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        portraitLayoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


    }
}

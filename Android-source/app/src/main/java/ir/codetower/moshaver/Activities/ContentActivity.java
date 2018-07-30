package ir.codetower.moshaver.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.hedgehog.ratingbar.RatingBar;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import ir.codetower.moshaver.Adapters.ImageAdapter;
import ir.codetower.moshaver.Adapters.VideoAdapter;
import ir.codetower.moshaver.App;
import ir.codetower.moshaver.Config;
import ir.codetower.moshaver.CustomTextView;
import ir.codetower.moshaver.Helpers.DownloadTask;
import ir.codetower.moshaver.Helpers.WebService;
import ir.codetower.moshaver.Models.Content;
import ir.codetower.moshaver.Models.Media;
import ir.codetower.moshaver.Param;
import ir.codetower.moshaver.R;

public class ContentActivity extends AppCompatActivity {


    private Timer timer;
    private Content content;
    private AppCompatButton sendRate;
    private Media currentMedia;
    private int rate = 3;
    private int first = 0;
    private LinearLayout media_root;
    private ArrayList<Media> medias = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private LinearLayout videoRoot, imageRoot;
    private RecyclerView videoList, imageList;
    AppCompatTextView currentDuration;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

//        media_root = findViewById(R.id.media_root);
        imageRoot = findViewById(R.id.image_root);
        videoRoot = findViewById(R.id.video_root);
        videoList = findViewById(R.id.video_list);
        imageList = findViewById(R.id.image_list);


        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            int id = intent.getExtras().getInt("id");
            content = Content.getContent(id);

            if (content.hasVideo()) {
                videoRoot.setVisibility(View.VISIBLE);
                videoList.setLayoutManager(new LinearLayoutManager(ContentActivity.this, LinearLayoutManager.HORIZONTAL, false));

                VideoAdapter videoAdapter = new VideoAdapter(content.getVideo().split(","));
                videoList.setAdapter(videoAdapter);

            }
            if (content.hasImage()) {
                imageRoot.setVisibility(View.VISIBLE);
                imageList.setLayoutManager(new LinearLayoutManager(ContentActivity.this, LinearLayoutManager.HORIZONTAL, false));

                ImageAdapter imageAdapter = new ImageAdapter(content.getImage().split(","));
                imageList.setAdapter(imageAdapter);
            }
            setupViews();
//            setupMediaPlayer();
        }
        Param.clear();
        Param.put("userId", App.prefManager.getPreference("userId"));
        App.webService.postRequest(Param.get(), Config.apiUrl + "getWallet", new WebService.OnPostReceived() {
            @Override
            public void onReceived(String message) {
                try {
                    int wallet = Integer.parseInt(message);
                    App.prefManager.savePreference("wallet", wallet + "");
                } catch (Exception e) {

                }
            }

            @Override
            public void onReceivedError(String message) {

            }
        });

    }

    private void setupViews() {
//        if (content.isPremium() || content.getPrice().equals("0")) {
//            findViewById(R.id.premium_root).setVisibility(View.GONE);
//        } else {
//            AppCompatTextView price = findViewById(R.id.price);
//            price.setText("مبلغ قابل پرداخت : " + content.getPrice() + "تومان");
//            findViewById(R.id.premium_root).setVisibility(View.VISIBLE);
//        }
        final View dialogView = LayoutInflater.from(App.context).inflate(R.layout.wallet_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(ContentActivity.this).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        final LinearLayout primium_root, play_root, download_root;
        download_root = findViewById(R.id.download_root);
        primium_root = findViewById(R.id.premium_root);
        play_root = findViewById(R.id.play_root);
        if (content.isPremium() || content.getPrice().equals("0")) {
            primium_root.setVisibility(View.GONE);
            File file = new File(Config.sdTempFolderAddress + content.getG_id() + "/" + content.getId());
            if (file.exists()) {
                play_root.setVisibility(View.VISIBLE);
                download_root.setVisibility(View.GONE);
                setupMediaPlayer();

            } else {
                final ProgressBar progressBar = findViewById(R.id.progressBar);

                play_root.setVisibility(View.GONE);
                download_root.setVisibility(View.VISIBLE);
                final Button download = findViewById(R.id.download);
                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        download.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setProgress(0);
                        DownloadTask downloadTask = new DownloadTask(new DownloadTask.ProgressPercentListener() {
                            @Override
                            public void onListen(int percent) {
                                progressBar.setProgress(percent);
                            }

                            @Override
                            public void onEnd(String result) {

                                play_root.setVisibility(View.VISIBLE);
                                download_root.setVisibility(View.GONE);
setupMediaPlayer();

                            }
                        });
                        downloadTask.execute(Config.apiUrl + "getSound?id=" + content.getId() + "&userId=" + App.prefManager.getPreference("userId"), Config.sdTempFolderAddress + content.getG_id() + "/" + content.getId());
                    }
                });
            }
        } else {
            primium_root.setVisibility(View.VISIBLE);
            play_root.setVisibility(View.GONE);
            download_root.setVisibility(View.GONE);
            CustomTextView price = findViewById(R.id.price);
            price.setText("مبلغ پرداختی برای این دوره : " + content.getPrice() + " تومان میباشد");
            findViewById(R.id.premium).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final View paidDialogView = LayoutInflater.from(App.context).inflate(R.layout.select_paid_dialog, null);
                    final AlertDialog paidDialog = new AlertDialog.Builder(ContentActivity.this).create();
                    paidDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    paidDialog.setView(paidDialogView);
                    paidDialog.setCancelable(true);
                    paidDialog.show();
                    Button realtime = paidDialogView.findViewById(R.id.realtime);
                    AppCompatTextView price_amount = paidDialogView.findViewById(R.id.amount);
                    price_amount.setText("مبلغ قابل پرداخت :" + content.getPrice() + " تومان");


                    realtime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String url = Config.apiUrl + "premiumRequest?id=" + content.getId() + "&userId=" + App.prefManager.getPreference("userId") + "&type=realtime";
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                            paidDialog.dismiss();
                        }
                    });
                }
            });
        }


//        AppCompatButton premium = findViewById(R.id.premium);


//        premium.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(App.prefManager.getPreference("registered").equals("registered")) {
//
//                }
//                else{
//                    loginUser();
//                }
//
//            }
//        });


        getAvrage();
        final RatingBar ratingBar = findViewById(R.id.ratingbar);

        ratingBar.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float RatingCount) {
                rate = (int) RatingCount;
            }
        });
        AppCompatTextView body = findViewById(R.id.body);
        CardView favorite = findViewById(R.id.favorite);
        final AppCompatImageView favorite_icon = findViewById(R.id.favorite_icon);

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content.setFavorite(!content.isFavorite());
                if (content.isFavorite()) {
                    favorite_icon.setColorFilter(Color.parseColor("#9b59b6"));
                    String fav = App.prefManager.getPreference("favorites");
                    if (!fav.equals("")) {
                        App.prefManager.savePreference("favorites", fav + "-" + content.getId());
                    } else {
                        App.prefManager.savePreference("favorites", content.getId() + "");
                    }
                } else {
                    favorite_icon.setColorFilter(Color.parseColor("#ffffff"));
                    String pref = App.prefManager.getPreference("favorites");
                    if (!pref.equals("")) {
                        String[] fav = pref.split("-");
                        pref = "";
                        for (int i = 0; i < fav.length; i++) {
                            if (!fav[i].equals(content.getId() + "")) {
                                pref += fav[i] + "-";
                            }
                        }
                        if (!pref.equals("")) {
                            pref = pref.substring(0, pref.length() - 1);
                        }
                        App.prefManager.savePreference("favorites", pref);
                    }
                }
                content.update();
            }
        });
        if (content.isFavorite()) {
            favorite_icon.setColorFilter(Color.parseColor("#9b59b6"));
        }
        body.setText(content.getBody());
        AppCompatTextView title = findViewById(R.id.title);
        title.setText(content.getTitle());
        sendRate = findViewById(R.id.send_rate);
        sendRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> params = new HashMap<>();

                params.put("IMEI", App.IMEI);
                params.put("rate", rate + "");
                params.put("id", content.getId() + "");

                App.webService.postRequest(params, Config.apiUrl + "saveRate", new WebService.OnPostReceived() {
                    @Override
                    public void onReceived(String message) {
                        Toast.makeText(App.context, "امتیاز با موفقیت ثبت شد", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onReceivedError(String message) {
                        Log.i("webservice", "onReceived: " + message);
                        Toast.makeText(App.context, "مشکل در شبکه بوجود آمده است", Toast.LENGTH_LONG).show();

                    }
                });

                sendRate.setEnabled(false);
            }
        });

    }

    private void getAvrage() {
        final AppCompatTextView average = findViewById(R.id.avrage);
        HashMap<String, String> params = new HashMap<>();
        params.put("id", content.getId() + "");
        App.webService.postRequest(params, Config.apiUrl + "getAvrage", new WebService.OnPostReceived() {
            @Override
            public void onReceived(String message) {
                average.setText("میانگین امتیازات : " + message);
            }

            @Override
            public void onReceivedError(String message) {

            }
        });
    }

    private void setupMediaPlayer() {
        setupMediaView();

        timer = new Timer();
        timer.schedule(new MainTimer(), 0, 1000);
    }

    private void setupMediaView() {


        seekBar = findViewById(R.id.seek_bar);
        final AppCompatImageView playButton = findViewById(R.id.button_play);

        final AppCompatTextView duration = findViewById(R.id.text_music_duration);
        currentDuration = findViewById(R.id.text_music_current_duration);
        ImageView forward = ((ImageView) findViewById(R.id.button_forward));
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000);
            }
        });
        ImageView rewind = ((ImageView) findViewById(R.id.button_rewind));
        rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000);
            }
        });


        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
            }
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(Config.sdTempFolderAddress + content.getG_id() + "/" + content.getId());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mediaPlayer) {
                    playButton.setVisibility(View.VISIBLE);

                    seekBar.setMax(mediaPlayer.getDuration());
                    playButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.pause();
                                playButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play, null));
                            } else {
                                mediaPlayer.start();
                                playButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause, null));
                            }
                        }
                    });
                    duration.setText(formatDuration(mediaPlayer.getDuration()));
                    currentDuration.setText(formatDuration(0));
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    playButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play, null));
                }
            });
            seekBar.setMax(mediaPlayer.getDuration());
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        mediaPlayer.seekTo(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private String formatDuration(long duration) {
        int seconds = (int) (duration / 1000);
        int minutes = seconds / 60;
        seconds %= 60;
        return String.format(Locale.ENGLISH, "%02d", minutes) + ":" + String.format(Locale.ENGLISH, "%02d", seconds);
    }

    private class MainTimer extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {
                        if (mediaPlayer != null && mediaPlayer.getDuration() > 0) {
                            int pos = mediaPlayer.getCurrentPosition();
                            seekBar.setProgress(pos);
                            currentDuration.setText(formatDuration(mediaPlayer.getCurrentPosition()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        if (timer != null) {
            timer.purge();
            timer.cancel();
        }

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        if (first>=2) {
            refreshData();
        }
        super.onResume();
        first++;
    }

    private void refreshData() {

            Param.clear();
            Param.put("id", content.getId() + "");
            Param.put("userId", App.prefManager.getPreference("userId") + "");
            App.webService.postRequest(Param.get(), Config.apiUrl + "getContent", new WebService.OnPostReceived() {
                @Override
                public void onReceived(String message) {
                    try {
                        content = Content.jsonToContent(message);
                        setupViews();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onReceivedError(String message) {

                }
            });

    }


}

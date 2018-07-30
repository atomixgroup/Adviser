package ir.codetower.moshaver.Models;

import android.media.MediaPlayer;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by Mr-R00t on 3/16/2018.
 */

public class Media {
    private int id;
    private MediaPlayer mediaPlayer;
    private AppCompatTextView duration;
    private AppCompatTextView currentDuration;
    private ImageView forward;
    private ImageView rewind;
    private SeekBar seekBar;
    private AppCompatImageView playButton;
    private AppCompatImageView download;
    private AVLoadingIndicatorView loading;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public AppCompatTextView getDuration() {
        return duration;
    }

    public void setDuration(AppCompatTextView duration) {
        this.duration = duration;
    }

    public AppCompatTextView getCurrentDuration() {
        return currentDuration;
    }

    public void setCurrentDuration(AppCompatTextView currentDuration) {
        this.currentDuration = currentDuration;
    }

    public ImageView getForward() {
        return forward;
    }

    public void setForward(ImageView forward) {
        this.forward = forward;
    }

    public ImageView getRewind() {
        return rewind;
    }

    public void setRewind(ImageView rewind) {
        this.rewind = rewind;
    }

    public SeekBar getSeekBar() {
        return seekBar;
    }

    public void setSeekBar(SeekBar seekBar) {
        this.seekBar = seekBar;
    }

    public AppCompatImageView getPlayButton() {
        return playButton;
    }

    public void setPlayButton(AppCompatImageView playButton) {
        this.playButton = playButton;
    }

    public AppCompatImageView getDownload() {
        return download;
    }

    public void setDownload(AppCompatImageView download) {
        this.download = download;
    }

    public AVLoadingIndicatorView getLoading() {
        return loading;
    }

    public void setLoading(AVLoadingIndicatorView loading) {
        this.loading = loading;
    }
}

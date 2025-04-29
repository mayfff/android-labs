package kpi.zakrevskyi.labs.audio.activity;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import kpi.zakrevskyi.labs.R;
import kpi.zakrevskyi.labs.audio.dataholder.AudioDataHolder;
import kpi.zakrevskyi.labs.audio.model.AudioModel;

public class AudioPlayerActivity extends AppCompatActivity {
    private ImageButton btnPlay, btnPause, btnNext, btnPrevious;
    private ImageView titleView;
    private TextView audioTitle, audioAuthor, currentDuration, endDuration;
    private MediaPlayer mediaPlayer;
    private SeekBar audioDuration;
    private int index;
    private ArrayList<AudioModel> audioList;

    private Runnable updateSeekBar;
    private final Handler updateHandler = new Handler();
    private volatile boolean isUpdating = true;

    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_audio_player);

        audioList = AudioDataHolder.getAudioList();
        index = getIntent().getIntExtra("INDEX", 0);

        audioTitle = findViewById(R.id.audioTitle);
        audioAuthor = findViewById(R.id.audioAuthor);
        audioDuration = findViewById(R.id.seekBar);
        titleView = findViewById(R.id.titleView);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        currentDuration = findViewById(R.id.currentDuration);
        endDuration = findViewById(R.id.endDuration);

        btnPlay.setOnClickListener(v -> playPauseAudio());
        btnPause.setOnClickListener(v -> playPauseAudio());
        btnPrevious.setOnClickListener(v -> playPreviousAudio());
        btnNext.setOnClickListener(v -> playNextAudio());

        audioTitle.setSelected(true);
        audioAuthor.setSelected(true);

        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    int current = mediaPlayer.getCurrentPosition();
                    currentDuration.setText(formatDuration(current));
                    audioDuration.setProgress(current);
                }
                updateHandler.postDelayed(this, 1000);
            }
        };

        setupPlayer();
        updateHandler.post(updateSeekBar);

        audioDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    private void setupPlayer() {
        AudioModel playAudio = audioList.get(index);

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();;
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(playAudio.getPath());
            mediaPlayer.setOnPreparedListener(mp -> {
                audioDuration.setMax(mp.getDuration());
                endDuration.setText(formatDuration(mp.getDuration()));
                playPauseAudio();
            });
            mediaPlayer.setOnCompletionListener(mp -> {
                btnPlay.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.GONE);
            });
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        audioTitle.setText(playAudio.getAudioName());
        audioAuthor.setText(playAudio.getAudioAuthor());

        if (playAudio.getBitmap() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(playAudio.getBitmap(), 0, playAudio.getBitmap().length);
            titleView.setImageBitmap(bitmap);
            titleView.clearColorFilter();
        } else {
            titleView.setImageResource(R.drawable.audio_icon);
        }
    }

    private String formatDuration(int millis) {
        int minutes = (millis / 1000) / 60;
        int seconds = (millis / 1000) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    private void playPauseAudio() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                btnPlay.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.GONE);
            } else {
                mediaPlayer.start();
                btnPlay.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
            }
        }
    }

    private void playNextAudio() {
        if (index < audioList.size() - 1) {
            index++;
            setupPlayer();
        }
    }

    private void playPreviousAudio() {
        if (index > 0) {
            index--;
            setupPlayer();
        }
    }

    private void pauseIfPlaying() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlay.setVisibility(View.VISIBLE);
            btnPause.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateHandler.removeCallbacks(updateSeekBar);
        pauseIfPlaying();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isUpdating = false;
        pauseIfPlaying();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateHandler.post(updateSeekBar);

        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            btnPlay.setVisibility(View.GONE);
            btnPause.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateHandler.removeCallbacks(updateSeekBar);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

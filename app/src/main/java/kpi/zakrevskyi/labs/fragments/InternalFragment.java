package kpi.zakrevskyi.labs.fragments;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

import kpi.zakrevskyi.labs.R;
import kpi.zakrevskyi.labs.audio.adapter.AudioAdapter;
import kpi.zakrevskyi.labs.audio.model.AudioModel;
import kpi.zakrevskyi.labs.video.adapter.VideoAdapter;
import kpi.zakrevskyi.labs.video.model.VideoModel;

public class InternalFragment extends Fragment {
    ArrayList<AudioModel> audioList;
    ArrayList<VideoModel> videoList;
    TextView noAudio;
    TextView noVideo;

    public InternalFragment() {
        audioList = new ArrayList<>();
        videoList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_internal, container, false);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        noAudio = view.findViewById(R.id.no_audio_internal);
        noVideo = view.findViewById(R.id.no_video_internal);

        Bundle args = getArguments();
        if (args != null) {
            String selectedItem = args.getString("selectedItem");

            if ("audio".equals(selectedItem)) {
                loadAudioList(view);
            } else if ("video".equals(selectedItem)) {
                loadVideoList(view);
            }
        }

        return view;
    }

    private void loadAudioList(View view) {
        RecyclerView audioView = view.findViewById(R.id.audio_internal_recycler);
        RecyclerView videoView = view.findViewById(R.id.video_internal_recycler);
        ProgressBar loadingSpinner = view.findViewById(R.id.loadingSpinner);

        videoView.setVisibility(View.GONE);
        loadingSpinner.setVisibility(View.VISIBLE);

        new Thread(() -> {
            File[] internalFiles = getContext().getFilesDir().listFiles();
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

            audioList.clear();
            for (File file : internalFiles) {
                if (checkIfAudio(file)) {
                    String path = file.getAbsolutePath();
                    mediaMetadataRetriever.setDataSource(path);
                    String title = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                    String artist = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                    String duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    byte[] embeddedPicture = mediaMetadataRetriever.getEmbeddedPicture();

                    audioList.add(new AudioModel(title, artist, duration, path, embeddedPicture));
                }
            }
            requireActivity().runOnUiThread(() -> {
                AudioAdapter audioAdapter = new AudioAdapter(audioList, getContext());

                if (audioList.isEmpty()) {
                    noAudio.setVisibility(View.VISIBLE);
                } else {
                    audioView.setLayoutManager(new LinearLayoutManager(getContext()));
                    audioView.setAdapter(audioAdapter);
                }
                loadingSpinner.setVisibility(View.GONE);
            });
        }).start();
    }

    private boolean checkIfAudio(File file) {
        if (file == null || !file.exists()) {
            return false;
        }

        String fileName = file.getName().toLowerCase();
        String[] audioExtensions = {".mp3", ".wav", ".ogg", ".flac", ".aac", ".m4a", ".wma"};

        for (String extension : audioExtensions) {
            if (fileName.endsWith(extension)) {
                return true;
            }
        }

        return false;
    }

    private void loadVideoList(View view) {
        RecyclerView audioView = view.findViewById(R.id.audio_internal_recycler);
        RecyclerView videoView = view.findViewById(R.id.video_internal_recycler);
        ProgressBar loadingSpinner = view.findViewById(R.id.loadingSpinner);

        videoView.setVisibility(View.VISIBLE);
        loadingSpinner.setVisibility(View.GONE);

        new Thread(() -> {
            File[] internalFiles = getContext().getFilesDir().listFiles();
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

            videoList.clear();
            for (File file : internalFiles) {
                if (checkIfVideo(file)) {
                    String path = file.getAbsolutePath();
                    mediaMetadataRetriever.setDataSource(path);
                    String title = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                    String duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    Bitmap preview = mediaMetadataRetriever.getFrameAtTime(1000000, MediaMetadataRetriever.OPTION_CLOSEST);

                    String previewToUse = (title != null && !title.isEmpty()) ? title : file.getName();
                    VideoModel videoModel = new VideoModel(previewToUse, duration, file.getAbsolutePath(), preview);
                    videoList.add(videoModel);
                }
            }
            requireActivity().runOnUiThread(() -> {
                VideoAdapter videoAdapter = new VideoAdapter(videoList, getContext());

                if (videoList.isEmpty()) {
                    noVideo.setVisibility(View.VISIBLE);
                } else {
                    videoView.setLayoutManager(new LinearLayoutManager(getContext()));
                    videoView.setAdapter(videoAdapter);
                }
                loadingSpinner.setVisibility(View.GONE);
            });
        }).start();
    }

    private boolean checkIfVideo(File file) {
        if (file == null || !file.exists()) {
            return false;
        }

        String fileName = file.getName().toLowerCase();
        String[] videoExtensions = {".mp4", ".avi", ".mkv", ".mov", ".wmv", ".flv", ".webm", ".3gp"};

        for (String extension : videoExtensions) {
            if (fileName.endsWith(extension)) {
                return true;
            }
        }

        return false;
    }
}

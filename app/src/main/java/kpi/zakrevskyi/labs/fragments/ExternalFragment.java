package kpi.zakrevskyi.labs.fragments;

import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

import kpi.zakrevskyi.labs.R;
import kpi.zakrevskyi.labs.audio.adapter.AudioAdapter;
import kpi.zakrevskyi.labs.audio.model.AudioModel;
import kpi.zakrevskyi.labs.video.adapter.VideoAdapter;
import kpi.zakrevskyi.labs.video.model.VideoModel;

public class ExternalFragment extends Fragment {

    ArrayList<AudioModel> audioList = new ArrayList<>();
    ArrayList<VideoModel> videoList = new ArrayList<>();

    TextView noAudio;
    TextView noVideo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_external, container, false);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        noAudio = view.findViewById(R.id.no_audio_external);
        noVideo = view.findViewById(R.id.no_video_external);


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
        RecyclerView audioView = view.findViewById(R.id.audio_external_recycler);
        RecyclerView videoView = view.findViewById(R.id.video_external_recycler);
        ProgressBar loadingSpinner = view.findViewById(R.id.loadingSpinner);

        videoView.setVisibility(View.GONE);
        loadingSpinner.setVisibility(View.VISIBLE);

        new Thread(() -> {
            String[] projection = {
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.DATA
            };

            audioList.clear();

            Cursor cursor = requireContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
            while (cursor.moveToNext()) {
                String title = cursor.getString(0);
                String artist = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);

                if (duration == null || duration.isEmpty()) {
                    continue;
                }

                byte[] embeddedPicture = null;
                try {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(path);
                    embeddedPicture = retriever.getEmbeddedPicture();
                    retriever.release();
                } catch (Exception e) {
                    Log.w("ExternalFragment", "Failed to retrieve cover for: " + path, e);
                }
                AudioModel audio = new AudioModel(title, artist, duration, path, embeddedPicture);
                audioList.add(audio);
            }
            cursor.close();

            requireActivity().runOnUiThread(() -> {
                AudioAdapter audioAdapter = new AudioAdapter(audioList, requireContext());


                if (audioList.isEmpty()) {
                    noAudio.setVisibility(View.VISIBLE);
                } else {
                    audioView.setLayoutManager(new LinearLayoutManager(requireContext()));
                    audioView.setAdapter(audioAdapter);
                }
                loadingSpinner.setVisibility(View.GONE);
            });
        }).start();
    }

    private void loadVideoList(View view) {
        RecyclerView audioView = view.findViewById(R.id.audio_external_recycler);
        RecyclerView videoView = view.findViewById(R.id.video_external_recycler);
        ProgressBar loadingSpinner = view.findViewById(R.id.loadingSpinner);
        VideoAdapter videoAdapter = new VideoAdapter(videoList, getContext());

        audioView.setVisibility(View.GONE);
        loadingSpinner.setVisibility(View.VISIBLE);

        new Thread(() -> {
            String[] projection = {
                    MediaStore.Video.Media.TITLE,
                    MediaStore.Video.Media.DURATION,
                    MediaStore.Video.Media.DATA
            };

            String sortOrder = MediaStore.Video.Media.DATE_ADDED + " DESC";

            videoList.clear();

            Cursor cursor = getContext().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, sortOrder);

            int count = 0;
            while (cursor.moveToNext() && count < 5) {
                String title = cursor.getString(0);
                String duration = cursor.getString(1);
                String path = cursor.getString(2);

                MediaMetadataRetriever mediaRetriever = new MediaMetadataRetriever();
                mediaRetriever.setDataSource(path);

                Bitmap preview = mediaRetriever.getFrameAtTime(1000000, MediaMetadataRetriever.OPTION_CLOSEST);

                String previewToUse = (title != null && !title.isEmpty()) ? title : new File(path).getName();
                VideoModel videoModel = new VideoModel(previewToUse, duration, path, preview);

                videoList.add(videoModel);
                count++;
            }
            cursor.close();

            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {

                    if (videoList.isEmpty()) {
                        noVideo.setVisibility(View.VISIBLE);
                    } else {
                        videoView.setLayoutManager(new LinearLayoutManager(getContext()));
                        videoView.setAdapter(videoAdapter);
                    }
                    loadingSpinner.setVisibility(View.GONE);
                });
            }
        }).start();
    }
}
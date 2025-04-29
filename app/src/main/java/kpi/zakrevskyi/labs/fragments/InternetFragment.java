package kpi.zakrevskyi.labs.fragments;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import kpi.zakrevskyi.labs.R;
import kpi.zakrevskyi.labs.audio.activity.AudioPlayerActivity;
import kpi.zakrevskyi.labs.audio.dataholder.AudioDataHolder;
import kpi.zakrevskyi.labs.audio.model.AudioModel;
import kpi.zakrevskyi.labs.video.activity.VideoPlayerActivity;
import kpi.zakrevskyi.labs.video.model.VideoModel;

public class InternetFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_internet, container, false);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Bundle args = getArguments();
        if (args != null) {
            String selectedItem = args.getString("selectedItem");

            if ("audio".equals(selectedItem)) {
                loadAudioUrl(view);
            } else if ("video".equals(selectedItem)) {
                loadVideoUrl(view);
            }
        }

        return view;
    }

    private void loadAudioUrl(View view) {
        LinearLayout audioContainer = view.findViewById(R.id.url_audio_container);
        EditText audioUrl = view.findViewById(R.id.url_input_audio);
        Button audioButton = view.findViewById(R.id.play_url_audio);

        audioContainer.setVisibility(View.VISIBLE);

        audioButton.setOnClickListener(v -> {
            String url = audioUrl.getText().toString().trim();
            if (url.isEmpty()) {
                Toast.makeText(getContext(), "Посилання не містить даних!", Toast.LENGTH_LONG).show();
                return;
            }

            ArrayList<AudioModel> tempList = new ArrayList<>();
            tempList.add(new AudioModel("Онлайн аудіо","Інтернет","0", url,null));

            AudioDataHolder.setAudioList(tempList);

            Intent intent = new Intent(getContext(), AudioPlayerActivity.class);
            intent.putExtra("CURRENT_INDEX", 0);
            startActivity(intent);
        });
    }

    private void loadVideoUrl(View view) {
        LinearLayout videoContainer = view.findViewById(R.id.url_video_container);
        EditText videoUrl = view.findViewById(R.id.url_input_video);
        Button videoButton = view.findViewById(R.id.play_url_video);

        videoContainer.setVisibility(View.VISIBLE);

        videoButton.setOnClickListener(v -> {
            String url = videoUrl.getText().toString().trim();
            if (url.isEmpty()) {
                Toast.makeText(getContext(), "Посилання не містить даних!", Toast.LENGTH_LONG).show();
                return;
            }

            VideoModel videoModel = new VideoModel("Онлайн відео", "0", url,null);

            Intent intent = new Intent(getContext(), VideoPlayerActivity.class);
            intent.putExtra("PATH", videoModel.getPath());
            startActivity(intent);
        });
    }
}

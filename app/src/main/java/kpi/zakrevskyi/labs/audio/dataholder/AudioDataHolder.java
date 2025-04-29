package kpi.zakrevskyi.labs.audio.dataholder;

import java.util.ArrayList;

import kpi.zakrevskyi.labs.audio.model.AudioModel;

public class AudioDataHolder {
    private static ArrayList<AudioModel> audioList;

    public static ArrayList<AudioModel> getAudioList() {
        return audioList;
    }

    public static void setAudioList(ArrayList<AudioModel> list) {
        audioList = list;
    }
}

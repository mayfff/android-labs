package kpi.zakrevskyi.labs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class OutputFragment extends Fragment {
    private final static String ARG_TEXT = "result_text";

    public static OutputFragment newInstance(String text) {
        OutputFragment fragment = new OutputFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_output, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textViewOrderInfo = view.findViewById(R.id.textViewOrderInfo);
        Button buttonClose = view.findViewById(R.id.buttonClose);

        if (getArguments() != null) {
            textViewOrderInfo.setText(getArguments().getString(ARG_TEXT));
        }

        buttonClose.setOnClickListener(v -> {
            ((MainActivity) getActivity()).clearForm();
        });
    }
}
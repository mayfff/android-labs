package kpi.zakrevskyi.labs.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.skydoves.powerspinner.PowerSpinnerView;

import kpi.zakrevskyi.labs.R;

public class SpinnerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spinner, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        String selectedArgs = args.getString("selectedType", "audio");

        PowerSpinnerView fileSpinner = view.findViewById(R.id.file_spinner);

        fileSpinner.setOnSpinnerItemSelectedListener((spinnerView, position, selectedItemText, selectedText) -> {
            Fragment fragmentToLoad = null;
            Bundle bundle = new Bundle();
            bundle.putString("selectedItem", selectedArgs);

            if (selectedText.equals("Внутрішнє сховище")) {
                fragmentToLoad = new InternalFragment();
            } else if (selectedText.equals("Зовнішнє сховище")) {
                fragmentToLoad = new ExternalFragment();
            } else if (selectedText.equals("З інтернету")) {
                fragmentToLoad = new InternetFragment();
            }

            if (fragmentToLoad != null) {
                fragmentToLoad.setArguments(bundle);
                replaceFragment(fragmentToLoad);
            }
        });
        fileSpinner.post(() -> {
            fileSpinner.selectItemByIndex(0);
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        PowerSpinnerView fileSpinner = getView().findViewById(R.id.file_spinner);
        if (fileSpinner != null) {
            fileSpinner.dismiss();
        }
    }
}

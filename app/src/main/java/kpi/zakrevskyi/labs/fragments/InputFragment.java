package kpi.zakrevskyi.labs.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import kpi.zakrevskyi.labs.MainActivity;
import kpi.zakrevskyi.labs.R;


public class InputFragment extends Fragment {

    private EditText editTextInfo;
    private TextView textViewOrderIssues;
    private CheckBox checkBoxMargarita, checkBoxPepperoni;
    private RadioGroup radioGroupMargaritaSize, radioGroupPepperoniSize;
    private Button buttonOrder;

    private Button buttonOrdersList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextInfo = view.findViewById(R.id.editTextName);
        textViewOrderIssues = view.findViewById(R.id.textViewOrderIssues);
        checkBoxMargarita = view.findViewById(R.id.checkBoxMargarita);
        checkBoxPepperoni = view.findViewById(R.id.checkBoxPepperoni);
        radioGroupMargaritaSize = view.findViewById(R.id.radioGroupMargaritaSize);
        radioGroupPepperoniSize = view.findViewById(R.id.radioGroupPepperoniSize);
        buttonOrder = view.findViewById(R.id.buttonOrder);
        buttonOrdersList = view.findViewById(R.id.buttonOrdersList);

        buttonOrder.setOnClickListener(v -> {
            String info = editTextInfo.getText().toString();
            StringBuilder order = new StringBuilder();
            String margaritaSize = "Відсутня";
            String peperoniSize = "Відсутня";

            if (info.isEmpty()) {
                textViewOrderIssues.setText("Будь ласка, введіть інформацію про замовлення.");
                return;
            }

            if (!checkBoxPepperoni.isChecked() && !checkBoxMargarita.isChecked()) {
                textViewOrderIssues.setText("Будь ласка, оберіть тип піци.");
                return;
            }

            if (checkBoxMargarita.isChecked()) {
                order.append("Тип піци: Маргарита. ");
                int margaritaSelectedSizeId = radioGroupMargaritaSize.getCheckedRadioButtonId();
                if (margaritaSelectedSizeId == -1) {
                    textViewOrderIssues.setText("Будь ласка, оберіть розмір для маргарити.");
                    return;
                }
                RadioButton margaritaSelectedSize = view.findViewById(margaritaSelectedSizeId);
                margaritaSize = margaritaSelectedSize.getText().toString();
                order.append("Розмір: ").append(margaritaSize).append("\n");
            }

            if (checkBoxPepperoni.isChecked()) {
                order.append("Тип піци: Пепероні. ");
                int peperoniSelectedSizeId = radioGroupPepperoniSize.getCheckedRadioButtonId();
                if (peperoniSelectedSizeId == -1) {
                    textViewOrderIssues.setText("Будь ласка, оберіть розмір для пепероні.");
                    return;
                }
                RadioButton peperoniSelectedSize = view.findViewById(peperoniSelectedSizeId);
                peperoniSize = peperoniSelectedSize.getText().toString();
                order.append("Розмір: ").append(peperoniSize).append("\n");
            }

            order.append("Дякуємо за замовлення!");
            ((MainActivity) getActivity()).onResult(order.toString(), info, margaritaSize, peperoniSize);
        });

        buttonOrdersList.setOnClickListener(v -> ((MainActivity) getActivity()).openOrdersActivity());
    }
}
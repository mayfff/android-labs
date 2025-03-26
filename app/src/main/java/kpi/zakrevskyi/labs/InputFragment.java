package kpi.zakrevskyi.labs;
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



public class InputFragment extends Fragment {

    private EditText editTextInfo;
    private TextView textViewOrderIssues;
    private CheckBox checkBoxMargarita, checkBoxPepperoni;
    private RadioGroup radioGroupMargaritaSize, radioGroupPepperoniSize;
    private Button buttonOrder;

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

        buttonOrder.setOnClickListener(v -> {
            String info = editTextInfo.getText().toString();
            StringBuilder orderDetails = new StringBuilder();

            if (info.isEmpty()) {
                textViewOrderIssues.setText("Будь ласка, введіть інформацію про замовлення.");
                return;
            }

            orderDetails.append("Інформація про замовлення:\n").append(info).append("\n");

            if (!checkBoxPepperoni.isChecked() && !checkBoxMargarita.isChecked()) {
                textViewOrderIssues.setText("Будь ласка, оберіть тип піци.");
                return;
            }

            if (checkBoxMargarita.isChecked()) {
                orderDetails.append("Тип піци: Маргарита. ");
                int margaritaSelectedSizeId = radioGroupMargaritaSize.getCheckedRadioButtonId();
                if (margaritaSelectedSizeId == -1) {
                    textViewOrderIssues.setText("Будь ласка, оберіть розмір для маргарити.");
                    return;
                }
                RadioButton margaritaSelectedSize = view.findViewById(margaritaSelectedSizeId);
                orderDetails.append("Розмір: ").append(margaritaSelectedSize.getText().toString()).append("\n");
            }

            if (checkBoxPepperoni.isChecked()) {
                orderDetails.append("Тип піци: Пепероні. ");
                int peperoniSelectedSizeId = radioGroupPepperoniSize.getCheckedRadioButtonId();
                if (peperoniSelectedSizeId == -1) {
                    textViewOrderIssues.setText("Будь ласка, оберіть розмір для пепероні.");
                    return;
                }
                RadioButton peperoniSelectedSize = view.findViewById(peperoniSelectedSizeId);
                orderDetails.append("Розмір: ").append(peperoniSelectedSize.getText().toString()).append("\n");
            }

            orderDetails.append("Дякуємо за замовлення!");
            ((MainActivity) getActivity()).onResult(orderDetails.toString());
        });

    }
}
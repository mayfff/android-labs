package kpi.zakrevskyi.labs;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText editTextInfo;
    private TextView textViewOrderInfo;
    private CheckBox checkBoxMargarita, checkBoxPepperoni;
    private RadioGroup radioGroupMargaritaSize, radioGroupPepperoniSize;
    private Button buttonOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextInfo = findViewById(R.id.editTextName);
        textViewOrderInfo = findViewById(R.id.textViewOrderInfo);
        checkBoxMargarita = findViewById(R.id.checkBoxMargarita);
        checkBoxPepperoni = findViewById(R.id.checkBoxPepperoni);
        radioGroupMargaritaSize = findViewById(R.id.radioGroupMargaritaSize);
        radioGroupPepperoniSize = findViewById(R.id.radioGroupPepperoniSize);
        buttonOrder = findViewById(R.id.buttonOrder);

        buttonOrder.setOnClickListener(v -> placeOrder());
    }

    private void placeOrder() {
        String info = editTextInfo.getText().toString();
        StringBuilder orderDetails = new StringBuilder();

        if (info.isEmpty()) {
            textViewOrderInfo.setText("Будь ласка, введіть інформацію про замовлення.");
            return;
        }

        orderDetails.append("Інформація про замовлення:\n").append(info).append("\n");

        if (!checkBoxPepperoni.isChecked() && !checkBoxMargarita.isChecked()) {
            textViewOrderInfo.setText("Будь ласка, оберіть тип піци.");
            return;
        }

        if (checkBoxMargarita.isChecked()) {
            orderDetails.append("Тип піци: Маргарита. ");
            int margaritaSelectedSizeId = radioGroupMargaritaSize.getCheckedRadioButtonId();
            if (margaritaSelectedSizeId == -1) {
                textViewOrderInfo.setText("Будь ласка, оберіть розмір для маргарити.");
                return;
            }
            RadioButton margaritaSelectedSize = findViewById(margaritaSelectedSizeId);
            orderDetails.append("Розмір: ").append(margaritaSelectedSize.getText().toString()).append("\n");
        }

        if (checkBoxPepperoni.isChecked()) {
            orderDetails.append("Тип піци: Пепероні. ");
            int peperoniSelectedSizeId = radioGroupPepperoniSize.getCheckedRadioButtonId();
            if (peperoniSelectedSizeId == -1) {
                textViewOrderInfo.setText("Будь ласка, оберіть розмір для пепероні.");
                return;
            }
            RadioButton peperoniSelectedSize = findViewById(peperoniSelectedSizeId);
            orderDetails.append("Розмір: ").append(peperoniSelectedSize.getText().toString()).append("\n");
        }

        orderDetails.append("Дякуємо за замовлення!");
        textViewOrderInfo.setText(orderDetails.toString());
    }
}
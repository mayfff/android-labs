package kpi.zakrevskyi.labs;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class OrdersActivity extends AppCompatActivity {

    private static final String FILE_NAME = "orders.txt";
    private TableLayout tableLayout;
    private Button buttonClear, buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        buttonBack = findViewById(R.id.buttonBack);
        buttonClear = findViewById(R.id.buttonClear);
        tableLayout = findViewById(R.id.tableLayout);

        loadData();

        buttonClear.setOnClickListener(v -> clearFile());

        buttonBack.setOnClickListener(v -> finish());
    }

    private void loadData() {
        File file = new File(getFilesDir(), FILE_NAME);
        tableLayout.removeAllViews();

        if (!file.exists()) {
            showEmptyMessage();
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            writeTableHeader();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                addTableRow(parts[0], parts[1], parts[2]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeTableHeader() {
        TableRow row = new TableRow(this);

        TextView orderInfo = new TextView(this);
        orderInfo.setText("Адреса");
        orderInfo.setTextSize(20);
        orderInfo.setTypeface(Typeface.DEFAULT_BOLD);
        row.addView(orderInfo);

        TextView margaritaSize = new TextView(this);
        margaritaSize.setText("Маргарита");
        margaritaSize.setTextSize(20);
        margaritaSize.setTypeface(Typeface.DEFAULT_BOLD);
        row.addView(margaritaSize);

        TextView peperoniSize = new TextView(this);
        peperoniSize.setText("Пепероні");
        peperoniSize.setTextSize(20);
        peperoniSize.setTypeface(Typeface.DEFAULT_BOLD);
        row.addView(peperoniSize);

        tableLayout.addView(row);
    }

    private void addTableRow(String part, String margaritaSize, String peperoniSize) {
        TableRow row = new TableRow(this);

        TextView orderInfo = new TextView(this);
        orderInfo.setText(part);
        orderInfo.setTextSize(18);
        row.addView(orderInfo);

        TextView margarita = new TextView(this);
        margarita.setText(margaritaSize);
        margarita.setTextSize(18);
        row.addView(margarita);

        TextView peperoni = new TextView(this);
        peperoni.setText(peperoniSize);
        peperoni.setTextSize(18);
        row.addView(peperoni);

        tableLayout.addView(row);
    }

    private void clearFile() {
        File file = new File(getFilesDir(), FILE_NAME);

        if (file.exists()) {
            file.delete();
            Toast.makeText(this, "Всі замовлення видалені", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Сховище замовлень порожнє", Toast.LENGTH_SHORT).show();
        }

        tableLayout.removeAllViews();
        showEmptyMessage();
    }


    private void showEmptyMessage() {
        TextView message = new TextView(this);
        message.setText("Замовлень немає");
        message.setTextSize(20);
        message.setGravity(Gravity.CENTER);
        tableLayout.removeAllViews();
        tableLayout.addView(message);
    }
}

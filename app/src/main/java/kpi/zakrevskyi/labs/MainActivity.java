package kpi.zakrevskyi.labs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;

import kpi.zakrevskyi.labs.fragments.InputFragment;
import kpi.zakrevskyi.labs.fragments.OutputFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new InputFragment())
                    .commit();
        }
    }

    public void onResult(String text, String info, String margaritaSize, String peperoniSize) {
        saveToFile(info, margaritaSize, peperoniSize);

        OutputFragment outputFragment = OutputFragment.newInstance(text);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, outputFragment)
                .addToBackStack(null)
                .commit();
    }

    public void clearForm() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new InputFragment())
                .commit();
    }

    private void saveToFile(String info, String margaritaSize, String peperoniSize) {
        String filename = "orders.txt";
        String data = info + ";" + margaritaSize + ";" + peperoniSize + "\n";
        Log.d("test", data);

        try (FileOutputStream fos = openFileOutput(filename, Context.MODE_APPEND)) {
            fos.write(data.getBytes());
            fos.flush();
            Toast.makeText(this, "Замовлення збережено", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Помилка при збереженні змовлення", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void openOrdersActivity() {
        Intent intent = new Intent(this, OrdersActivity.class);
        startActivity(intent);
    }
}
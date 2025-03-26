package kpi.zakrevskyi.labs;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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

   public void onResult(String text) {
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
}
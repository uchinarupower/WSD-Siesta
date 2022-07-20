package jp.ac.titech.itpro.sdl.siesta;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import jp.ac.titech.itpro.sdl.siesta.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MyNumberPicker np_hour, np_min;
    private Button btn_start;
    private int input_hour, input_minute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        findViews();
        initViews();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    private void findViews(){
        np_hour = findViewById(R.id.number_picker_h);
        np_min = findViewById(R.id.number_picker_m);
        btn_start = findViewById(R.id.btn_start);
    }

    private void initViews(){
        np_hour.setWrapSelectorWheel(true);
        np_hour.setMaxValue(23);
        np_hour.setMinValue(0);
        np_min.setWrapSelectorWheel(true);
        np_min.setMaxValue(59);
        np_min.setMinValue(0);
        np_min.setValue(30);

        btn_start.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                input_hour = np_hour.getValue();
                input_minute = np_min.getValue();
                //Log.d("hour", String.valueOf(input_hour));
                //Log.d("minute", String.valueOf(input_minute));
            }
        });
    }




}
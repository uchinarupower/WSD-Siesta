package jp.ac.titech.itpro.sdl.siesta;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import jp.ac.titech.itpro.sdl.siesta.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NumberPicker np_hour, np_min;
    private TextView textv_h, textv_m;

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
        //textView1 = (TextView)findViewById(R.id.textView1);
    }

    private void initViews(){
        np_hour.setWrapSelectorWheel(true);
        np_hour.setMaxValue(23);
        np_hour.setMinValue(0);
        np_min.setWrapSelectorWheel(true);
        np_min.setMaxValue(59);
        np_min.setMinValue(0);

        /*button1.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                textView1.setText(
                        numPicker.getValue() + "");
            }
        });*/
    }




}
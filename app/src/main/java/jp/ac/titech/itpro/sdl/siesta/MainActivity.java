package jp.ac.titech.itpro.sdl.siesta;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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

    final static String TAG = "Main";

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

                Intent intent_a = new Intent(MainActivity.this, sensingAlarmOperator.class);
                startService(intent_a);

                Intent intent_s = new Intent(MainActivity.this, SubActivity.class);
                intent_s.putExtra("HOUR", input_hour);
                intent_s.putExtra("MINUTE", input_minute);
                startActivity(intent_s);

                Log.d(TAG, "call Alarm");

                //runAlarm(input_hour, input_minute);

                Log.d(TAG, String.valueOf(input_hour));
                Log.d(TAG, String.valueOf(input_minute));
            }
        });
    }

    /*private void runAlarm(){
        // ブロードキャストリスナー
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action != null) {
                    if (action.equals(Intent.ACTION_SCREEN_ON)) {
                        // 画面ON時
                        Log.d(TAG, "SCREEN_ON");
                    } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                        // 画面OFF時
                        Log.d(TAG, "SCREEN_OFF");
                    }
                }
            }
        };
    }*/




}
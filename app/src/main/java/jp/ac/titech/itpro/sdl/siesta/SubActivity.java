package jp.ac.titech.itpro.sdl.siesta;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SubActivity extends AppCompatActivity {
    final static String TAG = "subActivity";
    private TextView text_timer;
    private long setting_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Button btn_stop = findViewById(R.id.btn_stop);
        text_timer = findViewById(R.id.timer);

        Intent i = getIntent();
        int input_hour = i.getIntExtra("HOUR", 0);
        int input_minute = i.getIntExtra("MINUTE", 0);
        // 0埋め
        text_timer.setText(String.format("%02d", new Integer(input_hour)) + ":" + String.format("%02d", new Integer(input_minute)));
        // ミリ秒に変換
        setting_time = (long) ((long)input_hour*60.0*60.0*1000.0 + (long)input_minute*60*1000.0);

        // receiver
        UpdateReceiver receiver = new UpdateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("USER_IS_SLEEPING");
        registerReceiver(receiver, filter);


        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            Bundle i = intent.getExtras();
            Boolean mIsSleeping = i.getBoolean("SLEEP");
            if (mIsSleeping){
                Log.d(TAG, "RUN TIMER");
                //startTimer();
            }

        }
    }
}

package jp.ac.titech.itpro.sdl.siesta;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class SubActivity extends AppCompatActivity {
    final static String TAG = "subActivity";
    private TextView text_timer;
    private long setting_time = 0;
    private long milliTimeLeft = 0;
    private CountDownTimer timer;
    private UpdateReceiver receiver;
    private IntentFilter filter;
    private Intent intent_a;
    private Uri uri;
    private MediaPlayer player;


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
        //text_timer.setText(String.format("%02d", new Integer(input_hour)) + ":" + String.format("%02d", new Integer(input_minute)));String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        text_timer.setText(String.format(Locale.getDefault(), "%02d:%02d:00", input_hour, input_minute));
        // ミリ秒に変換
        setting_time = (long) ((long)input_hour*60.0*60.0*1000.0 + (long)input_minute*60*1000.0);
        milliTimeLeft = setting_time;

        // receiver
        receiver = new UpdateReceiver();
        filter = new IntentFilter();
        filter.addAction("USER_IS_SLEEPING");

        // start sensingAlarmOperator
        intent_a = new Intent(getApplication(), sensingAlarmOperator.class);
        startService(intent_a);

        // sound
        player = MediaPlayer.create(this, R.raw.audio);
        player.setLooping(true);
        //player.setAudioStreamType(AudioManager.STREAM_ALARM);
        // 音量調整を端末のボタンに任せる
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        //player.start();



        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setting_time = 0;
                //milliTimeLeft = 0;
                //stopService(intent_a);
                if (player != null){
                    audioStop();
                }
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // ブロードキャストレシーバーを登録する。
        registerReceiver(receiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }


    private void startTimer(){
        timer = new CountDownTimer(milliTimeLeft,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                milliTimeLeft = millisUntilFinished;
                int hour = (int)(milliTimeLeft/1000)/(60*60);
                int minutes = (int)(milliTimeLeft/1000)/60 - hour*60;
                int seconds = (int)(milliTimeLeft/1000) - hour*60*60 - minutes*60;
                String timerLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minutes, seconds);
                text_timer.setText(timerLeftFormatted);
            }

            @Override
            public void onFinish() {
                text_timer.setText("00:00:00");
                // audio
                Log.d(TAG, "SOUND");
                audioPlay();
            }
        }.start();
    }

    private void audioPlay(){
        player.start();
    }

    private void audioStop(){
        player.stop();
        player.reset();
        player.release();
        player = null;
    }

    protected class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            Bundle i = intent.getExtras();
            Boolean mIsSleeping = i.getBoolean("SLEEP");
            if (mIsSleeping){
                Log.d(TAG, "RUN TIMER");
                //unregisterReceiver(receiver);
                //stopService(intent_a);
                startTimer();
            }

        }
    }
}

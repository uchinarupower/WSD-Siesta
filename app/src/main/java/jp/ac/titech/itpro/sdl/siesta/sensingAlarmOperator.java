package jp.ac.titech.itpro.sdl.siesta;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class sensingAlarmOperator extends Service implements SensorEventListener {
    final static String TAG = "sensingAlarm";
    final int INTERVAL_PERIOD = 6000; // 0.1min
    Timer timer = new Timer();

    private BroadcastReceiver screenStatusReceiver;
    private boolean mIsScreenOn = true;

    private  SensorEventListener listener;
    private SensorManager manager;
    Sensor rotation_vector, light;
    private float[] r_vector_value = new float[3];
    private float[] last_r_vector_value = new float[3];
    private float light_value;
    private  float diff_r = 0;
    final static double SCORE_THRESHOLD = 30;

    /*public sensingAlarm() {
        super("sensingAlarm");
    }*/

    /*@Override
    protected void onHandleIntent(Intent intent) {
        try {
            Log.d(TAG, "onHandleIntent");
            Thread.sleep(5000);
            timer.scheduleAtFixedRate(new TimerTask(){
                @Override
                public void run() {
                    Log.d(TAG, "Hello!");
                }
            }, 0, INTERVAL_PERIOD);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        // BroadcastReceiver
        screenStatusReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // screen off
                if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                    Log.d(TAG, "SCREEN OFF");
                    mIsScreenOn = false;
                }
                // screen on
                if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                    Log.d(TAG, "SCREEN ON");
                    mIsScreenOn = true;
                }
            }
        };

        // screen ON/OFF
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(screenStatusReceiver, filter);

        // sensor
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (manager == null) {
            Toast.makeText(this, "No sensor manager available", Toast.LENGTH_LONG).show();
            return;
        }
        rotation_vector = manager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if (rotation_vector == null) {
            Toast.makeText(this, "No rotation vector available", Toast.LENGTH_LONG).show();
        }
        light = manager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (light == null) {
            Toast.makeText(this, "No light available", Toast.LENGTH_LONG).show();
        }

        // sensorの起動
        // 1分ごとの判定なのでそんな更新周期が早くなくていい
        // LIGHTは値が更新される度なので、呼ばれないことも多々ある
        manager.registerListener(this, rotation_vector, SensorManager.SENSOR_DELAY_NORMAL);
        manager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                Log.d(TAG, "TIMER LOOP");
                double sleep_score = calcSleepScore();
                if (sleep_score < SCORE_THRESHOLD){
                    // alarm start
                    Intent intent = new Intent(getApplication(),SubActivity.class);
                    startActivity(intent);
                }
                Log.d(TAG, "SLEEP SCORE: " + String.valueOf(sleep_score));
                Log.d(TAG, "SLEEP SCORE: " + String.valueOf(sleep_score));
                diff_r = 0;
            }
        }, 0, INTERVAL_PERIOD);

        // serviceが落ちた時再起動する
        // 終了前のintentが保持されていてonStartCommandに再度渡される
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer != null){
            timer.cancel();
        }
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float omegaZ = event.values[2];  // z-axis angular velocity (rad/sec)

        int sensor_type = event.sensor.getType();
        if (sensor_type == Sensor.TYPE_ROTATION_VECTOR){
            Log.d(TAG, "GET ROTATION_VECTOR x: "+ String.valueOf(event.values[0]) + ", y: " + String.valueOf(event.values[1]) + ", z: " + String.valueOf(event.values[2]));
            r_vector_value[0] = event.values[0];
            r_vector_value[1] = event.values[1];
            r_vector_value[2] = event.values[2];
            diff_r += Math.abs(last_r_vector_value[0] - event.values[0]) + Math.abs(last_r_vector_value[1] - event.values[1]) + Math.abs(last_r_vector_value[2] - event.values[2]);
        }
        else if(sensor_type == Sensor.TYPE_LIGHT){
            Log.d(TAG, "GET LIGHT: " + String.valueOf(event.values[0]) );
            light_value = event.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "onAccuracyChanged: accuracy=" + accuracy);
    }

    private double calcSleepScore(){
        // diff_r : 閾値 15くらい
        // light_value : 閾値 15くらい
        // mIsScreenOn : trueなら20を返す falseなら0 (lightのmin10を足して大体threshold 30になるくらい = 画面付いてるならほんとに端末が止まってないとダメ)
        double sleep_score = 0;
        if (mIsScreenOn){
            sleep_score = diff_r + light_value + 20.0;
        }
        else{
            sleep_score = diff_r + light_value;
        }
        return sleep_score;



    }





    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}

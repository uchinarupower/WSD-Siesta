package jp.ac.titech.itpro.sdl.siesta;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class sensingAlarm extends Service {
    final static String TAG = "sensingAlarm";
    final int INTERVAL_PERIOD = 6000; // 0.1min
    Timer timer = new Timer();

    private BroadcastReceiver screenStatusReceiver;
    private boolean mIsScreenOn = true;

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

        // screen
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(screenStatusReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                Log.d(TAG, "loop start");

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

    private void startAlarm(){

    }


    private boolean sleepCheck(){
        return true;
    }

    private float calcSleepScore(){
        return 0;
    }





    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}

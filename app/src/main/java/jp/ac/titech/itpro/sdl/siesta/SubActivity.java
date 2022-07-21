package jp.ac.titech.itpro.sdl.siesta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SubActivity extends AppCompatActivity {
    private TextView text_timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Button btn_stop = findViewById(R.id.btn_stop);
        text_timer = findViewById(R.id.timer);

        Intent i = getIntent();
        int input_hour = i.getIntExtra("HOUR", 0);
        int input_minute = i.getIntExtra("MINUTE", 0);
        text_timer.setText(String.format("%02d", new Integer(input_hour)) + ":" + String.format("%02d", new Integer(input_minute)));


        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

package io.github.dvegasa.arcpointer_sample;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.github.dvegasa.arcpointer.ArcPointer;

public class SampleActivity extends AppCompatActivity {
    ArcPointer arcPointer;
    TextView centerTextView;
    Button btnValue;
    EditText etNotchReading;
    int notchReading = 0;
    final int delay = 20; // MILLI SECONDS
    int maxNotchReading = 0;
    public static final int NOTCH_COUNT = 33;
    public static final int RADIUS = 170;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        initViews();
        maxNotchReading = 100;
    }

    private void initViews() {
        arcPointer = (ArcPointer) findViewById(R.id.arcpointer);
        btnValue = (Button) findViewById(R.id.btnValue);
        etNotchReading = (EditText) findViewById(R.id.etNotchReading);
        centerTextView = (TextView) findViewById(R.id.tvCenterText);
        setCenterText("Value");
        btnValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    notchReading = 0;
                    int notchReading = Integer.parseInt(etNotchReading.getText().toString());
                    setNotchReading(notchReading);
                    setAnimation();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        arcPointer.setNotches(NOTCH_COUNT);
        arcPointer.setRadius(RADIUS);
        arcPointer.setGaugeType(ArcPointer.TYPE_DEFAULT_GAUGE);
    }

    public void setCenterText(String centerText) {
        centerTextView.setText(centerText);
    }

    public void setNotchReading(int notchReading) {
        this.maxNotchReading = notchReading;
    }

    private void setAnimation() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (notchReading < maxNotchReading) {
                    float value = 0.01f * notchReading;
                    arcPointer.setValue(value);
                    notchReading++;
                    handler.postDelayed(this, delay);
                }
            }
        };
        handler.postDelayed(runnable, 10);

        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        aniFade.setDuration(delay * maxNotchReading);
        centerTextView.startAnimation(aniFade);
    }

}

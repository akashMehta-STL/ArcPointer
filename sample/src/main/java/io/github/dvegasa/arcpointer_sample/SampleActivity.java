package io.github.dvegasa.arcpointer_sample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.github.dvegasa.arcpointer.ArcPointer;

public class SampleActivity extends AppCompatActivity {
    ArcPointer arcPointer;
    TextView centerTextView;
    Button btnValue;
    EditText etNotchReading;
    int notchReading = 0;
    final int delay = 20; // MILLI SECONDS
    int maxNotchReading = 0;
    private int min = 2;
    private int max = 20;
    private int gaugeType;
    public static final int NOTCH_COUNT = 33;
    public static final int RADIUS = 170;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        initViews();
        arcPointer.setValue(0);
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
                notchReading = 0;
                try {
                    int notchReading = Integer.parseInt(etNotchReading.getText().toString());
                    if (notchReading < min || notchReading > 20) {
                        Toast.makeText(SampleActivity.this, "Please Enter valid value", Toast.LENGTH_SHORT).show();
                    } else {
                        if (gaugeType != ArcPointer.TYPE_TWO_MARKER_GAUGE) {
                            int position = (int) getValuePos(min, max, notchReading);
                            setNotchReading(position);
                        } else {
                            int position = (int) getValuePos(min, max, max);
                            setNotchReading(position);
                        }

                        setAnimation();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        arcPointer.setNotches(NOTCH_COUNT);
        arcPointer.setRadius(RADIUS);

        gaugeType = ArcPointer.TYPE_TWO_MARKER_GAUGE;
        arcPointer.setGaugeType(gaugeType);
        // Ranges
        float[] parameterRange = new float[2];
        parameterRange[0] = 9;
        parameterRange[1] = 14;
        arcPointer.setGaugeMeterRange(getGaugeMeterRange(min, max, parameterRange));

        // Single part gauge
//        arcPointer.setGaugeType(ArcPointer.TYPE_SINGLE_PART_GAUGE);

        // Three part gauge
//        arcPointer.setGaugeType(ArcPointer.TYPE_DEFAULT_GAUGE);
//        // Ranges
//        float[] parameterRange = new float[2];
//        parameterRange[0] = 6;
//        parameterRange[1] = 9;
//        arcPointer.setGaugeMeterRange(getGaugeMeterRange(min, max, parameterRange));

        // Four part gauge
//        arcPointer.setGaugeType(ArcPointer.TYPE_FOUR_PART_GAUGE);
//        // Ranges
//        float[] parameterRange = new float[3];
//        parameterRange[0] = 4;
//        parameterRange[1] = 7;
//        parameterRange[2] = 15;
//        arcPointer.setGaugeMeterRange(getGaugeMeterRange(min, max, parameterRange));
    }

    private float getValuePos(float min, float max, float value) {
        float range = max - min;
        float outPercent = ((value - min) * 100) / range;
        return outPercent;
    }

    private float[] getGaugeMeterRange(float min, float max, float rangeAr[]) {
        float range = max - min;
        float[] outputRange = new float[rangeAr.length];
        for (int i = 0; i < rangeAr.length; i++) {
            float outPercent = (rangeAr[i] * 100) / range;
            outputRange[i] = (NOTCH_COUNT * outPercent) / 100;
        }
        return outputRange;
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

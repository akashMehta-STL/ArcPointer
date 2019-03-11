package io.github.dvegasa.arcpointer_sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.github.dvegasa.arcpointer.ArcHelper;
import io.github.dvegasa.arcpointer.ArcPointer;

public class SampleActivity extends AppCompatActivity {
    ArcPointer arcPointer;
    TextView centerTextView;
    Button btnValue;
    EditText etNotchReading;
    private int min = 2;
    private int max = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        initViews();
        arcPointer.setValue(0);
    }

    private void initViews() {
        arcPointer = findViewById(R.id.arcpointer);
        btnValue = findViewById(R.id.btnValue);
        etNotchReading = findViewById(R.id.etNotchReading);
        centerTextView = findViewById(R.id.tvCenterText);
        btnValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int notchReading = Integer.parseInt(etNotchReading.getText().toString());
                    if (notchReading < min || notchReading > max) {
                        Toast.makeText(SampleActivity.this, "Please Enter valid value", Toast.LENGTH_SHORT).show();
                    } else {
//
                        // TWO marker gauge
//                        float[] parameterRange = new float[]{6, 9};
//                        ArcHelper.getTwoMarkerGuage(SampleActivity.this, max, min,
//                                "#FF0000", parameterRange, notchReading)
//                                .setArcPointer(arcPointer)
//                                .setCenterTextView(centerTextView, "120/20")
//                                .startAnimation();

                        // Three part gauge
                        // Ranges
//                        centerTextView.setText("120/20");
//                        float[] parameterRange = new float[]{6, 9};
//                        String[] colorRange = new String[]{"#ff44dc91", "#fff48918", "#FFfe5965"};
//                        ArcHelper.getThreePartArc(max, min,
//                                parameterRange, colorRange, notchReading)
//                                .setArcPointer(arcPointer)
//                                .setCenterView(centerTextView)
//                                .startAnimation();

                        // four part gauge
                        // Ranges
                        centerTextView.setText("120/20");
                        float[] parameterRange = new float[]{6, 9, 11};
                        String[] colorRange = new String[]{"#ff44dc91", "#fff48918", "#FFfe5965", "#FF16a4fa"};
                        ArcHelper.getFourPartGaugeMeter(max, min,
                                parameterRange, colorRange, notchReading)
                                .setContext(SampleActivity.this)
                                .setArcPointer(arcPointer)
                                .setCenterView(centerTextView)
                                .startAnimation();

//                        // Single part gauge
//                        centerTextView.setText("120/20");
//                        ArcHelper.getSinglePartArc(max, min, "#FF330033",
//                                notchReading)
//                                .setContext(SampleActivity.this)
//                                .setArcPointer(arcPointer)
//                                .setCenterView(centerTextView)
//                                .startAnimation();


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }


}

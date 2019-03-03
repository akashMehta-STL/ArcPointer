package io.github.dvegasa.arcpointer_sample;

import android.content.Context;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import io.github.dvegasa.arcpointer.ArcPointer;

class ArcHelper {

    static ArcHelper getInstance(Context context, ArcPointer arcPointer) {
        return new ArcHelper(context, arcPointer);
    }

    private ArcHelper(Context context, ArcPointer arcPointer) {
        this.context = context;
        this.arcPointer = arcPointer;
    }

    public static final int GAUGE_ANIMATION_DELAY = 20;
    public static final int GAUGE_TEXT_FADE_DELAY = 10;
    public static final int NOTCH_COUNT = 33;
    public static final int RADIUS = 170;
    private final Context context;
    private final ArcPointer arcPointer;

    private int totalRangeMin;
    private int totalRangeMax;
    private int gaugeType;
    private int notchReading = 0;
    private int maxNotchReading;
    private TextView centerTextView;

    private float[] rangeList;
    private String[] colorList;

    public void startAnimation() {
        notchReading = 0;
        arcPointer.setNotches(NOTCH_COUNT);
        arcPointer.setRadius(RADIUS);
        if (colorList != null) {
            arcPointer.setNotchesColors(colorList);
        }
        arcPointer.setGaugeMeterRange(getGaugeMeterRange(totalRangeMin, totalRangeMax, rangeList));

        try {
            if (gaugeType != ArcPointer.TYPE_TWO_MARKER_GAUGE) {
                int position = (int) getValuePos(totalRangeMin, totalRangeMax, notchReading);
                setNotchReading(position);
            } else {
                int position = (int) getValuePos(totalRangeMin, totalRangeMax, totalRangeMax);
                setNotchReading(position);
            }
            setAnimation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArcHelper setCenterTextView(TextView centerTextView, String centerText) {
        this.centerTextView = centerTextView;
        this.centerTextView.setText(centerText);
        return this;
    }

    public ArcHelper setTotalRangeMin(int totalRangeMin) {
        this.totalRangeMin = totalRangeMin;
        return this;
    }

    public ArcHelper setTotalRangeMax(int totalRangeMax) {
        this.totalRangeMax = totalRangeMax;
        return this;
    }

    public ArcHelper setGaugeType(int gaugeType) {
        this.gaugeType = gaugeType;
        return this;
    }

    public ArcHelper setNotchReading(int maxNotchReading) {
        this.maxNotchReading = maxNotchReading;
        return this;
    }

    public ArcHelper setRangeList(float[] rangeList) {
        this.rangeList = rangeList;
        return this;
    }

    public ArcHelper setColorList(String[] colorList) {
        this.colorList = colorList;
        return this;
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

    private void setAnimation() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (notchReading < maxNotchReading) {
                    float value = 0.01f * notchReading;
                    arcPointer.setValue(value);
                    notchReading++;
                    handler.postDelayed(this, GAUGE_ANIMATION_DELAY);
                }
            }
        };
        handler.postDelayed(runnable, GAUGE_TEXT_FADE_DELAY);

        Animation aniFade = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        aniFade.setDuration(GAUGE_ANIMATION_DELAY * maxNotchReading);
        if (centerTextView != null) {
            centerTextView.startAnimation(aniFade);
        }
    }
}

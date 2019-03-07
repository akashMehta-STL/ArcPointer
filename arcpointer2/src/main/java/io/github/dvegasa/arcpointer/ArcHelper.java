package io.github.dvegasa.arcpointer;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class ArcHelper {

    private static ArcHelper getInstance() {
        return new ArcHelper();
    }

    private ArcHelper() {
    }

    private static final int GAUGE_ANIMATION_DELAY = 20;
    private static final int GAUGE_TEXT_FADE_DELAY = 10;
    private static final int NOTCH_COUNT = 33;
    private static final int RADIUS = 170;
    private Context context;
    private ArcPointer arcPointer;
    private int totalRangeMin;
    private int totalRangeMax;
    private int gaugeType;
    private int notchReading = 0;
    private int maxNotchReading;
    private View centerView;

    private String defaultColor;
    private String singlePartColor;

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
        arcPointer.setDefaultColor(defaultColor);
        arcPointer.setSinglePartColor(singlePartColor);
        arcPointer.setGaugeType(gaugeType);

        try {
            if (gaugeType != ArcPointer.TYPE_TWO_MARKER_GAUGE) {
                int position = (int) getValuePos(totalRangeMin, totalRangeMax, maxNotchReading);
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

    public static ArcHelper getSinglePartArc(int max, int min,
                                             String singlePartColor,
                                             int notchReading) {
        return ArcHelper.getInstance()
                .setTotalRangeMax(max)
                .setTotalRangeMin(min)
                .setRangeList(new float[]{6})
                .setSinglePartColor(singlePartColor)
                .setNotchReading(notchReading)
                .setGaugeType(ArcPointer.TYPE_SINGLE_PART_GAUGE);
    }

    public static ArcHelper getThreePartArc(int max, int min,
                                            float[] parameterRange,
                                            String[] colorRange, int notchReading) {
        return ArcHelper.getInstance()
                .setTotalRangeMax(max)
                .setTotalRangeMin(min)
                .setRangeList(parameterRange)
                .setColorList(colorRange)
                .setNotchReading(notchReading)
                .setGaugeType(ArcPointer.TYPE_DEFAULT_GAUGE);
    }


    public static ArcHelper getTwoMarkerGuage(int max, int min,
                                              String singlePartColor,
                                              float[] parameterRange,
                                              int notchReading) {
        return ArcHelper.getInstance()
                .setTotalRangeMax(max)
                .setTotalRangeMin(min)
                .setRangeList(parameterRange)
                .setNotchReading(notchReading)
                .setSinglePartColor(singlePartColor)
                .setGaugeType(ArcPointer.TYPE_TWO_MARKER_GAUGE);
    }

    public static ArcHelper getFourPartGaugeMeter(int max, int min,
                                                  float[] parameterRange,
                                                  String[] colorRange, int notchReading) {
        return ArcHelper.getInstance()
                .setTotalRangeMax(max)
                .setTotalRangeMin(min)
                .setRangeList(parameterRange)
                .setColorList(colorRange)
                .setNotchReading(notchReading)
                .setGaugeType(ArcPointer.TYPE_FOUR_PART_GAUGE);
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

        if (context != null) {
            Animation aniFade = AnimationUtils.loadAnimation(context, R.anim.fade_in);
            aniFade.setDuration(GAUGE_ANIMATION_DELAY * maxNotchReading);
            if (centerView != null) {
                centerView.startAnimation(aniFade);
            }
        }
    }


    public ArcHelper setDefaultColor(String defaultColor) {
        this.defaultColor = defaultColor;
        return this;
    }

    public ArcHelper setSinglePartColor(String singlePartColor) {
        this.singlePartColor = singlePartColor;
        return this;
    }

    public ArcHelper setCenterView(View centerView) {
        this.centerView = centerView;
        return this;
    }

    public ArcHelper setContext(Context context) {
        this.context = context;
        return this;
    }

    public ArcHelper setArcPointer(ArcPointer arcPointer) {
        this.arcPointer = arcPointer;
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

    public int getTotalRangeMin() {
        return totalRangeMin;
    }

    public int getTotalRangeMax() {
        return totalRangeMax;
    }

    public int getNotchReading() {
        return notchReading;
    }


    private float getValuePos(float min, float max, float value) {
        return ((value - min) * 100) / (max - min);
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
}

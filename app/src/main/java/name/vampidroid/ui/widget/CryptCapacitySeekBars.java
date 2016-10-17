package name.vampidroid.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import name.vampidroid.R;

/**
 * Created by fxjr on 21/09/16.
 */

public class CryptCapacitySeekBars extends LinearLayout {

    private static final String TAG = "CryptCapacitySeekBars";
    SeekBar seekBarMin;
    SeekBar seekBarMax;
    SeekBar.OnSeekBarChangeListener externalSeekBarChangeListenerMin;
    SeekBar.OnSeekBarChangeListener externalSeekBarChangeListenerMax;
    TextView textMinValue;
    TextView textMaxValue;

    public CryptCapacitySeekBars(Context context) {
        super(context);
        init();
    }

    public CryptCapacitySeekBars(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CryptCapacitySeekBars(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CryptCapacitySeekBars(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        CryptCapacitySeekBars seekBars = (CryptCapacitySeekBars) inflater.inflate(R.layout.widget_crypt_capacity_seek_bars, this);

        seekBarMin = (SeekBar) seekBars.findViewById(R.id.seekBarCapacityMin);
        seekBarMax = (SeekBar) seekBars.findViewById(R.id.seekBarCapacityMax);

        //        Reference: http://stackoverflow.com/questions/18400910/seekbar-in-a-navigationdrawer

        OnTouchListener seekBarDisallowDrawerInterceptTouchEvent = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow Drawer to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow Drawer to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle seekbar touch events.
                v.onTouchEvent(event);
                return true;

            }
        };

        seekBarMin.setOnTouchListener(seekBarDisallowDrawerInterceptTouchEvent);
        seekBarMax.setOnTouchListener(seekBarDisallowDrawerInterceptTouchEvent);

        SeekBar.OnSeekBarChangeListener internalSeekBarChangeListenerMin = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textMinValue.setText(String.valueOf(getMinSeekBarValue()));
                if (seekBarMax.getProgress() < progress) {
                    seekBarMax.setProgress(progress);
                }

                if (externalSeekBarChangeListenerMin != null) {
                    externalSeekBarChangeListenerMin.onProgressChanged(seekBar, progress, fromUser);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (externalSeekBarChangeListenerMin != null) {
                    externalSeekBarChangeListenerMin.onStartTrackingTouch(seekBar);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (externalSeekBarChangeListenerMin != null) {
                    externalSeekBarChangeListenerMin.onStopTrackingTouch(seekBar);
                }

            }
        };

        SeekBar.OnSeekBarChangeListener internalSeekBarChangeListenerMax = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textMaxValue.setText(String.valueOf(getMaxSeekBarValue()));
                if (seekBarMin.getProgress() > progress) {
                    seekBarMin.setProgress(progress);
                }
                if (externalSeekBarChangeListenerMax != null) {
                    externalSeekBarChangeListenerMax.onProgressChanged(seekBar, progress, fromUser);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (externalSeekBarChangeListenerMax != null) {
                    externalSeekBarChangeListenerMax.onStartTrackingTouch(seekBar);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (externalSeekBarChangeListenerMax != null) {
                    externalSeekBarChangeListenerMax.onStopTrackingTouch(seekBar);
                }

            }
        };

        seekBarMin.setOnSeekBarChangeListener(internalSeekBarChangeListenerMin);
        seekBarMax.setOnSeekBarChangeListener(internalSeekBarChangeListenerMax);


        textMinValue = (TextView) findViewById(R.id.textSeekBarMinValue);
        textMaxValue = (TextView) findViewById(R.id.textSeekBarMaxValue);

    }

    public void setOnSeekBarChangeListenerMin(SeekBar.OnSeekBarChangeListener listener) {

        externalSeekBarChangeListenerMin = listener;
    }

    public void setOnSeekBarChangeListenerMax(SeekBar.OnSeekBarChangeListener listener) {
        externalSeekBarChangeListenerMax = listener;
    }

    public void reset() {
        seekBarMin.setProgress(0);
        seekBarMax.setProgress(seekBarMax.getMax());
    }


    public int getMinSeekBarValue() {
        return seekBarMin.getProgress() + 1; // zero based.
    }

    public int getMaxSeekBarValue() {
        return seekBarMax.getProgress() + 1; // zero based.
    }
}

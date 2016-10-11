package name.vampidroid.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import name.vampidroid.R;

/**
 * Created by fxjr on 10/10/16.
 */

public class BadgeTextView extends TextView {


    boolean autoShowHide = false;

    Animation showBadgeAnimation;
    Animation hideBadgeAnimation;

    public BadgeTextView(Context context) {
        super(context);
        init();

    }

    public BadgeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BadgeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BadgeTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();

    }


    private void init() {

        showBadgeAnimation = AnimationUtils.loadAnimation(this.getContext(), R.anim.scale_up);
        hideBadgeAnimation = AnimationUtils.loadAnimation(this.getContext(), R.anim.scale_down);

        setBackgroundResource(R.drawable.badge_background);

        int defaultPadding = dpToPx(5);
        setPadding(defaultPadding, 0, defaultPadding, 0);

        setTypeface(Typeface.DEFAULT_BOLD);
        setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
    }


    public void setNumericText(int value) {

        // If we are in auto mode, show and hide based on the positive value set.
        if (autoShowHide) {
            if (value > 0 && getVisibility() == GONE) {
                startAnimation(showBadgeAnimation);
                setVisibility(VISIBLE);
            }

            if (value == 0 ) {
                startAnimation(hideBadgeAnimation);
                setVisibility(GONE);
                return;
            }
        }

        setText(String.valueOf(value));
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }

    public void setAutoShowHide(boolean autoShowHide) {
        this.autoShowHide = autoShowHide;
    }


    private int dpToPx(int dip) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
        return (int) px;
    }
}

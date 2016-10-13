package name.vampidroid;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by franciscojunior on 06/04/2015.
 */
public class FixIssue59368RelativeLayout extends RelativeLayout {


    private static final int AT_MOST_UNSPECIFIED = MeasureSpec.makeMeasureSpec((1 << 30) - 1, MeasureSpec.AT_MOST);

    public FixIssue59368RelativeLayout(Context context) {
        super(context);
    }

    public FixIssue59368RelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixIssue59368RelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        // RelativeLayout has bugs when measured in UNSPECIFIED height mode that were supposedly fixed
        // in API 18. However the 'fix' does not work with breaks gravity == center_vertical in TextView.
        // https://code.google.com/p/android/issues/detail?id=63673
        if (MeasureSpec.getMode(heightSpec) == MeasureSpec.UNSPECIFIED)
            heightSpec = AT_MOST_UNSPECIFIED;
        super.onMeasure(widthSpec, heightSpec);
    }
}

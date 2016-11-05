package name.vampidroid.ui.widget;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.StringRes;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import name.vampidroid.R;

/**
 * Created by fxjr on 01/11/16.
 */

public class PersistentSearchBar extends FrameLayout {


    private DrawerArrowDrawable drawerArrowDrawable;
    private ImageView imageViewSearchSettingsButton;
    private TextView search_bar_text_view;
    private OnClickListener hamburgerClickListener;
    private OnClickListener searchSettingsClickListener;

    public PersistentSearchBar(Context context) {
        super(context);
        init();

    }

    public PersistentSearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PersistentSearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PersistentSearchBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private void init() {

        LayoutInflater.from(getContext()).inflate(R.layout.persistent_search_bar, this);

        drawerArrowDrawable = new DrawerArrowDrawable(getContext());

        setupSearchContainter();


    }


    private void setupSearchContainter() {

        final ImageView imageViewLeftAction = (ImageView) findViewById(R.id.left_action);
        search_bar_text_view = (TextView) findViewById(R.id.search_bar_text);
        final ImageView imageViewCloseButton = (ImageView) findViewById(R.id.clear_btn);
        imageViewSearchSettingsButton = (ImageView) findViewById(R.id.search_settings);

        imageViewLeftAction.setImageDrawable(drawerArrowDrawable);

        imageViewLeftAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (drawerArrowDrawable.getProgress() != 0) {

                    if (search_bar_text_view.getText().length() > 0) {
                        search_bar_text_view.setText("");
                    } else {
                        playDrawerToggleAnim(drawerArrowDrawable);
                        // Reference: http://stackoverflow.com/questions/5056734/android-force-edittext-to-remove-focus/16477251#16477251
                        search_bar_text_view.clearFocus();
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(search_bar_text_view.getWindowToken(), 0);
                    }

                } else {
                    if (hamburgerClickListener != null) {
                        hamburgerClickListener.onClick(v);
                    }
                }

            }
        });


        search_bar_text_view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 0) {
                    imageViewCloseButton.setVisibility(View.VISIBLE);
                } else {
                    imageViewCloseButton.setVisibility(View.GONE);

                }


            }
        });


        imageViewCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_bar_text_view.setText("");
            }
        });


        imageViewSearchSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // If the keyboard is present, hide it.
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(search_bar_text_view.getWindowToken(), 0);

                search_bar_text_view.clearFocus();

                if (searchSettingsClickListener != null) {
                    searchSettingsClickListener.onClick(v);
                }
            }
        });

        search_bar_text_view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    // Only animate if we are showing the burger icon.
                    if (drawerArrowDrawable.getProgress() == 0.0)
                        playDrawerToggleAnim(drawerArrowDrawable);
//                } else {
//                    // Only animate if we are not showing the burger icon.
//                    if (drawerArrowDrawable.getProgress() == 1.0)
//                        playDrawerToggleAnim(drawerArrowDrawable);
                }

            }
        });


    }

    public void addSearchBarTextChangedListener(TextWatcher listener) {
        search_bar_text_view.addTextChangedListener(listener);
    }

    public void setSearchSettingsClickListener(View.OnClickListener listener) {
        searchSettingsClickListener = listener;
    }

    public void setHamburgerClickListener(View.OnClickListener listener) {
        hamburgerClickListener = listener;
    }
    public void setSearchBarTextHint(CharSequence hint) {
        search_bar_text_view.setHint(hint);
    }

    public void setSearchBarTextHint(@StringRes int resId) {
        search_bar_text_view.setHint(resId);
    }

    public void editSearchBarText() {
        search_bar_text_view.requestFocus();
        // Reference: http://stackoverflow.com/questions/2403632/android-show-soft-keyboard-automatically-when-focus-is-on-an-edittext
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(search_bar_text_view, InputMethodManager.SHOW_IMPLICIT);
    }

    public boolean isSearchBarTextFocused() {
        return search_bar_text_view.isFocused();
    }

    public void clearSearchBarTextFocus() {
        search_bar_text_view.clearFocus();
        playDrawerToggleAnim(drawerArrowDrawable); // Return the arrow back to hamburguer.

    }

    public void updateChangesIndicator(boolean haveChanges, int colorToIndicateChanges) {
        if (haveChanges) {
            imageViewSearchSettingsButton.setColorFilter(colorToIndicateChanges);
        } else {
            imageViewSearchSettingsButton.clearColorFilter();
        }
    }

    static void playDrawerToggleAnim(final DrawerArrowDrawable d) {
        float start = d.getProgress();
        float end = Math.abs(start - 1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ValueAnimator offsetAnimator = ValueAnimator.ofFloat(start, end);
            offsetAnimator.setDuration(300);
            offsetAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            offsetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float offset = 0;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        offset = (Float) animation.getAnimatedValue();
                    }
                    d.setProgress(offset);
                }
            });
            offsetAnimator.start();
        } else
            d.setProgress(end);
    }
}

package name.vampidroid.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import name.vampidroid.R;


/**
 * Created by fxjr on 26/09/16.
 */

public class CardFilters extends LinearLayout {

    private static final String TAG = "CardFilters";
    private View disciplinesHeader;
    private View disciplinesLayout;
    private View clansHeader;
    private View clansLayout;
    private View cardTypesHeader;
    private View cardTypesLayout;

    OnCardFiltersChangeListener cardFiltersChangeListener;


    int numberOfGroupFiltersApplied = 0;
    int numberOfCapacityFiltersApplied = 0;
    int numberOfCryptDisciplineFiltersApplied = 0;
    private SparseArray<Parcelable> container;

    public interface OnCardFiltersChangeListener {

        void onGroupsChanged(int group, boolean isChecked);

        void onCapacitiesChanged(int minCapacity, int maxCapacity);


        void onCryptDisciplineChanged(String discipline, boolean isBasic, boolean isChecked);
    }



    public CardFilters(Context context) {
        super(context);
        init();
    }

    public CardFilters(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CardFilters(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CardFilters(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View inflated = inflater.inflate(R.layout.widget_card_filters_layout, this, false);

        addView(inflated);
        initViews();

//        AsyncLayoutInflater asyncLayoutInflater = new AsyncLayoutInflater(getContext());
//        asyncLayoutInflater.inflate(R.layout.widget_card_filters_layout, this, new AsyncLayoutInflater.OnInflateFinishedListener() {
//            @Override
//            public void onInflateFinished(View view, int resid, ViewGroup parent) {
//
//                if (container != null) {
//                    CardFilters.super.dispatchRestoreInstanceState(container);
//                    container = null;
//                }
//
//                addView(view);
//                initViews();
//
//            }
//        });


    }

    void initViews() {


        disciplinesHeader = findViewById(R.id.disciplinesHeader);
        disciplinesLayout = findViewById(R.id.disciplinesLayout);

        clansHeader = findViewById(R.id.clansHeader);
        clansLayout = findViewById(R.id.clansLayout);

        cardTypesHeader = findViewById(R.id.cardTypesHeader);
        cardTypesLayout = findViewById(R.id.cardTypesLayout);


        setupGroupsHandler();

        setupCapacitiesHandler();

        setupCryptDisciplinesHandler();

        setupExpandLayout(disciplinesHeader, disciplinesLayout, (ImageView) findViewById(R.id.imgDisciplinesLayoutArrow));

        setupExpandLayout(clansHeader, clansLayout, (ImageView) findViewById(R.id.imgClansLayoutArrow));

        setupExpandLayout(cardTypesHeader, cardTypesLayout, (ImageView) findViewById(R.id.imgCardTypesLayoutArrow));
    }

    private void setupCapacitiesHandler() {
        final CryptCapacitySeekBars cryptCapacitySeekBars = (CryptCapacitySeekBars) findViewById(R.id.crypt_capacity_seekbars);

        SeekBar.OnSeekBarChangeListener cryptSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "onProgressChanged() called with: seekBar = [" + seekBar + "], progress = [" + progress + "], fromUser = [" + fromUser + "]");

                numberOfCapacityFiltersApplied = 0;
                numberOfCapacityFiltersApplied += cryptCapacitySeekBars.getMinSeekBarValue() > 1 ? 1 : 0;
                numberOfCapacityFiltersApplied += cryptCapacitySeekBars.getMaxSeekBarValue() < 11 ? 1 : 0;

                if (!fromUser) {
                    // The progress was changed because of a state restore. Report this change.
                    if (cardFiltersChangeListener != null) {
                        cardFiltersChangeListener.onCapacitiesChanged(cryptCapacitySeekBars.getMinSeekBarValue(), cryptCapacitySeekBars.getMaxSeekBarValue());
                    }
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (cardFiltersChangeListener != null) {
                    cardFiltersChangeListener.onCapacitiesChanged(cryptCapacitySeekBars.getMinSeekBarValue(), cryptCapacitySeekBars.getMaxSeekBarValue());
                }

            }
        };

        cryptCapacitySeekBars.setOnSeekBarChangeListenerMin(cryptSeekBarChangeListener);

        cryptCapacitySeekBars.setOnSeekBarChangeListenerMax(cryptSeekBarChangeListener);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        Log.d(TAG, "dispatchRestoreInstanceState() called with: container = [" + container + "]");
        super.dispatchRestoreInstanceState(container);
        // Doesn't restore child states yet because they may not have been inflated yet.
        // Keep the state to be used later.
//        this.container = container;
    }

//    @Override
//    protected Parcelable onSaveInstanceState() {
//
//        Log.d(TAG, "onSaveInstanceState() called");
//        Bundle state = new Bundle();
//        Parcelable superState = super.onSaveInstanceState();
//        state.putParcelable("superState", superState);
//
//        state.putInt("numberOfGroupFiltersApplied", numberOfGroupFiltersApplied);
//        state.putInt("numberOfCapacityFiltersApplied", numberOfCapacityFiltersApplied);
//        state.putInt("numberOfCryptDisciplineFiltersApplied", numberOfCryptDisciplineFiltersApplied);
//
//        return state;
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Parcelable state) {
//        Log.d(TAG, "onRestoreInstanceState() called with: state = [" + state + "]");
//
//        if (state instanceof Bundle) {
//            Log.d(TAG, "onRestoreInstanceState: Restoring from bundle");
//            Bundle bundle = (Bundle) state;
//            super.onRestoreInstanceState(bundle.getParcelable("superState"));
//
//            numberOfGroupFiltersApplied = bundle.getInt("numberOfGroupFiltersApplied");
//            numberOfCapacityFiltersApplied = bundle.getInt("numberOfCapacityFiltersApplied");
//            numberOfCryptDisciplineFiltersApplied = bundle.getInt("numberOfCryptDisciplineFiltersApplied");
//
//        } else {
//            super.onRestoreInstanceState(state);
//        }
//    }

    private void setupGroupsHandler() {

        CompoundButton.OnCheckedChangeListener groupsClickListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton checkbox, boolean isChecked) {

                Log.d(TAG, "onCheckedChanged() called with: checkbox = [" + checkbox + "], isChecked = [" + isChecked + "]");

                numberOfGroupFiltersApplied += isChecked ? 1 : -1;

                if (cardFiltersChangeListener != null) {
                    cardFiltersChangeListener.onGroupsChanged(Integer.parseInt(checkbox.getText().toString()), isChecked);
                }

            }
        };

        ViewGroup groupsContainer = (ViewGroup) findViewById(R.id.relativeLayoutGroups);

        for (int i = 0; i < groupsContainer.getChildCount(); i++) {
            if (groupsContainer.getChildAt(i) instanceof CheckBox) {
                CheckBox checkbox = (CheckBox) groupsContainer.getChildAt(i);
                checkbox.setOnCheckedChangeListener(groupsClickListener);

            }
        }

    }


    private void setupCryptDisciplinesHandler() {


        ViewGroup cryptDisciplinesContainer = (ViewGroup) findViewById(R.id.disciplinesLayout);

        for (int i = 0; i < cryptDisciplinesContainer.getChildCount(); i++) {
            ViewGroup cryptDisciplineRow = (ViewGroup) cryptDisciplinesContainer.getChildAt(i);

            CheckBox cryptDisciplineCheckboxBasic = (CheckBox) cryptDisciplineRow.getChildAt(1); // Basic Discipline
            CheckBox cryptDisciplineCheckboxAdv = (CheckBox) cryptDisciplineRow.getChildAt(2); // Adv Discipline

            setupCryptDisciplineHandler(cryptDisciplineCheckboxBasic, true);
            setupCryptDisciplineHandler(cryptDisciplineCheckboxAdv, false);

        }

    }

    private void setupCryptDisciplineHandler(CheckBox cryptDisciplineCheckBox, final boolean isBasic) {


        CompoundButton.OnCheckedChangeListener cryptDisciplineHandler = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton checkbox, boolean isChecked) {

                numberOfCryptDisciplineFiltersApplied += isChecked ? 1 : -1;

                // TODO: 01/10/16 Change to use a tag
                String discipline = getResources().getResourceEntryName(checkbox.getId());
                discipline = discipline.substring(discipline.length() - 3);

                if (cardFiltersChangeListener != null) {
                    cardFiltersChangeListener.onCryptDisciplineChanged(discipline, isBasic, isChecked);
                }

            }
        };

        cryptDisciplineCheckBox.setOnCheckedChangeListener(cryptDisciplineHandler);
    }


    private void setupExpandLayout(final View header, final View layoutToExpand, final ImageView imgArrow) {
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (layoutToExpand.isShown()) {
                    layoutToExpand.setVisibility(View.GONE);
                    imgArrow.setImageResource(R.drawable.ic_expand_more_black_24dp);
                } else {

                    imgArrow.setImageResource(R.drawable.ic_expand_less_black_24dp);


//                    Reference: http://stackoverflow.com/questions/19765938/show-and-hide-a-view-with-a-slide-up-down-animation
                    // Prepare the View for the animation
                    layoutToExpand.setVisibility(View.VISIBLE);
//					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
//						layoutToExpand.setAlpha(0.0f);
//
//						// Start the animation
//						layoutToExpand.animate()
//								.alpha(1.0f);
//					}
                }

            }
        });


    }


    public void setOnCardFiltersChangeListener(OnCardFiltersChangeListener listener) {
        cardFiltersChangeListener = listener;
    }

    public void showCryptFilters() {
        disciplinesHeader.setVisibility(VISIBLE);
        disciplinesLayout.setVisibility(VISIBLE);

        clansHeader.setVisibility(VISIBLE);
        clansLayout.setVisibility(VISIBLE);

    }

    public void showLibraryFilters() {

        disciplinesHeader.setVisibility(GONE);
        disciplinesLayout.setVisibility(GONE);

        clansHeader.setVisibility(GONE);
        clansLayout.setVisibility(GONE);
    }

    public int getNumberOfFiltersApplied() {

        return numberOfGroupFiltersApplied + numberOfCapacityFiltersApplied + numberOfCryptDisciplineFiltersApplied;

    }

}


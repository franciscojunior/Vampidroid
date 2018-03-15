package name.vampidroid.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.AsyncLayoutInflater;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import com.github.florent37.expansionpanel.ExpansionLayout;

import name.vampidroid.R;


/**
 * Created by fxjr on 26/09/16.
 */

public class CardFilters extends LinearLayout {

    private static final String TAG = "CardFilters";

    private View cryptFiltersLayoutGroup;
    private View libraryFiltersLayoutGroup;

    OnCardFiltersChangeListener cardFiltersChangeListener;

    int numberOfGroupFiltersApplied;
    int numberOfCapacityFiltersApplied;
    int numberOfCryptDisciplineFiltersApplied;
    int numberOfLibraryDisciplineFiltersApplied;
    int numberOfClansFiltersApplied;
    int numberOfCardTypesFiltersApplied;

    CryptCapacitySeekBars cryptCapacitySeekBars;


    boolean isResetting = false;
    private SparseArray<Parcelable> restoreInstanceStateData;

    private boolean isInflateFinished;
    private boolean showCryptFiltersAfterLayoutInflated = false;
    private boolean showLibraryFiltersAfterLayoutInflated = false;


    public interface OnCardFiltersChangeListener {

        void onGroupsChanged(int group, boolean isChecked);

        void onCryptDisciplineChanged(String discipline, boolean isBasic, boolean isChecked);

        void onClansChanged(String clan, boolean isChecked);

        void onCardTypeChanged(String cardType, boolean isChecked);

        void onLibraryDisciplineChanged(String discipline, boolean isChecked);

        void onCapacitiesChanged(int minCapacity, int maxCapacity);

        void onReset();
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

        isInflateFinished = false;
        AsyncLayoutInflater asyncLayoutInflater = new AsyncLayoutInflater(getContext());
        asyncLayoutInflater.inflate(R.layout.widget_card_filters_layout, this, new AsyncLayoutInflater.OnInflateFinishedListener() {
            @Override
            public void onInflateFinished(View view, int resid, ViewGroup parent) {

                addView(view);

                isInflateFinished = true;

                if (restoreInstanceStateData != null) {

                    // Only update header status when the layout has been inflated.
                    updateFilterHeaderStatus((TextView) findViewById(R.id.textClans), numberOfClansFiltersApplied);
                    updateFilterHeaderStatus((TextView) findViewById(R.id.textCardTypes), numberOfCardTypesFiltersApplied);
                    updateFilterHeaderStatus((TextView) findViewById(R.id.textLibraryDisciplines), numberOfLibraryDisciplineFiltersApplied);
                    updateFilterHeaderStatus((TextView) findViewById(R.id.textDisciplines), numberOfCryptDisciplineFiltersApplied);

                    // Restore state from the view which was just inflated.
                    // Here we use the state given by the dispatchRestoreState method. As at the time dispatchRestoreIntanceState method
                    // was called the view hasn't been inflated yet, we just stored that state to be used here when the view finishes being
                    // inflated.
                    view.restoreHierarchyState(restoreInstanceStateData);

                    restoreInstanceStateData = null;
                }

                cryptFiltersLayoutGroup = findViewById(R.id.cryptFiltersLayoutGroup);
                libraryFiltersLayoutGroup = findViewById(R.id.libraryFiltersLayoutGroup);

                setupViewHandlers();

                // Check if it was requested to show a group of filters before layout inflated.
                if (showCryptFiltersAfterLayoutInflated) {
                    showCryptFilters();
                } else if (showLibraryFiltersAfterLayoutInflated) {
                    showLibraryFilters();
                }


            }
        });


    }

    void setupViewHandlers() {

        setupGroupsHandler();

        setupCryptDisciplinesHandler();

        setupClansHandler();

        setupCardTypesHandler();

        setupLibraryDisciplinesHandler();

        setupCapacitiesHandler();

    }

    private void setupCapacitiesHandler() {
        cryptCapacitySeekBars = findViewById(R.id.crypt_capacity_seekbars);

        SeekBar.OnSeekBarChangeListener cryptSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

            boolean trackingTouchStarted = false;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "onProgressChanged() called with: seekBar = [" + seekBar + "], progress = [" + progress + "], fromUser = [" + fromUser + "]");

                numberOfCapacityFiltersApplied = 0;
                numberOfCapacityFiltersApplied += cryptCapacitySeekBars.getMinSeekBarValue() > 1 ? 1 : 0;
                numberOfCapacityFiltersApplied += cryptCapacitySeekBars.getMaxSeekBarValue() < 11 ? 1 : 0;

                if (!fromUser && !trackingTouchStarted) {
                    // The progress was changed because of a state restore. Report this change.
                    if (cardFiltersChangeListener != null & !isResetting) {
                        cardFiltersChangeListener.onCapacitiesChanged(cryptCapacitySeekBars.getMinSeekBarValue(), cryptCapacitySeekBars.getMaxSeekBarValue());
                    }
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                trackingTouchStarted = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (cardFiltersChangeListener != null && !isResetting) {
                    cardFiltersChangeListener.onCapacitiesChanged(cryptCapacitySeekBars.getMinSeekBarValue(), cryptCapacitySeekBars.getMaxSeekBarValue());
                }
                trackingTouchStarted = false;

            }
        };

        cryptCapacitySeekBars.setOnSeekBarChangeListenerMin(cryptSeekBarChangeListener);

        cryptCapacitySeekBars.setOnSeekBarChangeListenerMax(cryptSeekBarChangeListener);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        // Don't restore child states yet because they may not have been inflated yet.
        // Keep the state to be used later.
        this.restoreInstanceStateData = container;

        super.dispatchRestoreInstanceState(container);
    }

    @Override
    protected Parcelable onSaveInstanceState() {

        Log.d(TAG, "onSaveInstanceState() called");
        Bundle state = new Bundle();
        Parcelable superState = super.onSaveInstanceState();
        state.putParcelable("superState", superState);

        state.putInt("numberOfGroupFiltersApplied", numberOfGroupFiltersApplied);
        state.putInt("numberOfCapacityFiltersApplied", numberOfCapacityFiltersApplied);
        state.putInt("numberOfCryptDisciplineFiltersApplied", numberOfCryptDisciplineFiltersApplied);
        state.putInt("numberOfLibraryDisciplineFiltersApplied", numberOfLibraryDisciplineFiltersApplied);
        state.putInt("numberOfClansFiltersApplied", numberOfClansFiltersApplied);
        state.putInt("numberOfCardTypesFiltersApplied", numberOfCardTypesFiltersApplied);

        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if (state instanceof Bundle) {
            Log.d(TAG, "onRestoreInstanceState: Restoring from bundle");
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable("superState"));


            numberOfGroupFiltersApplied = bundle.getInt("numberOfGroupFiltersApplied");
            numberOfCapacityFiltersApplied = bundle.getInt("numberOfCapacityFiltersApplied");
            numberOfCryptDisciplineFiltersApplied = bundle.getInt("numberOfCryptDisciplineFiltersApplied");
            numberOfLibraryDisciplineFiltersApplied = bundle.getInt("numberOfLibraryDisciplineFiltersApplied");
            numberOfClansFiltersApplied = bundle.getInt("numberOfClansFiltersApplied");
            numberOfCardTypesFiltersApplied = bundle.getInt("numberOfCardTypesFiltersApplied");

        } else {
            super.onRestoreInstanceState(state);
        }
    }



    void updateFilterHeaderStatus(TextView headerText, int value) {
        if (value > 0) {
            headerText.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        } else {
            headerText.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
        }

    }
    //    Reference: http://stackoverflow.com/questions/11358121/how-to-handle-the-checkbox-ischecked-and-unchecked-event-in-android
    private void setupGroupsHandler() {

        CompoundButton.OnCheckedChangeListener groupsClickListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton checkbox, boolean isChecked) {

                Log.d(TAG, "onCheckedChanged() called with: checkbox = [" + checkbox + "], isChecked = [" + isChecked + "]");

                numberOfGroupFiltersApplied += isChecked ? 1 : -1;
                Log.d(TAG, "onCheckedChanged: numberOfGroupFiltersApplied:" + numberOfGroupFiltersApplied);

                if (cardFiltersChangeListener != null && !isResetting) {
                    cardFiltersChangeListener.onGroupsChanged(Integer.parseInt(checkbox.getTag().toString()), isChecked);
                }

            }
        };

        ViewGroup groupsContainer = findViewById(R.id.relativeLayoutGroups);

        for (int i = 0; i < groupsContainer.getChildCount(); i++) {
            if (groupsContainer.getChildAt(i) instanceof CheckBox) {
                CheckBox checkbox = (CheckBox) groupsContainer.getChildAt(i);
                checkbox.setOnCheckedChangeListener(groupsClickListener);

            }
        }

    }


    private void setupCryptDisciplinesHandler() {

        ViewGroup cryptDisciplinesContainer = findViewById(R.id.disciplinesLayout);

        // The first child viewgroup are just labels for the basic and advanced checkboxes columns. Skip it.
        for (int i = 1; i < cryptDisciplinesContainer.getChildCount(); i++) {
            ViewGroup cryptDisciplineRow = (ViewGroup) cryptDisciplinesContainer.getChildAt(i);

            CheckBox cryptDisciplineCheckboxBasic = (CheckBox) cryptDisciplineRow.getChildAt(1); // Basic Discipline
            CheckBox cryptDisciplineCheckboxAdv = (CheckBox) cryptDisciplineRow.getChildAt(2); // Adv Discipline

            setupCryptDisciplineHandlerHelper(cryptDisciplineCheckboxBasic, true);
            setupCryptDisciplineHandlerHelper(cryptDisciplineCheckboxAdv, false);

        }

    }

    private void setupCryptDisciplineHandlerHelper(CheckBox cryptDisciplineCheckBox, final boolean isBasic) {


        final TextView cryptHeaderText = findViewById(R.id.textDisciplines);

        CompoundButton.OnCheckedChangeListener cryptDisciplineHandler = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton checkbox, boolean isChecked) {

                numberOfCryptDisciplineFiltersApplied += isChecked ? 1 : -1;

                updateFilterHeaderStatus(cryptHeaderText, numberOfCryptDisciplineFiltersApplied);

                // TODO: 01/10/16 Change to use a tag
                String discipline = getResources().getResourceEntryName(checkbox.getId());
                discipline = discipline.substring(discipline.length() - 3);

                if (cardFiltersChangeListener != null && !isResetting) {
                    cardFiltersChangeListener.onCryptDisciplineChanged(discipline, isBasic, isChecked);
                }

            }
        };

        cryptDisciplineCheckBox.setOnCheckedChangeListener(cryptDisciplineHandler);
    }

    private void setupCardTypesHandler() {

        final TextView cardTypesHeaderText = findViewById(R.id.textCardTypes);

        ViewGroup cardTypesLayout = findViewById(R.id.cardTypesLayout);

        setupTextCheckBoxRowHandler(cardTypesLayout, new OnTextCheckBoxRowHandlerClickListener() {
            @Override
            public void onClick(TextView rowText, CheckBox rowCheckBox) {
                rowCheckBox.toggle();

                numberOfCardTypesFiltersApplied += rowCheckBox.isChecked() ? 1 : -1;

                updateFilterHeaderStatus(cardTypesHeaderText, numberOfCardTypesFiltersApplied);

                if (cardFiltersChangeListener != null && !isResetting) {
                    cardFiltersChangeListener.onCardTypeChanged(rowText.getText().toString(), rowCheckBox.isChecked());
                }
            }
        });
    }

    private void setupClansHandler() {

        final TextView clansHeaderText = findViewById(R.id.textClans);

        ViewGroup clansLayout = findViewById(R.id.clansLayout);
        setupTextCheckBoxRowHandler(clansLayout, new OnTextCheckBoxRowHandlerClickListener() {
            @Override
            public void onClick(TextView rowText, CheckBox rowCheckBox) {
                rowCheckBox.toggle();

                numberOfClansFiltersApplied += rowCheckBox.isChecked() ? 1 : -1;

                updateFilterHeaderStatus(clansHeaderText, numberOfClansFiltersApplied);

                if (cardFiltersChangeListener != null && !isResetting) {
                    cardFiltersChangeListener.onClansChanged(rowText.getText().toString(), rowCheckBox.isChecked());
                }
            }
        });
    }


    private void setupLibraryDisciplinesHandler() {


        final TextView libraryDisciplinesHeaderText = findViewById(R.id.textLibraryDisciplines);

        ViewGroup libraryDisciplinesLayout = findViewById(R.id.libraryDisciplinesLayout);

        setupTextCheckBoxRowHandler(libraryDisciplinesLayout, new OnTextCheckBoxRowHandlerClickListener() {
            @Override
            public void onClick(TextView rowText, CheckBox rowCheckBox) {
                rowCheckBox.toggle();

                numberOfLibraryDisciplineFiltersApplied += rowCheckBox.isChecked() ? 1 : -1;


                updateFilterHeaderStatus(libraryDisciplinesHeaderText, numberOfLibraryDisciplineFiltersApplied);

                if (cardFiltersChangeListener != null && !isResetting) {
                    cardFiltersChangeListener.onLibraryDisciplineChanged(rowText.getText().toString(), rowCheckBox.isChecked());
                }
            }
        });
    }

    interface OnTextCheckBoxRowHandlerClickListener {
        void onClick(TextView rowText, CheckBox rowCheckBox);
    }


    private void setupTextCheckBoxRowHandler(ViewGroup root, final OnTextCheckBoxRowHandlerClickListener rowClickListener) {

        for (int i = 0; i < root.getChildCount(); i++) {
            ViewGroup row = (ViewGroup) root.getChildAt(i);

            final TextView rowText = (TextView) row.getChildAt(0);
            final CheckBox rowCheckbox = (CheckBox) row.getChildAt(1);

            row.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    rowClickListener.onClick(rowText, rowCheckbox);
                }
            });


        }
    }

    public void setOnCardFiltersChangeListener(OnCardFiltersChangeListener listener) {
        cardFiltersChangeListener = listener;
    }

    public void showCryptFilters() {

        if (isInflateFinished) {
            cryptFiltersLayoutGroup.setVisibility(VISIBLE);
            libraryFiltersLayoutGroup.setVisibility(GONE);
        } else {
            showCryptFiltersAfterLayoutInflated = true;
            showLibraryFiltersAfterLayoutInflated = false;
        }


    }

    public void showLibraryFilters() {


        if (isInflateFinished) {
            libraryFiltersLayoutGroup.setVisibility(VISIBLE);
            cryptFiltersLayoutGroup.setVisibility(GONE);
        } else {
            showCryptFiltersAfterLayoutInflated = false;
            showLibraryFiltersAfterLayoutInflated = true;
        }

    }

    public int getNumberOfFiltersApplied() {

        return numberOfGroupFiltersApplied + numberOfCryptDisciplineFiltersApplied + numberOfClansFiltersApplied + numberOfCardTypesFiltersApplied + numberOfLibraryDisciplineFiltersApplied + numberOfCapacityFiltersApplied;

    }

    public int getNumberOfCryptFiltersApplied() {
        return numberOfGroupFiltersApplied + numberOfCryptDisciplineFiltersApplied + numberOfClansFiltersApplied + numberOfCapacityFiltersApplied;
    }

    public int getNumberOfLibraryFiltersApplied() {
        return numberOfCardTypesFiltersApplied + numberOfLibraryDisciplineFiltersApplied;
    }

    public void clearFilters() {

        isResetting = true;

        resetChildCheckboxes(this);
        cryptCapacitySeekBars.reset();

        numberOfGroupFiltersApplied = 0;
        numberOfCapacityFiltersApplied = 0;
        numberOfCryptDisciplineFiltersApplied = 0;
        numberOfLibraryDisciplineFiltersApplied = 0;
        numberOfClansFiltersApplied = 0;
        numberOfCardTypesFiltersApplied = 0;


        updateFilterHeaderStatus((TextView) findViewById(R.id.textClans), 0);
        updateFilterHeaderStatus((TextView) findViewById(R.id.textCardTypes), 0);
        updateFilterHeaderStatus((TextView) findViewById(R.id.textLibraryDisciplines), 0);


        isResetting = false;

        if (cardFiltersChangeListener != null) {
            cardFiltersChangeListener.onReset();
        }

    }

    private void resetChildCheckboxes(ViewGroup viewGroup) {

        int viewGroupChildCount = viewGroup.getChildCount();
        for (int i = 0; i < viewGroupChildCount; i++) {
            if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                resetChildCheckboxes((ViewGroup) viewGroup.getChildAt(i));
            }

            if (viewGroup.getChildAt(i) instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) viewGroup.getChildAt(i);
                checkBox.setChecked(false);
            }

        }
    }

}



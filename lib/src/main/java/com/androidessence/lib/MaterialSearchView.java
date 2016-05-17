package com.androidessence.lib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.support.design.widget.CoordinatorLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * A SearchView widget that implements Material Design. Inspiration taken from https://github.com/krishnakapil/MaterialSeachView
 *
 * Created by adammcneilly on 3/28/16.
 */
public class MaterialSearchView extends CoordinatorLayout implements Filter.FilterListener {
    //-- Class Properties --//
    /**
     * Whether or not the search view is open right now.
     */
    private boolean mOpen;

    /**
     * The Context that this view appears in.
     */
    private Context mContext;

    /**
     * Whether or not the MaterialSearchView will animate into view or just appear.
     */
    private boolean mShouldAnimate;

    /**
     * The maximum number of results we want to return from the voice recognition.
     */
    private static final int MAX_RESULTS = 1;

    /**
     * The identifier for the voice request intent.
     */
    public static final int REQUEST_VOICE = 0;

    /**
     * Flag for whether or not we are clearing focus.
     */
    private boolean mClearingFocus;

    //-- UI Elements --//

    /**
     * The tint that appears over the search view.
     */
    private View mTintView;

    /**
     * The root of the search view.
     */
    private CoordinatorLayout mRoot;

    /**
     * The bar at the top of the SearchView containing the EditText and ImageButtons.
     */
    private LinearLayout mSearchBar;

    /**
     * The EditText for entering a search.
     */
    private EditText mSearchEditText;

    /**
     * The ImageButton for navigating back.
     */
    private ImageButton mBack;

    /**
     * The ImageButton for initiating a voice search.
     */
    private ImageButton mVoice;

    /**
     * The ImageButton for clearing the search text.
     */
    private ImageButton mClear;

    /**
     * The ListView for displaying suggestions based on the search.
     */
    private ListView mSuggestionsListView;

    /**
     * Adapter for displaying suggestions.
     */
    private ListAdapter mAdapter;

    //-- Query properties --//

    /**
     * The previous query text.
     */
    private CharSequence mOldQuery;

    /**
     * The current query text.
     */
    private CharSequence mCurrentQuery;

    //-- Listeners --//

    /**
     * Listener for when the query text is submitted or changed.
     */
    private OnQueryTextListener mOnQueryTextListener;

    /**
     * Listener for when the search view opens and closes.
     */
    private SearchViewListener mSearchViewListener;

    //-- Constructors --//

    public MaterialSearchView(Context context) {
        this(context, null);
    }

    public MaterialSearchView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);

        init();
    }

    public MaterialSearchView(Context context, AttributeSet attributeSet, int defStyleAttributes) {
        super(context, attributeSet);

        // Set variables
        this.mContext = context;
        this.mShouldAnimate = true;

        // Initialize view
        init();

        // Initialize style
        initStyle(attributeSet, defStyleAttributes);
    }

    //-- Initializers --//

    /**
     * Preforms any required initializations for the search view.
     */
    private void init() {
        // Inflate view
        LayoutInflater.from(mContext).inflate(R.layout.search_view, this, true);

        // Get items
        mRoot = (CoordinatorLayout) findViewById(R.id.search_layout);
        mTintView = mRoot.findViewById(R.id.transparent_view);
        mSearchBar = (LinearLayout) mRoot.findViewById(R.id.search_bar);
        mBack = (ImageButton) mRoot.findViewById(R.id.action_back);
        mSearchEditText = (EditText) mRoot.findViewById(R.id.search_edit_text);
        mVoice = (ImageButton) mRoot.findViewById(R.id.action_voice);
        mClear = (ImageButton) mRoot.findViewById(R.id.action_clear);
        mSuggestionsListView = (ListView) mRoot.findViewById(R.id.suggestion_list);

        // Set click listeners
        mBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSearch();
            }
        });

        mVoice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onVoiceClicked();
            }
        });

        mClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchEditText.setText("");
            }
        });

        mTintView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSearch();
            }
        });

        // Show voice button
        displayVoiceButton(true);

        // Initialize the search view.
        initSearchView();

        // Start with the suggestions list gone
        mSuggestionsListView.setVisibility(View.GONE);
    }

    /**
     * Initializes the style of this view.
     * @param attributeSet The attributes to apply to the view.
     * @param defStyleAttribute An attribute to the style theme applied to this view.
     */
    private void initStyle(AttributeSet attributeSet, int defStyleAttribute) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attributeSet, R.styleable.MaterialSearchView, defStyleAttribute, 0);

        if(typedArray != null) {
            if(typedArray.hasValue(R.styleable.MaterialSearchView_searchBackground)) {
                setBackground(typedArray.getDrawable(R.styleable.MaterialSearchView_searchBackground));
            }

            if (typedArray.hasValue(R.styleable.MaterialSearchView_android_textColor)) {
                setTextColor(typedArray.getColor(R.styleable.MaterialSearchView_android_textColor, 0));
            }

            if (typedArray.hasValue(R.styleable.MaterialSearchView_android_textColorHint)) {
                setHintTextColor(typedArray.getColor(R.styleable.MaterialSearchView_android_textColorHint, 0));
            }

            if (typedArray.hasValue(R.styleable.MaterialSearchView_android_hint)) {
                setHint(typedArray.getString(R.styleable.MaterialSearchView_android_hint));
            }

            if (typedArray.hasValue(R.styleable.MaterialSearchView_searchVoiceIcon)) {
                setVoiceIcon(typedArray.getDrawable(R.styleable.MaterialSearchView_searchVoiceIcon));
            }

            if (typedArray.hasValue(R.styleable.MaterialSearchView_searchCloseIcon)) {
                setClearIcon(typedArray.getDrawable(R.styleable.MaterialSearchView_searchCloseIcon));
            }

            if (typedArray.hasValue(R.styleable.MaterialSearchView_searchBackIcon)) {
                setBackIcon(typedArray.getDrawable(R.styleable.MaterialSearchView_searchBackIcon));
            }

            if (typedArray.hasValue(R.styleable.MaterialSearchView_searchSuggestionBackground)) {
                setSuggestionBackground(typedArray.getDrawable(R.styleable.MaterialSearchView_searchSuggestionBackground));
            }

            typedArray.recycle();
        }
    }

    /**
     * Preforms necessary initializations on the SearchView.
     */
    private void initSearchView() {
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // When an edit occurs, submit the query.
                onSubmitQuery();
                return true;
            }
        });

        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // When the text changes, filter
                startFilter(s);
                MaterialSearchView.this.onTextChanged(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSearchEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // If we gain focus, show keyboard and show suggestions.
                if(hasFocus) {
                    showKeyboard(mSearchEditText);
                    showSuggestions();
                }
            }
        });
    }

    /**
     * Handles when the voice button is clicked and starts listening, then calls activity with voice search.
     */
    private void onVoiceClicked() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, mContext.getString(R.string.search));
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, MAX_RESULTS); // Quantity of results we want to receive

        if(mContext instanceof Activity) {
            ((Activity) mContext).startActivityForResult(intent, REQUEST_VOICE);
        }
    }

    //-- Show methods --//

    /**
     * Displays the keyboard with a focus on the Search EditText.
     * @param view The view to attach the keyboard to.
     */
    private void showKeyboard(View view) {
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1 && view.hasFocus()) {
            view.clearFocus();
        }

        view.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, 0);
    }

    /**
     * Changes the visibility of the voice button to VISIBLE or GONE.
     * @param display True to display the voice button, false to hide it.
     */
    private void displayVoiceButton(boolean display) {
        // Only display voice if we pass in true, and it's available
        if(display && isVoiceAvailable()) {
            mVoice.setVisibility(View.VISIBLE);
        } else {
            mVoice.setVisibility(View.GONE);
        }
    }

    /**
     * Changes the visibility of the clear button to VISIBLE or GONE.
     * @param display True to display the clear button, false to hide it.
     */
    private void displayClearButton(boolean display) {
        mClear.setVisibility(display ? View.VISIBLE : View.GONE);
    }

    /**
     * Displays the available suggestions, if any.
     */
    private void showSuggestions() {
        // If we have an adapter, and it has at least one item, show suggestions.
        if(mAdapter != null && mAdapter.getCount() > 0) {
            mSuggestionsListView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Displays the SearchView.
     */
    public void openSearch() {
        // If search is already open, just return.
        if(mOpen) {
            return;
        }

        // Get focus
        mSearchEditText.setText("");
        mSearchEditText.requestFocus();

        if(mShouldAnimate) {
            AnimationUtils.fadeInView(mRoot, AnimationUtils.ANIMATION_DURATION_SHORT, new AnimationUtils.AnimationListener() {
                @Override
                public boolean onAnimationStart(View view) {
                    return false;
                }

                @Override
                public boolean onAnimationEnd(View view) {
                    if(mSearchViewListener != null) {
                        mSearchViewListener.onSearchViewOpened();
                    }

                    Log.v("ADAM_MCNEILLY", "animation done.");

                    return false;
                }

                @Override
                public boolean onAnimationCancel(View view) {
                    return false;
                }
            });
        } else {
            mRoot.setVisibility(View.VISIBLE);
            Log.v("ADAM_MCNEILLY", "root visible.");
            if(mSearchViewListener != null) {
                mSearchViewListener.onSearchViewOpened();
            }
        }

        mOpen = true;
    }

    //-- Hide methods --//

    /**
     * Hides the keyboard displayed for the SearchEditText.
     * @param view The view to detach the keyboard from.
     */
    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Closes the search view if necessary.
     */
    public void closeSearch() {
        // If we're already closed, just return.
        if(!mOpen) {
            return;
        }

        // Clear text, values, and focus.
        mSearchEditText.setText("");
        dismissSuggestions();
        clearFocus();

        // Hide view
        mRoot.setVisibility(View.GONE);

        // Call listener if we have one
        if(mSearchViewListener != null) {
            mSearchViewListener.onSearchViewClosed();
        }

        mOpen = false;
    }

    /**
     * Hides the suggestion list view.
     */
    private void dismissSuggestions() {
        mSuggestionsListView.setVisibility(View.GONE);
    }

    //-- Interface methods --//

    /**
     * Filters and updates the buttons when text is changed.
     * @param newText The new text.
     */
    private void onTextChanged(CharSequence newText) {
        // Get current query
        mCurrentQuery = mSearchEditText.getText();

        // If the text is not empty, show the empty button and hide the voice button
        if(!TextUtils.isEmpty(mCurrentQuery)) {
            displayVoiceButton(false);
            displayClearButton(true);
        } else {
            displayClearButton(false);
            displayVoiceButton(true);
        }

        // If we have a query listener and the text has changed, call it.
        if(mOnQueryTextListener != null && !TextUtils.equals(mOldQuery, mCurrentQuery)) {
            mOnQueryTextListener.onQueryTextChange(newText.toString());
        }

        mOldQuery = mCurrentQuery;
    }

    /**
     * Called when a query is submitted. This will close the search view.
     */
    private void onSubmitQuery() {
        // Get the query.
        CharSequence query = mSearchEditText.getText();

        // If the query is not null and it has some text, submit it.
        if(query != null && TextUtils.getTrimmedLength(query) > 0) {

            // If we don't have a listener, or if the search view handled the query, close it.
            if(mOnQueryTextListener == null || !mOnQueryTextListener.onQueryTextSubmit(query.toString())) {
                closeSearch();
                mSearchEditText.setText("");
            }
        }
    }

    /**
     * Filters the current list of suggestions based on a given text.
     * @param query The text that the suggestions will be filtered by.
     */
    private void startFilter(CharSequence query) {
        // If we have an adapter and it implements filterable, filter it.
        if(mAdapter != null && mAdapter instanceof Filterable) {
            ((Filterable)mAdapter).getFilter().filter(query, MaterialSearchView.this);
        }
    }

    /**
     * Handles the completion of filtering.
     * @param count The number of results returned by the filter.
     */
    @Override
    public void onFilterComplete(int count) {
        // If count is greater than 0, show suggestions. Otherwise hide it
        if(count > 0) {
            showSuggestions();
        } else {
            dismissSuggestions();
        }
    }

    //-- Mutators --//

    /**
     * Sets the Adapter for the suggestions list. This should implement filterable, but we will not require it.
     * TODO: Consider requiring that.
     * @param adapter The adapter to be used for suggestions.
     */
    public void setAdapter(ListAdapter adapter) {
        mAdapter = adapter;
        mSuggestionsListView.setAdapter(adapter);
        startFilter(mSearchEditText.getText());
    }

    /**
     * Sets the background of the SearchView.
     * @param background The drawable to use as a background.
     */
    @Override
    public void setBackground(Drawable background) {
        // Method changed in jelly bean for setting background.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mSearchBar.setBackground(background);
        } else {
            //noinspection deprecation
            mSearchBar.setBackgroundDrawable(background);
        }
    }

    /**
     * Sets the background color of the SearchView.
     * @param color The color to use for the background.
     */
    @Override
    public void setBackgroundColor(int color) {
        // Set background color of search bar.
        mSearchBar.setBackgroundColor(color);
    }

    /**
     * Sets the text color of the EditText.
     * @param color The color to use for the EditText.
     */
    public void setTextColor(int color) {
        mSearchEditText.setTextColor(color);
    }

    /**
     * Sets the text color of the search hint.
     * @param color The color to be used for the hint text.
     */
    public void setHintTextColor(int color) {
        mSearchEditText.setHintTextColor(color);
    }

    /**
     * Sets the hint to be used for the search EditText.
     * @param hint The hint to be displayed in the search EditText.
     */
    public void setHint(CharSequence hint) {
        mSearchEditText.setHint(hint);
    }

    /**
     * Sets the icon for the voice action.
     * @param drawable The drawable to represent the voice action.
     */
    public void setVoiceIcon(Drawable drawable) {
        mVoice.setImageDrawable(drawable);
    }

    /**
     * Sets the icon for the clear action.
     * @param drawable The drawable to represent the clear action.
     */
    public void setClearIcon(Drawable drawable) {
        mClear.setImageDrawable(drawable);
    }

    /**
     * Sets the icon for the back action.
     * @param drawable The drawable to represent the back action.
     */
    public void setBackIcon(Drawable drawable) {
        mBack.setImageDrawable(drawable);
    }

    /**
     * Sets the background of the suggestions listview.
     * @param drawable The drawable to use as a background for the suggestions listview.
     */
    public void setSuggestionBackground(Drawable drawable) {
        // Method change in jelly bean
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mSuggestionsListView.setBackground(getBackground());
        } else {
            //noinspection deprecation
            mSuggestionsListView.setBackgroundDrawable(drawable);
        }
    }

    /**
     * Set this listener to listen to Query Change events.
     *
     * @param listener The listener to apply.
     */
    public void setOnQueryTextListener(OnQueryTextListener listener) {
        mOnQueryTextListener = listener;
    }

    /**
     * Set this listener to listen to Search View open and close events
     *
     * @param listener The listener to apply.
     */
    public void setOnSearchViewListener(SearchViewListener listener) {
        mSearchViewListener = listener;
    }

        //-- Accessors --//

    /**
     * Determines if the search view is opened or closed.
     * @return True if the search view is open, false if it is closed.
     */
    public boolean isOpen() {
        return mOpen;
    }

    /** Determines if the user's voice is available
     * @return True if we can collect the user's voice, false otherwise.
     */
    private boolean isVoiceAvailable() {
        // Get package manager
        PackageManager packageManager = mContext.getPackageManager();

        // Gets a list of activities that can handle this intent.
        List<ResolveInfo> activities = packageManager.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);

        // Returns true if we have at least one activity.
        return activities.size() > 0;
    }

    //-- View methods --//

    /**
     * Handles any cleanup when focus is cleared from the view.
     */
    @Override
    public void clearFocus() {
        this.mClearingFocus = true;
        hideKeyboard(this);
        super.clearFocus();
        mSearchEditText.clearFocus();
        this.mClearingFocus = false;
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        // Don't accept if we are clearing focus, or if the view isn't focusable.
        return !(mClearingFocus || !isFocusable()) && mSearchEditText.requestFocus(direction, previouslyFocusedRect);
    }

    //-- Interfaces --//

    /**
     * Interface that handles the submission and change of search queries.
     */
    public interface OnQueryTextListener {
        /**
         * Called when a search query is submitted.
         * @param query The text that will be searched.
         * @return True when the query is handled by the listener, false to let the SearchView handle the default case.
         */
        boolean onQueryTextSubmit(String query);

        /**
         * Called when a search query is changed.
         * @param newText The new text of the search query.
         * @return True when the query is handled by the listener, false to let the SearchView handle the default case.
         */
        boolean onQueryTextChange(String newText);
    }

    /**
     * Interface that handles the opening and closing of the SearchView.
     */
    public interface SearchViewListener {
        /**
         * Called when the searchview is opened.
         */
        void onSearchViewOpened();

        /**
         * Called when the search view closes.
         */
        void onSearchViewClosed();
    }
}

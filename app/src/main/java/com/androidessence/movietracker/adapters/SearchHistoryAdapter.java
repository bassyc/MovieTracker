package com.androidessence.movietracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidessence.movietracker.R;
import com.androidessence.movietracker.data.MovieContract;

/**
 * Displays a list of recent searches in a ListView.
 *
 * Created by adammcneilly on 5/7/16.
 */
public class SearchHistoryAdapter extends BaseAdapter {
    private Context context;
    private String[] searches;

    public static final String[] SEARCH_COLUMNS = new String[] {
            MovieContract.SearchEntry._ID,
            MovieContract.SearchEntry.COLUMN_TEXT
    };

    public static final int TEXT_INDEX = 1;

    public SearchHistoryAdapter(Context context, String[] searches) {
        this.context = context;
        this.searches = searches;
    }

    @Override
    public int getCount() {
        return searches.length;
    }

    @Override
    public Object getItem(int position) {
        return searches[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchViewHolder searchViewHolder;

        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_search, parent, false);
            searchViewHolder = new SearchViewHolder(convertView);
            convertView.setTag(searchViewHolder);
        } else {
            searchViewHolder = (SearchViewHolder) convertView.getTag();
        }

        searchViewHolder.bindSearch(searches[position]);

        return convertView;
    }

    private class SearchViewHolder {
        private TextView textView;

        public SearchViewHolder(View view) {
            textView = (TextView) view.findViewById(R.id.search_text_view);
        }

        public void bindSearch(String search) {
            textView.setText(search);
        }
    }
}
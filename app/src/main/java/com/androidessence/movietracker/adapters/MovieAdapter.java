package com.androidessence.movietracker.adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidessence.movietracker.pojos.Movie;
import com.androidessence.movietracker.R;

import java.util.List;

/**
 * Adapter class for displaying a list of movies in a RecyclerView.
 *
 * Created by adammcneilly on 3/26/16.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{
    private Context mContext;
    private List<Movie> mMovies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.mContext = context;
        this.mMovies = movies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bindMovie(mMovies.get(position));
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView mTitle;
        private final ImageView mMenu;

        public MovieViewHolder(View view) {
            super(view);

            mTitle = (TextView) view.findViewById(R.id.movie_title);
            mMenu = (ImageView) view.findViewById(R.id.movie_menu);

            mMenu.setOnClickListener(this);
        }

        public void bindMovie(Movie movie) {
            mTitle.setText(movie.getTitle());
        }


        @Override
        public void onClick(View v) {
            // Show popup for movie
            PopupMenu popupMenu = new PopupMenu(mContext, v);
            popupMenu.getMenuInflater().inflate(R.menu.movie_menu, popupMenu.getMenu());
            popupMenu.setGravity(Gravity.END);
            popupMenu.show();
        }
    }
}

package com.example.android.popularmoviesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.android.popularmoviesapp.MDBServiceAPI.BACKDROP_URL;

import com.squareup.picasso.Picasso;

/**
 * Created by nazaif on 5/2/18.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    final private TrailersListener trailersListener;
    private Context mContext;
    private MovieManager movieManager;

    public TrailerAdapter(Context mContext, MovieManager movieManager, TrailersListener trailersListener) {
        this.mContext = mContext;
        this.movieManager = movieManager;
        this.trailersListener = trailersListener;
    }

    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = R.layout.item_trailer;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutId, parent, false);
        TrailerViewHolder viewHolder = new TrailerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.TrailerViewHolder holder, int position) {
        TrailerResults trailerResults = movieManager.getTrailerResults();
        TrailerManager trailerManager = trailerResults.getTrailerResults().get(position);
        if (movieManager.getImages() != null) {
            String mbackdrop = movieManager.getImages().getBackdropsList().get(position).getPath();

            Picasso.with(mContext)
                    .load(BACKDROP_URL + mbackdrop)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error_img)
                    .into(holder.trailorBackdrop);
        }
        holder.trailorTitle.setText(trailerManager.getName());
        holder.itemView.setTag(trailerManager.getKey());
    }

    @Override
    public int getItemCount() {
        if (movieManager.getTrailerResults() == null) {
            return 0;
        } else
            return movieManager.getTrailerResults().getTrailerResults().size();
    }

    public interface TrailersListener {
        void onTrailersClick(int index);
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView trailorBackdrop;
        TextView trailorTitle;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            trailorBackdrop = (ImageView) itemView.findViewById(R.id.trailer_backdrop);
            trailorTitle = (TextView) itemView.findViewById(R.id.trailer_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            trailersListener.onTrailersClick(getAdapterPosition());
        }
    }
}

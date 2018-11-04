package app.nazaif.android.popularmoviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.nazaif.android.popularmoviesapp.pojo.MovieManager;
import app.nazaif.android.popularmoviesapp.R;
import app.nazaif.android.popularmoviesapp.pojo.ReviewManager;
import app.nazaif.android.popularmoviesapp.pojo.ReviewResults;

/**
 * Created by nazaif on 5/2/18.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    final ReviewListener reviewListener;
    private Context mContext;
    private MovieManager movieManager;

    public ReviewsAdapter(Context mContext, MovieManager movieManager, ReviewListener listener) {
        this.mContext = mContext;
        this.movieManager = movieManager;
        this.reviewListener = listener;
    }

    @Override
    public ReviewsAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = R.layout.item_reviews;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        ReviewViewHolder viewHolder = new ReviewViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ReviewViewHolder holder, int position) {
        ReviewResults results = movieManager.getReviewResults();
        ReviewManager manager = results.getReviews().get(position);
        holder.tvAuthor.setText(manager.getAuthor());
        holder.tvReview.setText(manager.getContent());
    }

    @Override
    public int getItemCount() {
        if (movieManager.getReviewResults() == null) {
            return 0;
        }
        return movieManager.getReviewResults().getReviews().size();
    }


    public interface ReviewListener {
        void onReviewsClick(int index);
    }

    protected class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvReview, tvAuthor;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            tvReview = (TextView) itemView.findViewById(R.id.tv_review);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (reviewListener == null) {
            } else
                reviewListener.onReviewsClick(getAdapterPosition());

        }
    }
}
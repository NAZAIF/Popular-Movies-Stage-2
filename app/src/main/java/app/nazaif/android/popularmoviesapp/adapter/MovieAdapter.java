package app.nazaif.android.popularmoviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.nazaif.android.popularmoviesapp.api.MDBServiceAPI;
import app.nazaif.android.popularmoviesapp.pojo.MovieManager;
import dev.nazaif.android.popularmoviesapp.R;
import app.nazaif.android.popularmoviesapp.utils.ColorUtils;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by nazaif on 20/12/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private static int holderCount;
    ClickListener clickListener;
    Context context;
    List<MovieManager> movieManager;

    public MovieAdapter(Context c, List<MovieManager> movieManagerList, ClickListener listener) {
        this.context = c;
        this.movieManager = movieManagerList;
        this.clickListener = listener;
        holderCount = 0;
    }

    public void setList(List<MovieManager> list) {
        if (list != null) {
            this.movieManager = list;
            notifyDataSetChanged();
        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        MovieAdapterViewHolder holder = new MovieAdapterViewHolder(v);
        int holderColor = ColorUtils.getViewHolderBackgroundColorFromInstance(context, holderCount);
        holder.title_textView.setBackgroundColor(holderColor);
        holderCount++;
        return holder;
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        MovieManager movie = movieManager.get(position);
        Picasso.with(context).load(MDBServiceAPI.POSTER_URL + movie.getPoster())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error_img)
                .into(holder.title_imageView);
        holder.title_textView.setText(movie.getTitle());
        holder.itemView.setTag(movie);
    }

    @Override
    public int getItemCount() {
        if (movieManager != null) {
            return movieManager.size();
        } else return 0;
    }

    public interface ClickListener {
        void onListClick(int index);
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        public TextView title_textView;
        public ImageView title_imageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            title_textView = (TextView) itemView.findViewById(R.id.cardTitleText);
            title_imageView = (ImageView) itemView.findViewById(R.id.cardImageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onListClick(getAdapterPosition());
        }
    }
}

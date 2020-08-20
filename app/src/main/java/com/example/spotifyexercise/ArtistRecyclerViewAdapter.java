package com.example.spotifyexercise;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ArtistRecyclerViewAdapter extends RecyclerView.Adapter<ArtistRecyclerViewAdapter.UserViewHolder> {

    private List<Artist> artistList;

    public interface OnItemClickListener {
        void onItemClick(Artist item);
    }

    private final ArtistRecyclerViewAdapter.OnItemClickListener listener;

    ArtistRecyclerViewAdapter(List<Artist> userDetailsList, OnItemClickListener listener) {
        this.artistList = userDetailsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_recycler_layout, parent, false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, final int position) {
        Artist artistDetail = artistList.get(position);
        holder.bind(artistDetail, listener);
        holder.name.setText(artistDetail.getName());
        holder.followers.setText(NumberFormat.getNumberInstance(Locale.US).format(artistDetail.getNumFollowers()) + " followers");
        holder.popularity.setRating((artistDetail.getPopularity()*5)/100.0f);
        if(artistDetail.getArtistImage() != null) {
            new DownloadImageTask(holder.image).execute(artistDetail.getArtistImage());
        }
        else{
            holder.image.setImageDrawable(null);
        }
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }


    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name, followers;
        RatingBar popularity;
        ImageView image;

        private UserViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.artistName);
            followers = itemView.findViewById(R.id.artistFollowers);
            popularity = itemView.findViewById(R.id.artistrating);
            image = itemView.findViewById(R.id.artistImage);
        }

        private void bind(final Artist item, final ArtistRecyclerViewAdapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}

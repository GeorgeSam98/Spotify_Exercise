package com.example.spotifyexercise;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.List;

public class AlbumRecyclerViewAdapter extends RecyclerView.Adapter<AlbumRecyclerViewAdapter.UserViewHolder> {

    private List<Album> albumList;

    AlbumRecyclerViewAdapter(List<Album> userDetailsList) {
        this.albumList = userDetailsList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_recycler_layout, parent, false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, final int position) {
        Album albumDetail = albumList.get(position);
        holder.name.setText(albumDetail.getName());
        holder.artist.setText(albumDetail.getArtists());
        holder.date.setText(albumDetail.getDateReleased());
        holder.trackNum.setText(albumDetail.getNumTracks() + " Tracks");
        holder.preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(albumDetail.getSpotifyLink()));
                v.getContext().startActivity(browserIntent);
            }
        });
        if(albumDetail.getImageURL() != null) {
            new DownloadImageTask(holder.image).execute(albumDetail.getImageURL());
        }
        else{
            holder.image.setImageDrawable(null);
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }


    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name, trackNum, artist, date;
        ImageView image;
        Button preview;

        private UserViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.albumName);
            artist = itemView.findViewById(R.id.albumArtist);
            date = itemView.findViewById(R.id.albumRelease);
            trackNum = itemView.findViewById(R.id.albumTracksNum);
            image = itemView.findViewById(R.id.albumImage);
            preview = itemView.findViewById(R.id.previewButton);
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
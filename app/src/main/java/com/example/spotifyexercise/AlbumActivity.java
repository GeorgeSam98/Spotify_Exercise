package com.example.spotifyexercise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AlbumActivity extends AppCompatActivity {

    String id;

    ArrayList<Album> albumArrayList = new ArrayList<>();

    AlbumRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    int offset = 0, limit = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");

        TextView albumArtist = findViewById(R.id.albumArtistName);
        albumArtist.setText(name);

        recyclerView = findViewById(R.id.recyclerAlbum);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }
        adapter = new AlbumRecyclerViewAdapter(albumArrayList);
        recyclerView.setAdapter(adapter);

        performSearch();
        populateRecycler();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    offset = offset + limit;
                    performSearch();
                }
            }
        });
    }

    private void performSearch(){
        String url = "https://api.spotify.com/v1/artists/"+id+"/albums?offset="+offset+"&limit="+limit;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {
                        JSONArray jsonArray = response.optJSONArray("items");

                        for (int n = 0; n < jsonArray.length(); n++) {
                            try {
                                JSONObject object = jsonArray.getJSONObject(n);

                                String albumName = object.getString("name");
                                int numTracks = object.getInt("total_tracks");

                                String releaseDate = object.getString("release_date");

                                JSONArray artistsObject = object.optJSONArray("artists");
                                JSONObject path = artistsObject.getJSONObject(0);
                                String artistNames = path.getString("name");

                                String imageURL;
                                try {
                                    JSONArray imagesArray = object.optJSONArray("images");
                                    JSONObject imageObject = imagesArray.optJSONObject(imagesArray.length()-1);
                                    imageURL = imageObject.getString("url");
                                }catch (Exception e){
                                    //image not found
                                    imageURL = null;
                                }
                                String id = object.getString("id");

                                JSONObject externalObject = object.optJSONObject("external_urls");
                                String spotify = externalObject.getString("spotify");

                                albumArrayList.add(new Album(id, albumName, numTracks, artistNames, releaseDate, imageURL, spotify));

                                adapter.notifyItemInserted(albumArrayList.size()-1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    catch (Exception e){ //JSONException
                        e.printStackTrace();
                    }

                }, error -> {
                    //an error occurs
                }) {
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> headers = new HashMap<>();
                String auth = "Bearer " + MainActivity.token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    private void populateRecycler(){
        adapter = new AlbumRecyclerViewAdapter(albumArrayList);
        recyclerView.setAdapter(adapter);
    }
}
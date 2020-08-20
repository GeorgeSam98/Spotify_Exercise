package com.example.spotifyexercise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

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

public class ArtistSearchActivity extends AppCompatActivity{

    SearchView searchView;

    ArrayList<Artist> artistArrayList = new ArrayList<>();

    ArtistRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    int offset = 0, limit = 20;
    String searchingArtist = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_search);

        searchView = findViewById(R.id.search);

        recyclerView = findViewById(R.id.recycler);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }
        adapter = new ArtistRecyclerViewAdapter(artistArrayList, new ArtistRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Artist item) {
                //empty list
            }
        });
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                artistArrayList.clear();
                offset = 0;
                searchingArtist = query;
                performSearch();
                populateRecycler();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                artistArrayList.clear();
                offset = 0;
                searchingArtist = newText;
                if(newText.length() != 0){
                    performSearch();
                }
                populateRecycler();
                return false;
            }
        });

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
        searchingArtist = searchingArtist.trim();
        searchingArtist = searchingArtist.replaceAll(" ", "%20");
        String url = "https://api.spotify.com/v1/search?q="+searchingArtist+"&offset="+offset+"&limit="+limit+"&&type=artist";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {
                        JSONObject artists = response.getJSONObject("artists");
                        JSONArray jsonArray = artists.optJSONArray("items");

                        for (int n = 0; n < jsonArray.length(); n++) {
                            try {
                                JSONObject object = jsonArray.getJSONObject(n);

                                JSONObject followersObject = object.optJSONObject("followers");
                                int followers = followersObject.getInt("total");

                                int popularity = object.getInt("popularity");

                                String name = object.getString("name");

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
                                artistArrayList.add(new Artist(id, name, followers, popularity, imageURL));

                                adapter.notifyItemInserted(artistArrayList.size()-1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    catch (JSONException e){
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
        adapter = new ArtistRecyclerViewAdapter(artistArrayList, new ArtistRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Artist item) {
                Intent intent = new Intent(ArtistSearchActivity.this, AlbumActivity.class);
                intent.putExtra("id", item.getID());
                intent.putExtra("name", item.getName());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

}
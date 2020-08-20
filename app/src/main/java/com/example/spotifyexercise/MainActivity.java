package com.example.spotifyexercise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

public class MainActivity extends AppCompatActivity {

    static String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(SpotifyObject.CLIENT_ID, AuthenticationResponse.Type.TOKEN, SpotifyObject.REDIRECT_URI);
                AuthenticationRequest request = builder.build();
                AuthenticationClient.openLoginActivity(MainActivity.this, SpotifyObject.REQUEST_CODE, request);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == SpotifyObject.REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    Intent login = new Intent(MainActivity.this, ArtistSearchActivity.class);
                    login.putExtra("token", response.getAccessToken());
                    token = response.getAccessToken();
                    startActivity(login);
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    Log.d("GEORGE E", response.getError());
                    break;

                // Most likely auth flow was cancelled
                default:
                    Log.d("GEORGE D", "DEFAULTED");
                    // Handle other cases
            }
        }
    }
}
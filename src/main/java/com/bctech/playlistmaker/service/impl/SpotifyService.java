package com.bctech.playlistmaker.service.impl;
import com.bctech.playlistmaker.repository.ScrapedDataRepository;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.data.playlists.AddItemsToPlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
@RequiredArgsConstructor
public class SpotifyService {

            // Set up the Spotify API credentials
            final String clientId = "831b6909fc344e2596fc6f9176d52f40";
            final String clientSecret = "84bde44329334b668087780934bd0431";
            final String redirectUri = "http://blessingchuks.tech/";

            private final ScrapedDataRepository scrapedDataRepository;



   public String createPlaylist(String playlistTitle, String username, String dataTag, String authorizationCode) {

       final SpotifyApi spotifyApi = getSpotifyApi(authorizationCode);


       var tracksURIs = searchAndGetTracks (spotifyApi, dataTag).toArray(new String[0]);
       // Create a new playlist using the Spotify API
      return createPlaylist(playlistTitle, username, spotifyApi, tracksURIs);
   }

    private SpotifyApi getSpotifyApi(String authorizationCode) {
        final SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(URI.create(redirectUri))
                .build();

        // Retrieve an access token using the authorization code
        final AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(authorizationCode).build();
        try {
            AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
        }
        catch (IOException | SpotifyWebApiException | ParseException e) {
            e.printStackTrace();
        }
        return spotifyApi;
    }

    private static String createPlaylist(String playlistTitle, String username, SpotifyApi spotifyApi, String[] tracksURIs) {
        CreatePlaylistRequest createPlaylistRequest = spotifyApi.createPlaylist(username, playlistTitle).build();
        try {
             Playlist playlist = createPlaylistRequest.execute();
             String playlistId = playlist.getId();
            // Add tracks to the playlist using the Spotify API
             AddItemsToPlaylistRequest addItemsToPlaylistRequest = spotifyApi.addItemsToPlaylist(playlistId, tracksURIs).build();
            addItemsToPlaylistRequest.execute();

            return playlist.getExternalUrls().get("spotify");

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String>  searchAndGetTracks( SpotifyApi spotifyApi,String dataTag ) {
        // Perform the track search using artist name and title
        List<String> trackUris = new ArrayList<>();
        var artistNames = scrapedDataRepository.findAllArtistByDataTag(dataTag);
        var  trackTitles =scrapedDataRepository.findAllTitleByDataTag(dataTag);

        for (String artistName : artistNames) {
            for (String trackTitle : trackTitles) {
                try {
                    final SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(artistName + " " + trackTitle).build();
                    final Track[] tracks = searchTracksRequest.execute().getItems();
                    for (Track track : tracks) {
                        String trackUri = track.getUri();
                        trackUris.add(trackUri);
                    }
                } catch (IOException | SpotifyWebApiException | ParseException e) {
                    e.printStackTrace();
                }
            }
    }
   return trackUris;
}

}

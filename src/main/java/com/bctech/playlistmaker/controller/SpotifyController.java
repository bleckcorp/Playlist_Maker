package com.bctech.playlistmaker.controller;

import com.bctech.playlistmaker.dtos.request.PlaylistDto;
import com.bctech.playlistmaker.service.impl.SpotifyService;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
public class SpotifyController {
    private final SpotifyApi spotifyApi;

    private final SpotifyService spotifyService;
    final String clientId = "831b6909fc344e2596fc6f9176d52f40";
    final String clientSecret = "84bde44329334b668087780934bd0431";
    final String redirectUri = "http://blessingchuks.tech/";
    final String scope = "user-read-private user-read-email";

    @GetMapping("/authorize")
    public String authorize() {

        String authorizationUrl = "https://accounts.spotify.com/authorize" +
                "?response_type=code" +
                "&client_id=" + clientId +
                "&scope=" + scope +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);

        return "redirect:" + authorizationUrl;
    }

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String authorizationCode, @ModelAttribute("playlistDto") PlaylistDto playlistDto) {


        try {
            final AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(authorizationCode)
                    .build();
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            String accessToken = authorizationCodeCredentials.getAccessToken();
            String refreshToken = authorizationCodeCredentials.getRefreshToken();

            spotifyService.createPlaylist(playlistDto.getPlaylistName(), getUserId(),playlistDto.getDataTag(),playlistDto.getArtist());



            return "callback";

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            e.printStackTrace();
            return "error";
        }
    }

    private String getUserId(){
        // Get the current user's profile
        try {
            final GetCurrentUsersProfileRequest getCurrentUsersProfileRequest = spotifyApi.getCurrentUsersProfile().build();
            final User user = getCurrentUsersProfileRequest.execute();
            final String userId = user.getId();
            return userId;
        } catch (IOException | SpotifyWebApiException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);

        }
        return null;
    }
}

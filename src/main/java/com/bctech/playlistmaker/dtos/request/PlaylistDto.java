package com.bctech.playlistmaker.dtos.request;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PlaylistDto {
    private String songTitle;
    private String artist;
    private String url;
    private String dataTag;
    private String playlistName;
    private String userId;
}

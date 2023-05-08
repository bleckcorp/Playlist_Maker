package com.bctech.playlistmaker.dtos.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ScrapeRequest {
    private  String url;
    private  String selector;
    private  String titleSelector;
    private  String artistSelector;
}

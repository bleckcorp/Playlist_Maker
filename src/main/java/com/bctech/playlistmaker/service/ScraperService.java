package com.bctech.playlistmaker.service;

import com.bctech.playlistmaker.dtos.request.ScrapeRequest;

import java.io.IOException;

public interface ScraperService {

    void scrapeSongByTitle(ScrapeRequest scrapeRequest) throws IOException;

    void scrapeSongByTitleAndArtist(ScrapeRequest scrapeRequest) throws IOException;
}

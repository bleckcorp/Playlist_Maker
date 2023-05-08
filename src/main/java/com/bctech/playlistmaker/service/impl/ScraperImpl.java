package com.bctech.playlistmaker.service.impl;

import com.bctech.playlistmaker.dtos.request.ScrapeRequest;
import com.bctech.playlistmaker.service.ScraperService;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScraperImpl implements ScraperService {


@Override
    public void scrapeSongByTitle(ScrapeRequest scrapeRequest) throws IOException {
        // Step 1: Scrape the website
        Document document = Jsoup.connect(scrapeRequest.getUrl()).get();

        Elements songElements = document.select(scrapeRequest.getSelector()); // Customize this selector to match the HTML structure of the scraped songs

        extractSongsFromWebScrape(songElements, scrapeRequest.getTitleSelector());
    }
@Override
public void scrapeSongByTitleAndArtist(ScrapeRequest scrapeRequest) throws IOException {
        // Step 1: Scrape the website
        Document document = Jsoup.connect(scrapeRequest.getUrl()).get();

        Elements songElements = document.select(scrapeRequest.getSelector()); // Customize this selector to match the HTML structure of the scraped songs

        extractSongsFromWebScrape(songElements, scrapeRequest.getTitleSelector(), scrapeRequest.getArtistSelector());
    }

    private static void extractSongsFromWebScrape(Elements songElements, String titleSelector) {
        // Extract the relevant information from the scraped songs
        List<String> songTitles = new ArrayList<>();
        for (Element songElement : songElements) {
            // Extract song title and artist name
            String title = songElement.select(titleSelector).text(); // Customize this selector to match the HTML structure of the song title
            songTitles.add(title);
        }
    }

    private static void extractSongsFromWebScrape(Elements songElements, String titleSelector, String artistSelector) {
        // Extract the relevant information from the scraped songs
        List<String> songTitles = new ArrayList<>();
        List<String> artistNames = new ArrayList<>();
        for (Element songElement : songElements) {
            // Extract song title and artist name
            String title = songElement.select(titleSelector).text(); // Customize this selector to match the HTML structure of the song title
            String artist = songElement.select(artistSelector).text(); // Customize this selector to match the HTML structure of the artist name
            songTitles.add(title);
            artistNames.add(artist);
        }
    }
}

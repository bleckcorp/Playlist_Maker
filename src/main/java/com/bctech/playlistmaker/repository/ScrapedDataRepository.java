package com.bctech.playlistmaker.repository;

import com.bctech.playlistmaker.model.ScrapedData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScrapedDataRepository extends JpaRepository<ScrapedData, Long> {

    List<String> findAllArtistByDataTag(String artist);

    List<String> findAllTitleByDataTag(String title);
}
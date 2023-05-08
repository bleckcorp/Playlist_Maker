package com.bctech.playlistmaker.model;

import jakarta.persistence.*;

@Entity
public class ScrapedData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = true)
    private String title;

    @Column(name = "artist", nullable = true)
    private String artist;

    @Column(name = "url", nullable = false)
    private String url;

    private String dataTag;

}

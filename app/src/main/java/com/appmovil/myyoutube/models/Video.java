package com.appmovil.myyoutube.models;

public class Video {
    private String title;
    private String channel;
    private int thumbnailResource;
    private String videoUrl;  // NUEVO: URL del video real

    // Constructor actualizado
    public Video(String title, String channel, int thumbnailResource, String videoUrl) {
        this.title = title;
        this.channel = channel;
        this.thumbnailResource = thumbnailResource;
        this.videoUrl = videoUrl;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getChannel() {
        return channel;
    }

    public int getThumbnailResource() {
        return thumbnailResource;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}

package com.appmovil.myyoutube.models;

public class Video {

    private String id;
    private String title;
    private String channel;
    private String thumbnailUrl;

    public Video(String id, String title, String channel, String thumbnailUrl) {
        this.id = id;
        this.title = title;
        this.channel = channel;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getChannel() {
        return channel;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    // 🔥 NUEVO MÉTODO para obtener la URL del video
    public String getUrl() {
        return "https://www.youtube.com/watch?v=" + id;
    }
}

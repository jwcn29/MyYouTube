package com.appmovil.myyoutube.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appmovil.myyoutube.R;
import com.appmovil.myyoutube.activities.VideoDetailActivity;
import com.appmovil.myyoutube.models.Video;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private Context context;
    private List<Video> videoList;
    private List<Video> videoListFull; // Lista original para filtrar

    public VideoAdapter(Context context, List<Video> videoList) {
        this.context = context;
        this.videoList = new ArrayList<>(videoList); // copia para mostrar
        this.videoListFull = new ArrayList<>(videoList); // copia para búsqueda
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = videoList.get(position);
        holder.titleTextView.setText(video.getTitle());
        holder.channelTextView.setText(video.getChannel());
        holder.thumbnailImageView.setImageResource(video.getThumbnailResource());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VideoDetailActivity.class);
            intent.putExtra("title", video.getTitle());
            intent.putExtra("channel", video.getChannel());
            intent.putExtra("thumbnail", video.getThumbnailResource());
            intent.putExtra("videoUrl", video.getVideoUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    // Método para búsqueda
    public void filter(String text) {
        videoList.clear();
        if (text.isEmpty()) {
            videoList.addAll(videoListFull);
        } else {
            text = text.toLowerCase();
            for (Video video : videoListFull) {
                if (video.getTitle().toLowerCase().contains(text)) {
                    videoList.add(video);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnailImageView;
        TextView titleTextView;
        TextView channelTextView;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.video_thumbnail);
            titleTextView = itemView.findViewById(R.id.video_title);
            channelTextView = itemView.findViewById(R.id.video_channel);
        }
    }
}

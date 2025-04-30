package com.appmovil.myyoutube.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appmovil.myyoutube.R;
import com.appmovil.myyoutube.activities.VideoPlayerActivity;
import com.appmovil.myyoutube.models.Video;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private List<Video> videoList;
    private List<Video> videoListFull; // 🔥 Copia completa para búsquedas
    private Context context;

    public VideoAdapter(List<Video> videoList, Context context) {
        this.videoList = videoList;
        this.context = context;
        this.videoListFull = new ArrayList<>(videoList);
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = videoList.get(position);

        holder.titleTextView.setText(video.getTitle());
        holder.channelTextView.setText(video.getChannel());

        Glide.with(context)
                .load(video.getThumbnailUrl())
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background) // Opcional: placeholder mientras carga
                .into(holder.thumbnailImageView);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VideoPlayerActivity.class);
            intent.putExtra(VideoPlayerActivity.EXTRA_VIDEO_ID, video.getId());
            context.startActivity(intent);
        });

        holder.buttonOptions.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.buttonOptions);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.video_options_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.action_save) {
                        Toast.makeText(context, "Video guardado: " + video.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (id == R.id.action_share) {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Mira este video");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, video.getUrl());
                        context.startActivity(Intent.createChooser(shareIntent, "Compartir video"));
                        return true;
                    }
                    return false;
                }
            });

            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, channelTextView;
        ImageView thumbnailImageView, buttonOptions;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textTitle);
            channelTextView = itemView.findViewById(R.id.textChannel);
            thumbnailImageView = itemView.findViewById(R.id.imageThumbnail);
            buttonOptions = itemView.findViewById(R.id.buttonOptions);
        }
    }

    // 🔥 Actualizar completamente la lista
    public void updateList(List<Video> newList) {
        videoList.clear();
        videoList.addAll(newList);
        videoListFull.clear();
        videoListFull.addAll(newList);
        notifyDataSetChanged();
    }

    // 🔥 Agregar más videos para scroll infinito
    public void addMoreVideos(List<Video> moreVideos) {
        videoList.addAll(moreVideos);
        videoListFull.addAll(moreVideos);
        notifyDataSetChanged();
    }

    // 🔥 Filtro dinámico mientras escribe
    public void filter(String text) {
        List<Video> filteredList = new ArrayList<>();

        if (text == null || text.trim().isEmpty()) {
            filteredList.addAll(videoListFull);
        } else {
            String query = text.toLowerCase().trim();
            for (Video video : videoListFull) {
                if (video.getTitle().toLowerCase().contains(query)) {
                    filteredList.add(video);
                }
            }
        }

        videoList.clear();
        videoList.addAll(filteredList);
        notifyDataSetChanged();
    }
}

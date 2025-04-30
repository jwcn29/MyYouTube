package com.appmovil.myyoutube.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.appmovil.myyoutube.R;
import com.appmovil.myyoutube.adapters.VideoAdapter;
import com.appmovil.myyoutube.models.Item;
import com.appmovil.myyoutube.models.Video;
import com.appmovil.myyoutube.models.YouTubeResponse;
import com.appmovil.myyoutube.utils.ApiClient;
import com.appmovil.myyoutube.utils.YouTubeApiService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VideoAdapter adapter;
    private List<Video> videoList;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private TextView textViewEmpty;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final String API_KEY = "AIzaSyDPaDgAmWO_vLL7XIVMqgQvfjD5s5nXnc8";

    private boolean isLoading = false;
    private String nextPageToken = "";
    private String currentQuery = "trending";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        textViewEmpty = findViewById(R.id.textViewEmpty);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        videoList = new ArrayList<>();
        adapter = new VideoAdapter(videoList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            nextPageToken = "";
            buscarVideosEnYoutube(currentQuery, "");
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading && layoutManager != null && layoutManager.findLastVisibleItemPosition() >= videoList.size() - 5) {
                    if (nextPageToken != null && !nextPageToken.isEmpty()) {
                        buscarVideosEnYoutube(currentQuery, nextPageToken);
                    }
                }
            }
        });

        buscarVideosEnYoutube(currentQuery, "");
    }

    private void buscarVideosEnYoutube(String query, String pageToken) {
        isLoading = true;
        YouTubeApiService apiService = ApiClient.getClient().create(YouTubeApiService.class);

        Call<YouTubeResponse> call = apiService.searchVideos(
                "snippet",
                query,
                API_KEY,
                20,
                "video",
                pageToken
        );

        call.enqueue(new Callback<YouTubeResponse>() {
            @Override
            public void onResponse(Call<YouTubeResponse> call, Response<YouTubeResponse> response) {
                swipeRefreshLayout.setRefreshing(false);
                isLoading = false;

                if (response.isSuccessful() && response.body() != null) {
                    List<Video> videos = new ArrayList<>();

                    for (Item item : response.body().getItems()) {
                        if (item.getId() != null && item.getId().getVideoId() != null) {
                            String id = item.getId().getVideoId();
                            String title = item.getSnippet().getTitle();
                            String channel = item.getSnippet().getChannelTitle();
                            String thumbnailUrl = item.getSnippet().getThumbnails().getHigh().getUrl();

                            videos.add(new Video(id, title, channel, thumbnailUrl));
                        }
                    }

                    if (!videos.isEmpty()) {
                        if (pageToken.isEmpty()) {
                            adapter.updateList(videos);
                        } else {
                            adapter.addMoreVideos(videos);
                        }
                        recyclerView.setVisibility(RecyclerView.VISIBLE);
                        textViewEmpty.setVisibility(TextView.GONE);
                    } else {
                        mostrarError("No se encontraron videos.");
                    }

                    nextPageToken = response.body().getNextPageToken();
                    currentQuery = query;
                } else {
                    mostrarError("No se pudo obtener respuesta del servidor.");
                }
            }

            @Override
            public void onFailure(Call<YouTubeResponse> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                isLoading = false;
                mostrarError("Error: " + t.getMessage());
            }

            private void mostrarError(String mensaje) {
                recyclerView.setVisibility(RecyclerView.GONE);
                textViewEmpty.setVisibility(TextView.VISIBLE);
                textViewEmpty.setText(mensaje);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        if (searchView != null) {
            searchView.setQueryHint("Buscar videos...");

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    nextPageToken = "";
                    buscarVideosEnYoutube(query, "");
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

            searchView.setOnCloseListener(() -> {
                nextPageToken = "";
                buscarVideosEnYoutube("trending", "");
                return false;
            });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_cast) {
            Toast.makeText(this, "Transmitir presionado", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_notifications) {
            Toast.makeText(this, "Notificaciones presionado", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

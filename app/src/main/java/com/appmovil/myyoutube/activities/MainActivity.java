package com.appmovil.myyoutube.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appmovil.myyoutube.R;
import com.appmovil.myyoutube.adapters.VideoAdapter;
import com.appmovil.myyoutube.models.Video;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VideoAdapter adapter;
    private List<Video> videoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.video_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Lista de videos simulados
        videoList = new ArrayList<>();

        videoList.add(new Video(
                "Cómo aprender Android",
                "Canal Android",
                R.drawable.ic_launcher_foreground,
                "https://www.youtube.com/watch?v=_n7LsTpNAiI&list=RD9g5uDIg378o&index=4"
        ));

        videoList.add(new Video(
                "Top 10 Apps de 2025",
                "Canal Tech",
                R.drawable.ic_launcher_background,
                "https://www.youtube.com/watch?v=5KTlHuNTwQA&list=RD9g5uDIg378o&index=12"
        ));

        videoList.add(new Video(
                "Trucos para Java",
                "Código Fácil",
                R.drawable.ic_launcher_foreground,
                "https://www.youtube.com/watch?v=i7WHjdRtVco"
        ));

        adapter = new VideoAdapter(this, videoList);
        recyclerView.setAdapter(adapter);
    }

    // Menú con búsqueda
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint("Buscar videos...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // No hacemos nada al presionar Enter
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText); // Filtra en tiempo real
                return true;
            }
        });

        return true;
    }
}

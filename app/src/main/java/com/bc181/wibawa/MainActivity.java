package com.bc181.wibawa;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Berita> dataBerita = new ArrayList<>();
    private RecyclerView rvBerita;
    private BeritaAdapter beritaAdapter;
    private DatabaseHandler dbHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvBerita = findViewById(R.id.rv_tampil_berita);
        dbHandler = new DatabaseHandler(this);
        tampilDataBerita();
    }
    private void tampilDataBerita(){
        dataBerita = dbHandler.getAllBerita();
        beritaAdapter = new BeritaAdapter(this, dataBerita);
        RecyclerView.LayoutManager layManager = new LinearLayoutManager(MainActivity.this);
        rvBerita.setLayoutManager(layManager);
        rvBerita.setAdapter(beritaAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.item_bagikan) {
            Intent bukaInput = new Intent(getApplicationContext(),InputActivity.class);
            bukaInput.putExtra("OPERASI", "insert");
            startActivity(bukaInput);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tampilDataBerita();
    }
}

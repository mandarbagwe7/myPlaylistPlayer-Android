package com.example.myplaylistplayer;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView songsListView;
    ArrayList<String> songs = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        songsListView = findViewById(R.id.songsListView);
        ImageView startImageView = findViewById(R.id.startImageView);
        TextView startTextView = findViewById(R.id.startTextView);

        songs.add("Move Like Jagger");
        songs.add("Memories");
        songs.add("Sugar");
        songs.add("Payphone");
        songs.add("One More Night");
        songs.add("Beautiful Goodbye");
        songs.add("Animals");
        songs.add("Treat You Better");
        songs.add("Senorita");
        songs.add("I Don't Wanna Live Forever");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, songs);
        songsListView.setAdapter(arrayAdapter);

        songsListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
                intent.putExtra("song", i);
                intent.putExtra("arrayLength", songs.size());
                intent.putExtra("songName", songs);
                startActivity(intent);
            }
        });


        startImageView.animate().alpha(0).setDuration(5000);
        startTextView.animate().alpha(0).setDuration(5000);
        
    }
}
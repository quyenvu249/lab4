package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String link = "https://jsonplaceholder.typicode.com/photos";
    ArrayList<ObjectItem> arrayList;
    ObjectAdapter adapter;
    RecyclerView rcView;
    String albumId, id, title, url, thumbnailUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rcView = findViewById(R.id.rcView);

        arrayList = new ArrayList<>();
        adapter = new ObjectAdapter(MainActivity.this, arrayList);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(link, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        albumId = jsonObject.getString("albumId");
                        id = jsonObject.getString("id");
                        title = jsonObject.getString("title");
                        url = jsonObject.getString("url");
                        thumbnailUrl = jsonObject.getString("thumbnailUrl");
                        ObjectItem objectItem = new ObjectItem(albumId, id, title, url, thumbnailUrl);
                        arrayList.add(objectItem);
                        rcView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        rcView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
        adapter.setOnItemClickListener(new ObjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Detail");
                View view2 = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_detail_object, null);
                ImageView imageView = view2.findViewById(R.id.imageView);
                TextView tvAlbumId = view2.findViewById(R.id.tvAlbumId);
                TextView tvId = view2.findViewById(R.id.tvId);
                TextView tvTitle = view2.findViewById(R.id.tvTitle);
                TextView tvUrl = view2.findViewById(R.id.tvUrl);
                TextView tvThumbnailUrl = view2.findViewById(R.id.tvThumbnailUrl);
                Picasso.get().load(url).into(imageView);
                tvAlbumId.setText("AlbumId: " + arrayList.get(pos).albumId);
                tvId.setText("Id: " + arrayList.get(pos).id);
                tvTitle.setText("Title: " + arrayList.get(pos).title);
                tvUrl.setText("Url: " + arrayList.get(pos).url);
                tvThumbnailUrl.setText("ThumbnailUrl: " + arrayList.get(pos).thumbnailUrl);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setView(view2);
                builder.show();
            }
        });
    }
}
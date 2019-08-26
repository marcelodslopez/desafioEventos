package com.example.desafioeventos;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.ProgressDialog;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.desafioeventos.adapter.EventAdapter;
import com.example.desafioeventos.api.EventAPI;
import com.example.desafioeventos.models.Event;
import com.example.desafioeventos.models.Person;
import com.example.desafioeventos.util.EventDeserializer;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Retrofit retrofit;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView ;
    @BindView(R.id.content_main_rv_events)    RecyclerView rvEvents ;
    @BindView(R.id.content_main_tv_no_item)  TextView tvNoItens ;
    private ProgressDialog dialog;
    private RecyclerView.LayoutManager layoutManager;
    EventAdapter eventAdapter;
    List<Event> eventsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        rvEvents.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        rvEvents.setLayoutManager(layoutManager);
        rvEvents.setItemAnimator(new DefaultItemAnimator());
        // specify an adapter (see also next example)
        eventAdapter = new EventAdapter(getApplicationContext(), eventsList);
        rvEvents.setAdapter(eventAdapter);

        LoadEvents();

    }

    @OnClick(R.id.fab) void load(View view){
        LoadEvents();
    }


    private void LoadEvents(){
        dialog = ProgressDialog.show(MainActivity.this, "", getApplicationContext().getResources().getString(R.string.loading)
                , true);
        dialog.show();

        Gson gson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .setDateFormat(DateFormat.LONG)
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .registerTypeAdapter(Event.class, new EventDeserializer())
                .create();

        retrofit = new Retrofit
                .Builder()
                .baseUrl(EventAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                //.addConverterFactory(GsonConverterFactory.create())
                .build();

        EventAPI eventAPI = retrofit.create(EventAPI.class);
        Call<List<Event>> requestEvents = eventAPI.getEvents();

        requestEvents.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.isSuccessful()){

                    List<Event> eventsList = response.body();

                    assert eventsList != null;
                    eventAdapter.setList(eventsList);
                    eventAdapter.notifyDataSetChanged();
                    tvNoItens.setVisibility(View.GONE);
                    dialog.dismiss();


                }else{
                    Toast.makeText(getApplicationContext(),"Erro: " + response.code(), Toast.LENGTH_LONG).show();
                    tvNoItens.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage().toString(), Toast.LENGTH_LONG).show();
                tvNoItens.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if (id == R.id.nav_exit) {

            exit();
        }else if (id == R.id.nav_load) {
            LoadEvents();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void exit() {
        final ProgressDialog   dialogExit = ProgressDialog.show(MainActivity.this, "",
                getApplicationContext().getResources().getString(R.string.exiting), true);

        dialogExit.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialogExit.dismiss();
                finish();
            }
        }, 3000);
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}

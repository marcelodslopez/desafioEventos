package com.example.desafioeventos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.desafioeventos.api.EventAPI;
import com.example.desafioeventos.models.Checkin;
import com.example.desafioeventos.models.Event;
import com.example.desafioeventos.util.EventDeserializer;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.config.IConfigurationProvider;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Marker;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.osmdroid.tileprovider.util.StorageUtils.getStorage;

public class DetailEventActivity extends AppCompatActivity {

    private MapView osmMapView;
    private IMapController controller;
    private Projection projection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        IConfigurationProvider provider = Configuration.getInstance();
        provider.setUserAgentValue(BuildConfig.APPLICATION_ID);
        provider.setOsmdroidBasePath(getStorage());
        provider.setOsmdroidTileCache(getStorage());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        DecimalFormat df = new DecimalFormat("0.##");

        checkPermissions();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        Event event = (Event) intent.getExtras().getSerializable("event");


        ImageView detailt_iv_picture = (ImageView) findViewById(R.id.detailt_iv_picture);
        ImageView share = (ImageView) findViewById(R.id.share);
        ImageView checkin = (ImageView) findViewById(R.id.checkin);
        TextView detailt_tv_name = (TextView) findViewById(R.id.detail_tv_name);
        TextView detail_tv_description = (TextView) findViewById(R.id.detail_tv_description);
        TextView detail_tv_date = (TextView) findViewById(R.id.detail_tv_date);
        TextView detail_tv_price = (TextView) findViewById(R.id.detail_tv_price);
        osmMapView = (MapView) findViewById(R.id.osm_map_view);


        osmMapView.setTileSource(TileSourceFactory.MAPNIK);
        osmMapView.setBuiltInZoomControls(true);
        osmMapView.setMultiTouchControls(true);

        projection = osmMapView.getProjection();

        controller = osmMapView.getController();
        controller.setZoom(16);


        Picasso.with(getApplicationContext())
                .load(event.getImage())
                .placeholder(getApplicationContext().getResources().getDrawable(R.drawable.event))
                .error(getApplicationContext().getResources().getDrawable(R.drawable.event))
                .into(detailt_iv_picture);

        detailt_tv_name.setText(event.getTitle());
        detail_tv_description.setText(event.getDescription());
        Date d = new Date(event.getDate());
        detail_tv_date.setText(format.format(d) + "Hs");
        detail_tv_price.setText("Preço R$: " + df.format(event.getPrice()));

        GeoPoint geoPoint = new GeoPoint((int)(event.getLatitude() * 1E6), (int)(event.getLongitude() * 1E6));
        controller.animateTo(geoPoint);
        //controller.setCenter(geoPoint);
        addMarker(geoPoint);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = event.getDescription();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,event.getTitle());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,format.format(d) + "Hs \n"+ shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Compartilhar"));

            }
        });

        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(DetailEventActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_checkin, null);
                final EditText etNome = (EditText) mView.findViewById(R.id.etNome);
                final EditText etEmail = (EditText) mView.findViewById(R.id.etEmail);
                Button btCheckin = (Button) mView.findViewById(R.id.btCheckin);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                btCheckin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!etNome.getText().toString().isEmpty() && !etEmail.getText().toString().isEmpty()){


                                    Retrofit retrofit = new Retrofit
                                    .Builder()
                                    .baseUrl(EventAPI.BASE_URL)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                                    EventAPI eventAPI = retrofit.create(EventAPI.class);

                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                        jsonObject.put("eventId", event.getId());
                                        jsonObject.put("name", etNome.getText().toString());
                                        jsonObject.put("email", etEmail.getText().toString());

                                        Call call = eventAPI.sendChekin(jsonObject.toString());

                                        call.enqueue(new Callback() {
                                            @Override
                                            public void onResponse(Call call, Response response) {
                                                if(response.isSuccessful()){
                                                    Toast.makeText(DetailEventActivity.this,
                                                            getResources().getString(R.string.success_checckin),
                                                            Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                }else{
                                                    Toast.makeText(DetailEventActivity.this,
                                                            getResources().getString(R.string.error_checkin_response)  +
                                                                    "Resposta: " + response.message() +
                                                                    "Code: " +response.code(),
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call call, Throwable t) {
                                                Toast.makeText(DetailEventActivity.this,
                                                        getResources().getString(R.string.error_checkin_response)+ t.getMessage().toString(),
                                                        Toast.LENGTH_SHORT).show();

                                            }
                                        });

                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }

                        }else{
                            Toast.makeText(DetailEventActivity.this,
                                    getResources().getString(R.string.error_checkin),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }
    }


    public  boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }


        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    public void addMarker(GeoPoint center){
        Marker marker = new Marker(osmMapView);
        marker.setPosition(center);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        osmMapView.getOverlays().clear();
        osmMapView.getOverlays().add(marker);
        osmMapView.invalidate();
        marker.setIcon(getResources().getDrawable(android.R.drawable.ic_menu_myplaces));

    }


}

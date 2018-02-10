package com.example.adi.geolocalizacionv10;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private double latitud;
    private double longitud;
    Marker m1=null;
    private double latitudM2;
    private double longitudM2;
    private double distancia;


    public double getDistancia() {
        return distancia;
    }



    public double getLatitudM2() {
        return latitudM2;
    }

    public double getLongitudM2() {
        return longitudM2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button botonDistancia = (Button)findViewById(R.id.btnDistancia);
        botonDistancia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double resultado = getDistancia();
                Toast.makeText(MapsActivity.this, "Esta es la distancia: "+resultado, Toast.LENGTH_LONG);
            }
        });
    }

    //Metodo que verifica si tenemos permiso de gps en el telefono celular
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ((requestCode)){
            case 10:
                geolocalizar();
                break;
            default:
                break;
        }
    }
//vrifica si tiene permisos
    public void geolocalizar(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){

                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.INTERNET}, 10);
                }
                return;
        }

        locationManager.requestLocationUpdates("gps",0,0,locationListener);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    public void setLatLong(double latitud, double longitud){
        this.latitud=latitud;
        this.longitud=longitud;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Poner los con troles de zoom en el mapa
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //herramientas adicionales del mapa
        mMap.getUiSettings().setMapToolbarEnabled(true);
        //zoom con los toques en la pantalla
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        //tipo de mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            //Captura el evento de cambio de localizacion
            public void onLocationChanged(Location location) {
                latitud = location.getLatitude();
                longitud = location.getLongitude();
               //setLatLong(location.getLongitude(), location.getLatitude());
                //Variable para añadir en el mapa la direccion
                LatLng actual = new LatLng(latitud, longitud);
                //Eliminar todas las marcas en el mapa
               // mMap.clear();
                mMap.addMarker(new MarkerOptions().position(actual).title("Ubicacion Actual").snippet("Esta es la posicion actual del usuario"));
                //Mover la camara a la posicion actual (Por default esta en las cordenadas 0,0// )
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(actual, 10));
                //Manda llamar la ventana de detalles de informacion
                mMap.setInfoWindowAdapter(new InfoWindow(getLayoutInflater()));
                //Toast.makeText(MapsActivity.this,"Latitud: "+latitud+ " Longitud: "+longitud, Toast.LENGTH_LONG);


                //nueva captura de otra marca
                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {

                    }
                });
                //evento que captura otra marca en el mapa
               mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                       mMap.clear();
                        //agregar la marca anterior
                        LatLng actual = new LatLng(latitud, longitud);
                        mMap.addMarker(new MarkerOptions().position(actual).title("Ubicacion Actual").snippet("Esta es la posicion actual del usuario"));
                        //Mover la camara a la posicion actual (Por default esta en las cordenadas 0,0// )
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(actual, 15));
                        //Manda llamar la ventana de detalles de informacion
                        mMap.setInfoWindowAdapter(new InfoWindow(getLayoutInflater()));

                        //Agregar una marca nueva
                        m1=mMap.addMarker(new MarkerOptions().position(latLng).title("Estga es la nueva marca").snippet("Esta es la nueva marca Marca 2"));

                        MapsActivity.this.latitudM2=latLng.latitude;
                        MapsActivity.this.longitudM2=latLng.longitude;
                        Location locActual = new Location("Actual");
                        locActual.setLatitude(latitud);
                        locActual.setLongitude(longitud);

                        Location locMaarca2 = new Location("Marca dos");
                        locMaarca2.setLatitude(getLatitudM2());
                        locMaarca2.setLongitude(getLongitudM2());
                        //Distancia en metros de la marca 2
                        MapsActivity.this.distancia= locActual.distanceTo(locMaarca2);

                    }
                });
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            //Cuando el gps esta apagado
            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        geolocalizar();

        /*
        // Localizacion especifica dada por la computadora
        LatLng sydney = new LatLng(20.543072, -99.793680);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */


    }
}

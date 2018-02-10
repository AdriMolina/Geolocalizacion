package com.example.adi.geolocalizacionv10;

import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class InfoWindow implements GoogleMap.InfoWindowAdapter {

    private View info=null;
    private LayoutInflater inflater = null;

    public InfoWindow(LayoutInflater inflater) {

        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        if (info == null){
            info=inflater.inflate(R.layout.activity_info_window, null);
        }
        TextView view =(TextView)info.findViewById(R.id.title);
        view.setText(marker.getTitle());
        view=(TextView)info.findViewById(R.id.snippet);
        view.setText(marker.getSnippet());

        final int clicks = 0;
       final Button boton = (Button)info.findViewById(R.id.Calcular_distancia);
        boton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                    boton.setText("Has pulsado el boton "+clicks);
                 }
        });
        return info;
    }
}

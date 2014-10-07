package com.upm.fi.jmn.muestramapa;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

    private GoogleMap mMap;
    private Marker mMarker;
    private LocationManager mLocationManager;
    private Spinner mTipoMapa;
    
    private void cambiarMapa(){
    	Log.e("TIPO_MAPA",""+mTipoMapa.getSelectedItemPosition());
    	switch (mTipoMapa.getSelectedItemPosition()) {
		case 0:
			Log.e("TIPO_MAPA","NORMAL");
			mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			break;
		case 1:
			Log.e("TIPO_MAPA","SATELITE");
			mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			break;
		case 2:
			Log.e("TIPO_MAPA","HYBRID");
			mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			break;
		default:
			Log.e("TIPO_MAPA","NONE");
			mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
			break;
		}
    	
    }
    
    private void irAPosicion(LatLng posicion){
    	if (mMarker!=null){
    		mMarker.remove();
    	} else {
    		Log.e("MARKER","MARKER NULL");
    	}
    	mMarker = mMap.addMarker(new MarkerOptions()
        				.position(posicion)
        				.title("We are @"+posicion.toString()));
    	mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion, 15));
    }
    
    private void irAMiLocalizacion(){
    	Criteria criterio = new Criteria();
        criterio.setCostAllowed(false);
        String provider =  mLocationManager.getBestProvider(criterio, true);
        Log.e("PROVIDER", provider);
    	Location mLocation = mLocationManager.getLastKnownLocation(provider);
    	LatLng posicion = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
    	Log.d("BOTON_MI_LOCALIZACION","Ir a: "+posicion.toString());
    	irAPosicion(posicion);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        this.setUpMapIfNeeded();
        
        Button botonIr = (Button) findViewById(R.id.buttonIr);
        botonIr.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText latitud = (EditText) findViewById(R.id.editTextLatitud);
				EditText longitud = (EditText) findViewById(R.id.editTextLongitud);
				Log.e("LAT",latitud.getText().toString());
				Log.e("LONG",longitud.getText().toString());
				if(latitud.getText().toString().equals("") || longitud.getText().toString().equals("")){
					Toast.makeText(MainActivity.this, "Introduzca los valores", Toast.LENGTH_SHORT).show();
				} else {
					LatLng posicion = new LatLng(Double.valueOf(latitud.getText().toString()), Double.valueOf(longitud.getText().toString()));
					Log.d("BOTON_IR","Ir a: "+posicion.toString());
					irAPosicion(posicion);
				}
			}
		});
        
        Button botonCasa = (Button) findViewById(R.id.buttonCasa);
        botonCasa.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LatLng posicion = new LatLng(40.12048, -3.85299);
				Log.d("BOTON_CASA","Ir a: "+posicion.toString());
				irAPosicion(posicion);
			}
		});
        
        Button botonMiPosicion = (Button) findViewById(R.id.buttonMiPosicion);
        botonMiPosicion.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				irAMiLocalizacion();
			}
		});
    }
    
    protected void onResume() {
    	super.onResume();
		this.setUpMapIfNeeded();
	}
    
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        mTipoMapa = (Spinner) findViewById(R.id.spinnerTipoMapa);
        mTipoMapa.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				cambiarMapa();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
        });
    	if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                                .getMap();
            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            // Other supported types include: MAP_TYPE_NORMAL,
            // MAP_TYPE_TERRAIN, MAP_TYPE_HYBRID and MAP_TYPE_NONE
            this.cambiarMapa();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                // The Map is verified. It is now safe to manipulate the map.
            	Log.v("MAP", "Map no necesario");
            }
        }
    }
}
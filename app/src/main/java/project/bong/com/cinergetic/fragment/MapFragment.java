package project.bong.com.cinergetic.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import project.bong.com.cinergetic.MainActivity;
import project.bong.com.cinergetic.R;

public class MapFragment extends Fragment  implements OnMapReadyCallback{

    private GoogleMap googleMap;
    private MapView mapView;

    LatLng loc = new LatLng(35.149899,126.912236);
    CameraPosition cp = new CameraPosition.Builder().target((loc)).zoom(16).build();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_map, container, false);

        mapView = (MapView) rootView.findViewById(R.id.cine_map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    ((MainActivity)getActivity()).changeFragment(((MainActivity)getActivity()).FRAGMENT_MAIN);
                    return true;
                }else{
                    return false;
                }
            }
        });

        return rootView;
    }


    @Override
    public void onMapReady(final GoogleMap map){

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(loc);
        markerOptions.title("광주극장");
        map.addMarker(markerOptions);

        map.moveCamera(CameraUpdateFactory.newLatLng(loc));
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
    }
}

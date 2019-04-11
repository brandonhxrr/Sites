package com.foxware.sites;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NearMe2 extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private LocationManager locationManager;
    private Location loc;
    private GoogleApiClient mGoogleApiClient;
    ArrayList<ListaSitios> lista;
    RecyclerView RV;
    DatabaseReference myRef;
    ListaSitiosAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_start, container, false);

        Sites sites = new Sites(getContext());
        sites.setSType("near");

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        checkConnection();

        FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();
        myRef = mdatabase.getReference("Sitios");

        RV= (RecyclerView) rootView.findViewById(R.id.RecyclerItems);
        RV.setHasFixedSize(true);
        LinearLayoutManager LM = new LinearLayoutManager(inflater.getContext());
        LM.setOrientation(LinearLayoutManager.VERTICAL);
        RV.setLayoutManager(LM);
        lista = new ArrayList<>();
        adapter = new ListaSitiosAdapter(lista);
        RV.setAdapter(adapter);

        ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Extras.makeText(getActivity(), "Sin Permisos");
        }else{
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if(loc != null){
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        fillSites(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }else{
                Extras.makeText(getActivity(), getString(R.string.location));
            }
        }


        return rootView;
    }



    public void fillSites(DataSnapshot dataSnapshot){
        lista.clear();
        for (DataSnapshot snapshot:
                dataSnapshot.getChildren() ) {

            ListaSitios sitio = snapshot.getValue(ListaSitios.class);

            Location location2 = new Location("Site");
            location2.setLatitude(Double.parseDouble(sitio.getLat()));
            location2.setLongitude(Double.parseDouble(sitio.getLong()));

            Float d = loc.distanceTo(location2);
            if(d.intValue() / 1000 <=15){//Para ampliar rango de sitios cerca
                lista.add(sitio);
            }
            sitio.setID(snapshot.getKey());

        }

        if(lista.isEmpty()) {
            final FragmentManager fm = getActivity().getSupportFragmentManager();

            Fragment Alert = new Alerts();
            Bundle args = new Bundle();
            args.putString("TYPE", "Empty");
            args.putString("Title", getString(R.string.alerts_empty1) );
            args.putString("Button", getString(R.string.add) );
            Alert.setArguments(args);
            fm.beginTransaction().replace(R.id.Contenedor, Alert).commit();
        }
        adapter.notifyDataSetChanged();

    }

    public void checkConnection(){
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        final FragmentManager fm= getActivity().getSupportFragmentManager();
        Fragment Alert = new Alerts();

        if(!isConnected){
            Bundle args = new Bundle();
            args.putString("TYPE", "Near");
            Alert.setArguments(args);
            fm.beginTransaction().replace(R.id.Contenedor, Alert ).commit();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }
}

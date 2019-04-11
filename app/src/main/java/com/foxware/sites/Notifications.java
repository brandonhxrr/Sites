package com.foxware.sites;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Notifications extends Fragment {

    SwipeRefreshLayout Swipe;
    ImageView Empty;

    ArrayList<ListaSitios> lista;
    RecyclerView RV;
    ListaSitiosAdapter adapter;
    DatabaseReference myRef;
    FragmentManager fm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_start, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();
        myRef = mdatabase.getReference("Guardados").child(user.getUid());


        checkConnection();


        RV= (RecyclerView) rootView.findViewById(R.id.RecyclerItems);
        RV.setHasFixedSize(true);
        LinearLayoutManager LM = new LinearLayoutManager(inflater.getContext());
        LM.setOrientation(LinearLayoutManager.VERTICAL);
        RV.setLayoutManager(LM);
        lista = new ArrayList<>();
        adapter = new ListaSitiosAdapter(lista);
        RV.setAdapter(adapter);
        Sites sites = new Sites(getContext());
        sites.setSType("saved");


                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        lista.clear();
                        for (DataSnapshot snapshot:
                                dataSnapshot.getChildren() ) {

                            ListaSitios sitio = snapshot.getValue(ListaSitios.class);
                            sitio.setID(snapshot.getKey());
                            lista.add(sitio);
                        }

                        if(lista.isEmpty()) {
                            checkConnection();
                            Empty();

                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        return rootView ;
    }

    public void checkConnection(){
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        fm= getActivity().getSupportFragmentManager();
        Fragment Alert = new Alerts();

        if(!isConnected){
            Bundle args = new Bundle();
            args.putString("TYPE", "Save");
            Alert.setArguments(args);
            fm.beginTransaction().replace(R.id.Contenedor, Alert).commit();
        }
    }

    public void Empty(){
        Fragment Alert = new Alerts();
        Bundle args = new Bundle();
        args.putString("TYPE", "NOSAVED");
        args.putString("Title", getString(R.string.alerts_notsaved) );
        args.putString("Button", getString(R.string.alerts_notsavedmes) );
        Alert.setArguments(args);
        fm.beginTransaction().replace(R.id.Contenedor, Alert).commitAllowingStateLoss();
    }


}

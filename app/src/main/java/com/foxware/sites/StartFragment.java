package com.foxware.sites;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class StartFragment extends Fragment{

    ArrayList<ListaSitios> lista;
    RecyclerView RV;
    DatabaseReference myRef;
    ListaSitiosAdapter adapter;
    AppBarLayout bar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {

        View rootView = inflater.inflate(R.layout.fragment_start, container, false);
        FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();
        myRef = mdatabase.getReference("Sitios");
        bar = getActivity().findViewById(R.id.appbar);

        Extras.checkConnection2(getActivity());;

        RV=  rootView.findViewById(R.id.RecyclerItems);
        RV.setHasFixedSize(true);
        LinearLayoutManager LM = new LinearLayoutManager(inflater.getContext());
        LM.setOrientation(LinearLayoutManager.VERTICAL);
        RV.setLayoutManager(LM);
        lista = new ArrayList<>();
        adapter = new ListaSitiosAdapter(lista);
        RV.setAdapter(adapter);

        Sites sites = new Sites(getContext());
        sites.setSType("start");

        fillS();

  //     if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
  //        RV.addOnScrollListener(new RecyclerView.OnScrollListener() {
  //            @Override
  //            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
  //                super.onScrollStateChanged(recyclerView, newState);
  //            }

  //            @Override
  //            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
  //                super.onScrolled(recyclerView, dx, dy);
  //                if(dy>0){
  //                    bar.setElevation(7);
  //                }else{
  //                    bar.setElevation(0);
  //                }
  //            }
  //        });

  //     }

        return rootView;
    }




    void fillS(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
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

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        thread.run();

    }
    }




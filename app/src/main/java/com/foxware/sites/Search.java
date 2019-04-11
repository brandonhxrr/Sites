
package com.foxware.sites;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class Search extends AppCompatActivity implements SearchView.OnQueryTextListener {

    SearchView search;

    ArrayList<ListaSitios> lista;
    RecyclerView RV;
    DatabaseReference myRef;
    SitesAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Sites sites = new Sites(Search.this);
        sites.setSType("search");

        search = (SearchView) findViewById(R.id.searchView);
        search.setOnQueryTextListener(this);

        FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();
        myRef = mdatabase.getReference("Sitios");

        RV= (RecyclerView) findViewById(R.id.ContentSearch);
        RV.setHasFixedSize(true);
        LinearLayoutManager LM = new LinearLayoutManager(Search.this);
        LM.setOrientation(LinearLayoutManager.VERTICAL);
        RV.setLayoutManager(LM);
        lista = new ArrayList<>();
        adapter = new SitesAdapter(lista);
        RV.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        if(query.isEmpty() || query.equals(" ")){
            lista.clear();
        }else{
            searchSites(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText.isEmpty()|| newText.equals(" ")){
            lista.clear();
        }else{
            searchSites(newText);
        }

        return false;
    }

    public void searchSites(final String searchtxt){
      final String query = searchtxt.toLowerCase();

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
                          if(sitio.getHist().toLowerCase().contains(query) || sitio.getTitulo().toLowerCase().contains(query) || sitio.getSubtitulo().toLowerCase().contains(query) || sitio.getDesc().toLowerCase().contains(query)) {

                              sitio.setID(snapshot.getKey());
                              lista.add(sitio);
                          }

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

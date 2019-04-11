package com.foxware.sites;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class ListaSitiosAdapter extends RecyclerView.Adapter<ListaSitiosAdapter.SitiosViewHolder> {

    private List<ListaSitios> items;

    public  static  class SitiosViewHolder extends RecyclerView.ViewHolder{

    ImageView Imagen;
    TextView Titulo, Subtitulo;
    public CardView cardSites;
    public Context context;
    String ID;
    DatabaseReference myRef;

        public SitiosViewHolder(View itemView) {
            super(itemView);

            Imagen = (ImageView) itemView.findViewById(R.id.siteImage);
            Titulo = (TextView) itemView.findViewById(R.id.siteName);
            Subtitulo = (TextView) itemView.findViewById(R.id.siteLocation);
            cardSites = (CardView) itemView.findViewById(R.id.cardSites);

            context = itemView.getContext();
        }
    }

    ListaSitiosAdapter(List<ListaSitios> items){
        this.items=items;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public SitiosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);

        return new SitiosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final SitiosViewHolder holder, int position) {

        String site_name = ListaSitiosAdapter.ParseSite(items.get(position).getTitulo()) ;
        String site_loc = ListaSitiosAdapter.ParseSite(items.get(position).getSubtitulo());

        Picasso.with(holder.context)
                .load(items.get(position).getImagen())
                .error(R.color.colorGrey2)
                .into(holder.Imagen);
        holder.Titulo.setText(site_name);
        holder.Subtitulo.setText(site_loc);


        holder.ID = items.get(position).getID();

        FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();
        holder.myRef = mdatabase.getReference("Sitios").child(holder.ID);

        holder.cardSites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fill(holder, holder.context);
            }
        });
    }

    public void fill(final SitiosViewHolder holder, final Context context){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                holder.myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        ListaSitios sitio = dataSnapshot.getValue(ListaSitios.class);
                        String xNombre = sitio.getTitulo();
                        String xUbicacion = sitio.getSubtitulo();
                        String xDesc= sitio.getDesc();
                        String xHist = sitio.getHist();
                        String xTel = sitio.getTel();
                        String xWebsite = sitio.getWeb();
                        String xRating = sitio.getRating();
                        String Path = sitio.getImagen();
                        String Lat = sitio.getLat();
                        String Long = sitio.getLong();
                        String ID = dataSnapshot.getKey();
                        String Namae = sitio.getUserName();
                        String Photo = sitio.getUser_pic();

                        NextStep(context, xNombre, xUbicacion, xDesc, xHist, xTel, xWebsite, xRating, Path, Lat, Long, ID, Namae, Photo);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        thread.run();
    }

    private void NextStep(Context context, String Nombre, String Loc, String Desc, String Hist, String Tel, String Web, String Rat, String Path, String lat, String Long, String ID,  String N, String P){
        Intent x = new Intent(context, DetailActivity.class);
        x.putExtra("site_name", Nombre);
        x.putExtra("site_location", Loc);
        x.putExtra("site_desc", Desc);
        x.putExtra("site_hist", Hist);
        x.putExtra("site_tel", Tel);
        x.putExtra("site_web", Web);
        x.putExtra("site_rat", Rat);
        x.putExtra("site_path", Path);
        x.putExtra("site_lat", lat);
        x.putExtra("site_long", Long);
        x.putExtra("site_id", ID);
        x.putExtra("us_name", N);
        x.putExtra("us_pic", P);
        context.startActivity(x);
    }

    private static  String ParseSite(String Name){
        if(Name.length() > 50){
            Name = Name.substring(0, 50)+"...";
        }else{
            Name = Name;
        }return Name;
    }
}

package com.foxware.sites;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class SitesAdapter extends RecyclerView.Adapter<SitesAdapter.SitiosViewHolder> {

    private List<ListaSitios> items;

    public  static  class SitiosViewHolder extends RecyclerView.ViewHolder{

    TextView Titulo, Subtitulo;
    public CardView cardSites;
    public Context context;
    public CircleImageView user_pic;
    String ID;
    DatabaseReference myRef;

        SitiosViewHolder(View itemView) {
            super(itemView);

            Titulo = (TextView) itemView.findViewById(R.id.siteName);
            Subtitulo = (TextView) itemView.findViewById(R.id.siteLocation);
            cardSites = (CardView) itemView.findViewById(R.id.cardSites);
            user_pic = (CircleImageView) itemView.findViewById(R.id.user_pic);
            context = itemView.getContext();
        }

    }

    SitesAdapter(List<ListaSitios> items){
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
                .inflate(R.layout.site, parent, false);

        return new SitiosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final SitiosViewHolder holder, int position) {

        holder.Titulo.setText(items.get(position).getTitulo());
        holder.Subtitulo.setText(items.get(position).getSubtitulo());

        Picasso.with(holder.context)
                .load(items.get(position).getImagen())
                .error(R.color.colorGrey2)
                .into(holder.user_pic);

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

    public void fill(SitiosViewHolder holder, final Context context){

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

                NextStep(context, xNombre, xUbicacion, xDesc, xHist, xTel, xWebsite, xRating, Path);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void NextStep(Context context, String Nombre, String Loc, String Desc, String Hist, String Tel, String Web, String Rat, String Path){
        Intent x = new Intent(context, DetailActivity.class);
        x.putExtra("site_name", Nombre);
        x.putExtra("site_location", Loc);
        x.putExtra("site_desc", Desc);
        x.putExtra("site_hist", Hist);
        x.putExtra("site_tel", Tel);
        x.putExtra("site_web", Web);
        x.putExtra("site_rat", Rat);
        x.putExtra("site_path", Path);
        context.startActivity(x);
    }


}

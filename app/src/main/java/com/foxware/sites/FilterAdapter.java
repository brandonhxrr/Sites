package com.foxware.sites;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {

    private List<Filter> items;

    public  static  class FilterViewHolder extends RecyclerView.ViewHolder{

    CardView Card;
    TextView Titulo;
    ImageView Imagen;
    public Context context;

        FilterViewHolder(View itemView) {
            super(itemView);

            Titulo = (TextView) itemView.findViewById(R.id.filter_name);
            Card = (CardView) itemView.findViewById(R.id.filter_card);
            Imagen = (ImageView) itemView.findViewById(R.id.filter_img);
            context = itemView.getContext();
        }
    }

    FilterAdapter(List<Filter> items){
        this.items=items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter, parent, false);

        return new FilterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, final int position) {

        holder.Titulo.setText(items.get(position).getContenido());
        holder.Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.get(position).getSearch().setQuery(items.get(position).getSearch().getQuery()
                        + items.get(position).getContenido(), false);
            }
        });

        Picasso.with(holder.context)
                .load(items.get(position).getImagen())
                .into(holder.Imagen);
    }
}

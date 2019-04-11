package com.foxware.sites;

import android.widget.SearchView;

public class Filter {

    String Contenido;
    int Imagen;
    SearchView search;

    public SearchView getSearch() {
        return search;
    }

    public void setSearch(SearchView search) {
        this.search = search;
    }

    public Filter(String contenido, SearchView searchView, int imagen ){
        this.Contenido = contenido;
        this.search = searchView;
        this.Imagen = imagen;
    }

    public String getContenido() {
        return Contenido;
    }

    public int getImagen() {
        return Imagen;
    }

}
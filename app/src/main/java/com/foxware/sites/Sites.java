package com.foxware.sites;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

public class Sites {

     private String estado, first_use, user_name, Type, OldPass, SType;
     private Uri photo_uri, Cache;
     private SharedPreferences Pref;
     private SharedPreferences.Editor editor;

    public Sites(Context context) {
        Pref = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        editor=Pref.edit();
    }

    public String getSType() {
        SType = Pref.getString("sType", "");
        return SType;
    }

    public void setSType(String SType) {
        editor.putString("sType", SType);
        editor.apply();
        this.SType = SType;
    }

    public Uri getCache() {
        Cache = Uri.parse(Pref.getString("Cache", ""));
        return Cache;
    }

    public void setCache(Uri cache) {
        editor.putString("Cache", cache.toString());
        editor.apply();
        Cache = cache;
    }

    public String getOldPass() {
        OldPass = Pref.getString("Pass", "");
        return OldPass;
    }

    public void setOldPass(String oldPass) {
        editor.putString("Pass", oldPass);
        editor.apply();
        this.OldPass = oldPass;
    }

    public String getType() {
        Type = Pref.getString("Type", "");
        return Type;
    }

    public void setType(String type) {
        editor.putString("Type", type);
        editor.apply();
        this.Type = type;
    }

    public Uri getPhoto_uri() {
        photo_uri = Uri.parse(Pref.getString("photo_uri", ""));
        return photo_uri;
    }

    public void setPhoto_uri(Uri photo_uri) {
        editor.putString("photo_uri", photo_uri.toString());
        editor.apply();
        this.photo_uri = photo_uri;
    }

    public String getEstado() {
        estado = Pref.getString("Use", "");
        return estado;
    }

    public void setEstado(String estado) {
        editor.putString("Use", estado);
        editor.apply();
        this.estado = estado;
    }

    public String getFirst_use() {
        first_use = Pref.getString("State", "");
        return first_use;
    }

    public void setFirst_use(String first_use) {
        editor.putString("State", first_use);
        editor.apply();
        this.first_use = first_use;
    }
    public String getUser_name() {
        user_name = Pref.getString("Name", "");
        return user_name;
    }

    public void setUser_name(String user_name) {
        editor.putString("Name", user_name);
        editor.apply();
        this.user_name = user_name;
    }

}

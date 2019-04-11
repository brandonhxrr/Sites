package com.foxware.sites;

public class ListaSitios {

    private String Titulo, Subtitulo, Desc, Hist, Tel, Web, Rating, Imagen, ID, UserName, user_pic, Lat, Long;

    public ListaSitios(){

    }

    public ListaSitios(String titulo, String subtitulo, String desc, String hist, String tel, String web, String rating, String imagen, String userName, String UserP, String lat, String longi) {
        Titulo = titulo;
        Subtitulo = subtitulo;
        Desc = desc;
        Hist = hist;
        Tel = tel;
        Web = web;
        Rating = rating;
        Imagen = imagen;
        UserName = userName;
        user_pic = UserP;
        Lat = lat;
        Long = longi;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLong() {
        return Long;
    }

    public void setLong(String aLong) {
        Long = aLong;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUser_pic() {
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getSubtitulo() {
        return Subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        Subtitulo = subtitulo;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getHist() {
        return Hist;
    }

    public void setHist(String hist) {
        Hist = hist;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }

    public String getWeb() {
        return Web;
    }

    public void setWeb(String web) {
        Web = web;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }
}

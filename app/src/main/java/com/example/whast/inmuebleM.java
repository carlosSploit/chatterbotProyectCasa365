package com.example.whast;

import java.util.ArrayList;

public class inmuebleM {
    private String nombre;
    private String precio;
    private String direccion;
    private String Url;
    private String Urlgenera;
    private imbuebleEdi ed;
    private ArrayList<String> urlIm;


    public inmuebleM(String nombre, String precio, String direccion, String url , String Urlgenera) {
        this.nombre = nombre;
        this.precio = precio;
        this.direccion = direccion;
        this.Url = url;
        this.Urlgenera = Urlgenera;
    }

    public void setEd(imbuebleEdi ed) {
        this.ed = ed;
    }

    public void setUrl(ArrayList<String> url) {
        this.urlIm = url;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getUrl() {
        return Url;
    }

    public String getUrlgenera() {
        return Urlgenera;
    }

    public imbuebleEdi getEd() {
        return ed;
    }

    public ArrayList<String> getURLSI() {
        return urlIm;
    }
}

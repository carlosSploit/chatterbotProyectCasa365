package com.example.whast;

public class Messeg {
    private  String contenmef;
    private  char tipo;
    private String hora;
    private inmuebleM meg = null;

    public Messeg(String contenmef, char tipo, String hora, inmuebleM meg) {
        this.contenmef = contenmef;
        this.tipo = tipo;
        this.hora = hora;
        this.meg = meg;
    }

    public String getContenmef() {
        return contenmef;
    }

    public char getTipo() {
        return tipo;
    }

    public String getHora() {
        return hora;
    }

    public inmuebleM getMeg() {
        return meg;
    }
}

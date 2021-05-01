package com.example.whast;

public class imbuebleEdi {
    private String  total;
    private String  techado;
    private String  dorm;
    private String baños;
    private String estac;

    public imbuebleEdi(String total, String techado, String dorm, String baños, String estac) {
        this.total = total;
        this.techado = techado;
        this.dorm = dorm;
        this.baños = baños;
        this.estac = estac;
    }

    public String getTotal() {
        return total;
    }

    public String getTechado() {
        return techado;
    }

    public String getDorm() {
        return dorm;
    }

    public String getBaños() {
        return baños;
    }

    public String getEstac() {
        return estac;
    }
}

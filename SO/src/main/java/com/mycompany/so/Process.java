package com.mycompany.so;

public class Process {
    private final String nombre;
    private int tiempoEjecucion;
    private int tiempoLlegada;
    
    public Process(String nombre, int tiempoEjecucion, int tiempoLlegada) {
        this.nombre = nombre;
        this.tiempoEjecucion = tiempoEjecucion;
        this.tiempoLlegada = tiempoLlegada;
    }
    
    public void CicloComun() {
        if(tiempoLlegada > 0) tiempoLlegada--;
    }
    
    public void CicloEnEjecucion() {
        if(tiempoEjecucion > 0) tiempoEjecucion--;
    }
    
    public boolean EstaListo() {
        return tiempoLlegada == 0;
    }
    
    public boolean EstaFinalizado() {
        return tiempoEjecucion == 0;
    }
    
    public String getNombre() {
        return this.nombre;
    }
    
    public int getTiempoEjecucion() {
        return this.tiempoEjecucion;
    }
    
    public int getTiempoLlegada() {
        return this.tiempoLlegada;
    }
}